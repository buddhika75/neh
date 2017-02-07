package lk.gov.health.neh.jsf;

import lk.gov.health.neh.entity.AppointmentTimeSlot;
import lk.gov.health.neh.jsf.util.JsfUtil;
import lk.gov.health.neh.jsf.util.JsfUtil.PersistAction;
import lk.gov.health.neh.session.AppointmentTimeSlotFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import lk.gov.health.neh.session.PersonFacade;

@Named(value = "appointmentTimeSlotController")
@SessionScoped
public class AppointmentTimeSlotController implements Serializable {

    @EJB
    private lk.gov.health.neh.session.AppointmentTimeSlotFacade ejbFacade;
    private List<AppointmentTimeSlot> items = null;
    private AppointmentTimeSlot selected;
    @EJB
    AppointmentTimeSlotFacade appointmentTimeSlotFacade;

    public AppointmentTimeSlotController() {
    }

    public AppointmentTimeSlot getSelected() {
        return selected;
    }

    public void setSelected(AppointmentTimeSlot selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AppointmentTimeSlotFacade getFacade() {
        return ejbFacade;
    }

    public AppointmentTimeSlot prepareCreate() {
        System.out.println("selected = " + selected);
        if (selected != null) {
            System.out.println("selected.getId() = " + selected.getId());
        }
        selected = new AppointmentTimeSlot();
        System.out.println("selected = " + selected);
        System.out.println("selected.getId() = " + selected.getId());
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AppointmentTimeSlotCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AppointmentTimeSlotUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AppointmentTimeSlotDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AppointmentTimeSlot> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.DELETE) {
                    System.out.println("deleting");
                    appointmentTimeSlotFacade.remove(selected);
                } else if (persistAction == PersistAction.CREATE) {
                    System.out.println("creating");
                    appointmentTimeSlotFacade.create(selected);
                } else {
                    System.out.println("editing  ");
                    appointmentTimeSlotFacade.edit(selected);
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

    public List<AppointmentTimeSlot> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AppointmentTimeSlot> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AppointmentTimeSlot.class)
    public static class AppointmentTimeSlotControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AppointmentTimeSlotController controller = (AppointmentTimeSlotController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "appointmentTimeSlotController");
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
            if (object instanceof AppointmentTimeSlot) {
                AppointmentTimeSlot o = (AppointmentTimeSlot) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AppointmentTimeSlot.class.getName()});
                return null;
            }
        }

    }

}
