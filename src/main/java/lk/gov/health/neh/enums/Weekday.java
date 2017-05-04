/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.enums;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author User
 */
public enum Weekday {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday;

    public static Weekday getWeekday(Date date) {
        Calendar temCal = Calendar.getInstance();
        temCal.setTime(date);
        int weekDay = temCal.get(Calendar.DAY_OF_WEEK);
        switch (weekDay) {
            case Calendar.MONDAY:
                return Monday;
            case Calendar.TUESDAY:
                return Tuesday;
            case Calendar.WEDNESDAY:
                return Wednesday;
            case Calendar.THURSDAY:
                return Thursday;
            case Calendar.FRIDAY:
                return Friday;
            case Calendar.SATURDAY:
                return Saturday;
            case Calendar.SUNDAY:
                return Sunday;
            default:
                break;
        }
        return null;
    }

}
