/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlanMembersGroup;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.accessControl.IPersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.IUserGroup;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularPlansMembersManagementBackingBean extends FenixBackingBean {

    private Integer[] selectedPersonGroupsIDsToRemove;

    private Integer[] selectedPersonsIDsToAdd;

    public Collection<SelectItem> getDegreeCurricularPlans() throws FenixFilterException,
            FenixServiceException {

        List<SelectItem> result = new ArrayList<SelectItem>();
        List<InfoDegreeCurricularPlan> degreeCurricularPlans = (List<InfoDegreeCurricularPlan>) ServiceUtils
                .executeService(getUserView(), "ReadDegreeCurricularPlans", null);

        for (InfoDegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            result.add(new SelectItem(degreeCurricularPlan.getIdInternal(), degreeCurricularPlan
                    .getName()));
        }

        return result;

    }

    public void addMembers(ActionEvent event) throws FenixFilterException, FenixServiceException {

        IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        IDegreeCurricularPlanMembersGroup curricularPlanMembersGroup = degreeCurricularPlan
                .getCurricularPlanMembersGroup();

        if (curricularPlanMembersGroup == null) {
            Object[] argsCreateGroup = { degreeCurricularPlan };
            curricularPlanMembersGroup = (IDegreeCurricularPlanMembersGroup) ServiceUtils
                    .executeService(getUserView(), "CreateCurricularPlanMembersGroup", argsCreateGroup);
        }

        if (selectedPersonsIDsToAdd != null) {
            Object[] argsUpdateGroup = { curricularPlanMembersGroup, selectedPersonsIDsToAdd, null };
            ServiceUtils.executeService(getUserView(), "UpdateCurricularPlanMembersGroup",
                    argsUpdateGroup);
        }
    }

    public void removeMembers(ActionEvent event) throws FenixFilterException, FenixServiceException {

        if (selectedPersonGroupsIDsToRemove != null) {
            Object[] args = { getDegreeCurricularPlan().getCurricularPlanMembersGroup(), null,
                    selectedPersonGroupsIDsToRemove };
            ServiceUtils.executeService(getUserView(), "UpdateCurricularPlanMembersGroup", args);
        }
    }

    public IDegreeCurricularPlan getDegreeCurricularPlan() throws FenixServiceException,
            FenixFilterException {
        Object[] args = { DegreeCurricularPlan.class, getSelectedCurricularPlanID() };
        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) ServiceUtils
                .executeService(getUserView(), "ReadDomainObject", args);
        return degreeCurricularPlan;
    }

    private IDepartment getDepartment() throws FenixServiceException, FenixFilterException {

        if (getSelectedDepartmentID() != null) {
            Object[] args = { Department.class, getSelectedDepartmentID() };
            IDepartment department = (IDepartment) ServiceUtils.executeService(getUserView(),
                    "ReadDomainObject", args);
            return department;
        }
        return null;
    }

    public List<SelectItem> getDepartments() throws FenixFilterException, FenixServiceException {

        Object[] args = { Department.class };
        Collection<IDepartment> departments = (Collection<IDepartment>) ServiceUtils.executeService(
                getUserView(), "ReadAllDomainObjects", args);

        List<SelectItem> result = new ArrayList<SelectItem>();
        for (IDepartment department : departments) {
            result.add(new SelectItem(department.getIdInternal(), department.getRealName()));
        }

        return result;
    }

    public List<SelectItem> getGroupMembers() throws FenixFilterException, FenixServiceException {

        List<SelectItem> result = new ArrayList<SelectItem>();

        IDegreeCurricularPlanMembersGroup curricularPlanMembersGroup = getDegreeCurricularPlan()
                .getCurricularPlanMembersGroup();
        if (curricularPlanMembersGroup != null) {
            for (IUserGroup member : curricularPlanMembersGroup.getParts()) {
                IPerson person = ((IPersonGroup) member).getPerson();
                result.add(new SelectItem(member.getIdInternal(), person.getNome() + " ("
                        + person.getUsername() + ")"));
            }
        }

        return result;
    }

    public List<SelectItem> getDepartmentEmployees() throws FenixFilterException, FenixServiceException {
        List<SelectItem> result = new ArrayList<SelectItem>();

        IDepartment department = getDepartment();
        if (department != null) {
            List<IEmployee> employees = getDepartment().getCurrentActiveWorkingEmployees();

            for (IEmployee departmentEmployee : employees) {
                IPerson person = departmentEmployee.getPerson();
                result.add(new SelectItem(person.getIdInternal(), person.getNome() + " ("
                        + person.getUsername() + ")"));
            }
        }

        return result;
    }

    public Integer[] getSelectedPersonGroupsIDsToRemove() {
        return selectedPersonGroupsIDsToRemove;
    }

    public void setSelectedPersonGroupsIDsToRemove(Integer[] selectedPersonGroupsIDsToRemove) {
        this.selectedPersonGroupsIDsToRemove = selectedPersonGroupsIDsToRemove;
    }

    public Integer[] getSelectedPersonsIDsToAdd() {
        return selectedPersonsIDsToAdd;
    }

    public void setSelectedPersonsIDsToAdd(Integer[] selectedPersonsIDsToAdd) {
        this.selectedPersonsIDsToAdd = selectedPersonsIDsToAdd;
    }

    public Integer getSelectedCurricularPlanID() {
        if (this.getViewState().getAttribute("selectedCurricularPlanID") != null) {
            return (Integer) this.getViewState().getAttribute("selectedCurricularPlanID");
        } else {
            return (Integer) getAndHoldIntegerParameter("dcpId");    
        }
    }

    public void setSelectedCurricularPlanID(Integer selectedCurricularPlanID) {
        this.getViewState().setAttribute("selectedCurricularPlanID", selectedCurricularPlanID);
    }

    public Integer getSelectedDepartmentID() {
        return (Integer) this.getViewState().getAttribute("selectedDepartmentID");
    }

    public void setSelectedDepartmentID(Integer selectedDepartmentID) {
        this.getViewState().setAttribute("selectedDepartmentID", selectedDepartmentID);
    }

}
