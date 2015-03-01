package lk.gov.health.neh.jsf;

import lk.gov.health.neh.entity.Consultant;
import lk.gov.health.neh.jsf.util.JsfUtil;
import lk.gov.health.neh.jsf.util.JsfUtil.PersistAction;
import lk.gov.health.neh.session.ConsultantFacade;

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
import lk.gov.health.neh.session.PersonFacade;

@ManagedBean(name = "consultantController")
@SessionScoped
public class ConsultantController implements Serializable {

    @EJB
    private lk.gov.health.neh.session.ConsultantFacade ejbFacade;
    private List<Consultant> items = null;
    private Consultant selected;
    @EJB
    PersonFacade personFacade;

    public ConsultantController() {
    }

    public Consultant getSelected() {
        return selected;
    }

    public void setSelected(Consultant selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ConsultantFacade getFacade() {
        return ejbFacade;
    }

    public Consultant prepareCreate() {
        System.out.println("selected = " + selected);
        if (selected != null) {
            System.out.println("selected.getId() = " + selected.getId());
        }
        selected = new Consultant();
        System.out.println("selected = " + selected);
        System.out.println("selected.getId() = " + selected.getId());
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ConsultantCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ConsultantUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ConsultantDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Consultant> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        System.out.println("selected = " + selected);
        System.out.println("selected.getId() = " + selected.getId());
        System.out.println("selected.getName() = " + selected.getName());
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.DELETE) {
                    System.out.println("deleting");
                    personFacade.remove(selected);
                } else if (persistAction == PersistAction.CREATE) {
                    System.out.println("creating");
                    personFacade.create(selected);
                } else {
                    System.out.println("editing  ");
                    personFacade.edit(selected);
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

    public List<Consultant> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Consultant> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Consultant.class)
    public static class ConsultantControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConsultantController controller = (ConsultantController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "consultantController");
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
            if (object instanceof Consultant) {
                Consultant o = (Consultant) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Consultant.class.getName()});
                return null;
            }
        }

    }

}
