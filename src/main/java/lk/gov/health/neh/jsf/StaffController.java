package lk.gov.health.neh.jsf;

import lk.gov.health.neh.entity.Staff;
import lk.gov.health.neh.jsf.util.JsfUtil;
import lk.gov.health.neh.jsf.util.JsfUtil.PersistAction;
import lk.gov.health.neh.session.StaffFacade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.inject.Inject;
import javax.persistence.Column;
import lk.gov.health.neh.entity.Unit;

@Named(value = "staffController")
@SessionScoped
public class StaffController implements Serializable {

    @EJB
    private lk.gov.health.neh.session.StaffFacade ejbFacade;
    @Inject
    CommonController commonController;
    private List<Staff> items = null;
    private Staff selected;

    Staff loggedStaff;
    Unit loggedUnit;
    boolean logged;
    String userName;
    String password;

    public Unit getLoggedUnit() {
        return loggedUnit;
    }

    public void setLoggedUnit(Unit loggedUnit) {
        this.loggedUnit = loggedUnit;
    }

    
    
    public Staff getLoggedStaff() {
        return loggedStaff;
    }

    public void setLoggedStaff(Staff loggedStaff) {
        this.loggedStaff = loggedStaff;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StaffController() {
    }

    public String logout() {
        logged = false;
        loggedStaff = null;
        return "index";
    }

    public String login() {
        if (getFacade().count() == 0) {
            Staff s = new Staff();
            s.setName(userName);
            s.setUserName(userName);
            s.setPassword(CommonController.makeHash(password));
            getFacade().create(s);
            loggedStaff = s;
            logged = true;
        }
        String j = "select s from Staff s where s.retired=false and s.userName=:un order by s.id desc";
        Map m = new HashMap();
        m.put("un", userName);
        Staff s = getFacade().findFirstBySQL(j, m);
        if (s == null) {
            logged = false;
            loggedStaff = null;
            loggedUnit = null;
            JsfUtil.addErrorMessage("No such user");
            return "index";
        }
        System.out.println("s.getUserName() = " + s.getUserName());
        System.out.println("s.getPassword() = " + s.getPassword());
        System.out.println("password = " + password);
        if (CommonController.checkPassword(password, s.getPassword())) {
            logged = true;
            loggedStaff = s;
            loggedUnit = s.getUnit();
            userName = "";
            password = "";
            JsfUtil.addSuccessMessage("Logged Successfully");
        } else {
            logged = false;
            loggedStaff = null;
            loggedUnit = null;
            JsfUtil.addErrorMessage("Wrong Password");
        }
        return "index";
    }

    public Staff getSelected() {
        return selected;
    }

    public void setSelected(Staff selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private StaffFacade getFacade() {
        return ejbFacade;
    }

    public Staff prepareCreate() {
        selected = new Staff();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        System.out.println("saving staff");
        String pw = CommonController.makeHash(password);
        password = "";
        selected.setPassword(pw);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("StaffCreated"));
        System.out.println("saved");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("StaffUpdated"));
    }

    public void updatePassword() {
        System.out.println("selected = " + selected);
        System.out.println("password = " + password);
        String pw = CommonController.makeHash(password);
        password = "";
        selected.setPassword(pw);
        
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("StaffUpdated"));
        System.out.println("selected.getPassword() = " + selected.getPassword());
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("StaffDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Staff> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        System.out.println("persist");
        System.out.println("selected = " + selected);
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

    public List<Staff> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Staff> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Staff.class)
    public static class StaffControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StaffController controller = (StaffController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "staffController");
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
            if (object instanceof Staff) {
                Staff o = (Staff) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Staff.class.getName()});
                return null;
            }
        }

    }

    @FacesConverter("staffConverter")
    public static class StaffConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StaffController controller = (StaffController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "staffController");
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
            if (object instanceof Staff) {
                Staff o = (Staff) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Staff.class.getName()});
                return null;
            }
        }

    }
}
