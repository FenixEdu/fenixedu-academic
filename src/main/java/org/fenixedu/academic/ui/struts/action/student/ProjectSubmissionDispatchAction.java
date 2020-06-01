/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.student;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Project;
import org.fenixedu.academic.domain.ProjectSubmission;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.projectSubmission.CreateProjectSubmissionBean;
import org.fenixedu.academic.dto.student.ManageStudentStatuteBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.projectSubmission.CreateProjectSubmission;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentSubmitApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import com.google.common.io.ByteStreams;

/**
 *
 * @author naat
 *
 */
@StrutsFunctionality(app = StudentSubmitApp.class, path = "projects", titleKey = "projects")
@Mapping(module = "student", path = "/projectSubmission")
@Forwards(value = {
        @Forward(name = "viewProjectSubmissions", path = "/student/projectSubmissions/viewProjectSubmissions.jsp"),
        @Forward(name = "viewProjectsWithOnlineSubmission",
        path = "/student/projectSubmissions/viewProjectsWithOnlineSubmission.jsp"),
        @Forward(name = "submitProject", path = "/student/projectSubmissions/submitProject.jsp") })
public class ProjectSubmissionDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward viewProjectsWithOnlineSubmission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        Student student = getUserView(request).getPerson().getStudent();
        ManageStudentStatuteBean bean = getRenderedObject("studentBean");
        if (bean == null) {
            bean = new ManageStudentStatuteBean(student);
        }

        request.setAttribute("studentBean", bean);
        request.setAttribute("attends", student.getAttendsForExecutionPeriod(bean.getExecutionPeriod()));

        return mapping.findForward("viewProjectsWithOnlineSubmission");

    }

    public ActionForward viewProjectSubmissions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        final Attends attends = getAttends(request);
        final Project project = getProject(request);
        final StudentGroup studentGroup = project.getGrouping().getStudentGroupByAttends(attends);

        request.setAttribute("project", project);

        if (studentGroup != null) {
            final List<ProjectSubmission> projectSubmissionsSortedByMostRecent =
                    getProjectSubmissionsForStudentGroupSortedByMostRecent(project, studentGroup);

            request.setAttribute("attends", attends);
            request.setAttribute("projectSubmissions", projectSubmissionsSortedByMostRecent);
        } else {
            request.setAttribute("noStudentGroupForGrouping", true);
        }

        return mapping.findForward("viewProjectSubmissions");

    }

    public ActionForward viewObservation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        ProjectSubmission submission = getProjectSubmission(request);

        if (submission != null && submission.getStudentGroup().isPersonInStudentGroup(getLoggedPerson(request))) {
            Attends attends = getAttends(request);
            Project project = getProject(request);
            StudentGroup studentGroup = project.getGrouping().getStudentGroupByAttends(attends);
            String rowClasses = "";
            for (ProjectSubmission oneSubmission : getProjectSubmissionsForStudentGroupSortedByMostRecent(project, studentGroup)) {
                if (oneSubmission.equals(submission)) {
                    rowClasses += "selected,";
                } else {
                    rowClasses += ",";
                }
            }

            request.setAttribute("submission", submission);
            request.setAttribute("rowClasses", rowClasses);
        }

        return viewProjectSubmissions(mapping, form, request, response);
    }

    private List<ProjectSubmission> getProjectSubmissionsForStudentGroupSortedByMostRecent(final Project project,
            final StudentGroup studentGroup) {
        return project.getProjectSubmissionsByStudentGroup(studentGroup);
    }

    public ActionForward prepareProjectSubmission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        Attends attends = getAttends(request);
        Project project = getProject(request);
        StudentGroup studentGroup = project.getGrouping().getStudentGroupByAttends(attends);

        request.setAttribute("attends", attends);
        request.setAttribute("project", getProject(request));
        request.setAttribute("studentGroup", studentGroup);
        request.setAttribute("person", getUserView(request).getPerson());
        request.setAttribute("projectSubmission", new CreateProjectSubmissionBean());

        RenderUtils.invalidateViewState("createProjectSubmission");
        return mapping.findForward("submitProject");

    }

    public ActionForward submitProject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, IOException {

        final IViewState viewState = RenderUtils.getViewState("createProjectSubmission");
        final CreateProjectSubmissionBean createProjectSubmissionBean =
                (CreateProjectSubmissionBean) viewState.getMetaObject().getObject();

        try (InputStream is = createProjectSubmissionBean.getInputStream()) {
            if (is == null) {
                saveActionMessageOnRequest(request, "error.file.empty", null);

                return prepareProjectSubmission(mapping, form, request, response);
            }
            byte[] bytes = ByteStreams.toByteArray(is);
            try {
                CreateProjectSubmission.run(bytes, createProjectSubmissionBean.getFilename(),
                        createProjectSubmissionBean.getAttends(), createProjectSubmissionBean.getProject(),
                        createProjectSubmissionBean.getStudentGroup(), createProjectSubmissionBean.getPerson());

            } catch (DomainException ex) {
                saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());

                return prepareProjectSubmission(mapping, form, request, response);
            }
        }

        return viewProjectSubmissions(mapping, form, request, response);
    }

    private void saveActionMessageOnRequest(HttpServletRequest request, String errorKey, String[] args) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add(errorKey, new ActionMessage(errorKey, args));
        saveMessages(request, actionMessages);
    }

    private ProjectSubmission getProjectSubmission(HttpServletRequest request) {
        return getDomainObject(request, "projectSubmissionId");
    }

    private Project getProject(HttpServletRequest request) {
        return getDomainObject(request, "projectId");
    }

    private Attends getAttends(HttpServletRequest request) {
        return getDomainObject(request, "attendsId");
    }

}