package lk.gov.health.neh.jsf;

import lk.gov.health.neh.entity.AppointmentSession;
import lk.gov.health.neh.jsf.util.JsfUtil;
import lk.gov.health.neh.jsf.util.JsfUtil.PersistAction;
import lk.gov.health.neh.session.AppointmentSessionFacade;

import java.io.Serializable;
import java.text.DateFormat;
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
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.TemporalType;
import lk.gov.health.neh.entity.AppointmentTimeSlot;
import lk.gov.health.neh.entity.Consultant;
import lk.gov.health.neh.entity.Encounter;
import lk.gov.health.neh.entity.Patient;
import lk.gov.health.neh.entity.Unit;
import lk.gov.health.neh.entity.Ward;
import lk.gov.health.neh.enums.AppointmentSessionDateType;
import lk.gov.health.neh.enums.AppointmentSessionType;
import lk.gov.health.neh.enums.EncounterType;
import lk.gov.health.neh.enums.NumberAndTime;
import lk.gov.health.neh.enums.Weekday;
import lk.gov.health.neh.session.AppointmentTimeSlotFacade;
import lk.gov.health.neh.session.EncounterFacade;
import lk.gov.health.neh.session.PatientFacade;
import org.primefaces.event.SelectEvent;

@Named(value = "appointmentSessionController")
@SessionScoped
public class AppointmentSessionController implements Serializable {

    @EJB
    private AppointmentSessionFacade ejbFacade;
    @EJB
    EncounterFacade encounterFacade;
    @EJB
    PatientFacade patientFacade;
    @EJB
    AppointmentTimeSlotFacade appointmentTimeSlotFacade;

    @Inject
    StaffController staffController;
    @Inject
    PersonController personController;

    private List<AppointmentSession> items = null;
    private AppointmentSession selected;

    Patient patient;
    Encounter encounter;
    Unit unit;
    Ward ward;
    Consultant consultant;
    Date fromDate;
    Date toDate;
    Date selectedDate;
    Encounter selectedAppointment;
    AppointmentSession selectedAppointmentSession;
    List<AppointmentTimeSlot> selectedAppointmentTimeSlots;
    AppointmentTimeSlot selectedAppointmentTimeSlot;
    List<Encounter> selectedAppointments;
    List<AppointmentSession> selectedAppointmentSessions;
    boolean printing;

    public String toAddNewAppointmentByAddingPatient() {
        patient = new Patient();
        encounter = new Encounter();
        encounter.setEncounterType(EncounterType.Appointment);
        printing = false;
        return "/appointments/new_appointment_for_registered_patients";
    }

    public String toAddNewAppointmentAfterSavingPatient(Patient pt) {
        patient = pt;
        encounter = new Encounter();
        encounter.setEncounterType(EncounterType.Appointment);
        printing = false;
        return "/appointments/new_appointment_for_registered_patients";
    }

    public void selectedDateChanged(SelectEvent event) {
        selectedAppointment = null;
        selectedAppointmentSession = null;
        selectedAppointmentTimeSlots = null;
        selectedAppointmentSessions = new ArrayList<AppointmentSession>();
        selectedAppointments = new ArrayList<Encounter>();
        Map m = new HashMap();
        String j;

        if (staffController.getLoggedStaff() != null) {
            j = "Select a from AppointmentSession a "
                    + " where a.unit=:unit "
                    + " and a.sessionWeekday=:swd ";
            m.put("unit", staffController.getLoggedUnit());
            m.put("swd", Weekday.getWeekday(selectedDate));
        } else {
            j = "Select a from AppointmentSession a "
                    + " where a.sessionWeekday=:swd ";
            m.put("swd", Weekday.getWeekday(selectedDate));
        }
        selectedAppointmentSessions = getFacade().findBySQL(j, m);

        if (selectedAppointmentSessions == null || selectedAppointmentSessions.isEmpty()) {
            System.out.println("selectedAppointmentSessions is null or empty");
            return;
        }

        selectedAppointmentSession = selectedAppointmentSessions.get(0);
        selectedAppointmentTimeSlots = getAppointmentTimeSlots(selectedAppointmentSession, selectedDate);

        selectedSessionChanged();

    }

    public void selectedSessionChanged() {
        Map m = new HashMap();
        String j;
        if (selectedAppointmentSession == null) {
            selectedAppointments = new ArrayList<Encounter>();
            selectedAppointment = null;
            selectedAppointmentTimeSlots = null;
            return;
        }

        j = "Select e from Encounter e "
                + " where e.encounterType=:et "
                + " and e.appointmentSession=:aps "
                + " and e.encounterDate=:ed";
        m = new HashMap();
        m.put("et", EncounterType.Appointment);
        m.put("aps", selectedAppointmentSession);
        m.put("ed", selectedDate);

        selectedAppointments = encounterFacade.findBySQL(j, m);
        selectedAppointmentTimeSlots = getAppointmentTimeSlots(selectedAppointmentSession, selectedDate);

    }

    public String makeAnAppointment() {
        if (patient == null) {
            JsfUtil.addErrorMessage("Patient ?");
            return "";
        }
        if (encounter == null) {
            JsfUtil.addErrorMessage("Encounter ?");
            return "";
        }
        if (selectedAppointmentSession == null) {
            JsfUtil.addErrorMessage("Please select a clinic");
            return "";
        }
        List<Encounter> temAppointments;
        encounter.setAppointmentSession(selectedAppointmentSession);
        encounter.setEncounterDate(selectedDate);
        encounter.setEncounterType(EncounterType.Appointment);

        encounter.setPatient(patient);
        encounter.setIntDailyNo(selectedAppointments.size());

        if (convertDailyToBlock(encounter)) {
            getEncounterFacade().edit(encounter);
            printing = true;
            JsfUtil.addSuccessMessage("New Appointment Added");
            System.out.println("encounter.getEncounterDate() = " + encounter.getEncounterDate());
            return "";
        } else {
            printing = false;
            JsfUtil.addErrorMessage("Can not Add Appointment");
            return "";
        }
    }

    public List<AppointmentTimeSlot> getAppointmentTimeSlots(AppointmentSession as, Date date) {

        System.out.println("getAppointmentTimeSlots");

        List<AppointmentTimeSlot> tss = new ArrayList<AppointmentTimeSlot>();

        int timePerSlot = as.getDurationInMinutes();
        long timeSlotsPerSession;
        int intTimeSlotsPerSesson;
        long durationBetweenStartAndEnd;

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        Calendar sessionStart = Calendar.getInstance();
        sessionStart.setTime(as.getSessionFrom());

        Calendar sessionEnd = Calendar.getInstance();
        sessionEnd.setTime(as.getSessionTo());

        System.err.println("as.getSessionFrom() = " + as.getSessionFrom());
        System.err.println("as.getSessionTo() = " + as.getSessionTo());

        System.out.println("sessionStart = " + sessionStart);
        System.out.println("sessionEnd = " + sessionEnd);

        start.set(Calendar.MILLISECOND, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.HOUR_OF_DAY, sessionStart.get(Calendar.HOUR_OF_DAY));
        start.set(Calendar.MINUTE, sessionStart.get(Calendar.MINUTE));
        System.out.println("start = " + start);

        end.set(Calendar.MILLISECOND, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.HOUR_OF_DAY, sessionEnd.get(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, sessionEnd.get(Calendar.MINUTE));
        System.out.println("end = " + end);

        durationBetweenStartAndEnd = (end.getTime().getTime() - start.getTime().getTime()) / (1000 * 60);
        timeSlotsPerSession = durationBetweenStartAndEnd / timePerSlot;
        System.out.println("timePerSlot = " + timePerSlot);
        System.out.println("durationBetweenStartAndEnd = " + durationBetweenStartAndEnd);

        intTimeSlotsPerSesson = (int) timeSlotsPerSession;

        System.out.println("intTimeSlotsPerSesson = " + intTimeSlotsPerSesson);

        for (int i = 0; i < intTimeSlotsPerSesson; i++) {
            Calendar cts = Calendar.getInstance();
            cts.setTime(date);
            cts.set(Calendar.MILLISECOND, 0);
            cts.set(Calendar.SECOND, 0);
            cts.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY));
            cts.set(Calendar.MINUTE, start.get(Calendar.MINUTE));
            cts.add(Calendar.MINUTE, i * as.getDurationInMinutes());
            String j = "select a "
                    + " from AppointmentTimeSlot a "
                    + " where a.timeSlotTimeStamp=:ats "
                    + " and a.appointmentSession=:apps";
            Map m = new HashMap();
            m.put("ats", cts.getTime());
            m.put("apps", as);
            AppointmentTimeSlot ts = getAppointmentTimeSlotFacade().findFirstBySQL(j, m, TemporalType.TIMESTAMP);
            System.out.println("ts = " + ts);
            if (ts == null) {
                ts = new AppointmentTimeSlot();
                ts.setAppointmentSession(as);
                ts.setTimeSlotDate(cts.getTime());
                ts.setTimeSlotTime(cts.getTime());
                ts.setTimeSlotTimeStamp(cts.getTime());
                DateFormat df = new SimpleDateFormat("HH:mm");
                Date appTime = cts.getTime();
                String appName = df.format(appTime);
                ts.setTimeSlotName(appName);
                getAppointmentTimeSlotFacade().create(ts);
                System.out.println("create"
                        + "");
            }
            tss.add(ts);
        }
        selectedAppointmentTimeSlot = null;
        return tss;

    }

    public boolean convertDailyToBlock(Encounter e) {
        String j;
        Map m;
        boolean canAdd = false;
        System.out.println("selectedAppointmentTimeSlots = " + selectedAppointmentTimeSlots);

        for (AppointmentTimeSlot ts : selectedAppointmentTimeSlots) {
            System.out.println("ts = " + ts);
            j = "Select count(e) from Encounter e "
                    + " where e.encounterType=:et "
                    + " and e.appointmentSession=:aps "
                    + " and e.encounterDate=:ed "
                    + " and e.appointmentTimeSlot=:ats";
            m = new HashMap();
            m.put("et", EncounterType.Appointment);
            m.put("aps", selectedAppointmentSession);
            m.put("ed", selectedDate);
            m.put("ats", ts);
            long count = getEncounterFacade().findLongByJpql(j, m);
            System.out.println("selectedAppointmentTimeSlot = " + selectedAppointmentTimeSlot);
            if (selectedAppointmentTimeSlot == null) {
                System.out.println("selectedAppointmentSession.getDurationBlockNumber() = " + selectedAppointmentSession.getDurationBlockNumber());
                System.out.println("count = " + count);
                if (count < selectedAppointmentSession.getDurationBlockNumber()) {
                    System.out.println("1. ts.getTimeSlotTimeStamp() = " + ts.getTimeSlotTimeStamp());
                    e.setEncounterDate(ts.getTimeSlotTimeStamp());
                    e.setEncounterTime(ts.getTimeSlotTimeStamp());
                    System.out.println("e.getEncounterDate() = " + e.getEncounterDate());
                    e.setSerialNumber(count + 1);
                    e.setIntSerialNo((int) count + 1);
                    e.setAppointmentTimeSlot(ts);
                    return true;
                }
            } else if (selectedAppointmentTimeSlot.equals(ts)) {
                System.out.println("2. ts.getTimeSlotTimeStamp() = " + ts.getTimeSlotTimeStamp());
                e.setEncounterDate(ts.getTimeSlotTimeStamp());
                e.setEncounterTime(ts.getTimeSlotTimeStamp());
                System.out.println("e.getEncounterDate() = " + e.getEncounterDate());
                e.setSerialNumber(count + 1);
                e.setIntSerialNo((int) count + 1);
                e.setAppointmentTimeSlot(ts);
                return true;
            }
        }
        return canAdd;
    }

    public void convertDailyToBlockOld(Encounter e) {
        int timePerSlot = e.getAppointmentSession().getDurationInMinutes();
        int countForBlock = e.getAppointmentSession().getDurationBlockNumber();
        Date startTime = e.getAppointmentSession().getSessionFrom();
        int modBlockAtStart = e.getIntDailyNo() / countForBlock;

        System.out.println("timePerSlot = " + timePerSlot);
        System.out.println("modBlockAtStart = " + modBlockAtStart);

        Calendar app = Calendar.getInstance();
        app.setTime(e.getEncounterDate());
        System.out.println("app = " + app);

        Calendar ses = Calendar.getInstance();
        ses.setTime(e.getAppointmentSession().getSessionFrom());
        System.out.println("ses = " + ses);

        int appHour = ses.get(Calendar.HOUR);
        System.out.println("appHour = " + appHour);
        int appMin = ses.get(Calendar.MINUTE);
        System.out.println("appMin = " + appMin);

        app.set(Calendar.HOUR, appHour);
        app.set(Calendar.MINUTE, appMin);
        app.add(Calendar.MINUTE, (timePerSlot * modBlockAtStart));

        e.setEncounterDate(app.getTime());
        System.out.println("e.getEncounterDate() = " + e.getEncounterDate());

        e.setSerialNumber((e.getIntDailyNo() % e.getAppointmentSession().getDurationBlockNumber()) + 1);

        System.out.println("e.getSerialNumber() = " + e.getSerialNumber());

        e.setIntSerialNo((e.getIntDailyNo() % e.getAppointmentSession().getDurationBlockNumber()) + 1);

        System.out.println("e.getIntSerialNo() = " + e.getIntSerialNo());

    }

    public String clearForNewAppointment() {
        selectedDateChanged(null);
        patient = new Patient();
        encounter = new Encounter();
        encounter.setEncounterType(EncounterType.Appointment);
        return toAddNewAppointmentByAddingPatient();
    }

    public String clearForNewPatient() {
        selectedDateChanged(null);
        patient = new Patient();
        encounter = new Encounter();
        encounter.setEncounterType(EncounterType.Appointment);
        return personController.toAddNewAppointmentByAddingPatient();
    }

    public String clearForSearchPatient() {
        selectedDateChanged(null);
        patient = new Patient();
        encounter = new Encounter();
        encounter.setEncounterType(EncounterType.Appointment);
        return personController.toAddNewAppointmentBySearchingPatient();
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public Consultant getConsultant() {
        return consultant;
    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
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

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public List<Encounter> getSelectedAppointments() {
        return selectedAppointments;
    }

    public void setSelectedAppointments(List<Encounter> selectedAppointments) {
        this.selectedAppointments = selectedAppointments;
    }

    public AppointmentSessionController() {
    }

    public AppointmentSession getSelected() {
        return selected;
    }

    public void setSelected(AppointmentSession selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AppointmentSessionFacade getFacade() {
        return ejbFacade;
    }

    public AppointmentSession prepareCreate() {
        selected = new AppointmentSession();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        if (selected == null) {
            return;
        }
        if (selected.getType() == null) {
            selected.setType(AppointmentSessionType.ClinicBooking);
        }
        if (selected.getDateType() == null) {
            selected.setDateType(AppointmentSessionDateType.Weekday);
        }

        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("Created"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("Updated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("Deleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<AppointmentSession> getItems() {
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
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("Error"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("Error"));
            }
        }
    }

    public List<AppointmentSession> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AppointmentSession> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public Encounter getSelectedAppointment() {
        return selectedAppointment;
    }

    public void setSelectedAppointment(Encounter selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }

    public AppointmentSession getSelectedAppointmentSession() {
        return selectedAppointmentSession;
    }

    public void setSelectedAppointmentSession(AppointmentSession selectedAppointmentSession) {
        this.selectedAppointmentSession = selectedAppointmentSession;
    }

    public List<AppointmentSession> getSelectedAppointmentSessions() {
        return selectedAppointmentSessions;
    }

    public void setSelectedAppointmentSessions(List<AppointmentSession> selectedAppointmentSessions) {
        this.selectedAppointmentSessions = selectedAppointmentSessions;
    }

    public AppointmentSessionFacade getEjbFacade() {
        return ejbFacade;
    }

    public EncounterFacade getEncounterFacade() {
        return encounterFacade;
    }

    public PatientFacade getPatientFacade() {
        return patientFacade;
    }

    public StaffController getStaffController() {
        return staffController;
    }

    public boolean isPrinting() {
        return printing;
    }

    public void setPrinting(boolean printing) {
        this.printing = printing;
    }

    public AppointmentTimeSlotFacade getAppointmentTimeSlotFacade() {
        return appointmentTimeSlotFacade;
    }

    public PersonController getPersonController() {
        return personController;
    }

    public List<AppointmentTimeSlot> getSelectedAppointmentTimeSlots() {
        return selectedAppointmentTimeSlots;
    }

    public void setSelectedAppointmentTimeSlots(List<AppointmentTimeSlot> selectedAppointmentTimeSlots) {
        this.selectedAppointmentTimeSlots = selectedAppointmentTimeSlots;
    }

    public AppointmentTimeSlot getSelectedAppointmentTimeSlot() {
        return selectedAppointmentTimeSlot;
    }

    public void setSelectedAppointmentTimeSlot(AppointmentTimeSlot selectedAppointmentTimeSlot) {
        this.selectedAppointmentTimeSlot = selectedAppointmentTimeSlot;
    }

    @FacesConverter(forClass = AppointmentSession.class)
    public static class AppointmentSessionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AppointmentSessionController controller = (AppointmentSessionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "appointmentSessionController");
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
            if (object instanceof AppointmentSession) {
                AppointmentSession o = (AppointmentSession) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AppointmentSession.class.getName()});
                return null;
            }
        }

    }

    @FacesConverter(value = "appointmentSessionConverter")
    public static class AppointmentSessionConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AppointmentSessionController controller = (AppointmentSessionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "appointmentSessionController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            try {
                key = Long.valueOf(value);
            } catch (Exception e) {
                key = 0l;
                System.out.println("e = " + e);
            }
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
            if (object instanceof AppointmentSession) {
                AppointmentSession o = (AppointmentSession) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AppointmentSession.class.getName()});
                return null;
            }
        }

    }

}
