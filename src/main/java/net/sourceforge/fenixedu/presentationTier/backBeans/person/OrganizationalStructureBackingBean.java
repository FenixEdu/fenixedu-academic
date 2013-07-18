/*
 * Created on Nov 10, 2005
 *	by angela
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYearsService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.StringNormalizer;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class OrganizationalStructureBackingBean extends FenixBackingBean {

    public Integer choosenExecutionYearID;

    public Unit parentUnit;

    public Integer personID;

    public ResourceBundle bundle;

    public String listType;

    private HtmlInputHidden unitIDHidden;

    public OrganizationalStructureBackingBean() {
        if (getRequestParameter("unitID") != null) {
            getUnitIDHidden().setValue(getRequestParameter("unitID"));
        }
        this.bundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
    }

    public List<SelectItem> getExecutionYears() throws  FenixServiceException {
        final Set<ExecutionYear> executionYears = rootDomainObject.getExecutionYearsSet();

        List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
        for (ExecutionYear executionYear : executionYears) {
            if (executionYear.getYear().compareTo("2005/2006") >= 0) {
                result.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear(), executionYear.getState()
                        .getStateCode()));
            }
        }

        Collections.reverse(result);
        if (getChoosenExecutionYearID() == null) {
            for (SelectItem selectExecutionYear : result) {
                if (selectExecutionYear.getDescription().equals(PeriodState.CURRENT_CODE)) {
                    setChoosenExecutionYearID((Integer) selectExecutionYear.getValue());
                }
            }
        }

        return result;
    }

    public String getUnits() throws  FenixServiceException {
        StringBuilder buffer = new StringBuilder();
        YearMonthDay currentDate = new YearMonthDay();
        String partyTypeOrClassificationName = null;

        Map<String, Set<Unit>> allInstitutionSubUnits = getAllInstitutionSubUnits();

        for (String typeOrClassificationName : allInstitutionSubUnits.keySet()) {

            partyTypeOrClassificationName = null;

            for (Unit unit : allInstitutionSubUnits.get(typeOrClassificationName)) {

                // Title
                if (partyTypeOrClassificationName == null) {
                    partyTypeOrClassificationName = typeOrClassificationName;

                    buffer.append("<h3 class='mtop2'>")
                            .append(hasKey(bundle, partyTypeOrClassificationName) ? getBundle().getString(
                                    partyTypeOrClassificationName) : partyTypeOrClassificationName).append("</h3>\r\n");
                }

                buffer.append("<ul class='padding nobullet'>\r\n");

                List<Unit> activeSubUnits = unit.getActiveSubUnits(currentDate, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);
                Collections.sort(activeSubUnits, Unit.COMPARATOR_BY_NAME_AND_ID);

                if (!activeSubUnits.isEmpty()) {

                    buffer.append("\t<li><img ").append("src='").append(getContextPath())
                            .append("/images/toggle_plus10.gif' id='img").append(unit.getIdInternal()).append("'")
                            .append("onClick=\"check(document.getElementById('aa").append(unit.getIdInternal())
                            .append("'),document.getElementById('").append(unit.getIdInternal()).append("'));return false;\"/> ");

                    buffer.append("<a href='").append(getContextPath())
                            .append("/messaging/organizationalStructure/chooseUnit.faces?unitID=").append(unit.getIdInternal())
                            .append("'>").append(unit.getNameWithAcronym()).append("</a></li>\r\n");

                } else {

                    buffer.append("\t<li><a href='").append(getContextPath())
                            .append("/messaging/organizationalStructure/chooseUnit.faces?unitID=").append(unit.getIdInternal())
                            .append("'>").append(unit.getNameWithAcronym()).append("</a></li>\r\n");

                }

                getInstitutionSubUnitsTree(buffer, unit, activeSubUnits, currentDate);
                buffer.append("</ul>\r\n");
            }
        }

        return buffer.toString();
    }

    private void getInstitutionSubUnitsTree(StringBuilder buffer, Unit parentUnit, List<Unit> activeSubUnits,
            YearMonthDay currentDate) {

        if (!activeSubUnits.isEmpty()) {
            buffer.append("\t<li class='nobullet'><ul class='mvert0' id='aa").append(parentUnit.getIdInternal())
                    .append("' style='display:none'>\r\n");
            for (Unit subUnit : activeSubUnits) {
                getSubUnitsWithoutAggregatedUnitsList(buffer, currentDate, subUnit);
            }
            buffer.append("\t</ul></li>\r\n");
        }
    }

    private void getSubUnitsList(Unit parentUnit, StringBuilder buffer, YearMonthDay currentDate) {

        buffer.append("\t\t<li><a href='").append(getContextPath())
                .append("/messaging/organizationalStructure/chooseUnit.faces?unitID=").append(parentUnit.getIdInternal())
                .append("'>").append(parentUnit.getNameWithAcronym()).append("</a></li>\r\n");

        List<Unit> activeSubUnits = parentUnit.getActiveSubUnits(currentDate, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);

        if (!activeSubUnits.isEmpty()) {
            buffer.append("\t\t<li class='nobullet'><ul class='mvert0'>\r\n");
        }

        for (Unit subUnit : activeSubUnits) {
            getSubUnitsWithoutAggregatedUnitsList(buffer, currentDate, subUnit);
        }

        if (!activeSubUnits.isEmpty()) {
            buffer.append("\t\t</ul></li>\r\n");
        }
    }

    private void getSubUnitsWithoutAggregatedUnitsList(StringBuilder buffer, YearMonthDay currentDate, Unit subUnit) {
        List<Unit> validInstitutionSubUnits = null;
        if (subUnit.isAggregateUnit()) {
            validInstitutionSubUnits = getValidSubUnits(subUnit, currentDate);
        }
        if (validInstitutionSubUnits != null) {
            for (Unit validSubUnit : validInstitutionSubUnits) {
                getSubUnitsList(validSubUnit, buffer, currentDate);
            }
        } else {
            getSubUnitsList(subUnit, buffer, currentDate);
        }
    }

    public Map<String, Set<Unit>> getAllInstitutionSubUnits() throws  FenixServiceException {

        YearMonthDay currentDate = new YearMonthDay();

        Map<String, Set<Unit>> resultMap = new TreeMap<String, Set<Unit>>(new Comparator<String>() {
            @Override
            public int compare(String arg0, String arg1) {
                String firstString = StringNormalizer.normalize(hasKey(getBundle(), arg0) ? getBundle().getString(arg0) : arg0);
                String secondString = StringNormalizer.normalize(hasKey(getBundle(), arg1) ? getBundle().getString(arg1) : arg1);
                return firstString.compareToIgnoreCase(secondString);
            }
        });

        Unit istUnit = UnitUtils.readInstitutionUnit();
        if (istUnit == null) {
            return new HashMap<String, Set<Unit>>();
        }

        for (Unit subUnit : istUnit.getActiveSubUnits(currentDate, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE)) {
            if (subUnit.isAggregateUnit()) {
                for (Unit unit : getValidSubUnits(subUnit, currentDate)) {
                    addUnitToMap(resultMap, unit);
                }
            } else {
                addUnitToMap(resultMap, subUnit);
            }
        }

        return resultMap;
    }

    private void addUnitToMap(Map<String, Set<Unit>> resultMap, Unit subUnit) {

        String typeName = subUnit.getClassification() != null ? subUnit.getClassification().getName() : null;
        if (StringUtils.isEmpty(typeName)) {
            typeName = subUnit.getType() != null ? subUnit.getType().getName() : null;
        }

        if (typeName != null) {
            if (!resultMap.containsKey(typeName)) {
                Set<Unit> newSet = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
                newSet.add(subUnit);
                resultMap.put(typeName, newSet);
            } else {
                resultMap.get(typeName).add(subUnit);
            }
        }
    }

    private List<Unit> getValidSubUnits(Unit unit, YearMonthDay currentDate) {
        List<Unit> result = new ArrayList<Unit>();
        for (Unit subUnit : unit.getActiveSubUnits(currentDate, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE)) {
            if (!subUnit.isAggregateUnit()) {
                result.add(subUnit);
            } else {
                result.addAll(getValidSubUnits(subUnit, currentDate));
            }
        }
        return result;
    }

    public String getInstituitionName() throws FenixServiceException {
        Unit institution = UnitUtils.readInstitutionUnit();
        if (institution != null) {
            return institution.getName();
        }
        return null;
    }

    public String getTitle() throws  FenixServiceException {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<p><em>");
        buffer.append(this.getUnit().getParentUnitsPresentationName());
        buffer.append("</em></p>");
        buffer.append("<h2>").append(this.getUnit().getNameWithAcronym()).append("</h2>");
        return buffer.toString();
    }

    public String getFunctions() throws  FenixServiceException {

        StringBuilder buffer = new StringBuilder();
        YearMonthDay currentDate = new YearMonthDay();
        Unit chooseUnit = this.getUnit();
        ExecutionYear iExecutionYear = getExecutionYear(getChoosenExecutionYearID());

        buffer.append("<ul class='mtop3 nobullet noindent'><li>");
        // buffer.append("<image
        // src='").append(getContextPath()).append("/images/unit-icon.gif'/>")
        // .append(" ");
        buffer.append("<strong class='highlight6' id='aa");
        buffer.append(chooseUnit.getIdInternal()).append("'>");
        buffer.append(chooseUnit.getName()).append("</strong>");

        if (StringUtils.isEmpty(getListType()) || getListType().equals("#") || getListType().equals("0")) {
            printUnitWorkingEmployees(chooseUnit, iExecutionYear, buffer);
        }

        for (Function function : getSortFunctionList(chooseUnit)) {
            if (function.belongsToPeriod(iExecutionYear.getBeginDateYearMonthDay(), iExecutionYear.getEndDateYearMonthDay())) {
                if (StringUtils.isEmpty(getListType()) || getListType().equals("#") || getListType().equals("1")) {
                    buffer.append("<ul><li class='tree_label'>").append(function.getName()).append(": ");
                    buffer.append((function.getParentInherentFunction() != null) ? " (Cargo Inerente)" : "");
                    getPersonFunctionsList(chooseUnit, function, buffer, iExecutionYear);
                    buffer.append("</li></ul>");
                }
            }
        }

        List<Unit> activeSubUnit = chooseUnit.getActiveSubUnits(currentDate, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);
        Collections.sort(activeSubUnit, Unit.COMPARATOR_BY_NAME_AND_ID);

        for (Unit subUnit : activeSubUnit) {
            getSubUnitsWithoutAggregatedUnitsToFunctionList(buffer, iExecutionYear, currentDate, subUnit);
        }

        buffer.append("</li></ul>");
        return buffer.toString();
    }

    private void getSubUnitsFunctions(Unit subUnit, YearMonthDay currentDate, ExecutionYear iExecutionYear, StringBuilder buffer) {

        buffer.append("<ul class='mtop1 nobullet'><li>");
        // buffer.append("<image
        // src='").append(getContextPath()).append("/images/unit-icon.gif'/>")
        // .append(" ");
        buffer.append("<strong id='aa").append(subUnit.getIdInternal()).append("' >").append(subUnit.getName())
                .append("</strong>");

        if (StringUtils.isEmpty(getListType()) || getListType().equals("#") || getListType().equals("0")) {
            printUnitWorkingEmployees(subUnit, iExecutionYear, buffer);
        }

        for (Function function : getSortFunctionList(subUnit)) {
            if (function.belongsToPeriod(iExecutionYear.getBeginDateYearMonthDay(), iExecutionYear.getEndDateYearMonthDay())) {
                if (StringUtils.isEmpty(getListType()) || getListType().equals("#") || getListType().equals("1")) {
                    buffer.append("<ul><li class='tree_label'>").append(function.getName()).append(": ");
                    buffer.append((function.getParentInherentFunction() != null) ? " (Cargo Inerente)" : "");
                    getPersonFunctionsList(subUnit, function, buffer, iExecutionYear);
                    buffer.append("</li></ul>");
                }
            }
        }

        for (Unit subUnit2 : subUnit.getActiveSubUnits(currentDate, AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE)) {
            getSubUnitsWithoutAggregatedUnitsToFunctionList(buffer, iExecutionYear, currentDate, subUnit2);
        }

        buffer.append("</li></ul>");
    }

    private String getHomePageUrl(Person person) {
        StringBuilder buffer = new StringBuilder();
        String appContext = PropertiesManager.getProperty("app.context");

        if (person.getHomepage() != null && person.getHomepage().getActivated()) {
            buffer.append(getRequest().getScheme()).append("://").append(getRequest().getServerName()).append(":")
                    .append(getRequest().getServerPort()).append("/")
                    .append(!StringUtils.isEmpty(appContext) ? appContext + "/" : "").append("homepage/")
                    .append(person.getIstUsername());
        }
        return buffer.toString();
    }

    private void printPersonHomePage(Person person, StringBuilder buffer) {
        String homePageUrl = getHomePageUrl(person);
        if (!StringUtils.isEmpty(homePageUrl)) {
            buffer.append("<a href='").append(homePageUrl).append("' target='_blank'>").append(person.getNickname())
                    .append("</a>");
            // buffer.append(" <image
            // src='").append(getContextPath()).append("/images/external.gif'/>");
        } else {
            buffer.append(person.getNickname());
        }
    }

    private void printUnitWorkingEmployees(Unit subUnit, ExecutionYear iExecutionYear, StringBuilder buffer) {

        buffer.append("<ul class='unit3'>");
        List<Contract> contractsByContractType = subUnit.getWorkingContracts();
        Collections.sort(contractsByContractType, Contract.CONTRACT_COMPARATOR_BY_PERSON_NAME);

        for (Contract contract : contractsByContractType) {
            if (contract.belongsToPeriod(iExecutionYear.getBeginDateYearMonthDay(), iExecutionYear.getEndDateYearMonthDay())) {

                buffer.append("<li>");

                // if
                // (contract.getEmployee().getPerson().getGender().equals(Gender.
                // MALE))
                // {
                // buffer.append("<image
                // src='").append(getContextPath()).append(
                // "/images/worker-icon.png'/>").append(" ");
                // } else if
                // (contract.getEmployee().getPerson().getGender().equals(Gender.
                // FEMALE))
                // {
                // buffer.append("<image
                // src='").append(getContextPath()).append(
                // "/images/woman-icon.png'/>").append(" ");
                // } else {
                // buffer.append("<image
                // src='").append(getContextPath()).append(
                // "/images/person-icon.gif'/>").append(" ");
                // }

                printPersonHomePage(contract.getPerson(), buffer);
                buffer.append("</li>");
            }
        }
        buffer.append("</ul>");
    }

    private void getSubUnitsWithoutAggregatedUnitsToFunctionList(StringBuilder buffer, ExecutionYear iExecutionYear,
            YearMonthDay currentDate, Unit subUnit) {
        List<Unit> validInstitutionSubUnits = null;
        if (subUnit.isAggregateUnit()) {
            validInstitutionSubUnits = getValidSubUnits(subUnit, currentDate);
        }
        if (validInstitutionSubUnits != null) {
            for (Unit validSubUnit : validInstitutionSubUnits) {
                getSubUnitsFunctions(validSubUnit, currentDate, iExecutionYear, buffer);
            }
        } else {
            getSubUnitsFunctions(subUnit, currentDate, iExecutionYear, buffer);
        }
    }

    private void getPersonFunctionsList(Unit unit, Function function, StringBuilder buffer, ExecutionYear iExecutionYear) {
        addPersonFunctions(function, buffer, iExecutionYear);
        if (function.getParentInherentFunction() != null) {
            addPersonFunctions(function.getParentInherentFunction(), buffer, iExecutionYear);
        }
    }

    private void addPersonFunctions(Function function, StringBuilder buffer, ExecutionYear iExecutionYear) {
        Collection<PersonFunction> validPersonFunction = getValidPersonFunction(iExecutionYear, function);
        if (!validPersonFunction.isEmpty()) {
            buffer.append("<ul class='unit1'>");
            for (PersonFunction personFunction : validPersonFunction) {
                buffer.append("<li>");
                // buffer.append("<image
                // src='").append(getContextPath()).append(
                // "/images/person-icon.gif'/>").append(" ");
                printPersonHomePage(personFunction.getPerson(), buffer);
                buffer.append(" (");
                buffer.append(personFunction.getBeginDate().toString()).append(" - ");
                if (personFunction.getEndDate() != null) {
                    buffer.append(personFunction.getEndDate().toString());
                }
                buffer.append(")").append("</li>");
            }
            buffer.append("</ul>");
        }
    }

    private SortedSet<Function> getSortFunctionList(Unit unit) {
        SortedSet<Function> functions = unit.getOrderedFunctions();

        Iterator<Function> iterator = functions.iterator();
        while (iterator.hasNext()) {
            Function function = iterator.next();

            if (function.isVirtual()) {
                iterator.remove();
            }
        }

        return functions;
    }

    public ExecutionYear getExecutionYear(Integer executionYear) throws  FenixServiceException {

        ExecutionYear iExecutionYear = ReadExecutionYearsService.run(executionYear);
        return iExecutionYear;
    }

    public SortedSet<PersonFunction> getValidPersonFunction(ExecutionYear iExecutionYear, Function function) {
        SortedSet<PersonFunction> personFunctions = new TreeSet<PersonFunction>(PersonFunction.COMPARATOR_BY_PERSON_NAME);
        for (PersonFunction personFunction : function.getPersonFunctions()) {
            if (personFunction
                    .belongsToPeriod(iExecutionYear.getBeginDateYearMonthDay(), iExecutionYear.getEndDateYearMonthDay())) {
                personFunctions.add(personFunction);
            }
        }
        return personFunctions;
    }

    public List<SelectItem> getListingType() {
        List<SelectItem> list = new ArrayList<SelectItem>();

        SelectItem selectItem = new SelectItem();
        selectItem.setLabel("Funcionários");
        selectItem.setValue("0");
        SelectItem selectItem2 = new SelectItem();
        selectItem2.setLabel("Cargos de Gestão");
        selectItem2.setValue("1");

        list.add(selectItem);
        list.add(selectItem2);

        ResourceBundle bundle = getResourceBundle("resources/MessagingResources");
        addDefaultSelectedItem(list, bundle);

        return list;
    }

    private void addDefaultSelectedItem(List<SelectItem> list, ResourceBundle bundle) {
        SelectItem firstItem = new SelectItem();
        firstItem.setLabel(hasKey(bundle, "label.find.organization.listing.type.default") ? bundle
                .getString("label.find.organization.listing.type.default") : "label.find.organization.listing.type.default");
        firstItem.setValue("#");
        list.add(0, firstItem);
    }

    public Unit getUnit() throws  FenixServiceException {
        if (parentUnit == null) {
            this.parentUnit = (Unit) rootDomainObject.readPartyByOID(Integer.valueOf((String) getUnitIDHidden().getValue()));
        }
        return parentUnit;
    }

    public void setUnit(Unit unit) {
        this.parentUnit = unit;
    }

    public Integer getPersonID() {
        return personID;
    }

    public void setPersonID(Integer personID) {
        this.personID = personID;
    }

    @Override
    protected String getRequestParameter(String parameterName) {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(parameterName);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public HtmlInputHidden getUnitIDHidden() {
        if (this.unitIDHidden == null) {
            this.unitIDHidden = new HtmlInputHidden();
        }
        return unitIDHidden;
    }

    public void setUnitIDHidden(HtmlInputHidden unitIDHidden) {
        this.unitIDHidden = unitIDHidden;
    }

    public Integer getChoosenExecutionYearID() {
        return choosenExecutionYearID;
    }

    public void setChoosenExecutionYearID(Integer choosenExecutionYearID) {
        this.choosenExecutionYearID = choosenExecutionYearID;
    }

    private boolean hasKey(ResourceBundle bundle, String key) {
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String nextKey = keys.nextElement();
            if (nextKey.equals(key)) {
                return true;
            }
        }
        return false;
    }
}