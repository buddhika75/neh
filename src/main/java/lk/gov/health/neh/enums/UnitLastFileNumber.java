/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.enums;

import lk.gov.health.neh.entity.Unit;

/**
 *
 * @author User
 */
public class UnitLastFileNumber {

    Unit unit;
    int annualCount;
    int yearValue;
    String fileNo;

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public int getAnnualCount() {
        return annualCount;
    }

    public void setAnnualCount(int annualCount) {
        this.annualCount = annualCount;
    }

    public int getYearValue() {
        return yearValue;
    }

    public void setYearValue(int yearValue) {
        this.yearValue = yearValue;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }
    
    
    
}
