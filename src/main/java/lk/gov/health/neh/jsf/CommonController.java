/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.jsf;

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

}
