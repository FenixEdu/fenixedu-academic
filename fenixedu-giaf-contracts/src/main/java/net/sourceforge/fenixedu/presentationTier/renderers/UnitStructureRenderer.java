/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.renderers;

import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.Accountability;
import org.fenixedu.academic.domain.organizationalStructure.Contract;
import org.fenixedu.academic.domain.organizationalStructure.EmployeeContract;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.Face;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlList;
import pt.ist.fenixWebFramework.renderers.components.HtmlListItem;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

import com.google.common.base.Strings;

public class UnitStructureRenderer extends OutputRenderer {

    private String rootUnitClasses;

    private String unitClasses;

    private String levelClasses;

    private String employeesSectionClasses;

    private String employeesClasses;

    private String functionsClasses;

    private String implicitFunctionsClasses;

    private String membersClasses;

    private String homepageLocation;

    private boolean showEmployeesSection;

    private boolean showOnlyPeopleWithFunctions;

    private boolean showEmptyEmployeesSection;

    private boolean showEmptyFunctions;

    private boolean showImplicityFunctionLabel;

    private boolean showDates;

    private String bundle;

    private String employeesSectionKey;

    private String implicityFunctionKey;

    private String dateFromKey;

    private String dateToKey;

    private String dateSinceKey;

    public boolean isShowOnlyPeopleWithFunctions() {
        return showOnlyPeopleWithFunctions;
    }

    public void setShowOnlyPeopleWithFunctions(boolean showPeopleWithFunctions) {
        this.showOnlyPeopleWithFunctions = showPeopleWithFunctions;
    }

    public boolean isShowEmployeesSection() {
        return showEmployeesSection;
    }

    public String getBundle() {
        return bundle;
    }

    /**
     * Chooses the bundle for all messages. If no bundle is specified the
     * default bundle for the current module is used.
     * 
     * @property
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getDateFromKey() {
        return dateFromKey;
    }

    /**
     * The resource key to be used when displaying a date in the form: <em>key</em> &lt;begin&gt; to &lt;end&gt;.
     * 
     * @property
     */
    public void setDateFromKey(String dateFromKey) {
        this.dateFromKey = dateFromKey;
    }

    public String getDateSinceKey() {
        return dateSinceKey;
    }

    /**
     * The resource key to be used when displaying a date in the form: <em>key</em> &lt;begin&gt;.
     * 
     * @property
     */
    public void setDateSinceKey(String dateSinceKey) {
        this.dateSinceKey = dateSinceKey;
    }

    public String getDateToKey() {
        return dateToKey;
    }

    /**
     * The resource key to be used when displaying a date in the form: From
     * &lt;begin&gt; <em>key</em> &lt;end&gt;.
     * 
     * @property
     */
    public void setDateToKey(String dateToKey) {
        this.dateToKey = dateToKey;
    }

    public String getEmployeesClasses() {
        return employeesClasses;
    }

    /**
     * Sets the classes to be used when displaying each employee item.
     * 
     * @property
     */
    public void setEmployeesClasses(String employeesClasses) {
        this.employeesClasses = employeesClasses;
    }

    public String getEmployeesSectionClasses() {
        return employeesSectionClasses;
    }

    /**
     * Sets the classes to be used in the employees section item.
     * 
     * @property
     */
    public void setEmployeesSectionClasses(String employeesSectionClasses) {
        this.employeesSectionClasses = employeesSectionClasses;
    }

    public String getEmployeesSectionKey() {
        return employeesSectionKey;
    }

    /**
     * Sets the key to be used has the label of the employees section.
     * 
     * @property
     */
    public void setEmployeesSectionKey(String employeesSectionKey) {
        this.employeesSectionKey = employeesSectionKey;
    }

    public String getFunctionsClasses() {
        return functionsClasses;
    }

    /**
     * Sets the classes to use in each function item.
     * 
     * @property
     */
    public void setFunctionsClasses(String functionsClasses) {
        this.functionsClasses = functionsClasses;
    }

    public String getHomepageLocation() {
        return homepageLocation;
    }

    /**
     * Indicates the url for each person's homepage. The url can have the form <code>/path/%s/subpath</code> where <code>%s</code>
     * will be replaced by
     * the person's system's username.
     * 
     * @property
     */
    public void setHomepageLocation(String homepageLocation) {
        this.homepageLocation = homepageLocation;
    }

    public String getImplicitFunctionsClasses() {
        return implicitFunctionsClasses;
    }

    /**
     * Sets the classe sto use when displaying an implicit function item. If
     * this property is not set then the normal function classes will be used.
     * 
     * @param implicitFunctionsClasses
     */
    public void setImplicitFunctionsClasses(String implicitFunctionsClasses) {
        this.implicitFunctionsClasses = implicitFunctionsClasses;
    }

    public String getImplicityFunctionKey() {
        return implicityFunctionKey;
    }

    /**
     * Sets the key to be used has the label used to mark implicity functions.
     * 
     * @property
     */
    public void setImplicityFunctionKey(String implicityFunctionKey) {
        this.implicityFunctionKey = implicityFunctionKey;
    }

    public String getLevelClasses() {
        return levelClasses;
    }

    /**
     * Sets the classes to use to mark each successive list. To the value given
     * here, the renderer will append the depth of the inner list.
     * 
     * @property
     */
    public void setLevelClasses(String levelClasses) {
        this.levelClasses = levelClasses;
    }

    public String getMembersClasses() {
        return membersClasses;
    }

    /**
     * Sets the classes to use when displaying each person's item.
     * 
     * @property
     */
    public void setMembersClasses(String membersClasses) {
        this.membersClasses = membersClasses;
    }

    public String getRootUnitClasses() {
        return rootUnitClasses;
    }

    /**
     * Chooses the classes to apply to he root unit.
     * 
     * @property
     */
    public void setRootUnitClasses(String rootUnitClasses) {
        this.rootUnitClasses = rootUnitClasses;
    }

    public boolean isShowEmptyEmployeesSection() {
        return showEmptyEmployeesSection;
    }

    /**
     * Chooses if the employees sections is shown when there are not direct
     * employees.
     * 
     * @property
     */
    public void setShowEmptyEmployeesSection(boolean showEmptyEmployeesSection) {
        this.showEmptyEmployeesSection = showEmptyEmployeesSection;
    }

    public boolean isShowEmptyFunctions() {
        return showEmptyFunctions;
    }

    /**
     * Chooses if each function is shown when there aren't persons under that
     * function.
     * 
     * @property
     */
    public void setShowEmptyFunctions(boolean showEmptyFunctions) {
        this.showEmptyFunctions = showEmptyFunctions;
    }

    public String getUnitClasses() {
        return unitClasses;
    }

    /**
     * Sets the classes to be used when generating each unit's item.
     * 
     * @property
     */
    public void setUnitClasses(String unitClasses) {
        this.unitClasses = unitClasses;
    }

    /**
     * Indicates if all employees should be grouped under an employees section.
     * 
     * @property
     */
    public void setShowEmployeesSection(boolean showEmployeesSection) {
        this.showEmployeesSection = showEmployeesSection;
    }

    public boolean isShowImplicityFunctionLabel() {
        return showImplicityFunctionLabel;
    }

    /**
     * Indicates if each implicity function should be added a label.
     * 
     * @property
     */
    public void setShowImplicityFunctionLabel(boolean showImplicityFunctionLabel) {
        this.showImplicityFunctionLabel = showImplicityFunctionLabel;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new OrganigramLayout();
    }

    protected class OrganigramLayout extends Layout {

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            Unit unit = (Unit) object;

            return createStructure(unit, new HtmlList(), 1);
        }

    }

    protected HtmlComponent createStructure(Unit unit, HtmlList root, int level) {
        HtmlListItem item = createItem(root, getUnitClasses(level), generateUnitName(unit, getUnitFace(level)));

        HtmlList elements = createList(level, null);

        if (!isShowOnlyPeopleWithFunctions()) {
            addEmployees(unit, elements, level);
        }
        addFunctions(unit, elements, level);
        addSubUnits(unit, elements, level);

        if (!elements.getChildren().isEmpty()) {
            item.addChild(elements);
        }

        return root;
    }

    protected String getUnitClasses(int level) {
        return level == 1 ? getRootUnitClasses() : getUnitClasses();
    }

    protected Face getUnitFace(int level) {
        return level == 1 ? Face.H3 : Face.STRONG;
    }

    protected HtmlList createList(int level, HtmlListItem item) {
        HtmlList list = new HtmlList();

        if (getLevelClasses() != null) {
            list.setClasses(getLevelClasses() + level);
        }

        if (item != null) {
            item.addChild(list);
        }

        return list;
    }

    protected HtmlComponent generateUnitName(Unit unit, Face face) {
        HtmlText header = new HtmlText(unit.getNameI18n().getContent());
        header.setFace(face);
        return header;
    }

    protected void addEmployees(Unit unit, HtmlList root, int level) {
        SortedSet<Person> persons = getPersonsWithContract(unit, new TreeSet<Person>(Person.COMPARATOR_BY_NAME_AND_ID));

        HtmlList destiny = root;

        if ((isShowEmployeesSection() && !persons.isEmpty()) || isShowEmptyEmployeesSection()) {
            HtmlListItem employeesItem = createItem(root, getEmployeesSectionClasses(), getEmployeesSectionKey(), Face.STRONG);

            if (!persons.isEmpty()) {
                destiny = createList(level + 1, employeesItem);
            }
        }

        for (Person person : persons) {
            addEmployee(person, destiny);
        }
    }

    private SortedSet<Person> getPersonsWithContract(Unit unit, SortedSet<Person> persons) {
        for (Contract contract : EmployeeContract.getWorkingContracts(unit)) {
            if (!isAccountabilityVisible(contract)) {
                continue;
            }

            persons.add(contract.getPerson());
        }

        return persons;
    }

    protected HtmlListItem createItem(HtmlList root, String classes, String key, Face face) {
        return createItem(root, classes, key, true, face);
    }

    protected HtmlListItem createItem(HtmlList root, String classes, String key, boolean resource, Face face) {
        return createItem(root, classes, createLabel(key, resource, face));
    }

    protected HtmlListItem createItem(HtmlList root, String classes, HtmlComponent component) {
        HtmlListItem item = root.createItem();
        item.setClasses(classes);
        if (component != null) {
            item.addChild(component);
        }
        return item;
    }

    protected HtmlComponent createLabel(String key, boolean resource, Face face) {
        HtmlText text = new HtmlText(resource ? getString(key) : key);
        text.setFace(face);
        return text;
    }

    protected String getString(String key) {
        return RenderUtils.getResourceString(getBundle(), key);
    }

    protected boolean isAccountabilityVisible(Accountability accountability) {
        return accountability.isActive(new YearMonthDay());
    }

    private void addEmployee(Person person, HtmlList root) {
        createItem(root, getEmployeesClasses(), generatePerson(person));
    }

    protected HtmlComponent generatePerson(Person person) {
        HtmlComponent name = generatePersonName(person);

        if (!Strings.isNullOrEmpty(person.getHomepageWebAddress())) {
            HtmlLink link = new HtmlLinkWithPreprendedComment(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
            link.setUrl(person.getHomepageWebAddress());
            link.setModuleRelative(false);
            link.setIndented(false);
            link.setBody(name);
            name = link;
        }

        return name;
    }

    private HtmlComponent generatePersonName(Person person) {
        RenderKit instance = RenderKit.getInstance();
        return instance.renderUsing(new PersonNameRenderer(), getContext(), person.getName(), String.class);
    }

    protected void addFunctions(Unit unit, HtmlList root, int level) {
        for (Function function : unit.getOrderedFunctions()) {
            SortedSet<PersonFunction> pfs =
                    getPersonFunctions(function, new TreeSet<PersonFunction>(PersonFunction.COMPARATOR_BY_PERSON_NAME));

            if (!pfs.isEmpty() || isShowEmptyFunctions()) {
                HtmlListItem item =
                        createItem(root, getFunctionClasses(function), function.getTypeName().getContent(), false, Face.STRONG);

                if (function.isInherentFunction() && isShowImplicityFunctionLabel()) {
                    addImplicitMark(item);
                }

                if (!pfs.isEmpty()) {
                    HtmlList persons = createList(level + 1, item);

                    for (PersonFunction pf : pfs) {
                        addPersonFunction(pf, persons);
                    }
                }
            }

        }
    }

    private String getFunctionClasses(Function function) {
        if (function.isInherentFunction() && getImplicitFunctionsClasses() != null) {
            return getImplicitFunctionsClasses();
        } else {
            return getFunctionsClasses();
        }
    }

    protected void addImplicitMark(HtmlListItem item) {
        HtmlText mark = new HtmlText("(" + getString(getImplicityFunctionKey()) + ")");
        mark.setFace(Face.STANDARD);
        item.addChild(mark);
    }

    protected SortedSet<PersonFunction> getPersonFunctions(Function function, TreeSet<PersonFunction> collection) {
        for (PersonFunction pf : PersonFunction.getPersonFunctions(function)) {
            if (!isAccountabilityVisible(pf)) {
                continue;
            }

            collection.add(pf);
        }

        return collection;
    }

    private void addPersonFunction(PersonFunction pf, HtmlList root) {
        HtmlComponent name = generatePerson(pf.getPerson());
        HtmlComponent range = generateDateRange(pf.getBeginDate(), pf.getEndDate());

        HtmlListItem item = root.createItem();
        item.setClasses(getMembersClasses());

        item.addChild(name);
        if (isShowDates()) {
            item.addChild(range);
        }
    }

    protected HtmlComponent generateDateRange(YearMonthDay beginDate, YearMonthDay endDate) {
        HtmlText text = new HtmlText();

        if (endDate == null) {
            text.setText(String.format("(%s %s)", getString(getDateSinceKey()), beginDate));
        } else {
            text.setText(String.format("(%s %s %s %s)", getString(getDateFromKey()), beginDate, getString(getDateToKey()),
                    endDate));
        }

        text.setFace(Face.STANDARD);
        return text;
    }

    protected void addSubUnits(Unit unit, HtmlList root, int level) {
        SortedSet<Unit> units = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
        units.addAll(unit.getActiveSubUnits(new YearMonthDay()));

        for (Unit sub : units) {
            if (hasPersonFunctions(sub)) {
                createStructure(sub, root, level + 1);
            }
        }
    }

    public boolean isShowDates() {
        return showDates;
    }

    public void setShowDates(boolean showDates) {
        this.showDates = showDates;
    }

    private boolean hasPersonFunctions(Unit unit) {

        for (Function function : unit.getOrderedActiveFunctions()) {
            if (!getPersonFunctions(function, new TreeSet<PersonFunction>(PersonFunction.COMPARATOR_BY_PERSON_NAME)).isEmpty()) {
                return true;
            }
        }
        return false;

    }
}
