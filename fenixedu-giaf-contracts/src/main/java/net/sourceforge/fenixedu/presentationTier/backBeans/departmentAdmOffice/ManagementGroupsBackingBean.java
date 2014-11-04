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
package org.fenixedu.academic.ui.faces.bean.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.fenixedu.academic.service.services.departmentAdmOffice.UpdateDepartmentsCompetenceCourseManagementGroup;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Employee;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

public class ManagementGroupsBackingBean extends FenixBackingBean {

    private final List<Employee> employees = getEmployees();

    private String[] selectedPersonsIDsToAdd;
    private String[] selectedPersonsIDsToRemove;

    public Department getDepartment() {
        return (getUserView().getPerson().getEmployee() != null) ? getUserView().getPerson().getEmployee()
                .getCurrentDepartmentWorkingPlace() : null;
    }

    public List<Employee> getEmployees() {
        List<Employee> result =
                (getDepartment() != null) ? new ArrayList<Employee>(Employee.getAllCurrentActiveWorkingEmployees(getDepartment()
                        .getDepartmentUnit())) : null;

        if (result != null) {
            ComparatorChain chainComparator = new ComparatorChain();
            chainComparator.addComparator(new BeanComparator("person.name"), false);
            chainComparator.addComparator(new BeanComparator("employeeNumber"), false);
            Collections.sort(result, chainComparator);
        }

        return result;
    }

    public List getDepartmentEmployeesSelectItems() {
        Group competenceCoursesManagementGroup = getDepartment().getCompetenceCourseMembersGroup();

        List<SelectItem> result = new ArrayList<SelectItem>(employees.size());
        for (Employee departmentEmployee : employees) {
            Person person = departmentEmployee.getPerson();
            if (competenceCoursesManagementGroup == null || !competenceCoursesManagementGroup.isMember(person.getUser())) {
                result.add(new SelectItem(person.getExternalId(), person.getName() + " (" + person.getUsername() + ")"));
            }
        }
        return result;

    }

    public int getDepartmentEmployeesSize() {
        return employees.size();
    }

    public List<SelectItem> getSelectedDepartmentEmployeesSelectItems() throws FenixServiceException {

        List<SelectItem> result = new ArrayList<SelectItem>();

        Group competenceCoursesManagementGroup = getDepartment().getCompetenceCourseMembersGroup();
        if (competenceCoursesManagementGroup != null) {
            for (User user : competenceCoursesManagementGroup.getMembers()) {
                result.add(new SelectItem(user.getPerson().getExternalId(), user.getPerson().getName() + " ("
                        + user.getUsername() + ")"));
            }
        }

        return result;
    }

    public void setSelectedPersonsIDsToAdd(String[] selectedPersonsIDs) {
        this.selectedPersonsIDsToAdd = selectedPersonsIDs;
    }

    public String[] getSelectedPersonsIDsToAdd() {
        return selectedPersonsIDsToAdd;
    }

    public void setSelectedPersonsIDsToRemove(String[] selectedPersonsIDsToRemove) {
        this.selectedPersonsIDsToRemove = selectedPersonsIDsToRemove;
    }

    public String[] getSelectedPersonsIDsToRemove() {
        return selectedPersonsIDsToRemove;
    }

    public void addMembers(ActionEvent event) throws FenixServiceException {
        if (selectedPersonsIDsToAdd != null) {

            UpdateDepartmentsCompetenceCourseManagementGroup.run(getDepartment(), selectedPersonsIDsToAdd, null);
        }
        // avoid preset check-boxes after action
        selectedPersonsIDsToAdd = null;
        selectedPersonsIDsToRemove = null;
    }

    public void removeMembers(ActionEvent event) throws FenixServiceException {
        if (selectedPersonsIDsToRemove != null) {

            UpdateDepartmentsCompetenceCourseManagementGroup.run(getDepartment(), null, selectedPersonsIDsToRemove);
        }
        // avoid preset check-boxes after action
        selectedPersonsIDsToAdd = null;
        selectedPersonsIDsToRemove = null;
    }

}