/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.jsf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import lk.gov.health.neh.enums.ConsultantRole;
import lk.gov.health.neh.enums.ItemType;
import lk.gov.health.neh.enums.Nationality;
import lk.gov.health.neh.enums.Religion;
import lk.gov.health.neh.enums.Sex;
import lk.gov.health.neh.enums.Title;
import lk.gov.health.neh.enums.UserRole;

/**
 *
 * @author buddhika
 */
@ManagedBean
@ApplicationScoped
public class EnumController {

    /**
     * Creates a new instance of EnumController
     */
    public EnumController() {
    }
    
    public Title[] getTitles(){
        return Title.values();
    }
    
    public Religion[] getReligions(){
        return Religion.values();
    }
    
     public UserRole[] getUserRoles(){
        return UserRole.values();
    }
    
    public Nationality[] getNationalities(){
        return Nationality.values();
    }
    
    public ConsultantRole[] getConsultantRoles(){
        return ConsultantRole.values();
    }
    
    public Sex[] getSex(){
        return Sex.values();
    }
    
    public ItemType[] getItemTypes(){
        return ItemType.values();
    }
}
