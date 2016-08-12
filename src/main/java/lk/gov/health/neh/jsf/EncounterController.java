package lk.gov.health.neh.jsf;

import lk.gov.health.neh.entity.Encounter;
import lk.gov.health.neh.jsf.util.JsfUtil;
import lk.gov.health.neh.jsf.util.JsfUtil.PersistAction;
import lk.gov.health.neh.session.EncounterFacade;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.persistence.TemporalType;
import lk.gov.health.neh.enums.EncounterType;
import lk.gov.health.neh.enums.RecordData;
import lk.gov.health.neh.enums.Sex;

@ManagedBean(name = "encounterController")
@SessionScoped
public class EncounterController implements Serializable {

    @EJB
    private lk.gov.health.neh.session.EncounterFacade ejbFacade;
    private List<Encounter> items = null;
    private Encounter selected;

    Long maleCount;
    Long femaleCount;
    Long otherSexCount;
    Long missingSexCount;
    Long childrenCount;
    Long adultCount;
    Long opdCount;
    Long casultyCount;
    Long maleChildrenCount;
    Long femaleChildrenCount;
    Long maleAdultCount;
    Long femaleAdultCount;
    Long totalCount;
    Date fromDate;
    Date toDate;
    List<RecordData> recordData;
    String dateString;
    String headerString;

    public String visitCountByGender() {
        recordData = new ArrayList<RecordData>();
        
        maleCount = visitCount(null, Sex.Male, fromDate, toDate);
        RecordData rmc = new RecordData("Males",maleCount);
        recordData.add(rmc);
        
        femaleCount = visitCount(null, Sex.Female, fromDate, toDate);
        RecordData rmf = new RecordData("Females",femaleCount);
        recordData.add(rmf);
        
        otherSexCount = visitCount(null, Sex.Other, fromDate, toDate);
        RecordData osc = new RecordData("Other",otherSexCount);
        recordData.add(osc);
        
        totalCount = visitCount(null, null, fromDate, toDate);
        
        missingSexCount = totalCount - (maleCount+femaleCount+otherSexCount);
        RecordData mc = new RecordData("Missing",missingSexCount);
        recordData.add(mc);
        
        RecordData tc = new RecordData("Total",totalCount);
        recordData.add(tc);
        
        return "";
    }

    public Long visitCount(EncounterType type, Sex sex, Date fromDate, Date toDate) {
        headerString = "OPD & Casulty Visit Counts";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy"); 
        dateString = "From " + sdf. format(fromDate) + " to "+ sdf. format(toDate) ;
        
        String jpql = "select count(e) "
                + " from Encounter e "
                + " where e.encounterDate between :fd and :td";
        Map m = new HashMap();
        m.put("fd", fromDate);
        m.put("td", toDate);
        if (type != null) {
            jpql += "e.encounterType=:et ";
            m.put("et", type);
        }
        if (sex != null) {
            jpql += " and e.patient.sex=:sex";
            m.put("sex", sex);
        }
        return getFacade().findAggregateLong(jpql, m, TemporalType.DATE);
    }

    public List<RecordData> getRecordData() {
        return recordData;
    }

    public void setRecordData(List<RecordData> recordData) {
        this.recordData = recordData;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getHeaderString() {
        return headerString;
    }

    public void setHeaderString(String headerString) {
        this.headerString = headerString;
    }

    
    
    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getOtherSexCount() {
        return otherSexCount;
    }

    public void setOtherSexCount(Long otherSexCount) {
        this.otherSexCount = otherSexCount;
    }

    public Long getMissingSexCount() {
        return missingSexCount;
    }

    public void setMissingSexCount(Long missingSexCount) {
        this.missingSexCount = missingSexCount;
    }

    
    
    
    public Long getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(Long maleCount) {
        this.maleCount = maleCount;
    }

    public Long getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(Long femaleCount) {
        this.femaleCount = femaleCount;
    }

    public Long getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(Long childrenCount) {
        this.childrenCount = childrenCount;
    }

    public Long getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Long adultCount) {
        this.adultCount = adultCount;
    }

    public Long getOpdCount() {
        return opdCount;
    }

    public void setOpdCount(Long opdCount) {
        this.opdCount = opdCount;
    }

    public Long getCasultyCount() {
        return casultyCount;
    }

    public void setCasultyCount(Long casultyCount) {
        this.casultyCount = casultyCount;
    }

    public Long getMaleChildrenCount() {
        return maleChildrenCount;
    }

    public void setMaleChildrenCount(Long maleChildrenCount) {
        this.maleChildrenCount = maleChildrenCount;
    }

    public Long getFemaleChildrenCount() {
        return femaleChildrenCount;
    }

    public void setFemaleChildrenCount(Long femaleChildrenCount) {
        this.femaleChildrenCount = femaleChildrenCount;
    }

    public Long getMaleAdultCount() {
        return maleAdultCount;
    }

    public void setMaleAdultCount(Long maleAdultCount) {
        this.maleAdultCount = maleAdultCount;
    }

    public Long getFemaleAdultCount() {
        return femaleAdultCount;
    }

    public void setFemaleAdultCount(Long femaleAdultCount) {
        this.femaleAdultCount = femaleAdultCount;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public EncounterController() {
    }

    public Encounter getSelected() {
        return selected;
    }

    public void setSelected(Encounter selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private EncounterFacade getFacade() {
        return ejbFacade;
    }

    public Encounter prepareCreate() {
        selected = new Encounter();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EncounterCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EncounterUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EncounterDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Encounter> getItems() {
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

    public List<Encounter> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Encounter> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Encounter.class)
    public static class EncounterControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EncounterController controller = (EncounterController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "encounterController");
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
            if (object instanceof Encounter) {
                Encounter o = (Encounter) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Encounter.class.getName()});
                return null;
            }
        }

    }

}
