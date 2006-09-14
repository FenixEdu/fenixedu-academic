package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ExamCoordinatorManagement extends FenixDispatchAction {

    public ActionForward prepareExamCoordinator(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("preserveState");
        if (viewState == null) {
            prepareManagementBean(request);
        } else {
            VigilantGroupBean bean = (VigilantGroupBean) viewState.getMetaObject().getObject();
            request.setAttribute("bean", bean);
        }
        return mapping.findForward("prepareExamCoordinator");
    }

    public ActionForward addExamCoordinator(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("preserveState");
        VigilantGroupBean bean = (VigilantGroupBean) viewState.getMetaObject().getObject();
        request.setAttribute("bean", bean);

        Object args[] = { bean.getPerson(), bean.getExecutionYear(), bean.getSelectedUnit() };
        executeService(request, "AddExamCoordinator", args);

        return mapping.findForward("prepareExamCoordinator");
    }

    public ActionForward deleteExamCoordinator(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String oid = request.getParameter("oid");
        Integer idInternal = Integer.valueOf(oid);
        String departmentId = request.getParameter("deparmentId");
        String unitId = request.getParameter("unitId");

        ExamCoordinator coordinator = (ExamCoordinator) RootDomainObject.readDomainObjectByOID(
                ExamCoordinator.class, idInternal);

        Object[] args = { coordinator };
        executeService(request, "DeleteExamCoordinator", args);

        Department deparment = (Department) RootDomainObject.readDomainObjectByOID(Department.class,
                Integer.valueOf(departmentId));
        Unit unit = (Unit) RootDomainObject.readDomainObjectByOID(Unit.class, Integer.valueOf(unitId));

        VigilantGroupBean bean = new VigilantGroupBean();
        bean.setSelectedDepartment(deparment);
        bean.setSelectedUnit(unit);
        bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());

        request.setAttribute("bean", bean);
        return mapping.findForward("prepareExamCoordinator");
    }

    public ActionForward prepareAddExamCoordinatorWithState(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String departmentId = request.getParameter("deparmentId");
        String unitId = request.getParameter("unitId");

        Department deparment = (Department) RootDomainObject.readDomainObjectByOID(Department.class,
                Integer.valueOf(departmentId));
        Unit unit = (Unit) RootDomainObject.readDomainObjectByOID(Unit.class, Integer.valueOf(unitId));

        VigilantGroupBean bean = new VigilantGroupBean();
        bean.setSelectedDepartment(deparment);
        bean.setSelectedUnit(unit);
        bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());

        request.setAttribute("bean", bean);
        return mapping.findForward("prepareExamCoordinator");
    }

    public ActionForward editExamCoordinators(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String departmentId = request.getParameter("deparmentId");
        String unitId = request.getParameter("unitId");

        Department deparment = (Department) RootDomainObject.readDomainObjectByOID(Department.class,
                Integer.valueOf(departmentId));
        Unit unit = (Unit) RootDomainObject.readDomainObjectByOID(Unit.class, Integer.valueOf(unitId));

        VigilantGroupBean bean = new VigilantGroupBean();
        bean.setSelectedDepartment(deparment);
        bean.setSelectedUnit(unit);
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        bean.setExecutionYear(executionYear);
        bean.setExamCoordinators(unit.getExamCoordinatorsForGivenYear(executionYear));
        request.setAttribute("bean", bean);
        return mapping.findForward("editExamCoordinator");
    }

    public ActionForward selectUnitForCoordinator(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("selectUnit")
                .getMetaObject().getObject();
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("selectUnit");
        return mapping.findForward("prepareExamCoordinator");
    }

    private void prepareManagementBean(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {

        VigilantGroupBean bean = new VigilantGroupBean();
        ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
        bean.setExecutionYear(currentYear);
        Person person = getLoggedPerson(request);
        Employee employee = person.getEmployee();
        if (employee != null) {
            bean.setSelectedDepartment(employee.getCurrentDepartmentWorkingPlace());
        }
        request.setAttribute("bean", bean);
    }

}