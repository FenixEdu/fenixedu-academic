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
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.UserGroup;
import net.sourceforge.fenixedu.domain.department.CompetenceCourseMembersGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class ManagementGroupsBackingBean extends FenixBackingBean {

    private List<Employee> employees = getEmployees();

    private Integer[] selectedPersonGroupsIDsToRemove;

    private Integer[] selectedPersonsIDsToAdd;

    public Department getDepartment() {
        return (getUserView().getPerson().getEmployee() != null) ? getUserView().getPerson()
                .getEmployee().getCurrentDepartmentWorkingPlace() : null;
    }

    public List<Employee> getEmployees() {
        List<Employee> result = (getDepartment() != null) ? new ArrayList<Employee>(getDepartment().getCurrentActiveWorkingEmployees()) : null;
        
        if (result != null) {
            ComparatorChain chainComparator = new ComparatorChain();
            chainComparator.addComparator(new BeanComparator("person.nome"), false);
            chainComparator.addComparator(new BeanComparator("employeeNumber"), false);
            Collections.sort(result, chainComparator);
        }
        
        return result;
    }

    public List getDepartmentEmployeesSelectItems() {
        List<SelectItem> result = new ArrayList<SelectItem>(employees.size());
        for (Employee departmentEmployee : employees) {
            Person person = departmentEmployee.getPerson();
            result.add(new SelectItem(person.getIdInternal(), person.getNome() + " ("
                    + person.getUsername() + ")"));
        }
        return result;
    }

    public int getDepartmentEmployeesSize() {
        return employees.size();
    }

    public List<SelectItem> getSelectedDepartmentEmployeesSelectItems() throws FenixFilterException,
            FenixServiceException {

        List<SelectItem> result = new ArrayList<SelectItem>();

        CompetenceCourseMembersGroup competenceCourseMembersGroup = getDepartment()
                .getCompetenceCourseMembersGroup();
        if (competenceCourseMembersGroup != null) {
            for (UserGroup member : competenceCourseMembersGroup.getParts()) {
                Person person = ((PersonGroup) member).getPerson();
                result.add(new SelectItem(member.getIdInternal(), person.getNome() + " ("
                        + person.getUsername() + ")"));
            }
        }

        return result;
    }

    public Integer[] getSelectedPersonsIDs() {
        return null;
    }

    public void setSelectedPersonsIDs(Integer[] selectedPersonsIDs) {
        this.selectedPersonsIDsToAdd = selectedPersonsIDs;
    }

    public Integer[] getSelectedPersonGroupsIDsToRemove() {
        return null;
    }

    public void setSelectedPersonGroupsIDsToRemove(Integer[] selectedPersonGroupsIDsToRemove) {
        this.selectedPersonGroupsIDsToRemove = selectedPersonGroupsIDsToRemove;
    }

    public void addMembers(ActionEvent event) throws FenixFilterException, FenixServiceException {
        
        CompetenceCourseMembersGroup competenceCourseMembersGroup = getDepartment()
                .getCompetenceCourseMembersGroup();
        if (competenceCourseMembersGroup == null) {
            Object[] args = { getDepartment() };
            competenceCourseMembersGroup = (CompetenceCourseMembersGroup) ServiceUtils.executeService(
                    getUserView(), "CreateCompetenceCourseMembersGroup", args);
        }

        if (selectedPersonsIDsToAdd != null) {
            Object[] args = { competenceCourseMembersGroup, selectedPersonsIDsToAdd, null,
                    RoleType.BOLONHA_MANAGER };
            ServiceUtils.executeService(getUserView(), "UpdateCompetenceCourseMembersGroup", args);
        }
    }

    public void removeMembers(ActionEvent event) throws FenixFilterException, FenixServiceException {

        if (selectedPersonGroupsIDsToRemove != null) {
            Object[] args = { getDepartment().getCompetenceCourseMembersGroup(), null,
                    selectedPersonGroupsIDsToRemove, RoleType.BOLONHA_MANAGER };
            ServiceUtils.executeService(getUserView(), "UpdateCompetenceCourseMembersGroup", args);
        }
    }

}
