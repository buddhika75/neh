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
import javax.persistence.TemporalType;
import lk.gov.health.neh.entity.ClosedUnit;
import lk.gov.health.neh.entity.Patient;
import lk.gov.health.neh.entity.Unit;
import lk.gov.health.neh.entity.Ward;
import lk.gov.health.neh.enums.ConsultantRole;
import lk.gov.health.neh.enums.EncounterType;
import lk.gov.health.neh.session.ClosedUnitFacade;
import lk.gov.health.neh.session.UnitFacade;

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
    Unit opdVisit;
    Unit closedUnitVisit;
    Unit speUnit;

    boolean printPreview = false;
    boolean viewPrint = false;

    
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

    public boolean isPrintPreview() {
        return printPreview;
    }

    public boolean isViewPrint() {
        return viewPrint;
    }

    public void setViewPrint(boolean viewPrint) {
        this.viewPrint = viewPrint;
    }

    public void setPrintPreview(boolean printPreview) {
        this.printPreview = printPreview;
    }

    

    public void listVisits() {
        String j;
        Map m = new HashMap();
        m.put("fd", fromDate);
        m.put("td", toDate);
        j = "select v from OpdVisit v where v.encounterDate between :fd and :td";
        visits = getFacade().findBySQL(j, m);
    }

    public String viewForm() {
        viewPrint = true;
        printPreview = true;
        if(selected.getEncounterType() == EncounterType.OpdVisit){
        return "/opdVisit/opd_visit";
        }
        if(selected.getEncounterType() == EncounterType.Casulty){
        return "/opdVisit/casulty_visit";
        }
        if(selected.getEncounterType() == EncounterType.CloseUnitVisit){
        return "/opdVisit/close_unit_visit";
        }else
        return "/opdVisit/special_unit_visit";
        
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public List<OpdVisit> getVisits() {
        return visits;
    }

    public OpdVisitFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(OpdVisitFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public Unit getCasultyUnit() {
        return casultyUnit;
    }

    public void setCasultyUnit(Unit casultyUnit) {
        this.casultyUnit = casultyUnit;
    }

    public Unit getOpdVisit() {
        return opdVisit;
    }

    public void setOpdVisit(Unit opdVisit) {
        this.opdVisit = opdVisit;
    }

    public Unit getClosedUnitVisit() {
        return closedUnitVisit;
    }

    public void setClosedUnitVisit(Unit closedUnitVisit) {
        this.closedUnitVisit = closedUnitVisit;
    }

    public Unit getSpeUnit() {
        return speUnit;
    }

    public void setSpeUnit(Unit speUnit) {
        this.speUnit = speUnit;
    }

    public void setVisits(List<OpdVisit> visits) {
        this.visits = visits;
    }

    public OpdVisitController() {
    }

    public OpdVisit getSelected() {
        if (selected == null) {
            selected = new OpdVisit();
        }
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
        selected.setUnit(nextOpdUnit(EncounterType.OpdVisit, new Date(), ConsultantRole.OPD));
        updateDailyNo();
        initializeEmbeddableKey();
        printPreview = false;
        return "/opdVisit/opd_visit";
    }

    @EJB
    ClosedUnitFacade closedUnitFacade;
    @EJB
    UnitFacade unitFacade;

    public boolean todayUnitIsClosed(Date cd, Unit unit) {
        System.out.println("checking today is closed");
        String j;
        Map m = new HashMap();
        j = "select c from ClosedUnit c where c.closedDate=:cd and c.closedUnit=:cu";
        m.put("cd", cd);
        m.put("cu", unit);
        System.out.println("m = " + m);
        System.out.println("j = " + j);
        List<ClosedUnit> c = closedUnitFacade.findBySQL(j, m);
        System.out.println("c = " + c);
        System.out.println("c != null = " + c.isEmpty());
        return !c.isEmpty();
    }

    ClosedUnit todaysClosedUnit;
    ClosedUnit todaysCasualtyUnit;
    Unit indexNo;

    public String saveTodaysClosedUnit(){
        getTodaysClosedUnit();
        closedUnitFacade.edit(todaysClosedUnit);
        return "";
    }
    public String saveindexNumber(){
        unitFacade.edit(indexNo);
        return "";
    }
    
    public String saveTodaysCasualtyUnit(){
        getTodaysClosedUnit();
        closedUnitFacade.edit(todaysCasualtyUnit);
        return "";
    }
    
    
    public ClosedUnit getTodaysClosedUnit() {
        if(todaysClosedUnit==null){
            todaysClosedUnit=findTodayClosedUnit(new Date());
        }
        if(!CommonController.twoDaysEqual(new Date(), todaysClosedUnit.getClosedDate())){
            todaysClosedUnit=findTodayClosedUnit(new Date());
        }
        return todaysClosedUnit;
    }

    public Unit getIndexNo() {
        if(indexNo == null){
            indexNo = new Unit();
        }
        return indexNo;
    }

    public void setIndexNo(Unit indexNo) {
        this.indexNo = indexNo;
    }

    

    public void setTodaysClosedUnit(ClosedUnit todaysClosedUnit) {
        this.todaysClosedUnit = todaysClosedUnit;
    }

    public ClosedUnit getTodaysCasualtyUnit() {
        if(todaysCasualtyUnit==null){
            todaysCasualtyUnit=findTodayClosedUnit(new Date());
        }
        if(!CommonController.twoDaysEqual(new Date(), todaysCasualtyUnit.getClosedDate())){
            todaysCasualtyUnit=findTodayClosedUnit(new Date());
        }
        return todaysCasualtyUnit;
    }

    public void setTodaysCasualtyUnit(ClosedUnit todaysCasualtyUnit) {
        this.todaysCasualtyUnit = todaysCasualtyUnit;
    }
    
    
    
    public ClosedUnit findTodayClosedUnit(Date date) {
        System.out.println("getting today's closed unit");
        String j;
        Map m = new HashMap();
        j = "select c from ClosedUnit c where c.closedDate=:cd";
        m.put("cd", date);
        ClosedUnit c = closedUnitFacade.findFirstBySQL(j, m);
        if(c==null){
            c=new ClosedUnit();
            c.setClosedDate(date);
            closedUnitFacade.create(c);
        }
        return c;
    }
    
    public List<Unit> todayOpenUnits(Date cd) {
        
        System.out.println("getting today's open units");
        String j;
        Map m = new HashMap();
        j = "select u from Unit u where type(u)!=:uc and u.consultantRole=:cr and u.id not in(select c.closedUnit.id from ClosedUnit c where c.closedDate=:cd)";
        m.put("cd", cd);
        m.put("cr", ConsultantRole.OPD);
        m.put("uc", Ward.class);
//        System.out.println("m = " + m);
//        System.out.println("j = " + j);
        List<Unit> c = unitFacade.findBySQL(j, m);
//        System.out.println("c = " + c);
//        System.out.println("c != null = " + c.isEmpty());
        return c;
    }
//0788044212
    public Unit nextOpdUnit(EncounterType et, Date ed, ConsultantRole cr) {
        System.out.println("calculating next opd unit");
        List<Unit> units = todayOpenUnits(ed);
        System.out.println("units available = " + units);
        Unit selectedUnit=null;
        long selectedCount=1000000;
        String j;
        Map m;
        for (Unit u : units) {
            m = new HashMap();
            System.out.println("u = " + u);
            m.put("et", et);
            m.put("ed", ed);
            m.put("u", u);
            m.put("cr", cr);
            
            j = "select count(v) "
                    + " from OpdVisit v "
                    + " where v.unit=:u "
                    + " and v.encounterDate=:ed "
                    + " and v.unit.consultantRole=:cr "
                    + " and v.encounterType=:et ";
            Long count = getFacade().findLongByJpql(j, m, TemporalType.DATE);
            
            System.out.println("count = " + count);
            System.out.println("selectedCount = " + selectedCount);
            
            if(count<=selectedCount){
                System.out.println("setting it as the selected count");
                selectedCount = count;
                selectedUnit=u;
            }
        }

        return selectedUnit;
    }

//    public Unit nextOpdUnit(EncounterType et, Date ed) {
//        System.out.println("calculating next opd unit");
////        System.out.println("ed = " + ed);
////        System.out.println("et = " + et);
//        String j;
//        Map m = new HashMap();
//        m.put("et", et);
//        m.put("ed", ed);
////        System.out.println("m = " + m);
//        j = "select v.unit from OpdVisit v where v.encounterDate=:ed and v.encounterType=:et group by v.unit order by count(v) desc";
////        System.out.println("j = " + j);
//        List<Object> ts = getFacade().findAggregates(j, m, TemporalType.DATE);
//        System.out.println("Used Units by descending order = " + ts);
//        Unit u;
//
//        List<Unit> units = todayOpenUnits(ed);
//        System.out.println("units available = " + units);
//        System.out.println("checking new unit or from already units");
////        System.out.println("units.size() = " + units.size());
////        System.out.println("ts.size() = " + ts.size());
////        System.out.println("units.size() < ts.size() = " + (units.size() < ts.size()));
//        if (units.size() > ts.size()) {
//
//            System.out.println("still some units are yet to fill.");
//            for (Unit tu : units) {
//                boolean alreadyGiven = false;
//                System.out.println("available unit = " + tu);
//                for (Object t : ts) {
//                    u = (Unit) t;
//                    System.out.println("given unit = " + u);
//
//                    if (tu.equals(u)) {
//                        System.out.println("already given.");
//                        alreadyGiven = true;
//                    }
//                }
//                if (!alreadyGiven) {
//                    System.out.println("not reviously given.");
//                    return tu;
//                }
//
//            }
//
//        } else {
//
//            System.out.println("all units are used. now checking least filled.");
//
//            j = "select v.unit, count(v) from OpdVisit v where v.encounterDate=:ed and v.encounterType=:et group by v.unit order by 2 desc";
//            System.out.println("j = " + j);
//            List<Object[]> tss = getFacade().findAggregates(j, m, TemporalType.DATE);
//            System.out.println("tss = " + tss);
//
//            for (Object tso[] : tss) {
//                Object t = tso[0];
//                System.out.println("selected unit = " + t);
//                System.out.println("count = " + tso[1]);
//            }
//
//            for (Object tso[] : tss) {
//
//                Object t = tso[0];
//                System.out.println("selected unit = " + t);
//                System.out.println("count = " + tso[1]);
//                if (t instanceof Unit) {
//                    u = (Unit) t;
//                    System.out.println("u = " + u);
//                    if (!todayUnitIsClosed(ed, u)) {
//                        System.out.println("selected u = " + u);
//                        return u;
//                    }
//                }
//            }
//
//        }
//        System.out.println("returning null");
//        return null;
//    }
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
 
        if (todaysClosedUnit.getClosedUnit() == null){
            JsfUtil.addErrorMessage("Set Todays Casualty Unit");
            return "";
        }
        
        printPreview = false;

        selected = new OpdVisit();
        Patient pt = new Patient();
        selected.setPatient(pt);
        selected.setIntSerialNo(annualCount().intValue());
        selected.setSerialNo(stringConversionOfSerialNo(selected.getIntSerialNo()));
        selected.setIntDailyNo(todaysCasualtyCount().intValue());
        selected.setDailyNo(stringConversionOfDailyNo(selected.getIntDailyNo()));
        selected.setEncounterDate(new Date());
        selected.setEncounterType(EncounterType.Casulty);
        initializeEmbeddableKey();
        return "/opdVisit/casulty_visit";
    }

    public String addNewSpecialUnitVisit() {
        printPreview = false;
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

    public String addNewCloseUnitVisit() {
        printPreview = false;
        selected = new OpdVisit();
        Patient pt = new Patient();
        selected.setPatient(pt);
        selected.setIntSerialNo(annualCount().intValue());
        selected.setSerialNo(stringConversionOfSerialNo(selected.getIntSerialNo()));
        
        selected.setEncounterType(EncounterType.CloseUnitVisit);
        selected.setEncounterDate(new Date());
        initializeEmbeddableKey();
        return "/opdVisit/close_unit_visit";
    }
    
    public String stringConversionOfSerialNo(int sn) {
        int snt = sn + 27981; //27981 because neh next sereal number is 28124 bt they tested 144 bills then (28124-144+1)
        Calendar c = Calendar.getInstance();
        return snt + "/" + c.get(Calendar.YEAR);
    }
    
    public String stringConversionOfDailyNo(int sn) {
        int snt = sn + 1;
        return todaysClosedUnit.getClosedUnit().getCode()+" "+snt;
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
    
    
    public Long todaysCasualtyCount() {
        String j = "Select count(o) from OpdVisit o where o.encounterDate=:ed "
                + " and o.encounterType=:ec ";
        Map m = new HashMap();
        m.put("ed", new Date());
        m.put("ec", EncounterType.Casulty);
         System.out.println("getFacade().findLongByJpql(j, m) = " + getFacade().findLongByJpql(j, m));
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
        long count;
        //select u from Unit u where type(u)!=:uc and u.id not in(select c.closedUnit.id from ClosedUnit c where c.closedDate=:cd
        String j = "Select count(o) from OpdVisit o where o.encounterDate=:ed and o.unit=:u ";
        Map m = new HashMap();
        m.put("ed", new Date());
        m.put("u", u);
        long a = getFacade().findLongByJpql(j, m);
        count = a + 1;
        return count;
    }
    
//    public Long todaysCasualtyCount(Unit u) {
//
//        String j;
//        long count;
//        Map m = new HashMap();
//        j = "select count(c) from ClosedUnit c where c.closedDate=:cd";
//        m.put("u", u);
//        m.put("cd", new Date());
//        long c = closedUnitFacade.findLongByJpql(j, m);
//        count = c + 1;
//        return count;
//        
//    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("OpdVisitCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("OpdVisitUpdated"));
    }

    public void clear() {
        selected = null;
    }

    public void opdVisitRegister() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing to register");
        } else {
            register(EncounterType.OpdVisit);
        }
    }

    public void casualtyVisitRegister() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing to register");
        } else {
            register(EncounterType.Casulty);
        }
    }

    public void closedUnitVisitRegister() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing to register");
        } else {
            register(EncounterType.CloseUnitVisit);
        }
    }

    public void specialVisitRegister() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing to register");
        } else {
            register(EncounterType.SpecialUnitVisit);
        }

    }

    public String registerOpdVisit() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing to register");
            return "";
        }
        if (selected.getEncounterType() == EncounterType.Casulty) {
            casultyUnit = selected.getUnit();
        }
        if (selected.getEncounterType() == EncounterType.OpdVisit) {
            opdVisit = selected.getUnit();
        }
        if (selected.getEncounterType() == EncounterType.CloseUnitVisit) {
            closedUnitVisit = selected.getUnit();
        }
        if (selected.getEncounterType() == EncounterType.SpecialUnitVisit) {
            speUnit = selected.getUnit();
        }
        if (selected.getId() == null) {

            getFacade().create(selected);
            JsfUtil.addSuccessMessage("Registered");
            printPreview = true;

            if (selected.getEncounterType() == EncounterType.OpdVisit) {
                return addNewOpdVisit();
            } else if (selected.getEncounterType() == EncounterType.Casulty) {
                return addNewCasultyVisit();
            } else if (selected.getEncounterType() == EncounterType.SpecialUnitVisit) {
                return addNewSpecialUnitVisit();
            } else if (selected.getEncounterType() == EncounterType.CloseUnitVisit) {
                return addNewCloseUnitVisit();
            }
        } else {
            getFacade().edit(selected);
            JsfUtil.addSuccessMessage("Updated");
            return "";
        }
        return "";
    }

    public String register(EncounterType encounter) {
        System.out.println("Method in");
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing to register");
            return "";
        }
        if (selected.getEncounterType() == EncounterType.Casulty) {
            casultyUnit = selected.getUnit();
        }
        if (selected.getEncounterType() == EncounterType.OpdVisit) {
            opdVisit = selected.getUnit();
        }
        if (selected.getEncounterType() == EncounterType.CloseUnitVisit) {
            closedUnitVisit = selected.getUnit();
        }
        if (selected.getEncounterType() == EncounterType.SpecialUnitVisit) {
            speUnit = selected.getUnit();
        }
        if (selected.getId() == null) {

            getFacade().create(selected);
            System.out.println("Create");
            JsfUtil.addSuccessMessage("Registered");
            printPreview = true;

        } else {
            getFacade().edit(selected);
            System.out.println("Updated");
            JsfUtil.addSuccessMessage("Updated");
            return "";
        }
        return "";
    }
    
    public List<ClosedUnit> getFindTodayCasualtyUnit() {
        String j;
        Map m = new HashMap();
        j = "select c from ClosedUnit c where c.closedDate=:cd";
        m.put("cd", new Date());
        System.out.println("m = " + m);
        System.out.println("j = " + j);
        List<ClosedUnit> c = getClosedUnitFacade().findBySQL(j, m);
        return c;
    }

    public ClosedUnitFacade getClosedUnitFacade() {
        return closedUnitFacade;
    }

    public void setClosedUnitFacade(ClosedUnitFacade closedUnitFacade) {
        this.closedUnitFacade = closedUnitFacade;
    }

    public UnitFacade getUnitFacade() {
        return unitFacade;
    }

    public void setUnitFacade(UnitFacade unitFacade) {
        this.unitFacade = unitFacade;
    }
    

    public String recreateForm() {
        System.out.println("Recreate  form");
        System.out.println("selected.getEncounterType() = " + selected.getEncounterType());
        if (selected.getEncounterType() == EncounterType.OpdVisit) {
            return addNewOpdVisit();
        } else if (selected.getEncounterType() == EncounterType.Casulty) {
            return addNewCasultyVisit();
        } else if (selected.getEncounterType() == EncounterType.SpecialUnitVisit) {
            return addNewSpecialUnitVisit();
        } else if (selected.getEncounterType() == EncounterType.CloseUnitVisit) {
            return addNewCloseUnitVisit();
        }
        return "";
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
