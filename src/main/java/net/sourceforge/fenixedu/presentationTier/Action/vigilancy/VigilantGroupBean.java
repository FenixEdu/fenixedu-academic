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
package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;

public class VigilantGroupBean extends VigilantBean implements Serializable {

    private Person person;

    private Unit unit;

    private Unit selectedUnit;

    private Department selectedDepartment;

    private VigilantWrapper selectedVigilantWrapper;

    private List<Employee> employees = new ArrayList<Employee>();

    private String name;

    private String ConvokeStrategy;

    private ExamCoordinator examCoordinator;

    private List<ExamCoordinator> examCoordinators = new ArrayList<ExamCoordinator>();

    private DateTime beginFirstUnavailablePeriod;

    private DateTime endFirstUnavailablePeriod;

    private DateTime beginSecondUnavailablePeriod;

    private DateTime endSecondUnavailablePeriod;

    private boolean showCourseColumn = Boolean.TRUE;

    private boolean showVigilantColumn = Boolean.TRUE;

    private boolean showCoordinators = Boolean.TRUE;

    private List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();

    private String username = "";

    private String contactEmail = "";

    private String emailPrefix = "";

    private String rulesLink = "";

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getRulesLink() {
        return rulesLink;
    }

    public void setRulesLink(String rulesLink) {
        this.rulesLink = rulesLink;
    }

    public boolean isShowCoordinators() {
        return showCoordinators;
    }

    public void setShowCoordinators(boolean showCoordinators) {
        this.showCoordinators = showCoordinators;
    }

    public boolean isShowCourseColumn() {
        return showCourseColumn;
    }

    public void setShowCourseColumn(boolean showCompetenceCourseColumn) {
        this.showCourseColumn = showCompetenceCourseColumn;
    }

    public boolean isShowVigilantColumn() {
        return showVigilantColumn;
    }

    public void setShowVigilantColumn(boolean showVigilantColumn) {
        this.showVigilantColumn = showVigilantColumn;
    }

    public VigilantGroupBean() {
        setPerson(null);
        setUnit(null);
        setSelectedVigilantGroup(null);
        setBeginFirstUnavailablePeriod(null);
        setEndFirstUnavailablePeriod(null);
        setBeginSecondUnavailablePeriod(null);
        setEndSecondUnavailablePeriod(null);
        setExecutionYear(null);
        setExamCoordinator(null);
        setSelectedDepartment(null);
        setSelectedUnit(null);
        setSelectedVigilantWrapper(null);
    }

    public DateTime getBeginFirstUnavailablePeriod() {
        return beginFirstUnavailablePeriod;
    }

    public void setBeginFirstUnavailablePeriod(DateTime beginFirstUnavailablePeriod) {
        this.beginFirstUnavailablePeriod = beginFirstUnavailablePeriod;
    }

    public DateTime getBeginSecondUnavailablePeriod() {
        return beginSecondUnavailablePeriod;
    }

    public void setBeginSecondUnavailablePeriod(DateTime beginSecondUnavailablePeriod) {
        this.beginSecondUnavailablePeriod = beginSecondUnavailablePeriod;
    }

    public DateTime getEndFirstUnavailablePeriod() {
        return endFirstUnavailablePeriod;
    }

    public void setEndFirstUnavailablePeriod(DateTime endFirstUnavailablePeriod) {
        this.endFirstUnavailablePeriod = endFirstUnavailablePeriod;
    }

    public DateTime getEndSecondUnavailablePeriod() {
        return endSecondUnavailablePeriod;
    }

    public void setEndSecondUnavailablePeriod(DateTime endSecondUnavailablePeriod) {
        this.endSecondUnavailablePeriod = endSecondUnavailablePeriod;
    }

    public String getConvokeStrategy() {
        return ConvokeStrategy;
    }

    public void setConvokeStrategy(String convokeStrategy) {
        ConvokeStrategy = convokeStrategy;
    }

    public VigilantWrapper getSelectedVigilantWrapper() {
        return this.selectedVigilantWrapper;
    }

    public void setSelectedVigilantWrapper(VigilantWrapper vigilantWrapper) {
        this.selectedVigilantWrapper = vigilantWrapper;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setExamCoordinator(ExamCoordinator examCoordinator) {
        this.examCoordinator = examCoordinator;
    }

    public ExamCoordinator getExamCoordinator() {
        return this.examCoordinator;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public List getEmployees() {
        List employees = new ArrayList<Employee>();
        for (Employee employee : this.employees) {
            if (employee != null) {
                employees.add(employee);
            }
        }
        return employees;
    }

    public void setEmployees(List<Employee> employeesList) {
        this.employees = new ArrayList<Employee>();
        for (Employee employee : employeesList) {
            if (employee != null) {
                this.employees.add(employee);
            }
        }
    }

    public List getExamCoordinators() {
        List coordinators = new ArrayList<ExamCoordinator>();
        for (ExamCoordinator coordinator : this.examCoordinators) {
            if (coordinator != null) {
                coordinators.add(coordinator);
            }
        }
        return coordinators;
    }

    public void setExamCoordinators(List<ExamCoordinator> coordinatorsList) {
        this.examCoordinators = new ArrayList<ExamCoordinator>();
        for (ExamCoordinator coordinator : coordinatorsList) {
            if (coordinator != null) {
                this.examCoordinators.add(coordinator);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VigilantWrapper> getVigilants() {
        List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilant : this.vigilants) {
            if (vigilant != null) {
                vigilants.add(vigilant);
            }
        }
        return vigilants;
    }

    public void setVigilants(List<VigilantWrapper> vigilants) {
        this.vigilants = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilant : vigilants) {
            if (vigilant != null) {
                this.vigilants.add(vigilant);
            }
        }
    }

    public List<VigilantWrapper> getVigilantsWithIncompatiblePerson() {
        List<VigilantWrapper> vigilants = this.getVigilants();
        List<VigilantWrapper> vigilantsWithIncompatiblePerson = new ArrayList<VigilantWrapper>();

        for (VigilantWrapper vigilant : vigilants) {
            if (vigilant.getPerson().hasIncompatibleVigilant()) {
                vigilantsWithIncompatiblePerson.add(vigilant);
            }
        }

        return vigilantsWithIncompatiblePerson;
    }

    public void resetEmployees() {
        this.employees = new ArrayList<Employee>();
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(Unit unit) {
        this.selectedUnit = unit;
    }

    public Department getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(Department department) {
        this.selectedDepartment = department;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<VigilantWrapper> getVigilantWrappersForGroupsInBean() {
        Set<VigilantWrapper> vigilantWrappers = new HashSet<VigilantWrapper>();
        Collection<VigilantGroup> groups = this.getVigilantGroups();
        for (VigilantGroup group : groups) {
            vigilantWrappers.addAll(group.getVigilantWrappers());
        }
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(VigilantWrapper.POINTS_COMPARATOR);
        chain.addComparator(VigilantWrapper.CATEGORY_COMPARATOR);
        chain.addComparator(VigilantWrapper.USERNAME_COMPARATOR);
        List<VigilantWrapper> vigilantWrappersList = new ArrayList<VigilantWrapper>(vigilantWrappers);
        Collections.sort(vigilantWrappersList, chain);
        return vigilantWrappersList;
    }

    public String getEmailPrefix() {
        return emailPrefix;
    }

    public void setEmailPrefix(String emailPrefix) {
        this.emailPrefix = emailPrefix;
    }
}
