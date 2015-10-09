/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.health.neh.entity.SerialNumber;

/**
 *
 * @author pdhs-sp
 */
@Stateless
public class SerialNumberFacade extends AbstractFacade<SerialNumber> {
    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SerialNumberFacade() {
        super(SerialNumber.class);
    }
    
}
