package lk.gov.health.neh.jsf;

import lk.gov.health.neh.entity.Person;
import lk.gov.health.neh.jsf.util.JsfUtil;
import lk.gov.health.neh.jsf.util.JsfUtil.PersistAction;
import lk.gov.health.neh.session.PersonFacade;

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
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import lk.gov.health.neh.entity.Patient;
import lk.gov.health.neh.enums.UnitLastFileNumber;
import lk.gov.health.neh.session.PatientFacade;

@Named(value = "personController")
@SessionScoped
public class PersonController implements Serializable {

    @EJB
    private lk.gov.health.neh.session.PersonFacade ejbFacade;
    @EJB
    PatientFacade patientFacade;
    private List<Person> items = null;
    private Person selected;
    Patient selectedPatient;
    String searchText;
    @Inject
    AppointmentSessionController appointmentSessionController;
    @Inject
    ApplicationController applicationController;
    @Inject
    StaffController staffController;

    private List<Person> selectedItems = null;

    public void searchPatients() {
        if (searchText == null) {
            return;
        }
        String j = "select p from Person p "
                + " where (upper(p.name) like :pn "
                + " or p.clinicFileNo like :cfn ) "
                + " and p.unit=:punit "
                + " order by p.clinicFileNo";
        Map m = new HashMap();
        m.put("pn", "%" + searchText.toUpperCase() + "%");
        m.put("cfn", "%" + searchText + "%");
        m.put("punit", staffController.getLoggedUnit());
        Person p = new Person();
        p.getName();
        p.getNic();
        p.getClinicFileNo();
        selectedItems = getFacade().findBySQL(j, m);
    }

    public String toAddNewAppointmentByAddingPatient() {
        selectedPatient = new Patient();
        selectedPatient.setUnit(staffController.getLoggedUnit());
        selectedPatient.setRegistered(true);
        UnitLastFileNumber f = applicationController.giveAFileNumber(staffController.getLoggedUnit());
        System.out.println("f = " + f);
        selectedPatient.setClinicFileNoYearlySerial(f.getAnnualCount());
        System.out.println("f.getAnnualCount() = " + f.getAnnualCount());
        selectedPatient.setClinicFileNoYear(f.getYearValue());
        selectedPatient.setClinicFileNo(staffController.getLoggedUnit() + "/" + selectedPatient.getClinicFileNoYearlySerial() + "/" + selectedPatient.getClinicFileNoYear());

        return "/appointments/register_patient";
    }

    public String toAddNewAppointmentBySearchingPatient() {
        selectedPatient = null;
        selectedItems = new ArrayList<Person>();
        searchText = "";
        return "/appointments/search_patient";
    }

    public String saveNewPatientAndGoToAppointments() {
        saveNewPatient();
        appointmentSessionController.toAddNewAppointmentAfterSavingPatient(selectedPatient);
        appointmentSessionController.setPrinting(false);
        System.out.println("f.getAnnualCount() = " + selectedPatient.getClinicFileNo());
        return "/appointments/new_appointment_for_registered_patients";
    }

    public String saveOldPatientAndGoToAppointments() {
        System.out.println("saveOldPatientAndGoToAppointments");
        System.out.println("selected = " + selectedPatient);
        if (selectedPatient == null) {
            return "";
        }
        saveOldPatient();
        appointmentSessionController.toAddNewAppointmentAfterSavingPatient(selectedPatient);
        appointmentSessionController.setPrinting(false);
        System.out.println("f.getAnnualCount() = " + selectedPatient.getClinicFileNo());
        return "/appointments/new_appointment_for_registered_patients";
    }

    public String saveNewPatientAndPrepareForAnotherNewPatient() {
        saveNewPatient();
        selectedPatient = new Patient();
        selectedPatient.setRegistered(true);
        UnitLastFileNumber f = applicationController.giveAFileNumber(staffController.getLoggedUnit());
        selectedPatient.setClinicFileNoYearlySerial(f.getAnnualCount());
        selectedPatient.setClinicFileNoYear(f.getYearValue());
        selectedPatient.setClinicFileNo(selectedPatient.getClinicFileNoYearlySerial() + "/" + selectedPatient.getClinicFileNoYear());
        return "/appointments/register_patient";
    }

    public PersonController() {
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    
    
    public Person getSelected() {
        return selected;
    }

    public void setSelected(Person selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PersonFacade getFacade() {
        return ejbFacade;
    }

    public Person prepareCreate() {
        selected = new Person();
        selectedPatient = new Patient();
        initializeEmbeddableKey();
        return selected;
    }

    public void saveNewPatient() {
        patientFacade.create(selectedPatient);
        JsfUtil.addSuccessMessage("Patient Saved.");
    }

    public void saveOldPatient() {
        patientFacade.edit(selectedPatient);
        JsfUtil.addSuccessMessage("Patient Details Updated.");
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PersonCreated"));
        persistPerson(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PersonCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PersonUpdated"));
        persistPerson(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PersonUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PersonDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Person> getItems() {
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
    
    private void persistPerson(PersistAction persistAction, String successMessage) {
        if (selectedPatient != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    patientFacade.edit(selectedPatient);
                } else {
                    patientFacade.remove(selectedPatient);
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

    public List<Person> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Person> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<Person> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<Person> selectedItems) {
        this.selectedItems = selectedItems;
    }

    @FacesConverter(forClass = Person.class)
    public static class PersonControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonController controller = (PersonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personController");
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
            if (object instanceof Person) {
                Person o = (Person) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Person.class.getName()});
                return null;
            }
        }

    }

}
