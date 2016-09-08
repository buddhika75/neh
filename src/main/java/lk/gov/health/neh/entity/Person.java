/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import lk.gov.health.neh.enums.Nationality;
import lk.gov.health.neh.enums.Religion;
import lk.gov.health.neh.enums.Sex;
import lk.gov.health.neh.enums.Title;
import lk.gov.health.neh.enums.UserRole;

/**
 *
 * @author buddhika
 */
@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String name;
    String nic;
    @Enumerated(EnumType.STRING)
    Sex sex;
    @Enumerated(EnumType.STRING)
    Title title;
    @Enumerated(EnumType.STRING)
    Nationality nationality;
    @Enumerated(EnumType.STRING)
    Religion religion;

    boolean retired;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date dob;
    String age;
    @Transient
    String nameWithTitle;
    @Lob
    String address;
    @ManyToOne
    Item occupation;
    @ManyToOne
    Item district;
    String telephoneNo;
    @ManyToOne
    Unit unit;

    boolean registered;
    
    @Enumerated(EnumType.STRING)
    UserRole userRole;

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    
    String clinicFileNo;
    int clinicFileNoYearlySerial;
    int clinicFileNoYear;
    String opdNo;
    String phn;

    public int getClinicFileNoYearlySerial() {
        return clinicFileNoYearlySerial;
    }

    public void setClinicFileNoYearlySerial(int clinicFileNoYearlySerial) {
        this.clinicFileNoYearlySerial = clinicFileNoYearlySerial;
    }

    public int getClinicFileNoYear() {
        return clinicFileNoYear;
    }

    public void setClinicFileNoYear(int clinicFileNoYear) {
        this.clinicFileNoYear = clinicFileNoYear;
    }
    
    

    public String getClinicFileNo() {
        return clinicFileNo;
    }

    public void setClinicFileNo(String clinicFileNo) {
        this.clinicFileNo = clinicFileNo;
    }

    public String getOpdNo() {
        return opdNo;
    }

    public void setOpdNo(String opdNo) {
        this.opdNo = opdNo;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }
    
    
    
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNameWithTitle() {
        if (title != null) {
            return title + " " + name;
        }else{
            return name;
        }
    }

    public void setNameWithTitle(String nameWithTitle) {
        this.nameWithTitle = nameWithTitle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Item getOccupation() {
        if (occupation == null) {
            occupation = new Item();
        }
        return occupation;
    }

    public void setOccupation(Item occupation) {
        this.occupation = occupation;
    }

    public Item getDistrict() {
        return district;
    }

    public void setDistrict(Item district) {
        this.district = district;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
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
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

}
