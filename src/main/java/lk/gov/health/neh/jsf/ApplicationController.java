/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.jsf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lk.gov.health.neh.entity.Patient;
import lk.gov.health.neh.entity.Unit;
import lk.gov.health.neh.enums.EncounterType;
import lk.gov.health.neh.enums.UnitLastFileNumber;
import lk.gov.health.neh.session.PatientFacade;
import lk.gov.health.neh.session.UnitFacade;

/**
 *
 * @author User
 */
@Named(value = "applicationController")
@ApplicationScoped
public class ApplicationController {

    List<UnitLastFileNumber> lastFileNumbers = null;
    @EJB
    UnitFacade unitFacade;
    @EJB
    PatientFacade patientFacade;

    /**
     * Creates a new instance of ApplicationController
     */
    public ApplicationController() {
    }

    public UnitLastFileNumber giveAFileNumber(Unit unit) {
        System.out.println("giveAFileNumber " );
        UnitLastFileNumber u = null;
        for (UnitLastFileNumber n : getLastFileNumbers()) {
            if (n.getUnit().equals(unit)) {
                System.out.println("n.getUnit().equals(unit");
                
                u=n;
            }
        }
        if (u == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        if (u.getYearValue() != c.get(Calendar.YEAR)) {
            u.setYearValue(c.get(Calendar.YEAR));
            u.setAnnualCount(0);
        } else {
            u.setAnnualCount(u.getAnnualCount() + 1);
        }
        return u;
    }

    public List<UnitLastFileNumber> fillFileNumbers() {
        System.out.println("fillFileNumbers");
        List<UnitLastFileNumber> tmLfn = new ArrayList<UnitLastFileNumber>();
        String j;
        HashMap m = new HashMap();
        j = "Select u from Unit u";
        List<Unit> units = unitFacade.findBySQL(j);
        System.out.println("units = " + units);
        for (Unit u : units) {
            System.out.println("u = " + u);
            UnitLastFileNumber ul = new UnitLastFileNumber();
            ul.setUnit(u);
            j = "Select p from Patient p "
                    + " where p.registered=:reg "
                    + " and p.unit=:unit "
                    + " order by p.id desc";
            m = new HashMap();
            m.put("unit", u);
            m.put("reg", true);
            Patient p = patientFacade.findFirstBySQL(j, m);
            System.out.println("p = " + p);
            Calendar c = Calendar.getInstance();
            if (p == null) {
                System.out.println("p == null");
                ul.setFileNo("");
                ul.setYearValue(c.get(Calendar.YEAR));
                ul.setAnnualCount(0);
            } else if (p.getClinicFileNoYear() != c.get(Calendar.YEAR)) {
                System.out.println("p.getClinicFileNoYear() != c.get(Calendar.YEAR)");
                ul.setFileNo("");
                ul.setYearValue(c.get(Calendar.YEAR));
                ul.setAnnualCount(0);
            } else {
                System.out.println("else");
                ul.setFileNo(p.getClinicFileNo());
                ul.setYearValue(c.get(Calendar.YEAR));
                ul.setAnnualCount(p.getClinicFileNoYearlySerial());
            }
            tmLfn.add(ul);
        }
        return tmLfn;
    }

    public List<UnitLastFileNumber> getLastFileNumbers() {
        System.out.println("getLastFileNumbers");
        if (lastFileNumbers == null) {
            lastFileNumbers = fillFileNumbers();
        }
        return lastFileNumbers;
    }

    public void setLastFileNumbers(List<UnitLastFileNumber> lastFileNumbers) {
        this.lastFileNumbers = lastFileNumbers;
    }

}
