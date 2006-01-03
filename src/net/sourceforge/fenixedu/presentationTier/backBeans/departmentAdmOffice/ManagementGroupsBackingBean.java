package net.sourceforge.fenixedu.presentationTier.backBeans.departmentAdmOffice;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.accessControl.IPersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.IUserGroup;
import net.sourceforge.fenixedu.domain.department.ICompetenceCourseMembersGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class ManagementGroupsBackingBean extends FenixBackingBean {

    private List<IEmployee> employees = getDepartment().getCurrentActiveWorkingEmployees();

    private Integer[] selectedPersonGroupsIDsToRemove;

    private Integer[] selectedPersonsIDsToAdd;

    public IDepartment getDepartment() {
        return getUserView().getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
    }

    public List getDepartmentEmployeesSelectItems() {
        List<SelectItem> result = new ArrayList<SelectItem>(employees.size());
        for (IEmployee departmentEmployee : employees) {
            IPerson person = departmentEmployee.getPerson();
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

        ICompetenceCourseMembersGroup competenceCourseMembersGroup = getDepartment()
                .getCompetenceCourseMembersGroup();
        if (competenceCourseMembersGroup != null) {
            for (IUserGroup member : competenceCourseMembersGroup.getParts()) {
                IPerson person = ((IPersonGroup) member).getPerson();
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
        
        ICompetenceCourseMembersGroup competenceCourseMembersGroup = getDepartment()
                .getCompetenceCourseMembersGroup();
        if (competenceCourseMembersGroup == null) {
            Object[] args = { getDepartment() };
            competenceCourseMembersGroup = (ICompetenceCourseMembersGroup) ServiceUtils.executeService(
                    getUserView(), "CreateCompetenceCourseMembersGroup", args);
        }

        if (selectedPersonsIDsToAdd != null) {
            Object[] args = { competenceCourseMembersGroup, selectedPersonsIDsToAdd, null, RoleType.BOLONHA_MANAGER };
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
