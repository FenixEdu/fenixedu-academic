package net.sourceforge.fenixedu.presentationTier.Action.coordinator.transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "scientificCouncil", path = "/competenceCourses/manageVersions", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "manageVersions", path = "/scientificCouncil/bolonha/manageVersions.jsp", tileProperties = @Tile(
                title = "private.scientificcouncil.bolognaprocess.versionproposals")),
        @Forward(name = "listRequests", path = "/scientificCouncil/bolonha/listVersions.jsp", tileProperties = @Tile(
                title = "private.scientificcouncil.bolognaprocess.versionproposals")),
        @Forward(name = "viewVersionDetails", path = "/scientificCouncil/bolonha/viewVersionDetails.jsp", tileProperties = @Tile(
                title = "private.scientificcouncil.bolognaprocess.versionproposals")) })
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
        Department department = (Department) AbstractDomainObject.fromExternalId(departmentID);
        putChangeRequestInRequest(request, department);

        return mapping.findForward("listRequests");

    }

    public ActionForward viewVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourseInformationChangeRequest changeRequest = getChangeRequest(request);
        if (changeRequest != null && isAllowedToViewChangeRequest(getLoggedPerson(request), changeRequest)) {
            request.setAttribute("changeRequest", changeRequest);
        }
        return mapping.findForward("viewVersionDetails");
    }

    public ActionForward approveRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourseInformationChangeRequest changeRequest = getChangeRequest(request);
        if (changeRequest != null && isAllowedToViewChangeRequest(getLoggedPerson(request), changeRequest)) {
            try {
                changeRequest.approve(getLoggedPerson(request));
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }

        return displayRequest(mapping, form, request, response);
    }

    public ActionForward rejectRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourseInformationChangeRequest changeRequest = getChangeRequest(request);
        if (changeRequest != null && isAllowedToViewChangeRequest(getLoggedPerson(request), changeRequest)) {
            try {
                changeRequest.reject(getLoggedPerson(request));
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }

        return displayRequest(mapping, form, request, response);
    }

    private CompetenceCourseInformationChangeRequest getChangeRequest(HttpServletRequest request) {
        String competenceCourseInformationChangeRequestId = request.getParameter("changeRequestID");
        CompetenceCourseInformationChangeRequest changeRequest =
                (CompetenceCourseInformationChangeRequest) AbstractDomainObject
                        .fromExternalId(competenceCourseInformationChangeRequestId);
        return changeRequest;
    }

    private CompetenceCourse getCompetenceCourse(HttpServletRequest request) {
        String competenceCourseID = request.getParameter("competenceCourseID");
        CompetenceCourse course = (CompetenceCourse) AbstractDomainObject.fromExternalId(competenceCourseID);
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