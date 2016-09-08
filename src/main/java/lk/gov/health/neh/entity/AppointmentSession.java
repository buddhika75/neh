/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import lk.gov.health.neh.enums.AppointmentSessionDateType;
import lk.gov.health.neh.enums.AppointmentSessionType;
import lk.gov.health.neh.enums.Weekday;

/**
 *
 * @author User
 */
@Entity
public class AppointmentSession implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    Unit unit;
    @ManyToOne
    Ward ward;
    @ManyToOne
    Consultant consultant;
    String sessionName;
    AppointmentSessionType type;
    AppointmentSessionDateType dateType;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date sessionDate;
    Weekday sessionWeekday;
    int weekOfMonth;
    @Temporal(javax.persistence.TemporalType.TIME)
    Date sessionFrom;
    @Temporal(javax.persistence.TemporalType.TIME)
    Date sessionTo;
    int numberOfAppointments;
    int durationInMinutes;
    int additionalManagerAppointments;
    int additionalSuperUserAppointments;
    int durationBlockNumber;

    public int getDurationBlockNumber() {
        return durationBlockNumber;
    }

    public void setDurationBlockNumber(int durationBlockNumber) {
        this.durationBlockNumber = durationBlockNumber;
    }
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public Consultant getConsultant() {
        return consultant;
    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public AppointmentSessionType getType() {
        return type;
    }

    public void setType(AppointmentSessionType type) {
        this.type = type;
    }

    public AppointmentSessionDateType getDateType() {
        return dateType;
    }

    public void setDateType(AppointmentSessionDateType dateType) {
        this.dateType = dateType;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public Weekday getSessionWeekday() {
        return sessionWeekday;
    }

    public void setSessionWeekday(Weekday sessionWeekday) {
        this.sessionWeekday = sessionWeekday;
    }

    public int getWeekOfMonth() {
        return weekOfMonth;
    }

    public void setWeekOfMonth(int weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }

    public Date getSessionFrom() {
        return sessionFrom;
    }

    public void setSessionFrom(Date sessionFrom) {
        this.sessionFrom = sessionFrom;
    }

    public Date getSessionTo() {
        return sessionTo;
    }

    public void setSessionTo(Date sessionTo) {
        this.sessionTo = sessionTo;
    }

    public int getNumberOfAppointments() {
        return numberOfAppointments;
    }

    public void setNumberOfAppointments(int numberOfAppointments) {
        this.numberOfAppointments = numberOfAppointments;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public int getAdditionalManagerAppointments() {
        return additionalManagerAppointments;
    }

    public void setAdditionalManagerAppointments(int additionalManagerAppointments) {
        this.additionalManagerAppointments = additionalManagerAppointments;
    }

    public int getAdditionalSuperUserAppointments() {
        return additionalSuperUserAppointments;
    }

    public void setAdditionalSuperUserAppointments(int additionalSuperUserAppointments) {
        this.additionalSuperUserAppointments = additionalSuperUserAppointments;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppointmentSession)) {
            return false;
        }
        AppointmentSession other = (AppointmentSession) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.health.neh.entity.AppointmentSession[ id=" + id + " ]";
    }
    
}
