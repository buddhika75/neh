/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.jsf;

import java.util.Calendar;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import lk.gov.health.neh.enums.Sex;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 *
 * @author buddhika
 */
@ManagedBean
@SessionScoped
public class CommonController {

    /**
     * Creates a new instance of CommonController
     */
    public CommonController() {
    }

    public static String makeHash(String password) {
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        return passwordEncryptor.encryptPassword(password);
    }

    public static boolean checkPassword(String inputPassword, String encryptedPassword) {
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        if (passwordEncryptor.checkPassword(inputPassword, encryptedPassword)) {
            return true;
        } else {
            return false;
        }
    }
    public Sex[] getSexs(){
        return Sex.values();
    }

    public static Date firstDateOfYear(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE,0);
        c.set(Calendar.HOUR,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        return c.getTime();
    }
    
     public static Date lastDateOfYear(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        c.set(Calendar.DATE,31);
        c.set(Calendar.HOUR,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND,59);
        c.set(Calendar.MILLISECOND,999);
        return c.getTime();
    }
    
}
