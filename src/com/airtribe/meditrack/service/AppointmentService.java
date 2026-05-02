package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.AppointmentStatus;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.DateUtil;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing appointments
 * Demonstrates business logic, CRUD operations, and polymorphism
 */
public class AppointmentService {
    
    private final DataStore<Appointment> appointmentStore;
    private final DoctorService doctorService;
    private final PatientService patientService;
    
    /**
     * Constructor
     * @param doctorService doctor service for validation
     * @param patientService patient service for validation
     */
    public AppointmentService(DoctorService doctorService, PatientService patientService) {
        this.appointmentStore = new DataStore<>(Appointment.class);
        this.doctorService = doctorService;
        this.patientService = patientService;
    }
    
    /**
     * Create a new appointment
     * @param appointment appointment to create
     * @return created appointment
     * @throws InvalidDataException if appointment data is invalid
     */
    public Appointment createAppointment(Appointment appointment) throws InvalidDataException {
        if (appointment == null) {
            throw new InvalidDataException("Appointment cannot be null");
        }
        
        // Validate appointment data
        if (!appointment.validate()) {
            throw new InvalidDataException("Invalid appointment data");
        }
        
        // Generate ID if not provided
        if (appointment.getId() == null || appointment.getId().trim().isEmpty()) {
            appointment.setId(IdGenerator.generateAppointmentId());
        }
        
        // Validate ID format
        if (!Validator.isValidAppointmentId(appointment.getId())) {
            throw new InvalidDataException("Invalid appointment ID format", appointment.getId());
        }
        
        // Check if appointment already exists
        if (appointmentStore.exists(appointment.getId())) {
            throw new InvalidDataException("Appointment with ID '" + appointment.getId() + "' already exists");
        }
        
        // Validate patient exists
        if (!patientService.patientExists(appointment.getPatient().getId())) {
            throw new InvalidDataException("Patient with ID '" + appointment.getPatient().getId() + "' not found");
        }
        
        // Validate doctor exists
        if (!doctorService.doctorExists(appointment.getDoctor().getId())) {
            throw new InvalidDataException("Doctor with ID '" + appointment.getDoctor().getId() + "' not found");
        }
        
        // Check for scheduling conflicts
        List<Appointment> doctorAppointments = getAppointmentsByDoctor(appointment.getDoctor().getId());
        for (Appointment existing : doctorAppointments) {
            if (existing.conflictsWith(appointment)) {
                throw new InvalidDataException("Appointment conflicts with existing appointment for Dr. " + 
                    appointment.getDoctor().getFullName() + " at " + 
                    DateUtil.formatDateTime(existing.getAppointmentDateTime()));
            }
        }
        
        return appointmentStore.save(appointment);
    }
    
    /**
     * Get appointment by ID
     * @param appointmentId appointment ID
     * @return optional containing appointment if found
     */
    public Optional<Appointment> getAppointmentById(String appointmentId) {
        return Optional.ofNullable(appointmentStore.findById(appointmentId));
    }
    
    /**
     * Get all appointments
     * @return list of all appointments
     */
    public List<Appointment> getAllAppointments() {
        return appointmentStore.findAll();
    }
    
    /**
     * Update appointment information
     * @param appointment appointment to update
     * @return updated appointment
     * @throws InvalidDataException if appointment data is invalid
     */
    public Appointment updateAppointment(Appointment appointment) throws InvalidDataException {
        if (appointment == null) {
            throw new InvalidDataException("Appointment cannot be null");
        }
        
        // Validate appointment data
        if (!appointment.validate()) {
            throw new InvalidDataException("Invalid appointment data");
        }
        
        // Check if appointment exists
        if (!appointmentStore.exists(appointment.getId())) {
            throw new InvalidDataException("Appointment with ID '" + appointment.getId() + "' not found");
        }
        
        return appointmentStore.update(appointment);
    }
    
    /**
     * Delete appointment by ID
     * @param appointmentId appointment ID
     * @return true if deleted
     */
    public boolean deleteAppointment(String appointmentId) {
        return appointmentStore.deleteById(appointmentId);
    }
    
    /**
     * Cancel appointment
     * @param appointmentId appointment ID
     * @return cancelled appointment
     * @throws AppointmentNotFoundException if appointment not found
     * @throws InvalidDataException if cannot cancel
     */
    public Appointment cancelAppointment(String appointmentId) throws AppointmentNotFoundException, InvalidDataException {
        Optional<Appointment> appointmentOpt = getAppointmentById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new AppointmentNotFoundException(appointmentId);
        }
        
        Appointment appointment = appointmentOpt.get();
        appointment.cancel();
        return updateAppointment(appointment);
    }
    
    /**
     * Confirm appointment
     * @param appointmentId appointment ID
     * @return confirmed appointment
     * @throws AppointmentNotFoundException if appointment not found
     * @throws InvalidDataException if cannot confirm
     */
    public Appointment confirmAppointment(String appointmentId) throws AppointmentNotFoundException, InvalidDataException {
        Optional<Appointment> appointmentOpt = getAppointmentById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new AppointmentNotFoundException(appointmentId);
        }
        
        Appointment appointment = appointmentOpt.get();
        appointment.confirm();
        return updateAppointment(appointment);
    }
    
    /**
     * Complete appointment
     * @param appointmentId appointment ID
     * @return completed appointment
     * @throws AppointmentNotFoundException if appointment not found
     * @throws InvalidDataException if cannot complete
     */
    public Appointment completeAppointment(String appointmentId) throws AppointmentNotFoundException, InvalidDataException {
        Optional<Appointment> appointmentOpt = getAppointmentById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new AppointmentNotFoundException(appointmentId);
        }
        
        Appointment appointment = appointmentOpt.get();
        appointment.complete();
        return updateAppointment(appointment);
    }
    
    /**
     * Mark appointment as no-show
     * @param appointmentId appointment ID
     * @return marked appointment
     * @throws AppointmentNotFoundException if appointment not found
     * @throws InvalidDataException if cannot mark
     */
    public Appointment markNoShow(String appointmentId) throws AppointmentNotFoundException, InvalidDataException {
        Optional<Appointment> appointmentOpt = getAppointmentById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new AppointmentNotFoundException(appointmentId);
        }
        
        Appointment appointment = appointmentOpt.get();
        appointment.markNoShow();
        return updateAppointment(appointment);
    }
    
    /**
     * Search appointments by query
     * @param query search query
     * @return list of matching appointments
     */
    public List<Appointment> searchAppointments(String query) {
        return appointmentStore.search(query);
    }
    
    /**
     * Get appointments by patient
     * @param patientId patient ID
     * @return list of patient's appointments
     */
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointmentStore.filter(appointment -> 
            appointment.getPatient().getId().equals(patientId));
    }
    
    /**
     * Get appointments by doctor
     * @param doctorId doctor ID
     * @return list of doctor's appointments
     */
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointmentStore.filter(appointment -> 
            appointment.getDoctor().getId().equals(doctorId));
    }
    
    /**
     * Get appointments by status
     * @param status appointment status
     * @return list of appointments with given status
     */
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentStore.filter(appointment -> 
            appointment.getStatus() == status);
    }
    
    /**
     * Get appointments by date
     * @param date appointment date
     * @return list of appointments on given date
     */
    public List<Appointment> getAppointmentsByDate(LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
        
        return appointmentStore.filter(appointment -> 
            !appointment.getAppointmentDateTime().isBefore(startOfDay) && 
            !appointment.getAppointmentDateTime().isAfter(endOfDay));
    }
    
    /**
     * Get appointments today
     * @return list of today's appointments
     */
    public List<Appointment> getTodayAppointments() {
        return appointmentStore.filter(Appointment::isToday);
    }
    
    /**
     * Get upcoming appointments
     * @return list of upcoming appointments
     */
    public List<Appointment> getUpcomingAppointments() {
        return appointmentStore.filter(Appointment::isUpcoming);
    }
    
    /**
     * Get past appointments
     * @return list of past appointments
     */
    public List<Appointment> getPastAppointments() {
        return appointmentStore.filter(Appointment::isPast);
    }
    
    /**
     * Get appointments by date range
     * @param startDate start date
     * @param endDate end date
     * @return list of appointments in date range
     */
    public List<Appointment> getAppointmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentStore.filter(appointment -> 
            !appointment.getAppointmentDateTime().isBefore(startDate) && 
            !appointment.getAppointmentDateTime().isAfter(endDate));
    }
    
    /**
     * Get appointments by symptoms
     * @param symptoms symptoms to search for
     * @return list of appointments with matching symptoms
     */
    public List<Appointment> getAppointmentsBySymptoms(String symptoms) {
        if (symptoms == null || symptoms.trim().isEmpty()) {
            return List.of();
        }
        
        return appointmentStore.filter(appointment -> 
            appointment.getSymptoms() != null && 
            appointment.getSymptoms().toLowerCase().contains(symptoms.toLowerCase()));
    }
    
    /**
     * Get available appointment slots for a doctor
     * @param doctorId doctor ID
     * @param date date to check
     * @return list of available time slots
     */
    public List<LocalDateTime> getAvailableSlots(String doctorId, LocalDateTime date) {
        List<Appointment> doctorAppointments = getAppointmentsByDoctor(doctorId);
        List<LocalDateTime> availableSlots = new ArrayList<>();
        
        // Generate slots from 9 AM to 5 PM (30-minute intervals)
        LocalDateTime currentSlot = date.withHour(9).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = date.withHour(17).withMinute(0).withSecond(0).withNano(0);
        
        while (currentSlot.isBefore(endOfDay)) {
            LocalDateTime slotEnd = currentSlot.plusMinutes(30);
            
            // Check if slot is available
            boolean isAvailable = true;
            for (Appointment appointment : doctorAppointments) {
                if (DateUtil.rangesOverlap(currentSlot, slotEnd, 
                    appointment.getAppointmentDateTime(), appointment.getEndTime())) {
                    isAvailable = false;
                    break;
                }
            }
            
            if (isAvailable && currentSlot.isAfter(LocalDateTime.now())) {
                availableSlots.add(currentSlot);
            }
            
            currentSlot = slotEnd;
        }
        
        return availableSlots;
    }
    
    /**
     * Get next available appointment for a doctor
     * @param doctorId doctor ID
     * @return next available slot
     */
    public LocalDateTime getNextAvailableSlot(String doctorId) {
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        if (doctorOpt.isEmpty()) {
            return null;
        }
        
        LocalDateTime nextSlot = DateUtil.getNextAvailableSlot(LocalDateTime.now(), 30);
        
        // Find first available slot
        for (int daysAhead = 0; daysAhead < 30; daysAhead++) { // Check up to 30 days ahead
            LocalDateTime checkDate = LocalDateTime.now().plusDays(daysAhead);
            List<LocalDateTime> availableSlots = getAvailableSlots(doctorId, checkDate);
            
            if (!availableSlots.isEmpty()) {
                return availableSlots.get(0);
            }
        }
        
        return null; // No available slots found
    }
    
    /**
     * Reschedule appointment
     * @param appointmentId appointment ID
     * @param newDateTime new date and time
     * @return rescheduled appointment
     * @throws AppointmentNotFoundException if appointment not found
     * @throws InvalidDataException if cannot reschedule
     */
    public Appointment rescheduleAppointment(String appointmentId, LocalDateTime newDateTime) 
            throws AppointmentNotFoundException, InvalidDataException {
        
        Optional<Appointment> appointmentOpt = getAppointmentById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new AppointmentNotFoundException(appointmentId);
        }
        
        Appointment appointment = appointmentOpt.get();
        
        // Check for conflicts with new time
        List<Appointment> doctorAppointments = getAppointmentsByDoctor(appointment.getDoctor().getId());
        for (Appointment existing : doctorAppointments) {
            if (!existing.getId().equals(appointmentId) && 
                DateUtil.rangesOverlap(newDateTime, newDateTime.plusMinutes(30),
                    existing.getAppointmentDateTime(), existing.getEndTime())) {
                throw new InvalidDataException("New time conflicts with existing appointment");
            }
        }
        
        appointment.setAppointmentDateTime(newDateTime);
        return updateAppointment(appointment);
    }
    
    /**
     * Get appointment statistics
     * @return formatted statistics string
     */
    public String getStatistics() {
        List<Appointment> allAppointments = getAllAppointments();
        
        long todayCount = allAppointments.stream()
            .filter(Appointment::isToday)
            .count();
        
        long upcomingCount = allAppointments.stream()
            .filter(Appointment::isUpcoming)
            .count();
        
        long pastCount = allAppointments.stream()
            .filter(Appointment::isPast)
            .count();
        
        // Count by status
        String statusStats = "";
        if (!allAppointments.isEmpty()) {
            statusStats = allAppointments.stream()
                .collect(Collectors.groupingBy(Appointment::getStatus, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));
        }
        
        double avgFee = allAppointments.stream()
            .mapToDouble(Appointment::getFee)
            .average()
            .orElse(0.0);
        
        return String.format(
            "Appointment Statistics:\n" +
            "  Total Appointments: %d\n" +
            "  Today's Appointments: %d\n" +
            "  Upcoming Appointments: %d\n" +
            "  Past Appointments: %d\n" +
            "  Average Fee: $%.2f\n" +
            "  Status Breakdown: %s",
            allAppointments.size(),
            todayCount,
            upcomingCount,
            pastCount,
            avgFee,
            statusStats.isEmpty() ? "None" : statusStats
        );
    }
    
    /**
     * Validate all appointments
     * @return list of invalid appointments
     */
    public List<Appointment> validateAllAppointments() {
        return appointmentStore.validateAll();
    }
    
    /**
     * Get appointment count
     * @return number of appointments
     */
    public int getAppointmentCount() {
        return appointmentStore.count();
    }
    
    /**
     * Check if appointment exists
     * @param appointmentId appointment ID
     * @return true if exists
     */
    public boolean appointmentExists(String appointmentId) {
        return appointmentStore.exists(appointmentId);
    }
    
    /**
     * Get appointments by room
     * @param roomNumber room number
     * @return list of appointments in specified room
     */
    public List<Appointment> getAppointmentsByRoom(String roomNumber) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            return List.of();
        }
        
        return appointmentStore.filter(appointment -> 
            roomNumber.equals(appointment.getRoomNumber()));
    }
}
