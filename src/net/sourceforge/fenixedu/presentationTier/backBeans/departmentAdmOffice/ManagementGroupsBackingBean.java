package net.sourceforge.fenixedu.presentationTier.backBeans.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class ManagementGroupsBackingBean extends FenixBackingBean {

    private List<Employee> employees = getEmployees();

    private Integer[] selectedPersonsIDsToAdd;
    private Integer[] selectedPersonsIDsToRemove;

    public Department getDepartment() {
        return (getUserView().getPerson().getEmployee() != null) ? getUserView().getPerson()
                .getEmployee().getCurrentDepartmentWorkingPlace() : null;
    }

    public List<Employee> getEmployees() {
        List<Employee> result = (getDepartment() != null) ? new ArrayList<Employee>(getDepartment().getAllCurrentActiveWorkingEmployees()) : null;
        
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
                result.add(new SelectItem(person.getIdInternal(), person.getNome() + " (" + person.getUsername() + ")"));    
            }
        }
        return result;
        
    }

    public int getDepartmentEmployeesSize() {
        return employees.size();
    }

    public List<SelectItem> getSelectedDepartmentEmployeesSelectItems() throws FenixFilterException,
            FenixServiceException {

        List<SelectItem> result = new ArrayList<SelectItem>();

        Group competenceCoursesManagementGroup = getDepartment().getCompetenceCourseMembersGroup();
        if (competenceCoursesManagementGroup != null) {            
            for(Person person: competenceCoursesManagementGroup.getElements()) {
                result.add(new SelectItem(person.getIdInternal(), person.getNome() + " (" + person.getUsername() + ")"));
            }
        }

        return result;
    }

    public void setSelectedPersonsIDsToAdd(Integer[] selectedPersonsIDs) {
        this.selectedPersonsIDsToAdd = selectedPersonsIDs;
    }
    
    public Integer[] getSelectedPersonsIDsToAdd() {
        return selectedPersonsIDsToAdd;
    }

    public void setSelectedPersonsIDsToRemove(Integer[] selectedPersonsIDsToRemove) {
        this.selectedPersonsIDsToRemove = selectedPersonsIDsToRemove;
    }
    
    public Integer[] getSelectedPersonsIDsToRemove() {
        return selectedPersonsIDsToRemove;
    }

    public void addMembers(ActionEvent event) throws FenixFilterException, FenixServiceException {
        if (selectedPersonsIDsToAdd != null) {
            Object[] args = { getDepartment(), selectedPersonsIDsToAdd, null };
            ServiceUtils.executeService(getUserView(), "UpdateDepartmentsCompetenceCourseManagementGroup", args);
        }
        // avoid preset check-boxes after action
        selectedPersonsIDsToAdd = null;
        selectedPersonsIDsToRemove = null;
    }

    public void removeMembers(ActionEvent event) throws FenixFilterException, FenixServiceException {
        if (selectedPersonsIDsToRemove != null) {
            Object[] args = { getDepartment(), null, selectedPersonsIDsToRemove };
            ServiceUtils.executeService(getUserView(), "UpdateDepartmentsCompetenceCourseManagementGroup", args);
        }
        // avoid preset check-boxes after action
        selectedPersonsIDsToAdd = null;
        selectedPersonsIDsToRemove = null;
    }

}
