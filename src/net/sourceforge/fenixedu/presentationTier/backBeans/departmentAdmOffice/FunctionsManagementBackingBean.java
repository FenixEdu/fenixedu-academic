/*
 * Created on Oct 18, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.departmentAdmOffice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class FunctionsManagementBackingBean extends FenixBackingBean {

    public List<Person> personsList, allPersonsList;

    public List<Function> inherentFunctions;

    public List<PersonFunction> activeFunctions, inactiveFunctions;

    public Integer page, personID, unitID, functionID, numberOfFunctions, personFunctionID;

    public Integer executionPeriod, duration, disabledVar, personNumber;

    public Unit unit;

    public Double credits;

    public String beginDate, endDate, personName, personType;

    public HtmlInputHidden creditsHidden, beginDateHidden, endDateHidden, personIDHidden, unitIDHidden,
            disabledVarHidden;

    public HtmlInputHidden functionIDHidden, personFunctionIDHidden, executionPeriodHidden,
            durationHidden;

    public Function function;

    public PersonFunction personFunction;

    public FunctionsManagementBackingBean() {
        getParametersFromLinks();
    }

    private void getParametersFromLinks() {

        if (getRequestParameter("personID") != null) {
            this.personID = Integer.valueOf(getRequestParameter("personID"));
        }
        if (getRequestParameter("page") != null) {
            this.page = Integer.valueOf(getRequestParameter("page").toString());
        }
        if (getRequestParameter("name") != null) {
            this.personName = getRequestParameter("name").toString();
        }
        if (getRequestParameter("unitID") != null) {
            this.unitID = Integer.valueOf(getRequestParameter("unitID").toString());
        }
        if (getRequestParameter("personFunctionID") != null
                && !getRequestParameter("personFunctionID").equals("")) {
            this.personFunctionID = Integer.valueOf(getRequestParameter("personFunctionID").toString());
        }
        if (getRequestParameter("functionID") != null && !getRequestParameter("functionID").equals("")) {
            this.functionID = Integer.valueOf(getRequestParameter("functionID").toString());
        }
        if (getRequestParameter("disabledVar") != null && !getRequestParameter("disabledVar").equals("")) {
            this.disabledVar = Integer.valueOf(getRequestParameter("disabledVar").toString());
        }
    }

    public String associateNewFunction() throws FenixFilterException, FenixServiceException,
            ParseException {

        if (this.getUnit() == null
                || (!this.getUnit().getTopUnits().isEmpty() && !this.getUnit().getTopUnits().contains(
                        getEmployeeDepartmentUnit()))
                || (this.getUnit().getTopUnits().isEmpty() && !this.getUnit().equals(
                        getEmployeeDepartmentUnit()))) {
            setErrorMessage("error.invalid.unit");
        } else if (!this.getUnit().getFunctions().contains(this.getFunction())) {
            setErrorMessage("error.invalid.function");        
        } else {

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Double credits = Double.valueOf(this.getCredits());
            Date beginDate_ = null, endDate_ = null;

            try {
                if (this.getBeginDate() != null) {
                    beginDate_ = format.parse(this.getBeginDate());
                } else {
                    setErrorMessage("error.notBeginDate");
                    return "";
                }
                if (this.getEndDate() != null) {
                    endDate_ = format.parse(this.getEndDate());
                } else {
                    setErrorMessage("error.notEndDate");
                    return "";
                }

                final Object[] argsToRead = { this.getFunctionID(), this.getPersonID(), credits,
                        YearMonthDay.fromDateFields(beginDate_), YearMonthDay.fromDateFields(endDate_) };
                ServiceUtils.executeService(getUserView(), "AssociateNewFunctionToPerson", argsToRead);
                setErrorMessage("message.success");
                return "success";

            } catch (ParseException e) {
                setErrorMessage("error.date1.format");
            } catch (FenixServiceException e) {
                setErrorMessage(e.getMessage());
            } catch (DomainException e) {
                setErrorMessage(e.getMessage());
            }
        }
        return "";
    }

    public String editFunction() throws FenixFilterException, FenixServiceException {

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Double credits = Double.valueOf(this.getCredits());
        Date beginDate_ = null, endDate_ = null;

        try {
            if (this.getBeginDate() != null) {
                beginDate_ = format.parse(this.getBeginDate());
            } else {
                setErrorMessage("error.notBeginDate");
                return "";
            }
            if (this.getEndDate() != null) {
                endDate_ = format.parse(this.getEndDate());
            } else {
                setErrorMessage("error.notEndDate");
                return "";
            }

            final Object[] argsToRead = { this.getPersonFunctionID(), this.getFunctionID(),
                    YearMonthDay.fromDateFields(beginDate_), YearMonthDay.fromDateFields(endDate_),
                    credits };
            ServiceUtils.executeService(getUserView(), "EditPersonFunction", argsToRead);
            setErrorMessage("message.success");
            return "alterFunction";

        } catch (ParseException e) {
            setErrorMessage("error.date1.format");
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        }
        return "";
    }

    public String deletePersonFunction() throws FenixFilterException, FenixServiceException {
        activeFunctionsClear();
        final Object[] argsToRead = { this.getPersonFunctionID() };
        ServiceUtils.executeService(getUserView(), "DeletePersonFunction", argsToRead);
        setErrorMessage("message.success");
        return "";
    }

    private void activeFunctionsClear() throws FenixFilterException, FenixServiceException {
        this.activeFunctions.remove(this.getPersonFunction());
        Iterator<Function> iter = this.inherentFunctions.iterator();
        while (iter.hasNext()) {
            if (iter.next().getParentInherentFunction().equals(this.getPersonFunction().getFunction())) {
                iter.remove();
            }
        }
    }

    public List<PersonFunction> getActiveFunctions() throws FenixFilterException, FenixServiceException {
        if (this.activeFunctions == null) {
            Person person = this.getPerson();
            List<PersonFunction> activeFunctions = person.getActivePersonFunctions();
            this.activeFunctions = new ArrayList<PersonFunction>();

            addValidFunctions(activeFunctions, this.activeFunctions);
            Collections.sort(this.activeFunctions, new BeanComparator("endDate"));
        }
        return this.activeFunctions;
    }

    public List<PersonFunction> getInactiveFunctions() throws FenixFilterException,
            FenixServiceException {

        if (this.inactiveFunctions == null) {
            Person person = this.getPerson();
            List<PersonFunction> inactiveFunctions = person.getInactivePersonFunctions();
            this.inactiveFunctions = new ArrayList<PersonFunction>();

            addValidFunctions(inactiveFunctions, this.inactiveFunctions);
            Collections.sort(this.inactiveFunctions, new BeanComparator("endDate"));
        }
        return this.inactiveFunctions;
    }

    private void addValidFunctions(List<PersonFunction> functions, List<PersonFunction> functions_) {
        for (PersonFunction person_function : functions) {
            Unit unit = person_function.getFunction().getUnit();
            if (unit.getParentUnits().isEmpty()) {
                if (unit.equals(this.getEmployeeDepartmentUnit())) {
                    functions_.add(person_function);
                }
            } else {
                if (addPersonFunction(unit.getParentUnits(), this.getEmployeeDepartmentUnit())) {
                    functions_.add(person_function);
                }
            }
        }
    }

    private boolean addPersonFunction(List<Unit> parentUnits, Unit employeeUnit) {
        boolean found = false;
        for (Unit unit : parentUnits) {
            if (unit.equals(employeeUnit)) {
                return true;
            } else {
                found = addPersonFunction(unit.getParentUnits(), employeeUnit);
                if (found) {
                    return found;
                }
            }
        }
        return false;
    }    

    public int getNumberOfPages() throws FenixFilterException, FenixServiceException {
        if ((getPersonsNumber() % SessionConstants.LIMIT_FINDED_PERSONS) != 0) {
            return (getPersonsNumber() / SessionConstants.LIMIT_FINDED_PERSONS) + 1;
        } else {
            return (getPersonsNumber() / SessionConstants.LIMIT_FINDED_PERSONS);
        }
    }

    public String searchPersonByName() throws FenixFilterException, FenixServiceException {

        if (allPersonsList == null) {
            allPersonsList = new ArrayList<Person>();
        }
        allPersonsList = getAllValidPersonsByName();

        if (allPersonsList.isEmpty()) {
            setErrorMessage("error.search.person");
        } else {
            setIntervalPersons();
        }
        return "";
    }

    private void setIntervalPersons() throws FenixFilterException, FenixServiceException {
        int begin = (this.getPage() - 1) * SessionConstants.LIMIT_FINDED_PERSONS;
        int end = begin + SessionConstants.LIMIT_FINDED_PERSONS;
        if (end >= allPersonsList.size()) {
            getPersonsList().addAll(allPersonsList.subList(begin, allPersonsList.size()));
        } else {
            getPersonsList().addAll(allPersonsList.subList(begin, end));
        }
    }

    private List<Person> getAllValidPersonsByName() throws FenixServiceException, FenixFilterException {

        List<Person> allPersons = getAllPersonsToSearch(RoleType.PERSON);

        SearchParameters searchParameters = new SearchPerson.SearchParameters(personName, null, null,
                null, null, null, null, null);
        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);
        allPersons = (List<Person>) CollectionUtils.select(allPersons, predicate);

        Collections.sort(allPersons, new BeanComparator("nome"));
        return allPersons;
    }

    private List<Person> getAllPersonsToSearch(RoleType roleType) throws FenixServiceException,
            FenixFilterException {
        final Object[] argsToRead = { roleType };
        Role role = (Role) ServiceUtils.executeService(null, "ReadRoleByRoleType", argsToRead);
        List<Person> allPersons = new ArrayList<Person>();
        allPersons.addAll((List<Person>) role.getAssociatedPersons());
        return allPersons;
    }

    private List<Person> getAllPersonsToSearchByClass() throws FenixServiceException,
            FenixFilterException {
        List<Person> allPersons = new ArrayList<Person>();
        RoleType personTypeAux = RoleType.valueOf(personType);
        if (personTypeAux.equals(RoleType.EMPLOYEE)) {
            List<Employee> allEmployees = new ArrayList<Employee>();
            allEmployees.addAll(rootDomainObject.getEmployees());
            for (Employee employee : allEmployees) {
                if (employee.getEmployeeNumber().equals(personNumber)) {
                    if (!employee.getPerson().hasRole(RoleType.TEACHER)) {
                        allPersons.add(employee.getPerson());
                    }
                    return allPersons;
                }
            }
        } else if (personTypeAux.equals(RoleType.TEACHER)) {
            List<Teacher> allTeachers = new ArrayList<Teacher>();
            allTeachers.addAll(rootDomainObject.getTeachers());
            for (Teacher teacher : allTeachers) {
                if (teacher.getTeacherNumber().equals(personNumber)) {
                    allPersons.add(teacher.getPerson());
                    return allPersons;
                }
            }
        } else if (personTypeAux.equals(RoleType.STUDENT)) {
            List<Registration> allStudents = new ArrayList<Registration>();
            allStudents.addAll(rootDomainObject.getRegistrations());
            for (Registration registration : allStudents) {
                if (registration.getNumber().equals(personNumber)) {
                    allPersons.add(registration.getPerson());
                    return allPersons;
                }
            }
        } else if (personTypeAux.equals(RoleType.GRANT_OWNER)) {
            List<GrantOwner> allGrantOwner = new ArrayList<GrantOwner>();
            allGrantOwner.addAll(rootDomainObject.getGrantOwners());
            for (GrantOwner grantOwner : allGrantOwner) {
                if (grantOwner.getNumber().equals(personNumber)) {
                    allPersons.add(grantOwner.getPerson());
                    return allPersons;
                }
            }
        }
        return allPersons;
    }

    public String searchPersonByNumber() throws FenixFilterException, FenixServiceException {
        getPersonsList();
        this.personsList = getAllPersonsToSearchByClass();
        if (this.personsList.isEmpty()) {
            setErrorMessage("error.search.person");
        } else {
            this.allPersonsList = this.personsList;
        }
        return "";
    }

    public List<Person> getPersonsList() throws FenixFilterException, FenixServiceException {
        if (this.personsList == null) {
            this.personsList = new ArrayList<Person>();
        }
        return this.personsList;
    }

    public List<SelectItem> getPersonTypes() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        ResourceBundle bundle = getResourceBundle("resources/EnumerationResources");

        SelectItem selectItem1 = new SelectItem();
        selectItem1.setLabel(bundle.getString(RoleType.EMPLOYEE.getName()).trim());
        selectItem1.setValue(RoleType.EMPLOYEE.name());

        SelectItem selectItem2 = new SelectItem();
        selectItem2.setLabel(bundle.getString(RoleType.TEACHER.getName()).trim());
        selectItem2.setValue(RoleType.TEACHER.name());

        SelectItem selectItem3 = new SelectItem();
        selectItem3.setLabel(bundle.getString(RoleType.GRANT_OWNER.getName()).trim());
        selectItem3.setValue(RoleType.GRANT_OWNER.name());

        SelectItem selectItem4 = new SelectItem();
        selectItem4.setLabel(bundle.getString(RoleType.STUDENT.getName()).trim());
        selectItem4.setValue(RoleType.STUDENT.name());

        list.add(selectItem1);
        list.add(selectItem2);
        list.add(selectItem3);
        list.add(selectItem4);

        Collections.sort(list, new BeanComparator("label"));
        return list;
    }

    public List<SelectItem> getValidFunctions() throws FenixFilterException, FenixServiceException {
        List<SelectItem> list = new ArrayList<SelectItem>();
        SelectItem selectItem = null;
        for (Function function : this.getUnit().getFunctions()) {
            if (!function.isInherentFunction()
                    && ((this.getPersonFunction() != null && function.equals(this.getPersonFunction()
                            .getFunction())) || (this.getPersonFunction() == null && function
                            .isActive(new YearMonthDay())))) {
                selectItem = new SelectItem();
                selectItem.setValue(function.getIdInternal());
                selectItem.setLabel(function.getName());
                list.add(selectItem);
            }
        }
        Collections.sort(list, new BeanComparator("label"));
        this.numberOfFunctions = list.size();

        return list;
    }

    public List<SelectItem> getDurationList() throws FenixFilterException, FenixServiceException {
        List<SelectItem> list = new ArrayList<SelectItem>();
        SelectItem selectItem = null;

        for (int i = 1; i <= 2; i++) {
            selectItem = new SelectItem();
            selectItem.setValue(new Integer(i));
            selectItem.setLabel(i + " sem.");
            list.add(selectItem);
        }

        return list;
    }

    public List<SelectItem> getExecutionPeriods() throws FenixFilterException, FenixServiceException {
        List<ExecutionYear> allExecutionYears = rootDomainObject.getExecutionYears();
        List<SelectItem> list = new ArrayList<SelectItem>();
        String[] year = null;

        for (ExecutionYear executionYear : allExecutionYears) {
            year = executionYear.getYear().split("/");
            if (Integer.valueOf(year[0].trim()) >= 2005) {
                newSelectItem(executionYear, list);
            }
        }
        Collections.sort(list, new BeanComparator("label"));
        return list;
    }

    private void newSelectItem(ExecutionYear executionYear, List<SelectItem> list) {
        for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
            SelectItem selectItem = new SelectItem();
            selectItem.setValue(executionPeriod.getIdInternal());
            selectItem.setLabel(executionYear.getYear() + " - " + executionPeriod.getSemester()
                    + "º Semestre");
            list.add(selectItem);
        }
    }

    protected Set<Unit> getSubUnits(Unit unit, YearMonthDay currentDate) {        
        Set<Unit> units = new HashSet();
        for (AccountabilityTypeEnum accountabilityTypeEnum : getAccountabilityTypes()) {
            units.addAll(unit.getActiveSubUnits(currentDate, accountabilityTypeEnum));
        }                   
        return units;
    }

    protected AccountabilityTypeEnum[] getAccountabilityTypes() {
        AccountabilityTypeEnum[] accountabilityTypeEnums = {
                AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE,
                AccountabilityTypeEnum.ACADEMIC_STRUCTURE };
        return accountabilityTypeEnums;
    }   
    
    public String getUnits() throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {
        StringBuilder buffer = new StringBuilder();
        YearMonthDay currentDate = new YearMonthDay();
        buffer.append("<ul class='padding nobullet'>");
        if (this.getEmployeeDepartmentUnit() != null
                && this.getEmployeeDepartmentUnit().isActive(new YearMonthDay())) {
            getUnitsList(this.getEmployeeDepartmentUnit(), buffer, currentDate);
        } else {
            return "";
        }
        buffer.append("</ul>");
        return buffer.toString();
    } 

    private void getUnitsList(Unit parentUnit, StringBuilder buffer, YearMonthDay currentDate) {

        openLITag(buffer);

        List<Unit> subUnits = new ArrayList<Unit>(getSubUnits(parentUnit, currentDate));
        Collections.sort(subUnits, new BeanComparator("name"));
        
        if (!subUnits.isEmpty()) {
            putImage(parentUnit, buffer);
        }

        buffer.append("<a href=\"").append(getContextPath()).append(
                "/departmentAdmOffice/functionsManagement/chooseFunction.faces?personID=").append(
                personID).append("&unitID=").append(parentUnit.getIdInternal()).append("\">").append(
                parentUnit.getName()).append("</a>").append("</li>");

        if (!subUnits.isEmpty()) {
            openULTag(parentUnit, buffer);
        }

        for (Unit subUnit : subUnits) {            
            getUnitsList(subUnit, buffer, currentDate);           
        }

        if (!subUnits.isEmpty()) {
            closeULTag(buffer);
        }
    }

    public Unit getEmployeeDepartmentUnit() {

        IUserView userView = getUserView();
        Employee personEmployee = userView.getPerson().getEmployee();
        Unit departmentUnit = null;

        if (personEmployee != null) {
            Department department = personEmployee.getCurrentDepartmentWorkingPlace();
            if (department != null) {
                departmentUnit = department.getDepartmentUnit();
            }
        }

        return departmentUnit;
    }

    public Person getPerson() throws FenixFilterException, FenixServiceException {
        return (Person) rootDomainObject.readPartyByOID(getPersonID());
    }

    public int getPersonsNumber() throws FenixFilterException, FenixServiceException {
        return getAllPersonsList().size();
    }

    public List<Person> getAllPersonsList() throws FenixFilterException, FenixServiceException {
        if (allPersonsList == null) {
            if (this.page != null) {
                if (this.personName != null) {
                    searchPersonByName();
                } else if (this.personNumber != null) {
                    searchPersonByNumber();
                }
            } else {
                allPersonsList = new ArrayList<Person>();
            }
        }
        return allPersonsList;
    }

    public void setAllPersonsList(List<Person> allPersonsList) {
        this.allPersonsList = allPersonsList;
    }

    public Integer getPersonID() {
        if (this.personID == null && this.personIDHidden != null
                && this.personIDHidden.getValue() != null && !this.personIDHidden.getValue().equals("")) {
            this.personID = Integer.valueOf(this.personIDHidden.getValue().toString());
        }
        return personID;
    }

    public Integer getPage() {
        if (page == null) {
            page = new Integer(1);
        }
        return page;
    }

    public Unit getUnit() throws FenixFilterException, FenixServiceException {
        if (this.unit == null && this.getUnitID() != null) {
            this.unit = (Unit) rootDomainObject.readPartyByOID(getUnitID());
        } else if (this.unit == null && this.getPersonFunctionID() != null) {
            this.unit = this.getPersonFunction().getFunction().getUnit();
        }

        return this.unit;
    }

    public Integer getUnitID() {
        if (this.unitID == null && this.unitIDHidden != null && this.unitIDHidden.getValue() != null
                && !this.unitIDHidden.getValue().equals("")) {
            this.unitID = Integer.valueOf(this.unitIDHidden.getValue().toString());
        }
        return unitID;
    }

    public HtmlInputHidden getPersonIDHidden() {
        if (this.personIDHidden == null) {
            this.personIDHidden = new HtmlInputHidden();
            this.personIDHidden.setValue(this.getPersonID());
        }
        return personIDHidden;
    }

    public void setPersonIDHidden(HtmlInputHidden personIDHidden) {
        if (this.personIDHidden != null) {
            this.setPersonID(Integer.valueOf(this.personIDHidden.getValue().toString()));
        }
        this.personIDHidden = personIDHidden;
    }

    public HtmlInputHidden getUnitIDHidden() {
        if (this.unitIDHidden == null) {
            this.unitIDHidden = new HtmlInputHidden();
            this.unitIDHidden.setValue(this.getUnitID());
        }
        return unitIDHidden;
    }

    public void setUnitIDHidden(HtmlInputHidden unitIDHidden) {
        if (this.unitIDHidden != null) {
            this.setUnitID(Integer.valueOf(this.unitIDHidden.getValue().toString()));
        }
        this.unitIDHidden = unitIDHidden;
    }

    public Function getFunction() throws FenixFilterException, FenixServiceException {
        if (this.function == null && this.getFunctionID() != null) {
            this.function = (Function) rootDomainObject.readAccountabilityTypeByOID(getFunctionID());
        } else if (this.function == null && this.getPersonFunctionID() != null) {
            this.function = this.getPersonFunction().getFunction();
        }
        return this.function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public void setUnitID(Integer unitID) {
        this.unitID = unitID;
    }

    public Integer getFunctionID() {
        if (this.functionID == null && this.functionIDHidden != null
                && this.functionIDHidden.getValue() != null
                && !this.functionIDHidden.getValue().equals("")) {
            this.functionID = Integer.valueOf(this.functionIDHidden.getValue().toString());
        }
        return functionID;
    }

    public void setFunctionID(Integer functionID) {
        this.functionID = functionID;
    }

    public String getBeginDate() throws FenixFilterException, FenixServiceException {

        if (this.beginDate == null && this.beginDateHidden != null
                && this.beginDateHidden.getValue() != null
                && !this.beginDateHidden.getValue().equals("")) {
            this.beginDate = this.beginDateHidden.getValue().toString();

        } else if (this.beginDate == null && this.getPersonFunctionID() != null) {
            this.beginDate = DateFormatUtil.format("dd/MM/yyyy", this.getPersonFunction()
                    .getBeginDateInDateType());

        } else if (this.beginDate == null && this.getPersonFunctionID() == null
                && (this.beginDateHidden == null || this.beginDateHidden.getValue() == null)
                && this.getExecutionPeriod() != null) {

            ExecutionPeriod executionPeriod = rootDomainObject
                    .readExecutionPeriodByOID(this.executionPeriod);

            this.beginDate = (executionPeriod != null) ? DateFormatUtil.format("dd/MM/yyyy",
                    executionPeriod.getBeginDate()) : null;
        }
        return this.beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public Double getCredits() throws FenixFilterException, FenixServiceException {
        if (this.credits == null && this.creditsHidden != null && this.creditsHidden.getValue() != null
                && !this.creditsHidden.getValue().equals("")) {
            this.credits = Double.valueOf(this.creditsHidden.getValue().toString());

        } else if (this.credits == null && this.getPersonFunctionID() != null) {
            this.credits = this.getPersonFunction().getCredits();
        } else if (this.credits == null) {
            this.credits = new Double(0);
        }
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public String getEndDate() throws FenixFilterException, FenixServiceException {

        if (this.endDate == null && this.endDateHidden != null && this.endDateHidden.getValue() != null
                && !this.endDateHidden.getValue().equals("")) {
            this.endDate = this.endDateHidden.getValue().toString();

        } else if (this.endDate == null && this.getPersonFunctionID() != null
                && this.getPersonFunction().getEndDate() != null) {
            this.endDate = DateFormatUtil.format("dd/MM/yyyy", this.getPersonFunction()
                    .getEndDateInDateType());

        } else if (this.endDate == null && this.getPersonFunctionID() == null
                && (this.endDateHidden == null || this.endDateHidden.getValue() == null)
                && this.getExecutionPeriod() != null) {

            ExecutionPeriod executionPeriod = rootDomainObject
                    .readExecutionPeriodByOID(this.executionPeriod);

            ExecutionPeriod executionPeriodWithDuration = getDurationEndDate(executionPeriod);
            this.endDate = DateFormatUtil.format("dd/MM/yyyy", executionPeriodWithDuration.getEndDate());
        }
        return this.endDate;
    }

    private ExecutionPeriod getDurationEndDate(ExecutionPeriod executionPeriod) {
        ExecutionPeriod finalExecutionPeriod = executionPeriod;
        if (this.getDurationHidden() != null && this.durationHidden.getValue() != null
                && !this.durationHidden.getValue().equals("")) {
            Integer duration = Integer.valueOf(this.durationHidden.getValue().toString());
            for (int i = 1; i < duration; i++) {
                if (finalExecutionPeriod.getNextExecutionPeriod() != null) {
                    finalExecutionPeriod = finalExecutionPeriod.getNextExecutionPeriod();
                } else {
                    return executionPeriod;
                }
            }
        }
        return finalExecutionPeriod;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void setPersonID(Integer personID) {
        this.personID = personID;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPersonsList(List<Person> personsList) {
        this.personsList = personsList;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public HtmlInputHidden getBeginDateHidden() throws FenixFilterException, FenixServiceException {
        if (this.beginDateHidden == null) {
            this.beginDateHidden = new HtmlInputHidden();
            this.beginDateHidden.setValue(this.getBeginDate());
        }
        return beginDateHidden;
    }

    public void setBeginDateHidden(HtmlInputHidden beginDateHidden) {
        if (this.beginDateHidden != null) {
            this.setBeginDate(this.beginDateHidden.getValue().toString());
        }
        this.beginDateHidden = beginDateHidden;
    }

    public HtmlInputHidden getCreditsHidden() {
        if (this.creditsHidden == null) {
            this.creditsHidden = new HtmlInputHidden();
            this.creditsHidden.setValue(this.credits);
        }

        return creditsHidden;
    }

    public void setCreditsHidden(HtmlInputHidden creditsHidden) {
        if (this.creditsHidden != null) {
            this.setCredits(Double.valueOf(this.creditsHidden.getValue().toString()));
        }
        this.creditsHidden = creditsHidden;
    }

    public HtmlInputHidden getEndDateHidden() throws FenixFilterException, FenixServiceException {
        if (this.endDateHidden == null) {
            this.endDateHidden = new HtmlInputHidden();
            this.endDateHidden.setValue(this.getEndDate());
        }
        return endDateHidden;
    }

    public void setEndDateHidden(HtmlInputHidden endDateHidden) {
        if (this.endDateHidden != null) {
            this.setEndDate(this.endDateHidden.getValue().toString());
        }
        this.endDateHidden = endDateHidden;
    }

    public HtmlInputHidden getFunctionIDHidden() {
        if (this.functionIDHidden == null) {
            this.functionIDHidden = new HtmlInputHidden();
            this.functionIDHidden.setValue(this.functionID);
        }
        return functionIDHidden;
    }

    public void setFunctionIDHidden(HtmlInputHidden functionIDHidden) {
        this.functionIDHidden = functionIDHidden;
    }

    public Integer getNumberOfFunctions() throws FenixFilterException, FenixServiceException {
        if (this.numberOfFunctions == null) {
            getValidFunctions();
        }
        return numberOfFunctions;
    }

    public void setNumberOfFunctions(Integer numberOfFunctions) {
        this.numberOfFunctions = numberOfFunctions;
    }

    public Integer getPersonFunctionID() {
        if (this.personFunctionID == null && this.personFunctionIDHidden != null
                && this.personFunctionIDHidden.getValue() != null
                && !this.personFunctionIDHidden.getValue().equals("")) {
            this.personFunctionID = Integer.valueOf(this.personFunctionIDHidden.getValue().toString());
        }
        return personFunctionID;
    }

    public void setPersonFunctionID(Integer personFunctionID) {
        this.personFunctionID = personFunctionID;
    }

    public PersonFunction getPersonFunction() throws FenixFilterException, FenixServiceException {
        if (this.personFunction == null) {
            this.personFunction = (PersonFunction) rootDomainObject.readAccountabilityByOID(this
                    .getPersonFunctionID());
        }
        return personFunction;
    }

    public void setPersonFunction(PersonFunction person_Function) {
        this.personFunction = person_Function;
    }

    public HtmlInputHidden getPersonFunctionIDHidden() {
        if (this.personFunctionIDHidden == null) {
            this.personFunctionIDHidden = new HtmlInputHidden();
            this.personFunctionIDHidden.setValue(this.personFunctionID);
        }
        return personFunctionIDHidden;
    }

    public void setPersonFunctionIDHidden(HtmlInputHidden personFunctionIDHidden) {
        if (this.personFunctionIDHidden != null) {
            this.personFunctionID = Integer.valueOf(this.personFunctionIDHidden.getValue().toString());
        }
        this.personFunctionIDHidden = personFunctionIDHidden;
    }

    public List<Function> getInherentFunctions() throws FenixFilterException, FenixServiceException {
        if (this.inherentFunctions == null) {
            this.inherentFunctions = new ArrayList<Function>();
            for (PersonFunction personFunction : this.getActiveFunctions()) {
                this.inherentFunctions.addAll(personFunction.getFunction().getInherentFunctions());
            }
        }
        return inherentFunctions;
    }

    public void setInherentFunctions(List<Function> inherentFunctions) {
        this.inherentFunctions = inherentFunctions;
    }

    public void setActiveFunctions(List<PersonFunction> activeFunctions) {
        this.activeFunctions = activeFunctions;
    }

    public void setInactiveFunctions(List<PersonFunction> inactiveFunctions) {
        this.inactiveFunctions = inactiveFunctions;
    }

    public Integer getExecutionPeriod() throws FenixFilterException, FenixServiceException {
        if (executionPeriod == null && executionPeriodHidden != null
                && executionPeriodHidden.getValue() != null
                && !executionPeriodHidden.getValue().equals("")) {
            executionPeriod = Integer.valueOf(executionPeriodHidden.getValue().toString());
        } else if (executionPeriod == null) {
            executionPeriod = getCurrentExecutionPeriodID();
        }
        return executionPeriod;
    }

    private Integer getCurrentExecutionPeriodID() throws FenixFilterException, FenixServiceException {
        List<ExecutionPeriod> allExecutionPeriods = rootDomainObject.getExecutionPeriods();
        for (ExecutionPeriod period : allExecutionPeriods) {
            if (period.getState().equals(PeriodState.CURRENT)) {
                return period.getIdInternal();
            }
        }
        return null;
    }

    public void setExecutionPeriod(Integer executionPeriodID) {
        this.executionPeriod = executionPeriodID;
        if (executionPeriodID != null) {
            this.executionPeriodHidden.setValue(executionPeriodID);
        }
    }

    public Integer getDuration() {
        if (duration == null && durationHidden != null && durationHidden.getValue() != null
                && !durationHidden.getValue().equals("")) {
            duration = Integer.valueOf(durationHidden.getValue().toString());

        } else if (this.duration == null) {
            this.duration = new Integer(1);
        }
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
        if (duration != null) {
            this.durationHidden.setValue(duration);
        }
    }

    public HtmlInputHidden getExecutionPeriodHidden() throws FenixFilterException, FenixServiceException {
        if (executionPeriodHidden == null) {
            executionPeriodHidden = new HtmlInputHidden();
            executionPeriodHidden.setValue(this.getExecutionPeriod());

        } else if (executionPeriodHidden.getValue() == null) {
            executionPeriodHidden.setValue(this.getExecutionPeriod());
        }
        return executionPeriodHidden;
    }

    public void setExecutionPeriodHidden(HtmlInputHidden executionPeriodHidden) {
        this.executionPeriodHidden = executionPeriodHidden;
    }

    public HtmlInputHidden getDurationHidden() {
        if (this.durationHidden == null) {
            this.durationHidden = new HtmlInputHidden();
            this.durationHidden.setValue(new Integer(1));
        }
        return durationHidden;
    }

    public void setDurationHidden(HtmlInputHidden durationHidden) {
        this.durationHidden = durationHidden;
    }

    public HtmlInputHidden getDisabledVarHidden() {
        if (disabledVarHidden == null) {
            disabledVarHidden = new HtmlInputHidden();
            if (getRequestParameter("disabledVar") != null
                    && !getRequestParameter("disabledVar").equals("")) {
                this.disabledVarHidden.setValue(Integer.valueOf(getRequestParameter("disabledVar")
                        .toString()));
            }
        }
        return disabledVarHidden;
    }

    public void setDisabledVarHidden(HtmlInputHidden disabledVarHidden) {
        this.disabledVarHidden = disabledVarHidden;
    }

    public Integer getDisabledVar() {
        if (disabledVar == null && disabledVarHidden != null && disabledVarHidden.getValue() != null
                && !disabledVarHidden.getValue().equals("")) {
            disabledVar = Integer.valueOf(disabledVarHidden.getValue().toString());
        } else if (disabledVar == null) {
            disabledVar = 0;
        }
        return disabledVar;
    }

    public void setDisabledVar(Integer disabledVar) {
        this.disabledVar = disabledVar;
    }

    public Integer getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Integer personNumber) {
        this.personNumber = personNumber;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    protected void openULTag(Unit parentUnit, StringBuilder buffer) {
        buffer.append("<ul class='mvert0' id=\"").append("aa").append(parentUnit.getIdInternal())
                .append("\" ").append("style='display:none'>\r\n");
    }

    protected void putImage(Unit parentUnit, StringBuilder buffer) {
        buffer.append("<img ").append("src='").append(getContextPath()).append(
                "/images/toggle_plus10.gif' id=\"").append(parentUnit.getIdInternal()).append("\" ")
                .append("indexed='true' onClick=\"").append("check(document.getElementById('").append(
                        "aa").append(parentUnit.getIdInternal()).append("'),document.getElementById('")
                .append(parentUnit.getIdInternal()).append("'));return false;").append("\"> ");
    }

    protected void closeULTag(StringBuilder buffer) {
        buffer.append("</ul>");
    }

    protected void openLITag(StringBuilder buffer) {
        buffer.append("<li>");
    }
}
