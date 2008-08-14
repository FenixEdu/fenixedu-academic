package net.sourceforge.fenixedu.presentationTier.Action.coordinator.transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageCompetenceCourseInformationChangeRequests extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Set<Department> departments = RootDomainObject.readAllDomainObjects(Department.class);
	request.setAttribute("departments", departments);

	return mapping.findForward("manageVersions");
    }

    public ActionForward displayRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	String departmentID = request.getParameter("departmentID");
	Department department = (Department) RootDomainObject.readDomainObjectByOID(Department.class, Integer
		.valueOf(departmentID));
	putChangeRequestInRequest(request, department);

	return mapping.findForward("listRequests");

    }

    public ActionForward viewVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	CompetenceCourseInformationChangeRequest changeRequest = getChangeRequest(request);
	if (changeRequest != null && isAllowedToViewChangeRequest(getLoggedPerson(request), changeRequest)) {
	    request.setAttribute("changeRequest", changeRequest);
	}
	return mapping.findForward("viewVersionDetails");
    }

    private ActionForward modifyRequestStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, Boolean status) throws FenixFilterException, FenixServiceException {

	CompetenceCourseInformationChangeRequest changeRequest = getChangeRequest(request);
	if (changeRequest != null && isAllowedToViewChangeRequest(getLoggedPerson(request), changeRequest)) {
	    try {
		executeService("ChangeCompetenceCourseInformationChangeRequestStatus", new Object[] { changeRequest,
			getLoggedPerson(request), status });
	    } catch (DomainException e) {
		addActionMessage(request, e.getMessage());
	    }
	}
	return displayRequest(mapping, form, request, response);
    }

    public ActionForward approveRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	return modifyRequestStatus(mapping, form, request, response, Boolean.TRUE);
    }

    public ActionForward rejectRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	return modifyRequestStatus(mapping, form, request, response, Boolean.FALSE);
    }

    private CompetenceCourseInformationChangeRequest getChangeRequest(HttpServletRequest request) {
	String competenceCourseInformationChangeRequestId = request.getParameter("changeRequestID");
	CompetenceCourseInformationChangeRequest changeRequest = (CompetenceCourseInformationChangeRequest) RootDomainObject
		.readDomainObjectByOID(CompetenceCourseInformationChangeRequest.class, Integer
			.valueOf(competenceCourseInformationChangeRequestId));
	return changeRequest;
    }

    private CompetenceCourse getCompetenceCourse(HttpServletRequest request) {
	String competenceCourseID = request.getParameter("competenceCourseID");
	CompetenceCourse course = (CompetenceCourse) RootDomainObject.readDomainObjectByOID(CompetenceCourse.class, Integer
		.valueOf(competenceCourseID));
	return course;
    }

    private void putChangeRequestInRequest(HttpServletRequest request, Department department) {
	List<CompetenceCourseInformationChangeRequest> requests = new ArrayList<CompetenceCourseInformationChangeRequest>();
	for (CompetenceCourse courses : department.getDepartmentUnit().getCompetenceCourses()) {
	    requests.addAll(courses.getCompetenceCourseInformationChangeRequests());
	}
	request.setAttribute("changeRequests", requests);
    }

    private boolean isAllowedToViewChangeRequest(Person loggedPerson, CompetenceCourseInformationChangeRequest changeRequest) {
	return loggedPerson.hasPersonRoles(Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL));
    }
}
