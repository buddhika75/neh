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
    String CGlassREye;
    String CGlassLEye;
    
    Boolean DM;
    Boolean HPT;
    Boolean IHD;
    Boolean BA;
    Boolean Allergy;
    
    
    
   Boolean  Trauma;
   Boolean Glaucoma;
   Boolean LaserRx;
   Boolean AvastinIng;
   Boolean PST;
   Boolean CataractSx;
   Boolean trab;
   Boolean TPPV;
   Boolean PKP;
   Boolean Squint;
   
   
   String pstmediOther ="";
   String OcularHxOther ="";
   String SugicalHxOther ="";
   
   
   
   
     String cnonAidREye;
    String cnonAidLEye;
    String cCGlassREye;
    String cCGlassLEye;
    
    Boolean cDM;
    Boolean cHPT;
    Boolean cIHD;
    Boolean cBA;
    Boolean cAllergy;
    
    
    
   Boolean  cTrauma;
   Boolean cGlaucoma;
   Boolean cLaserRx;
   Boolean cAvastinIng;
   Boolean cPST;
   Boolean cCataractSx;
   Boolean ctrab;
   Boolean cTPPV;
   Boolean cPKP;
   Boolean cSquint;
   
   
   String cpstmediOther ="";
   String cOcularHxOther ="";
   String cSugicalHxOther ="";

    public String getCnonAidREye() {
        return cnonAidREye;
    }

    public void setCnonAidREye(String cnonAidREye) {
        this.cnonAidREye = cnonAidREye;
    }

    public String getCnonAidLEye() {
        return cnonAidLEye;
    }

    public void setCnonAidLEye(String cnonAidLEye) {
        this.cnonAidLEye = cnonAidLEye;
    }

    public String getcCGlassREye() {
        return cCGlassREye;
    }

    public void setcCGlassREye(String cCGlassREye) {
        this.cCGlassREye = cCGlassREye;
    }

    public String getcCGlassLEye() {
        return cCGlassLEye;
    }

    public void setcCGlassLEye(String cCGlassLEye) {
        this.cCGlassLEye = cCGlassLEye;
    }

    public Boolean getcDM() {
        return cDM;
    }

    public void setcDM(Boolean cDM) {
        this.cDM = cDM;
    }

    public Boolean getcHPT() {
        return cHPT;
    }

    public void setcHPT(Boolean cHPT) {
        this.cHPT = cHPT;
    }

    public Boolean getcIHD() {
        return cIHD;
    }

    public void setcIHD(Boolean cIHD) {
        this.cIHD = cIHD;
    }

    public Boolean getcBA() {
        return cBA;
    }

    public void setcBA(Boolean cBA) {
        this.cBA = cBA;
    }

    public Boolean getcAllergy() {
        return cAllergy;
    }

    public void setcAllergy(Boolean cAllergy) {
        this.cAllergy = cAllergy;
    }

    public Boolean getcTrauma() {
        return cTrauma;
    }

    public void setcTrauma(Boolean cTrauma) {
        this.cTrauma = cTrauma;
    }

    public Boolean getcGlaucoma() {
        return cGlaucoma;
    }

    public void setcGlaucoma(Boolean cGlaucoma) {
        this.cGlaucoma = cGlaucoma;
    }

    public Boolean getcLaserRx() {
        return cLaserRx;
    }

    public void setcLaserRx(Boolean cLaserRx) {
        this.cLaserRx = cLaserRx;
    }

    public Boolean getcAvastinIng() {
        return cAvastinIng;
    }

    public void setcAvastinIng(Boolean cAvastinIng) {
        this.cAvastinIng = cAvastinIng;
    }

    public Boolean getcPST() {
        return cPST;
    }

    public void setcPST(Boolean cPST) {
        this.cPST = cPST;
    }

    public Boolean getcCataractSx() {
        return cCataractSx;
    }

    public void setcCataractSx(Boolean cCataractSx) {
        this.cCataractSx = cCataractSx;
    }

    public Boolean getCtrab() {
        return ctrab;
    }

    public void setCtrab(Boolean ctrab) {
        this.ctrab = ctrab;
    }

    public Boolean getcTPPV() {
        return cTPPV;
    }

    public void setcTPPV(Boolean cTPPV) {
        this.cTPPV = cTPPV;
    }

    public Boolean getcPKP() {
        return cPKP;
    }

    public void setcPKP(Boolean cPKP) {
        this.cPKP = cPKP;
    }

    public Boolean getcSquint() {
        return cSquint;
    }

    public void setcSquint(Boolean cSquint) {
        this.cSquint = cSquint;
    }

    public String getCpstmediOther() {
        return cpstmediOther;
    }

    public void setCpstmediOther(String cpstmediOther) {
        this.cpstmediOther = cpstmediOther;
    }

    public String getcOcularHxOther() {
        return cOcularHxOther;
    }

    public void setcOcularHxOther(String cOcularHxOther) {
        this.cOcularHxOther = cOcularHxOther;
    }

    public String getcSugicalHxOther() {
        return cSugicalHxOther;
    }

    public void setcSugicalHxOther(String cSugicalHxOther) {
        this.cSugicalHxOther = cSugicalHxOther;
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

    public String getCGlassREye() {
        return CGlassREye;
    }

    public void setCGlassREye(String CGlassREye) {
        this.CGlassREye = CGlassREye;
    }

    public String getCGlassLEye() {
        return CGlassLEye;
    }

    public void setCGlassLEye(String CGlassLEye) {
        this.CGlassLEye = CGlassLEye;
    }

    public Boolean getDM() {
        return DM;
    }

    public void setDM(Boolean DM) {
        this.DM = DM;
    }

    public Boolean getHPT() {
        return HPT;
    }

    public void setHPT(Boolean HPT) {
        this.HPT = HPT;
    }

    public Boolean getIHD() {
        return IHD;
    }

    public void setIHD(Boolean IHD) {
        this.IHD = IHD;
    }

    public Boolean getBA() {
        return BA;
    }

    public void setBA(Boolean BA) {
        this.BA = BA;
    }

    public Boolean getAllergy() {
        return Allergy;
    }

    public void setAllergy(Boolean Allergy) {
        this.Allergy = Allergy;
    }

    public Boolean getTrauma() {
        return Trauma;
    }

    public void setTrauma(Boolean Trauma) {
        this.Trauma = Trauma;
    }

    public Boolean getGlaucoma() {
        return Glaucoma;
    }

    public void setGlaucoma(Boolean Glaucoma) {
        this.Glaucoma = Glaucoma;
    }

    public Boolean getLaserRx() {
        return LaserRx;
    }

    public void setLaserRx(Boolean LaserRx) {
        this.LaserRx = LaserRx;
    }

    public Boolean getAvastinIng() {
        return AvastinIng;
    }

    public void setAvastinIng(Boolean AvastinIng) {
        this.AvastinIng = AvastinIng;
    }

    public Boolean getPST() {
        return PST;
    }

    public void setPST(Boolean PST) {
        this.PST = PST;
    }

    public Boolean getCataractSx() {
        return CataractSx;
    }

    public void setCataractSx(Boolean CataractSx) {
        this.CataractSx = CataractSx;
    }

    public Boolean getTrab() {
        return trab;
    }

    public void setTrab(Boolean trab) {
        this.trab = trab;
    }

    public Boolean getTPPV() {
        return TPPV;
    }

    public void setTPPV(Boolean TPPV) {
        this.TPPV = TPPV;
    }

    public Boolean getPKP() {
        return PKP;
    }

    public void setPKP(Boolean PKP) {
        this.PKP = PKP;
    }

    public Boolean getSquint() {
        return Squint;
    }

    public void setSquint(Boolean Squint) {
        this.Squint = Squint;
    }

    public String getPstmediOther() {
        return pstmediOther;
    }

    public void setPstmediOther(String pstmediOther) {
        this.pstmediOther = pstmediOther;
    }

    public String getOcularHxOther() {
        return OcularHxOther;
    }

    public void setOcularHxOther(String OcularHxOther) {
        this.OcularHxOther = OcularHxOther;
    }

    public String getSugicalHxOther() {
        return SugicalHxOther;
    }

    public void setSugicalHxOther(String SugicalHxOther) {
        this.SugicalHxOther = SugicalHxOther;
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
