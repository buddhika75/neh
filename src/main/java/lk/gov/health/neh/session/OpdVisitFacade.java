/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.health.neh.entity.OpdVisit;

/**
 *
 * @author buddhika
 */
@Stateless
public class OpdVisitFacade extends AbstractFacade<OpdVisit> {
    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OpdVisitFacade() {
        super(OpdVisit.class);
    }
    
}
