package net.sourceforge.fenixedu.presentationTier.backBeans.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice.UpdateDepartmentsCompetenceCourseManagementGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

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
                (getDepartment() != null) ? new ArrayList<Employee>(getDepartment().getAllCurrentActiveWorkingEmployees()) : null;

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
            if (competenceCoursesManagementGroup == null || !competenceCoursesManagementGroup.isMember(person)) {
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
            for (Person person : competenceCoursesManagementGroup.getElements()) {
                result.add(new SelectItem(person.getExternalId(), person.getName() + " (" + person.getUsername() + ")"));
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