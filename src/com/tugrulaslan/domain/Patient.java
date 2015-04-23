package com.tugrulaslan.domain;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Patient {

    private Integer hospitalId;
    private String name;

    public Patient(Integer hospitalId, String name) {
        this.hospitalId = hospitalId;
        this.name = name;
    }

    public Patient() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Patient{" + "hospitalId=" + hospitalId + ", name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.hospitalId);
        hash = 17 * hash + Objects.hashCode(this.name);
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
        final Patient other = (Patient) obj;
        if (!Objects.equals(this.hospitalId, other.hospitalId)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

}
