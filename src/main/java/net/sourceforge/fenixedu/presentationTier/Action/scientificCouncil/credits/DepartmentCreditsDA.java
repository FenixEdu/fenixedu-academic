package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/departmentCredits", module = "scientificCouncil")
@Forwards({ @Forward(name = "departmentCredits", path = "/scientificCouncil/credits/departmentCredits/departmentCredits.jsp",
        tileProperties = @Tile(title = "private.scientificcouncil.credits.departmentcredits")) })
public class DepartmentCreditsDA extends FenixDispatchAction {

    public ActionForward prepareDepartmentCredits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("departmentCredits");
    }

    public ActionForward showEmployeesByDepartment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        if (departmentCreditsBean.getDepartment() == null) {
            return mapping.findForward("departmentCredits");
        }
        return forwardDepartmentCredits(mapping, request, departmentCreditsBean);
    }

    public ActionForward addRoleDepartmentCredits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        Employee employee = getEmployeeFromBean(departmentCreditsBean);
        if (employee == null) {
            request.setAttribute("success", false);
            return forwardDepartmentCredits(mapping, request, departmentCreditsBean);
        }
        employee.assignPermission(departmentCreditsBean.getDepartment());
        request.setAttribute("success", true);
        return forwardDepartmentCredits(mapping, request, departmentCreditsBean);
    }

    public ActionForward removeRoleDepartmentCredits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Employee employee = getEmployeeFromRequest(request);
        Department department = getDepartmentFromRequest(request);
        employee.removePermission(department);
        return forwardDepartmentCredits(mapping, request, null);
    }

    private ActionForward forwardDepartmentCredits(ActionMapping mapping, HttpServletRequest request,
            DepartmentCreditsBean departmentCreditsBean) {
        if (departmentCreditsBean == null) {
            departmentCreditsBean = new DepartmentCreditsBean(getDepartmentFromRequest(request), null);
        }
        request.setAttribute("employeesOfDepartment", getEmployeesFromDepartment(departmentCreditsBean.getDepartment()));
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("departmentCredits");
    }

    private Employee getEmployeeFromBean(DepartmentCreditsBean departmentCreditsBean) {
        int employeeNumber = Integer.valueOf(departmentCreditsBean.getEmployeeNumber());
        return Employee.readByNumber(employeeNumber);
    }

    private List<Employee> getEmployeesFromDepartment(Department department) {
        List<Employee> employees = new LinkedList<Employee>();
        for (Person person : department.getAssociatedPersons()) {
            if (hasPersonPermissionCredits(person, department)) {
                employees.add(person.getEmployee());
            }
        }
        return employees;
    }

    private Employee getEmployeeFromRequest(HttpServletRequest request) {
        return FenixFramework.getDomainObject(request.getParameter("employeeId"));
    }

    private Department getDepartmentFromRequest(HttpServletRequest request) {
        return FenixFramework.getDomainObject(request.getParameter("departmentId"));
    }

    private boolean hasPersonPermissionCredits(Person person, Department department) {
        return person.hasRole(RoleType.DEPARTMENT_CREDITS_MANAGER) && person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)
                && person.getManageableDepartmentCreditsSet().contains(department);
    }

}
