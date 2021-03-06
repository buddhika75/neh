/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.neh.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lk.gov.health.neh.entity.Admission;
import lk.gov.health.neh.entity.AppointmentSession;

/**
 *
 * @author buddhika
 */
@Stateless
public class AppointmentSessionFacade extends AbstractFacade<AppointmentSession> {
    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AppointmentSessionFacade() {
        super(AppointmentSession.class);
    }
    
}
