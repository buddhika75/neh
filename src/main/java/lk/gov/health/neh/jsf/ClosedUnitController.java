package lk.gov.health.neh.jsf;

import lk.gov.health.neh.entity.ClosedUnit;
import lk.gov.health.neh.jsf.util.JsfUtil;
import lk.gov.health.neh.jsf.util.JsfUtil.PersistAction;
import lk.gov.health.neh.session.ClosedUnitFacade;

import java.io.Serializable;
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

@ManagedBean(name = "closedUnitController")
@SessionScoped
public class ClosedUnitController implements Serializable {

    @EJB
    private lk.gov.health.neh.session.ClosedUnitFacade ejbFacade;
    private List<ClosedUnit> items = null;
    private ClosedUnit selected;

    public ClosedUnitController() {
    }

    public ClosedUnit getSelected() {
        if(selected == null){
            selected = new ClosedUnit();
        }
        return selected;
    }

    public void setSelected(ClosedUnit selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ClosedUnitFacade getFacade() {
        return ejbFacade;
    }

    public ClosedUnit prepareCreate() {
        selected = new ClosedUnit();
        selected.setClosedDate(new Date());
        
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        String j;
        Map m = new HashMap();
        j="select c from ClosedUnit c where c.closedDate=:cd";
        m.put("cd", selected.getClosedDate());
        if(getFacade().findFirstBySQL(j, m)!=null){
            JsfUtil.addErrorMessage("Date already added. Please edit and change");
            return;
        }
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ClosedUnitCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ClosedUnitUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ClosedUnitDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ClosedUnit> getItems() {
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

    public List<ClosedUnit> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ClosedUnit> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ClosedUnit.class)
    public static class ClosedUnitControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClosedUnitController controller = (ClosedUnitController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "closedUnitController");
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
            if (object instanceof ClosedUnit) {
                ClosedUnit o = (ClosedUnit) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ClosedUnit.class.getName()});
                return null;
            }
        }

    }

}
