package com.tugrulaslan.util;

import com.tugrulaslan.domain.Appointment;
import com.tugrulaslan.domain.Patient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DatabaseHelper {

	public static List<String> specialists = new ArrayList<>();
	public static List<Patient> patients = new ArrayList<>();
	private static Map<Integer, List<Appointment>> appointments = new HashMap<>();
	private static Map<Integer, String> hourSlots = new TreeMap<Integer, String>();

	public void addToSpecialist(String specialist) {
		specialists.add(specialist);
	}

	public List<String> getallSpecialists() {
		return specialists;
	}

	public void addToPatients(Patient patient) {
		patients.add(patient);
	}

	public List<Patient> getAllPatients() {
		return patients;
	}

	public void addToAppointment(Integer hospitalId, Appointment appointment) {
		List<Appointment> list = appointments.get(hospitalId);
		if (list == null) {
			list = new ArrayList<>();
			appointments.put(hospitalId, list);
		}
		list.add(appointment);
	}

	public List<Appointment> getAllAppointments() {
		List<Appointment> allAppointments = new ArrayList<>();
		for (Entry<Integer, List<Appointment>> entry : appointments.entrySet()) {
			allAppointments.addAll(entry.getValue());
		}

		return allAppointments;
	}

	public Appointment getAppointment(Integer hospitalId, String bookingCode) {

		for (Entry<Integer, List<Appointment>> entry : appointments.entrySet()) {
			for (Appointment obj : entry.getValue()) {
				Patient patient = obj.getPatient();
				if (Objects.equals(patient.getHospitalId(), hospitalId)
						&& obj.getSlot().equals(bookingCode)) {
					return obj;
				}
			}
		}
		return null;
	}

	public boolean alterAppointmentStatus(Appointment appointment, String status) {
		for (Entry<Integer, List<Appointment>> entry : appointments.entrySet()) {
			for (Appointment obj : entry.getValue()) {
				if (Objects.equals(obj, appointment)) {
					obj.setAppointmentStatus(status);
					return true;
				}
			}
		}
		return false;
	}

	public Set<Appointment> getAppointmentsBySpecialist(String specialist) {
		Set<Appointment> specialistAppointments = new TreeSet<>();
		for (Entry<Integer, List<Appointment>> entry : appointments.entrySet()) {
			for (Appointment obj : entry.getValue()) {
				if (obj.getSpecialist().equals(specialist)) {
					specialistAppointments.add(obj);
				}
			}
		}
		return specialistAppointments;
	}

	public Set<Appointment> getPatientAppointments(Integer hospitalId) {
		Set<Appointment> patientAppointments = new TreeSet<>();
		for (Entry<Integer, List<Appointment>> entry : appointments.entrySet()) {
			for (Appointment obj : entry.getValue()) {
				Patient patient = obj.getPatient();
				if (patient.getHospitalId().equals(hospitalId)) {
					patientAppointments.add(obj);
				}
			}
		}
		return patientAppointments;
	}

	public boolean patientExists(Integer hospitalId) {

		for (Patient patient : patients) {
			if (Objects.equals(patient.getHospitalId(), hospitalId)) {
				return true;
			}
		}
		return false;
	}

	public String getPatientName(Integer hospitalId) {
		for (Patient patient : patients) {
			if (Objects.equals(patient.getHospitalId(), hospitalId)) {
				return patient.getName();
			}
		}
		return "";
	}

	public Map<Integer, String> getHourSlotList() {
		return hourSlots;
	}

	public void addToHourSlot(Integer hourInt, String hourString) {
		hourSlots.put(hourInt, hourString);
	}

	public Set<Appointment> getSpecialistAppointments(String specialist) {
		Set<Appointment> specialistSppointments = new TreeSet<>();
		for (Entry<Integer, List<Appointment>> entry : appointments.entrySet()) {
			for (Appointment obj : entry.getValue()) {
				if (obj.getSpecialist().equals(specialist)
						&& !(obj.getAppointmentStatus().equals(
								StatusCodes.STATUSCODE_FINISHED) || obj
								.getAppointmentStatus().equals(
										StatusCodes.STATUSCODE_CANCELLED))) {
					specialistSppointments.add(obj);
				}
			}
		}

		return specialistSppointments;
	}

	public Integer getHourIntegerValue(String hourStringValue) {
		for (Iterator<Entry<Integer, String>> iterator = hourSlots.entrySet()
				.iterator(); iterator.hasNext();) {
			Map.Entry<Integer, String> entry = iterator.next();
			if (entry.getValue().equals(hourStringValue)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public boolean isSpecialistOccupied(String specialsit) {
		for (Entry<Integer, List<Appointment>> entry : appointments.entrySet()) {
			for (Appointment obj : entry.getValue()) {
				if (obj.getSpecialist().equals(specialsit)
						&& obj.getAppointmentStatus().equals(
								StatusCodes.STATUSCODE_INPROGRESS)) {
					return true;
				}
			}

		}
		return false;
	}
}
