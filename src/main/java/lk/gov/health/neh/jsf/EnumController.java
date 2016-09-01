/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.jsf;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.bean.ApplicationScoped;
import lk.gov.health.neh.enums.AppointmentSessionDateType;
import lk.gov.health.neh.enums.AppointmentSessionType;
import lk.gov.health.neh.enums.ConsultantRole;
import lk.gov.health.neh.enums.ItemType;
import lk.gov.health.neh.enums.Nationality;
import lk.gov.health.neh.enums.Religion;
import lk.gov.health.neh.enums.Sex;
import lk.gov.health.neh.enums.Title;
import lk.gov.health.neh.enums.UserRole;
import lk.gov.health.neh.enums.Weekday;

/**
 *
 * @author buddhika
 */
@Named
@ApplicationScoped
public class EnumController implements Serializable{

    /**
     * Creates a new instance of EnumController
     */
    public EnumController() {
    }

    public AppointmentSessionType[] getAppointmentSessionTypes() {
        return AppointmentSessionType.values();
    }

    public AppointmentSessionDateType[] getAppointmentSessionDateTypes() {
        return AppointmentSessionDateType.values();
    }

    public Weekday[] getWeekdays() {
        return Weekday.values();
    }

    public Title[] getTitles() {
        return Title.values();
    }

    public Religion[] getReligions() {
        return Religion.values();
    }

    public UserRole[] getUserRoles() {
        return UserRole.values();
    }

    public Nationality[] getNationalities() {
        return Nationality.values();
    }

    public ConsultantRole[] getConsultantRoles() {
        return ConsultantRole.values();
    }

    public Sex[] getSex() {
        return Sex.values();
    }

    public ItemType[] getItemTypes() {
        return ItemType.values();
    }
}
