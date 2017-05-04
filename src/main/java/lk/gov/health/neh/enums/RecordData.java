/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.enums;

/**
 *
 * @author User
 */
public class RecordData {
    String name;
    Long value;

    public RecordData(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    public RecordData() {
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
    
    
    
}
