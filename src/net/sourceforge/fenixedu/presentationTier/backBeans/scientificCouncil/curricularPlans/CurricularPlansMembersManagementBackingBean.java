/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularPlansMembersManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle scouncilBundle = getResourceBundle("resources/ScientificCouncilResources");
    
    private Integer[] selectedPersonsIDsToAdd;
    private Integer[] selectedPersonsIDsToRemove;

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
        if (selectedPersonsIDsToAdd != null) {
            Object[] args = { getDegreeCurricularPlan(), selectedPersonsIDsToAdd, null };
            ServiceUtils.executeService(getUserView(), "UpdateDegreeCurricularPlanMembersGroup", args);
        }
        // avoid preset check-boxes after action
        selectedPersonsIDsToAdd = null;
        selectedPersonsIDsToRemove = null;
    }

    public void removeMembers(ActionEvent event) throws FenixFilterException, FenixServiceException {
        if (selectedPersonsIDsToRemove != null) {
            Object[] args = { getDegreeCurricularPlan(), null, selectedPersonsIDsToRemove };
            ServiceUtils.executeService(getUserView(), "UpdateDegreeCurricularPlanMembersGroup", args);
        }
        // avoid preset check-boxes after action
        selectedPersonsIDsToAdd = null;
        selectedPersonsIDsToRemove = null;
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

        Group curricularPlanMembersGroup = getDegreeCurricularPlan().getCurricularPlanMembersGroup();
        if (curricularPlanMembersGroup != null) {
            for(Person person: curricularPlanMembersGroup.getElements()){
                result.add(new SelectItem(person.getIdInternal(), person.getNome() + " (" + person.getUsername() + ")"));
            }
        }

        return result;
    }
    
    public List<String> getGroupMembersLabels() throws FenixFilterException, FenixServiceException {
        List<String> result = new ArrayList<String>();

        Group curricularPlanMembersGroup = getDegreeCurricularPlan().getCurricularPlanMembersGroup();
        if (curricularPlanMembersGroup != null) {
            for(Person person: curricularPlanMembersGroup.getElements()){
                result.add(person.getNome() + " (" + person.getUsername() + ")");
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
            chainComparator.addComparator(new BeanComparator("person.name"), false);
            chainComparator.addComparator(new BeanComparator("employeeNumber"), false);
            Collections.sort(employees, chainComparator);
            
            Group curricularPlanMembersGroup = this.getDegreeCurricularPlan().getCurricularPlanMembersGroup();
            for (Employee departmentEmployee : employees) {
                Person person = departmentEmployee.getPerson();
                if (curricularPlanMembersGroup == null || !curricularPlanMembersGroup.isMember(person)) {
                    result.add(new SelectItem(person.getIdInternal(), person.getNome() + " ("
                            + person.getUsername() + ")"));
                }
            }
        }

        return result;
    }

    public Integer[] getSelectedPersonsIDsToRemove() {
        return selectedPersonsIDsToRemove;
    }

    public void setSelectedPersonsIDsToRemove(Integer[] selectedPersonsIDsToRemove) {
        this.selectedPersonsIDsToRemove = selectedPersonsIDsToRemove;
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
        } else if (getAndHoldIntegerParameter("dcpId") != null) {
            return (Integer) getAndHoldIntegerParameter("dcpId");    
        } else {
            return (Integer) getAndHoldIntegerParameter("degreeCurricularPlanID");
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
