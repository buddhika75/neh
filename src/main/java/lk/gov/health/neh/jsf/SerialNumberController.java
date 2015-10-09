package lk.gov.health.neh.jsf;

import lk.gov.health.neh.entity.SerialNumber;
import lk.gov.health.neh.jsf.util.JsfUtil;
import lk.gov.health.neh.jsf.util.JsfUtil.PersistAction;
import lk.gov.health.neh.session.SerialNumberFacade;

import java.io.Serializable;
import java.util.List;
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

@ManagedBean(name = "serialNumberController")
@SessionScoped
public class SerialNumberController implements Serializable {

    @EJB
    private lk.gov.health.neh.session.SerialNumberFacade ejbFacade;
    private List<SerialNumber> items = null;
    private SerialNumber selected;

    public SerialNumberController() {
    }

    public SerialNumber getSelected() {
        return selected;
    }

    public void setSelected(SerialNumber selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SerialNumberFacade getFacade() {
        return ejbFacade;
    }

    public SerialNumber prepareCreate() {
        selected = new SerialNumber();
        initializeEmbeddableKey();
        return selected;
    }

    SerialNumber currentSerialNumber;

    public String findSerialNumber() {
        String j;
        j = "select sn from SerialNumber sn Where sn.retired=false order by sn.id desc";
        currentSerialNumber = getFacade().findFirstBySQL(j);

        return "/opdVisit/serial_number";
    }

    public SerialNumberFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(SerialNumberFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SerialNumber getCurrentSerialNumber() {
        if (currentSerialNumber == null) {
            currentSerialNumber = new SerialNumber();
        }
        return currentSerialNumber;
    }

    public void setCurrentSerialNumber(SerialNumber currentSerialNumber) {
        this.currentSerialNumber = currentSerialNumber;
    }

    public void createSerialNumber() {
        if (selected == null) {
            selected = new SerialNumber();
            selected.setSerialNumber(getCurrentSerialNumber().getSerialNumber());
            ejbFacade.create(selected);
            JsfUtil.addSuccessMessage("New Serial Number Inserted");
        } else {
            selected.setSerialNumber(getCurrentSerialNumber().getSerialNumber());
            selected.setRetired(false);
            ejbFacade.edit(selected);
            JsfUtil.addSuccessMessage("New Serial Number :- " + getCurrentSerialNumber().getSerialNumber());
        }
    }

    public void retireSerialNumber() {
        if (selected != null) {
            selected.setRetired(true);
            ejbFacade.edit(selected);
            JsfUtil.addSuccessMessage("Successfully Deleted");
        }
        JsfUtil.addErrorMessage("Serial Number Is Null");
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SerialNumberCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SerialNumberUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SerialNumberDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<SerialNumber> getItems() {
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

    public List<SerialNumber> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<SerialNumber> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = SerialNumber.class)
    public static class SerialNumberControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SerialNumberController controller = (SerialNumberController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "serialNumberController");
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
            if (object instanceof SerialNumber) {
                SerialNumber o = (SerialNumber) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), SerialNumber.class.getName()});
                return null;
            }
        }

    }

}
