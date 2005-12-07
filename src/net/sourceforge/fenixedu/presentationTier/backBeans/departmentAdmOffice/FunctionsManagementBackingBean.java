/*
 * Created on Oct 18, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.departmentAdmOffice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.IFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.IPersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;

import sun.text.Normalizer;

public class FunctionsManagementBackingBean extends FenixBackingBean {

    public List<IPerson> personsList, allPersonsList;

    public List<IFunction> inherentFunctions;

    public List<IPersonFunction> activeFunctions, inactiveFunctions;

    public Integer page, personID, unitID, functionID, numberOfFunctions, personFunctionID;

    public IUnit unit;

    public String credits, beginDate, endDate, personName;

    public HtmlInputHidden creditsHidden, beginDateHidden, endDateHidden, personIDHidden, unitIDHidden;

    public HtmlInputHidden functionIDHidden, personFunctionIDHidden;

    public IFunction function;

    public IPersonFunction personFunction;

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
        } else if (this.getPerson().containsActiveFunction(this.getFunction())) {
            setErrorMessage("error.duplicate.function");
        } else {

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Integer credits = Integer.valueOf(this.getCredits());

            Date beginDate_ = null, endDate_ = null;
            try {
                beginDate_ = format.parse(this.getBeginDate());
                endDate_ = format.parse(this.getEndDate());

                final Object[] argsToRead = { this.getFunctionID(), this.getPersonID(), credits,
                        beginDate_, endDate_ };
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

        if (this.getPerson().getInactiveFunctions().contains(this.getPersonFunction())
                && this.getPerson().containsActiveFunction(this.getFunction())) {

            setErrorMessage("error.duplicate.function");

        } else {

            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Integer credits = Integer.valueOf(this.getCredits());
            Date beginDate_ = null, endDate_ = null;

            try {
                beginDate_ = format.parse(this.getBeginDate());
                endDate_ = format.parse(this.getEndDate());

                final Object[] argsToRead = { this.getPersonFunctionID(), this.getFunctionID(),
                        beginDate_, endDate_, credits };
                ServiceUtils.executeService(getUserView(), "EditFunction", argsToRead);
                setErrorMessage("message.success");
                return "alterFunction";

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

    public List<IPersonFunction> getActiveFunctions() throws FenixFilterException, FenixServiceException {

        if (this.activeFunctions == null) {
            IPerson person = this.getPerson();
            List<IPersonFunction> activeFunctions = person.getActiveFunctions();
            this.activeFunctions = new ArrayList<IPersonFunction>();

            addValidFunctions(activeFunctions, this.activeFunctions);

            Collections.sort(this.activeFunctions, new BeanComparator("endDate"));
        }
        return this.activeFunctions;
    }

    public List<IPersonFunction> getInactiveFunctions() throws FenixFilterException,
            FenixServiceException {

        if (this.inactiveFunctions == null) {
            IPerson person = this.getPerson();
            List<IPersonFunction> inactiveFunctions = person.getInactiveFunctions();
            this.inactiveFunctions = new ArrayList<IPersonFunction>();

            addValidFunctions(inactiveFunctions, this.inactiveFunctions);

            Collections.sort(this.inactiveFunctions, new BeanComparator("endDate"));
        }
        return this.inactiveFunctions;
    }

    private void addValidFunctions(List<IPersonFunction> functions, List<IPersonFunction> functions_) {
        for (IPersonFunction person_function : functions) {
            IUnit unit = person_function.getFunction().getUnit();
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

    private boolean addPersonFunction(List<IUnit> parentUnits, IUnit employeeUnit) {
        boolean found = false;
        for (IUnit unit : parentUnits) {
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

    public String verifyFunction() throws FenixFilterException, FenixServiceException {

        if (this.getPerson().containsActiveFunction(this.getFunction())) {
            setErrorMessage("error.duplicate.function");
            return "";
        }

        return "confirmation";
    }

    public int getNumberOfPages() throws FenixFilterException, FenixServiceException {
        if ((getPersonsNumber() % SessionConstants.LIMIT_FINDED_PERSONS) != 0) {
            return (getPersonsNumber() / SessionConstants.LIMIT_FINDED_PERSONS) + 1;
        } else {
            return (getPersonsNumber() / SessionConstants.LIMIT_FINDED_PERSONS);
        }
    }

    public String searchPerson() throws FenixFilterException, FenixServiceException {

        if (allPersonsList == null) {
            allPersonsList = new ArrayList<IPerson>();
        }

        allPersonsList = getAllPersons();

        if (allPersonsList.isEmpty()) {
            setErrorMessage("error.search.person");
        } else {
            int begin = (this.getPage() - 1) * SessionConstants.LIMIT_FINDED_PERSONS;
            int end = begin + SessionConstants.LIMIT_FINDED_PERSONS;
            if (end >= allPersonsList.size()) {
                getPersonsList().addAll(allPersonsList.subList(begin, allPersonsList.size()));
            } else {
                getPersonsList().addAll(allPersonsList.subList(begin, end));
            }
        }
        return "";
    }

    private List<IPerson> getAllPersons() throws FenixServiceException, FenixFilterException {

        final Object[] argsToRead = { RoleType.PERSON };

        List<IPerson> allPersons = new ArrayList<IPerson>();
        IRole role = (IRole) ServiceUtils.executeService(null, "ReadRoleByRoleType", argsToRead);
        allPersons.addAll((List<IPerson>) role.getAssociatedPersons());

        String[] nameWords = personName.split(" ");
        normalizeName(nameWords);
        allPersons = getValidPersons(nameWords, allPersons);
        Collections.sort(allPersons, new BeanComparator("nome"));

        return allPersons;
    }

    public List<IPerson> getPersonsList() throws FenixFilterException, FenixServiceException {

        if (this.personsList == null) {
            this.personsList = new ArrayList<IPerson>();
        }
        return this.personsList;
    }

    private List<IPerson> getValidPersons(String[] nameWords, List<IPerson> allPersons) {

        List<IPerson> persons = new ArrayList();

        for (IPerson person : allPersons) {
            if (verifyNameEquality(nameWords, person)) {
                persons.add(person);
            }
        }
        return persons;
    }

    private boolean verifyNameEquality(String[] nameWords, IPerson person) {

        if (nameWords == null) {
            return true;
        }

        if (person.getNome() != null) {
            String[] personNameWords = person.getNome().trim().split(" ");
            normalizeName(personNameWords);
            int j, i;
            for (i = 0; i < nameWords.length; i++) {
                if (!nameWords[i].equals("")) {
                    for (j = 0; j < personNameWords.length; j++) {
                        if (personNameWords[j].equals(nameWords[i])) {
                            break;
                        }
                    }
                    if (j == personNameWords.length) {
                        return false;
                    }
                }
            }
            if (i == nameWords.length) {
                return true;
            }
        }
        return false;
    }

    public List<SelectItem> getValidFunctions() throws FenixFilterException, FenixServiceException {
        List<SelectItem> list = new ArrayList<SelectItem>();

        SelectItem selectItem = null;
        for (IFunction function : this.getUnit().getFunctions()) {
            if (!function.isInherentFunction()) {
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

    public String getUnits() throws FenixFilterException, FenixServiceException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<ul>");
        if (this.getEmployeeDepartmentUnit() != null
                && this.getEmployeeDepartmentUnit().isActive(Calendar.getInstance().getTime())) {
            getUnitsList(this.getEmployeeDepartmentUnit(), 0, buffer);
        } else {            
            return "";
        }
        buffer.append("</ul>");
        return buffer.toString();
    }

    private void getUnitsList(IUnit parentUnit, int index, StringBuffer buffer) {

        buffer.append("<li>").append("<a href=\"").append(getContextPath()).append(
                "/departmentAdmOffice/functionsManagement/chooseFunction.faces?personID=").append(
                personID).append("&unitID=").append(parentUnit.getIdInternal()).append("\">").append(
                parentUnit.getName()).append("</a>").append("</li>").append("<ul>");

        for (IUnit subUnit : parentUnit.getSubUnits()) {
            getUnitsList(subUnit, index + 1, buffer);
        }

        buffer.append("</ul>");
    }

    public IUnit getEmployeeDepartmentUnit() {

        IUserView userView = getUserView();
        IEmployee personEmployee = userView.getPerson().getEmployee();
        IUnit departmentUnit = null;

        if (personEmployee != null) {
            IDepartment department = personEmployee.getDepartmentWorkingPlace();
            if (department != null) {
                departmentUnit = department.getUnit();
            }
        }

        return departmentUnit;
    }

    public IPerson getPerson() throws FenixFilterException, FenixServiceException {

        final Object[] argsToRead = { Person.class, this.getPersonID() };
        return (IPerson) ServiceUtils.executeService(null, "ReadDomainObject", argsToRead);
    }

    private void normalizeName(String[] nameWords) {
        for (int i = 0; i < nameWords.length; i++) {
            nameWords[i] = normalize(nameWords[i]);
        }
    }

    private String normalize(String string) {
        return Normalizer.normalize(string, Normalizer.DECOMP, Normalizer.DONE).replaceAll(
                "[^\\p{ASCII}]", "").toLowerCase();
    }

    public int getPersonsNumber() throws FenixFilterException, FenixServiceException {
        return getAllPersonsList().size();
    }

    public List<IPerson> getAllPersonsList() throws FenixFilterException, FenixServiceException {
        if (allPersonsList == null) {
            if (this.page != null) {
                searchPerson();
            } else {
                allPersonsList = new ArrayList<IPerson>();
            }
        }
        return allPersonsList;
    }

    public void setAllPersonsList(List<IPerson> allPersonsList) {
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

    public IUnit getUnit() throws FenixFilterException, FenixServiceException {

        if (this.unit == null && this.getUnitID() != null) {
            final Object[] argsToRead = { Unit.class, this.getUnitID() };
            this.unit = (IUnit) ServiceUtils.executeService(null, "ReadDomainObject", argsToRead);

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

    public IFunction getFunction() throws FenixFilterException, FenixServiceException {
        if (this.function == null && this.getFunctionID() != null) {
            final Object[] argsToRead = { Function.class, this.getFunctionID() };
            this.function = (IFunction) ServiceUtils
                    .executeService(null, "ReadDomainObject", argsToRead);

        } else if (this.function == null && this.getPersonFunctionID() != null) {
            this.function = this.getPersonFunction().getFunction();
        }
        return this.function;
    }

    public void setFunction(IFunction function) {
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
            this.beginDate = processDate(this.getPersonFunction().getBeginDate());
        }
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getCredits() throws FenixFilterException, FenixServiceException {
        if (this.credits == null && this.creditsHidden != null && this.creditsHidden.getValue() != null
                && !this.creditsHidden.getValue().equals("")) {
            this.credits = this.creditsHidden.getValue().toString();

        } else if (this.credits == null && this.getPersonFunctionID() != null) {
            this.credits = this.getPersonFunction().getCredits().toString();
        }
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getEndDate() throws FenixFilterException, FenixServiceException {
        if (this.endDate == null && this.endDateHidden != null && this.endDateHidden.getValue() != null
                && !this.endDateHidden.getValue().equals("")) {
            this.endDate = this.endDateHidden.getValue().toString();

        } else if (this.endDate == null && this.getPersonFunctionID() != null) {
            this.endDate = processDate(this.getPersonFunction().getEndDate());
        }
        return endDate;
    }

    private String processDate(Date date) throws FenixFilterException, FenixServiceException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return (day + "/" + month + "/" + year);
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setUnit(IUnit unit) {
        this.unit = unit;
    }

    public void setPersonID(Integer personID) {
        this.personID = personID;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPersonsList(List<IPerson> personsList) {
        this.personsList = personsList;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public HtmlInputHidden getBeginDateHidden() {
        if (this.beginDateHidden == null) {
            this.beginDateHidden = new HtmlInputHidden();
            this.beginDateHidden.setValue(this.beginDate);
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
            this.setCredits(this.creditsHidden.getValue().toString());
        }
        this.creditsHidden = creditsHidden;
    }

    public HtmlInputHidden getEndDateHidden() {
        if (this.endDateHidden == null) {
            this.endDateHidden = new HtmlInputHidden();
            this.endDateHidden.setValue(this.endDate);
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

    public IPersonFunction getPersonFunction() throws FenixFilterException, FenixServiceException {
        if (this.personFunction == null) {
            final Object[] argsToRead = { PersonFunction.class, this.getPersonFunctionID() };
            this.personFunction = (IPersonFunction) ServiceUtils.executeService(null,
                    "ReadDomainObject", argsToRead);
        }
        return personFunction;
    }

    public void setPersonFunction(IPersonFunction person_Function) {
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

    public List<IFunction> getInherentFunctions() throws FenixFilterException, FenixServiceException {
        if (this.inherentFunctions == null) {
            this.inherentFunctions = new ArrayList<IFunction>();
            for (IPersonFunction personFunction : this.getActiveFunctions()) {
                this.inherentFunctions.addAll(personFunction.getFunction().getInherentFunctions());
            }
        }
        return inherentFunctions;
    }

    public void setInherentFunctions(List<IFunction> inherentFunctions) {
        this.inherentFunctions = inherentFunctions;
    }

    public void setActiveFunctions(List<IPersonFunction> activeFunctions) {
        this.activeFunctions = activeFunctions;
    }

    public void setInactiveFunctions(List<IPersonFunction> inactiveFunctions) {
        this.inactiveFunctions = inactiveFunctions;
    }
}
