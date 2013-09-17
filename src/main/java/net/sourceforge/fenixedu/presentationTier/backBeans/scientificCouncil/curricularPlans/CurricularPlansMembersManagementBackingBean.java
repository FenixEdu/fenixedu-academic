/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.UpdateDegreeCurricularPlanMembersGroup;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularPlansMembersManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle scouncilBundle = getResourceBundle("resources/ScientificCouncilResources");

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
        result.add(new SelectItem(0, scouncilBundle.getString("choose")));
        for (Department department : rootDomainObject.getDepartments()) {
            result.add(new SelectItem(department.getExternalId(), department.getRealName()));
        }

        return result;
    }

    public List<SelectItem> getGroupMembers() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        Group curricularPlanMembersGroup = getDegreeCurricularPlan().getCurricularPlanMembersGroup();
        if (curricularPlanMembersGroup != null) {
            for (Person person : curricularPlanMembersGroup.getElements()) {
                result.add(new SelectItem(person.getExternalId(), person.getName() + " (" + person.getUsername() + ")"));
            }
        }

        return result;
    }

    public List<String> getGroupMembersLabels() {
        List<String> result = new ArrayList<String>();

        Group curricularPlanMembersGroup = getDegreeCurricularPlan().getCurricularPlanMembersGroup();
        if (curricularPlanMembersGroup != null) {
            for (Person person : curricularPlanMembersGroup.getElements()) {
                result.add(person.getName() + " (" + person.getUsername() + ")");
            }
        }

        return result;
    }

    public List<SelectItem> getDepartmentEmployees() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        Department department = getDepartment();
        if (department != null) {
            List<Employee> employees = new ArrayList<Employee>(getDepartment().getAllCurrentActiveWorkingEmployees());
            ComparatorChain chainComparator = new ComparatorChain();
            chainComparator.addComparator(new BeanComparator("person.name"), false);
            chainComparator.addComparator(new BeanComparator("employeeNumber"), false);
            Collections.sort(employees, chainComparator);

            Group curricularPlanMembersGroup = this.getDegreeCurricularPlan().getCurricularPlanMembersGroup();
            for (Employee departmentEmployee : employees) {
                Person person = departmentEmployee.getPerson();
                if (curricularPlanMembersGroup == null || !curricularPlanMembersGroup.isMember(person)) {
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