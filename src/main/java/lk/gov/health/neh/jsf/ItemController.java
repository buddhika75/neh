package lk.gov.health.neh.jsf;

import lk.gov.health.neh.entity.Item;
import lk.gov.health.neh.jsf.util.JsfUtil;
import lk.gov.health.neh.jsf.util.JsfUtil.PersistAction;
import lk.gov.health.neh.session.ItemFacade;

import java.io.Serializable;
import java.util.ArrayList;
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
import lk.gov.health.neh.enums.ItemType;

@ManagedBean(name = "itemController")
@SessionScoped
public class ItemController implements Serializable {

    @EJB
    private lk.gov.health.neh.session.ItemFacade ejbFacade;
    private List<Item> items = null;
    private Item selected;

    public ItemController() {
    }

    public Item getSelected() {
        return selected;
    }

    public void setSelected(Item selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ItemFacade getFacade() {
        return ejbFacade;
    }

    public Item prepareCreate() {
        selected = new Item();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Item> getItems() {
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

    public List<Item> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Item> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Item> completeOccupations(String qry) {
        return completeItem(qry, ItemType.Occupation);
    }
    
    public List<Item> completeDistricts(String qry) {
        return completeItem(qry, ItemType.District);
    }

    public List<Item> completeItem(String qry, ItemType it) {
        if (qry == null) {
            return new ArrayList<Item>();
        }
        String j;
        Map m = new HashMap();
        m.put("it", it);
        m.put("n", "%"+ qry.toUpperCase() + "%");
        j = "Select i from Item i where i.retired=false and i.itemType=:it and upper(i.name) like :n order by i.name";
        return getFacade().findBySQL(j, m);
    }
    
    public void occList(){
        String sql;
        Map m = new HashMap();
        m.put("it", ItemType.Occupation);
        sql = "Select i from Item i where i.retired=false and i.itemType=:it";
        getFacade().findBySQL(sql, m);
    }
    
    public List<Item> occupationList(){
        String sql;
        Map m = new HashMap();
        m.put("it", ItemType.Occupation);
        sql = "Select i from Item i where i.retired=false and i.itemType=:it";
        List<Item> occList;
        occList = getFacade().findBySQL(sql, m);
        return occList;
    }
    
    public List<Item> diagnosisList(){
        String sql;
        Map m = new HashMap();
        m.put("it", ItemType.Diagnosis);
        sql = "Select i from Item i where i.retired=false and i.itemType=:it";
        List<Item> occList;
        occList = getFacade().findBySQL(sql, m);
        return occList;
    }
    
    public List<Item> complainList(){
        String sql;
        Map m = new HashMap();
        m.put("it", ItemType.PresentingCompalint);
        sql = "Select i from Item i where i.retired=false and i.itemType=:it";
        List<Item> occList;
        occList = getFacade().findBySQL(sql, m);
        return occList;
    }
    
    public List<Item> districtList(){
        String sql;
        Map m = new HashMap();
        m.put("it", ItemType.District);
        sql = "Select i from Item i where i.retired=false and i.itemType=:it";
        List<Item> occList;
        occList = getFacade().findBySQL(sql, m);
        return occList;
    }

    public ItemFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ItemFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    

    @FacesConverter(forClass = Item.class)
    public static class ItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItemController controller = (ItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "itemController");
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
            if (object instanceof Item) {
                Item o = (Item) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Item.class.getName()});
                return null;
            }
        }

    }

    @FacesConverter("itemConverter")
    public static class ItemConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItemController controller = (ItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "itemController");
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
            if (object instanceof Item) {
                Item o = (Item) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Item.class.getName()});
                return null;
            }
        }

    }

}
