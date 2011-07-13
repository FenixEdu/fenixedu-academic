package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.AddExamCoordinator;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.DeleteExamCoordinator;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "departmentAdmOffice", path = "/vigilancy/examCoordinatorManagement", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "prepareExamCoordinator", path = "/departmentAdmOffice/vigilancy/manageExamCoordinator.jsp"),
		@Forward(name = "editExamCoordinator", path = "/departmentAdmOffice/vigilancy/editExamCoordinator.jsp") })
public class ExamCoordinatorManagement extends FenixDispatchAction {

    public ActionForward prepareExamCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IViewState viewState = RenderUtils.getViewState("preserveState");
	if (viewState == null) {
	    prepareManagementBean(request);
	} else {
	    VigilantGroupBean bean = (VigilantGroupBean) viewState.getMetaObject().getObject();
	    request.setAttribute("bean", bean);
	}
	return mapping.findForward("prepareExamCoordinator");
    }

    public ActionForward addExamCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IViewState viewState = RenderUtils.getViewState("preserveState");
	VigilantGroupBean bean = (VigilantGroupBean) viewState.getMetaObject().getObject();
	request.setAttribute("bean", bean);

	String username = bean.getUsername();
	User user = User.readUserByUserUId(username);
	if (user != null && user.getPerson() != null) {

	    AddExamCoordinator.run(user.getPerson(), bean.getExecutionYear(), bean.getSelectedUnit());
	} else {
	    addActionMessage(request, "label.vigilancy.inexistingUsername");
	}

	return mapping.findForward("prepareExamCoordinator");
    }

    public ActionForward deleteExamCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String oid = request.getParameter("oid");
	Integer idInternal = Integer.valueOf(oid);
	String departmentId = request.getParameter("deparmentId");
	String unitId = request.getParameter("unitId");

	ExamCoordinator coordinator = (ExamCoordinator) RootDomainObject.readDomainObjectByOID(ExamCoordinator.class, idInternal);

	DeleteExamCoordinator.run(coordinator);

	Department deparment = (Department) RootDomainObject.readDomainObjectByOID(Department.class, Integer
		.valueOf(departmentId));
	Unit unit = (Unit) RootDomainObject.readDomainObjectByOID(Unit.class, Integer.valueOf(unitId));

	VigilantGroupBean bean = new VigilantGroupBean();
	bean.setSelectedDepartment(deparment);
	bean.setSelectedUnit(unit);
	bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());

	request.setAttribute("bean", bean);
	return mapping.findForward("prepareExamCoordinator");
    }

    public ActionForward prepareAddExamCoordinatorWithState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String departmentId = request.getParameter("deparmentId");
	String unitId = request.getParameter("unitId");

	Department deparment = (Department) RootDomainObject.readDomainObjectByOID(Department.class, Integer
		.valueOf(departmentId));
	Unit unit = (Unit) RootDomainObject.readDomainObjectByOID(Unit.class, Integer.valueOf(unitId));

	VigilantGroupBean bean = new VigilantGroupBean();
	bean.setSelectedDepartment(deparment);
	bean.setSelectedUnit(unit);
	bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());

	request.setAttribute("bean", bean);
	return mapping.findForward("prepareExamCoordinator");
    }

    public ActionForward editExamCoordinators(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String departmentId = request.getParameter("deparmentId");
	String unitId = request.getParameter("unitId");

	Department deparment = (Department) RootDomainObject.readDomainObjectByOID(Department.class, Integer
		.valueOf(departmentId));
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

    public ActionForward selectUnitForCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("selectUnit").getMetaObject().getObject();
	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState("selectUnit");
	return mapping.findForward("prepareExamCoordinator");
    }

    private void prepareManagementBean(HttpServletRequest request) throws FenixFilterException, FenixServiceException {

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