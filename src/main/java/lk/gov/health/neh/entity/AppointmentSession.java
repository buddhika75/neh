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
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
