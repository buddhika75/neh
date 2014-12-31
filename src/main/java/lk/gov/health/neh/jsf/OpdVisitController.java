package lk.gov.health.neh.jsf;

import lk.gov.health.neh.entity.OpdVisit;
import lk.gov.health.neh.jsf.util.JsfUtil;
import lk.gov.health.neh.jsf.util.JsfUtil.PersistAction;
import lk.gov.health.neh.session.OpdVisitFacade;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import lk.gov.health.neh.entity.Patient;
import lk.gov.health.neh.entity.Unit;
import lk.gov.health.neh.enums.EncounterType;

@ManagedBean(name = "opdVisitController")
@SessionScoped
public class OpdVisitController implements Serializable {

    @EJB
    private lk.gov.health.neh.session.OpdVisitFacade ejbFacade;
    private List<OpdVisit> items = null;
    private OpdVisit selected;

    Date fromDate;
    Date toDate;
    List<OpdVisit> visits;
    Unit casultyUnit;

    public Date getFromDate() {
        if (fromDate == null) {
            fromDate = new Date();
        }
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        if (toDate == null) {
            toDate = new Date();
        }
        return toDate;
    }

    public void listVisits() {
        String j;
        Map m = new HashMap();
        m.put("fd", fromDate);
        m.put("td", toDate);
        j = "select v from OpdVisit v where v.encounterDate between :fd and :td";
        visits = getFacade().findBySQL(j, m);
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public List<OpdVisit> getVisits() {
        return visits;
    }

    public void setVisits(List<OpdVisit> visits) {
        this.visits = visits;
    }

    public OpdVisitController() {
    }

    public OpdVisit getSelected() {
        return selected;
    }

    public void setSelected(OpdVisit selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private OpdVisitFacade getFacade() {
        return ejbFacade;
    }

    public OpdVisit prepareCreate() {
        selected = new OpdVisit();
        initializeEmbeddableKey();
        return selected;
    }

    public String addNewOpdVisit() {
        selected = new OpdVisit();
        Patient pt = new Patient();
        selected.setPatient(pt);
        selected.setIntSerialNo(annualCount().intValue());
        selected.setSerialNo(stringConversionOfSerialNo(selected.getIntSerialNo()));
        selected.setEncounterType(EncounterType.OpdVisit);
        selected.setEncounterDate(new Date());
        initializeEmbeddableKey();
        return "/opdVisit/opd_visit";
    }

//    public Unit findNextUnit(){
//        String j;
//        Map m = new HashMap();
//        m.put("ed", new Date());
//        j="select o from OpdVisit o where o.encounterDate=ed" ;
//        List<OpdVisit> vs = getFacade().findBySQL(j, m);
//        
//        for (OpdVisit v:vs){
//            
//        }
//    }
    
    public String addNewCasultyVisit() {
        selected = new OpdVisit();
        Patient pt = new Patient();
        selected.setPatient(pt);
        selected.setIntSerialNo(annualCount().intValue());
        selected.setSerialNo(stringConversionOfSerialNo(selected.getIntSerialNo()));
        selected.setEncounterDate(new Date());
        selected.setEncounterType(EncounterType.Casulty);
        initializeEmbeddableKey();
        return "/opdVisit/casulty_visit";
    }

    public String addNewSpecialUnitVisit() {
        selected = new OpdVisit();
        Patient pt = new Patient();
        selected.setPatient(pt);
        selected.setIntSerialNo(annualCount().intValue());
        selected.setSerialNo(stringConversionOfSerialNo(selected.getIntSerialNo()));
        selected.setEncounterDate(new Date());
        selected.setEncounterType(EncounterType.SpecialUnitVisit);
        initializeEmbeddableKey();
        return "/opdVisit/special_unit_visit";
    }

    public String stringConversionOfSerialNo(int sn) {
        Calendar c = Calendar.getInstance();
        return sn + "/" + c.get(Calendar.YEAR);
    }

    public Long annualCount() {
        String j = "Select count(o) from OpdVisit o where o.encounterDate between :fd and :td ";
        Map m = new HashMap();
        m.put("fd", CommonController.firstDateOfYear(new Date()));
        m.put("td", CommonController.lastDateOfYear(new Date()));
        return getFacade().findLongByJpql(j, m);
    }

    public Long todaysCount() {
        String j = "Select count(o) from OpdVisit o where o.encounterDate=:ed ";
        Map m = new HashMap();
        m.put("ed", new Date());
        return getFacade().findLongByJpql(j, m);
    }

    public void updateDailyNo() {
        if (selected == null || selected.getUnit() == null) {
            return;
        }
        selected.setIntDailyNo(todaysCount(selected.getUnit()).intValue());
        selected.setDailyNo(selected.getUnit().getCode() + selected.getIntDailyNo());
        System.out.println("selected.getDailyNo() = " + selected.getDailyNo());
    }

    public Long todaysCount(Unit u) {
        String j = "Select count(o) from OpdVisit o where o.encounterDate=:ed and o.unit=:u ";
        Map m = new HashMap();
        m.put("ed", new Date());
        m.put("u", u);
        return getFacade().findLongByJpql(j, m);
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("OpdVisitCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("OpdVisitUpdated"));
    }

    public String registerOpdVisit() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing to register");
            return "";
        }
        if (selected.getEncounterType() == EncounterType.Casulty) {
            casultyUnit = selected.getUnit();
        }
        if (selected.getId() == null) {

            getFacade().create(selected);
            JsfUtil.addSuccessMessage("Registered");
            if (selected.getEncounterType() == EncounterType.OpdVisit) {
                return addNewOpdVisit();
            } else if (selected.getEncounterType() == EncounterType.Casulty) {
                return addNewCasultyVisit();
            } else if (selected.getEncounterType() == EncounterType.SpecialUnitVisit) {
                return addNewSpecialUnitVisit();
            }
        } else {
            getFacade().edit(selected);
            JsfUtil.addSuccessMessage("Updated");
            return "";
        }
        return  "";
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("OpdVisitDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<OpdVisit> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<OpdVisit> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<OpdVisit> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = OpdVisit.class)
    public static class OpdVisitControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            OpdVisitController controller = (OpdVisitController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "opdVisitController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof OpdVisit) {
                OpdVisit o = (OpdVisit) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), OpdVisit.class.getName()});
                return null;
            }
        }

    }

}
