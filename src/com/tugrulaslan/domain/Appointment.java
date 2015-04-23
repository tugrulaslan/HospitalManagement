package com.tugrulaslan.domain;

import java.util.Date;
import java.util.Objects;

public class Appointment implements Comparable<Appointment> {

    private Patient patient;
    private String specialist;
    private Date appointmentDate;
    private String slot;
    private String appointmentStatus;

    public Appointment(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public Appointment() {

    }

    public Appointment(Patient patient, String specialist, Date appointmentDate, String slot, String appointmentStatus) {
        this.patient = patient;
        this.specialist = specialist;
        this.appointmentDate = appointmentDate;
        this.slot = slot;
        this.appointmentStatus = appointmentStatus;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    @Override
    public String toString() {
        return "Appointment{" + "patient=" + patient + ", specialist=" + specialist + ", appointmentDate=" + appointmentDate + ", slot=" + slot + ", appointmentStatus=" + appointmentStatus + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Appointment other = (Appointment) obj;
        if (!Objects.equals(this.patient, other.patient)) {
            return false;
        }
        if (!Objects.equals(this.specialist, other.specialist)) {
            return false;
        }
        if (!Objects.equals(this.appointmentDate, other.appointmentDate)) {
            return false;
        }
        if (!Objects.equals(this.slot, other.slot)) {
            return false;
        }
        if (!Objects.equals(this.appointmentStatus, other.appointmentStatus)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Appointment o) {
        return getAppointmentDate().compareTo(o.getAppointmentDate());
    }

}
