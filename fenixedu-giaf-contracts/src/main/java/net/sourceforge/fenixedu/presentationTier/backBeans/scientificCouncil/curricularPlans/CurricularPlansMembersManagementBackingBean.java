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
/**
 * 
 */
package org.fenixedu.academic.ui.faces.bean.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.scientificCouncil.UpdateDegreeCurricularPlanMembersGroup;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Employee;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.util.Bundle;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularPlansMembersManagementBackingBean extends FenixBackingBean {
    private String[] selectedPersonsIDsToAdd;
    private String[] selectedPersonsIDsToRemove;

    private String istIdToAdd;

    public void addMembers(ActionEvent event) throws FenixServiceException {
        if (!StringUtils.isEmpty(this.istIdToAdd)) {
            Person person = Person.findByUsername(this.istIdToAdd);

            if (person != null) {
                String[] personToAdd = new String[] { person.getExternalId() };

                UpdateDegreeCurricularPlanMembersGroup.run(getDegreeCurricularPlan(), personToAdd, null);
            }
        }

        selectedPersonsIDsToAdd = null;
        selectedPersonsIDsToRemove = null;
    }

    public void removeMembers(ActionEvent event) throws FenixServiceException {
        if (selectedPersonsIDsToRemove != null) {

            UpdateDegreeCurricularPlanMembersGroup.run(getDegreeCurricularPlan(), null, selectedPersonsIDsToRemove);
        }
        // avoid preset check-boxes after action
        selectedPersonsIDsToAdd = null;
        selectedPersonsIDsToRemove = null;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return FenixFramework.getDomainObject(getSelectedCurricularPlanID());
    }

    private Department getDepartment() {
        if (getSelectedDepartmentID() != null) {
            return FenixFramework.getDomainObject(getSelectedDepartmentID());
        }
        return null;
    }

    public List<SelectItem> getDepartments() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(0, BundleUtil.getString(Bundle.SCIENTIFIC, "choose")));
        for (Department department : rootDomainObject.getDepartmentsSet()) {
            result.add(new SelectItem(department.getExternalId(), department.getRealName()));
        }

        return result;
    }

    public List<SelectItem> getGroupMembers() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        Group curricularPlanMembersGroup = getDegreeCurricularPlan().getCurricularPlanMembersGroup();
        for (User user : curricularPlanMembersGroup.getMembers()) {
            result.add(new SelectItem(user.getPerson().getExternalId(), user.getPerson().getName() + " (" + user.getUsername()
                    + ")"));
        }

        return result;
    }

    public List<String> getGroupMembersLabels() {
        List<String> result = new ArrayList<String>();

        Group curricularPlanMembersGroup = getDegreeCurricularPlan().getCurricularPlanMembersGroup();
        if (curricularPlanMembersGroup != null) {
            for (User user : curricularPlanMembersGroup.getMembers()) {
                result.add(user.getPerson().getName() + " (" + user.getUsername() + ")");
            }
        }

        return result;
    }

    public List<SelectItem> getDepartmentEmployees() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        Department department = getDepartment();
        if (department != null) {
            List<Employee> employees =
                    new ArrayList<Employee>(Employee.getAllCurrentActiveWorkingEmployees(getDepartment().getDepartmentUnit()));
            ComparatorChain chainComparator = new ComparatorChain();
            chainComparator.addComparator(new BeanComparator("person.name"), false);
            chainComparator.addComparator(new BeanComparator("employeeNumber"), false);
            Collections.sort(employees, chainComparator);

            Group curricularPlanMembersGroup = this.getDegreeCurricularPlan().getCurricularPlanMembersGroup();
            for (Employee departmentEmployee : employees) {
                Person person = departmentEmployee.getPerson();
                if (curricularPlanMembersGroup == null || !curricularPlanMembersGroup.isMember(person.getUser())) {
                    result.add(new SelectItem(person.getExternalId(), person.getName() + " (" + person.getUsername() + ")"));
                }
            }
        }

        return result;
    }

    public String[] getSelectedPersonsIDsToRemove() {
        return selectedPersonsIDsToRemove;
    }

    public void setSelectedPersonsIDsToRemove(String[] selectedPersonsIDsToRemove) {
        this.selectedPersonsIDsToRemove = selectedPersonsIDsToRemove;
    }

    public String[] getSelectedPersonsIDsToAdd() {
        return selectedPersonsIDsToAdd;
    }

    public void setSelectedPersonsIDsToAdd(String[] selectedPersonsIDsToAdd) {
        this.selectedPersonsIDsToAdd = selectedPersonsIDsToAdd;
    }

    public String getSelectedCurricularPlanID() {
        if (this.getViewState().getAttribute("selectedCurricularPlanID") != null) {
            return (String) this.getViewState().getAttribute("selectedCurricularPlanID");
        } else if (getAndHoldStringParameter("dcpId") != null) {
            return getAndHoldStringParameter("dcpId");
        } else {
            return getAndHoldStringParameter("degreeCurricularPlanID");
        }
    }

    public void setSelectedCurricularPlanID(String selectedCurricularPlanID) {
        this.getViewState().setAttribute("selectedCurricularPlanID", selectedCurricularPlanID);
    }

    public String getSelectedDepartmentID() {
        return (String) this.getViewState().getAttribute("selectedDepartmentID");
    }

    public void setSelectedDepartmentID(String selectedDepartmentID) {
        this.getViewState().setAttribute("selectedDepartmentID", selectedDepartmentID);
    }

    public String getIstIdToAdd() {
        return istIdToAdd;
    }

    public void setIstIdToAdd(String istIdToAdd) {
        this.istIdToAdd = istIdToAdd;
    }

}