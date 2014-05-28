/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.projectSubmission.CreateProjectSubmission;
import net.sourceforge.fenixedu.dataTransferObject.projectSubmission.CreateProjectSubmissionBean;
import net.sourceforge.fenixedu.dataTransferObject.student.ManageStudentStatuteBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.ProjectSubmission;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentSubmitApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
        final List<ProjectSubmission> projectSubmissionsSortedByMostRecent =
                new ArrayList<ProjectSubmission>(project.getProjectSubmissionsByStudentGroup(studentGroup));
        Collections.sort(projectSubmissionsSortedByMostRecent, ProjectSubmission.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);
        return projectSubmissionsSortedByMostRecent;
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