/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanMembersGroup;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.UserGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularPlansMembersManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle scouncilBundle = getResourceBundle("ServidorApresentacao/ScientificCouncilResources");
    
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

        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        DegreeCurricularPlanMembersGroup curricularPlanMembersGroup = degreeCurricularPlan
                .getCurricularPlanMembersGroup();

        if (curricularPlanMembersGroup == null) {
            Object[] argsCreateGroup = { degreeCurricularPlan };
            curricularPlanMembersGroup = (DegreeCurricularPlanMembersGroup) ServiceUtils
                    .executeService(getUserView(), "CreateCurricularPlanMembersGroup", argsCreateGroup);
        }

        if (selectedPersonsIDsToAdd != null) {
            Object[] argsUpdateGroup = { curricularPlanMembersGroup, selectedPersonsIDsToAdd, null, RoleType.BOLONHA_MANAGER };
            ServiceUtils.executeService(getUserView(), "UpdateCurricularPlanMembersGroup",
                    argsUpdateGroup);
        }
    }

    public void removeMembers(ActionEvent event) throws FenixFilterException, FenixServiceException {

        if (selectedPersonGroupsIDsToRemove != null) {
            Object[] args = { getDegreeCurricularPlan().getCurricularPlanMembersGroup(), null,
                    selectedPersonGroupsIDsToRemove, RoleType.BOLONHA_MANAGER };
            ServiceUtils.executeService(getUserView(), "UpdateCurricularPlanMembersGroup", args);
        }
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() throws FenixServiceException,
            FenixFilterException {
        Object[] args = { DegreeCurricularPlan.class, getSelectedCurricularPlanID() };
        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) ServiceUtils
                .executeService(getUserView(), "ReadDomainObject", args);
        return degreeCurricularPlan;
    }

    private Department getDepartment() throws FenixServiceException, FenixFilterException {

        if (getSelectedDepartmentID() != null) {
            Object[] args = { Department.class, getSelectedDepartmentID() };
            Department department = (Department) ServiceUtils.executeService(getUserView(),
                    "ReadDomainObject", args);
            return department;
        }
        return null;
    }

    public List<SelectItem> getDepartments() throws FenixFilterException, FenixServiceException {

        Object[] args = { Department.class };
        Collection<Department> departments = (Collection<Department>) ServiceUtils.executeService(
                getUserView(), "ReadAllDomainObjects", args);

        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(0, scouncilBundle.getString("choose")));
        for (Department department : departments) {
            result.add(new SelectItem(department.getIdInternal(), department.getRealName()));
        }

        return result;
    }

    public List<SelectItem> getGroupMembers() throws FenixFilterException, FenixServiceException {

        List<SelectItem> result = new ArrayList<SelectItem>();

        DegreeCurricularPlanMembersGroup curricularPlanMembersGroup = getDegreeCurricularPlan()
                .getCurricularPlanMembersGroup();
        if (curricularPlanMembersGroup != null) {
            for (UserGroup member : curricularPlanMembersGroup.getParts()) {
                Person person = ((PersonGroup) member).getPerson();
                result.add(new SelectItem(member.getIdInternal(), person.getNome() + " ("
                        + person.getUsername() + ")"));
            }
        }

        return result;
    }

    public List<SelectItem> getDepartmentEmployees() throws FenixFilterException, FenixServiceException {
        List<SelectItem> result = new ArrayList<SelectItem>();

        Department department = getDepartment();
        if (department != null) {
            List<Employee> employees = new ArrayList<Employee>(getDepartment().getCurrentActiveWorkingEmployees());

            ComparatorChain chainComparator = new ComparatorChain();
            chainComparator.addComparator(new BeanComparator("person.nome"), false);
            chainComparator.addComparator(new BeanComparator("employeeNumber"), false);
            Collections.sort(employees, chainComparator);
            
            for (Employee departmentEmployee : employees) {
                Person person = departmentEmployee.getPerson();
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
