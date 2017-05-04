/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import lk.gov.health.neh.enums.EncounterType;

/**
 *
 * @author buddhika
 */
@Entity
public class Encounter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    Patient patient;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date encounterDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date encounterTime;
    @Enumerated(EnumType.STRING)
    EncounterType encounterType;
    String dailyNo;
    int intDailyNo;
    String serialNo;
    int intSerialNo;
    int yearNo;
    @ManyToOne
    Ward ward;
    @ManyToOne
    Unit unit;
    @ManyToOne
    ClosedUnit closedUnit;
    @Lob
    String presentingComplaint = "";
    @Lob
    String diagnosis = "";
    String varnc = "";
    String valnc = "";
    String varc = "";
    String valc = "";

    String nonAidREye;
    String nonAidLEye;
    String cglassREye;
    String cglassLEye;

    Boolean dm;
    Boolean hpt;
    Boolean ihd;
    Boolean ba;
    Boolean allergy;

    Boolean trauma;
    Boolean glaucoma;
    Boolean laserRx;
    Boolean avastinIng;
    Boolean pst;
    Boolean cataractSx;
    Boolean trab;
    Boolean tppv;
    Boolean pkp;
    Boolean squint;

    String pstmediOther = "";
    String ocularHxOther = "";
    String sugicalHxOther = "";
    long serialNumber;

    @ManyToOne
    AppointmentSession appointmentSession;
    @ManyToOne
    AppointmentTimeSlot appointmentTimeSlot;
    boolean cancelled;

    @ManyToOne
    Encounter parent;

    public AppointmentTimeSlot getAppointmentTimeSlot() {
        return appointmentTimeSlot;
    }

    public void setAppointmentTimeSlot(AppointmentTimeSlot appointmentTimeSlot) {
        this.appointmentTimeSlot = appointmentTimeSlot;
    }

    public Date getEncounterTime() {
        return encounterTime;
    }

    public void setEncounterTime(Date encounterTime) {
        this.encounterTime = encounterTime;
    }
    
    

    public Encounter getParent() {
        return parent;
    }

    public void setParent(Encounter parent) {
        this.parent = parent;
    }
    
    
    
    public AppointmentSession getAppointmentSession() {
        return appointmentSession;
    }

    public void setAppointmentSession(AppointmentSession appointmentSession) {
        this.appointmentSession = appointmentSession;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    

    public long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCglassREye() {
        return cglassREye;
    }

    public void setCglassREye(String cglassREye) {
        this.cglassREye = cglassREye;
    }

    public String getCglassLEye() {
        return cglassLEye;
    }

    public void setCglassLEye(String cglassLEye) {
        this.cglassLEye = cglassLEye;
    }

    public Boolean getDm() {
        return dm;
    }

    public void setDm(Boolean dm) {
        this.dm = dm;
    }

    public Boolean getHpt() {
        return hpt;
    }

    public void setHpt(Boolean hpt) {
        this.hpt = hpt;
    }

    public Boolean getIhd() {
        return ihd;
    }

    public void setIhd(Boolean ihd) {
        this.ihd = ihd;
    }

    public Boolean getBa() {
        return ba;
    }

    public void setBa(Boolean ba) {
        this.ba = ba;
    }

    public Boolean getAllergy() {
        return allergy;
    }

    public void setAllergy(Boolean allergy) {
        this.allergy = allergy;
    }

    public Boolean getTrauma() {
        return trauma;
    }

    public void setTrauma(Boolean trauma) {
        this.trauma = trauma;
    }

    public Boolean getGlaucoma() {
        return glaucoma;
    }

    public void setGlaucoma(Boolean glaucoma) {
        this.glaucoma = glaucoma;
    }

    public Boolean getLaserRx() {
        return laserRx;
    }

    public void setLaserRx(Boolean laserRx) {
        this.laserRx = laserRx;
    }

    public Boolean getAvastinIng() {
        return avastinIng;
    }

    public void setAvastinIng(Boolean avastinIng) {
        this.avastinIng = avastinIng;
    }

    public Boolean getPst() {
        return pst;
    }

    public void setPst(Boolean pst) {
        this.pst = pst;
    }

    public Boolean getCataractSx() {
        return cataractSx;
    }

    public void setCataractSx(Boolean cataractSx) {
        this.cataractSx = cataractSx;
    }

    public Boolean getTrab() {
        return trab;
    }

    public void setTrab(Boolean trab) {
        this.trab = trab;
    }

    public Boolean getTppv() {
        return tppv;
    }

    public void setTppv(Boolean tppv) {
        this.tppv = tppv;
    }

    public Boolean getPkp() {
        return pkp;
    }

    public void setPkp(Boolean pkp) {
        this.pkp = pkp;
    }

    public Boolean getSquint() {
        return squint;
    }

    public void setSquint(Boolean squint) {
        this.squint = squint;
    }

    public String getPstmediOther() {
        return pstmediOther;
    }

    public void setPstmediOther(String pstmediOther) {
        this.pstmediOther = pstmediOther;
    }

    public String getOcularHxOther() {
        return ocularHxOther;
    }

    public void setOcularHxOther(String ocularHxOther) {
        this.ocularHxOther = ocularHxOther;
    }

    public String getSugicalHxOther() {
        return sugicalHxOther;
    }

    public void setSugicalHxOther(String sugicalHxOther) {
        this.sugicalHxOther = sugicalHxOther;
    }

    public EncounterType getEncounterType() {
        return encounterType;
    }

    public void setEncounterType(EncounterType encounterType) {
        this.encounterType = encounterType;
    }

    public String getNonAidREye() {
        return nonAidREye;
    }

    public void setNonAidREye(String nonAidREye) {
        this.nonAidREye = nonAidREye;
    }

    public String getNonAidLEye() {
        return nonAidLEye;
    }

    public void setNonAidLEye(String nonAidLEye) {
        this.nonAidLEye = nonAidLEye;
    }

    public String getVarnc() {
        return varnc;
    }

    public void setVarnc(String varnc) {
        this.varnc = varnc;
    }

    public String getValnc() {
        return valnc;
    }

    public void setValnc(String valnc) {
        this.valnc = valnc;
    }

    public String getVarc() {
        return varc;
    }

    public void setVarc(String varc) {
        this.varc = varc;
    }

    public String getValc() {
        return valc;
    }

    public void setValc(String valc) {
        this.valc = valc;
    }

    public String getPresentingComplaint() {
        return presentingComplaint;
    }

    public void setPresentingComplaint(String presentingComplaint) {
        this.presentingComplaint = presentingComplaint;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Patient getPatient() {
        if (patient == null) {
            patient = new Patient();
        }
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getEncounterDate() {
        return encounterDate;
    }

    public void setEncounterDate(Date encounterDate) {
        this.encounterDate = encounterDate;
    }

    public String getDailyNo() {
        return dailyNo;
    }

    public void setDailyNo(String dailyNo) {
        this.dailyNo = dailyNo;
    }

    public int getIntDailyNo() {
        return intDailyNo;
    }

    public void setIntDailyNo(int intDailyNo) {
        this.intDailyNo = intDailyNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public int getIntSerialNo() {
        return intSerialNo;
    }

    public void setIntSerialNo(int intSerialNo) {
        this.intSerialNo = intSerialNo;
    }

    public int getYearNo() {
        return yearNo;
    }

    public void setYearNo(int yearNo) {
        this.yearNo = yearNo;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public ClosedUnit getClosedUnit() {
        return closedUnit;
    }

    public void setClosedUnit(ClosedUnit closedUnit) {
        this.closedUnit = closedUnit;
    }

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
        if (!(object instanceof Encounter)) {
            return false;
        }
        Encounter other = (Encounter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.health.neh.entity.Encounter[ id=" + id + " ]";
    }

}
