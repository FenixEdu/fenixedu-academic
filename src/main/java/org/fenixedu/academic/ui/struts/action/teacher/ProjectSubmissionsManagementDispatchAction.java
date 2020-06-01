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
package org.fenixedu.academic.ui.struts.action.teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Project;
import org.fenixedu.academic.domain.ProjectSubmission;
import org.fenixedu.academic.domain.ProjectSubmissionLog;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.VariantBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.teacher.NotifyStudentGroup;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.teacher.executionCourse.ExecutionCourseBaseAction;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "teacher", path = "/projectSubmissionsManagement", functionality = ManageExecutionCourseDA.class)
public class ProjectSubmissionsManagementDispatchAction extends ExecutionCourseBaseAction {

    protected ActionForward doForward(HttpServletRequest request, String path) {
        request.setAttribute("teacher$actual$page", path);
        return new ActionForward("/evaluation/evaluationFrame.jsp");
    }

    public ActionForward viewLastProjectSubmissionForEachGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        final Project project = getProject(request);
        final List<ProjectSubmission> projectSubmissions =
                new ArrayList<ProjectSubmission>(project.getLastProjectSubmissionForEachStudentGroup());

        final List<ProjectSubmission> deletedGroupsProjectSubmissions =
                new ArrayList<ProjectSubmission>(project.getLastProjectSubmissionForEachDeletedStudentGroup());

        Collections.sort(projectSubmissions, ProjectSubmission.COMPARATOR_BY_GROUP_NUMBER_AND_MOST_RECENT_SUBMISSION_DATE);
        Collections.sort(deletedGroupsProjectSubmissions,
                ProjectSubmission.COMPARATOR_BY_GROUP_NUMBER_AND_MOST_RECENT_SUBMISSION_DATE);

        setRequestParameters(request, project, projectSubmissions, null);
        request.setAttribute("deletedStudentGroupProjectSubmissions", deletedGroupsProjectSubmissions);

        return doForward(request, "/teacher/evaluation/viewLastProjectSubmissionForEachGroup.jsp");

    }

    public ActionForward viewProjectSubmissionsByGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        final Project project = getProject(request);
        final StudentGroup studentGroup = getStudentGroup(request);
        final List<ProjectSubmission> projectSubmissions = project.getProjectSubmissionsByStudentGroup(studentGroup);

        final List<ProjectSubmissionLog> projectSubmissionLogs =
                new ArrayList<ProjectSubmissionLog>(project.getProjectSubmissionLogsByStudentGroup(getStudentGroup(request)));
        Collections.sort(projectSubmissionLogs, ProjectSubmissionLog.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);

        setRequestParameters(request, project, projectSubmissions, projectSubmissionLogs);

        return doForward(request, "/teacher/evaluation/viewProjectSubmissionsByGroup.jsp");

    }

    public ActionForward downloadProjectsInZipFormat(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, IOException, ServletException {

        final Project project = getProject(request);
        final List<ProjectSubmission> projectSubmissions =
                new ArrayList<ProjectSubmission>(project.getLastProjectSubmissionForEachStudentGroup());

        try (ZipOutputStream stream = new ZipOutputStream(response.getOutputStream())) {
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + project.getName() + ".zip\"");

            for (ProjectSubmission submission : projectSubmissions) {
                StudentGroup group = submission.getStudentGroup();
                ZipEntry zipEntry =
                        new ZipEntry(group.getGroupNumber() + getStudentsISTID(group) + "/"
                                + submission.getProjectSubmissionFile().getFilename());
                stream.putNextEntry(zipEntry);
                stream.write(submission.getProjectSubmissionFile().getContent());
                stream.closeEntry();
            }

        }

        return null;
    }

    public ActionForward prepareSelectiveDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, IOException, ServletException {

        IViewState viewState = RenderUtils.getViewState("selectiveDownload");
        Integer value = (viewState != null) ? (Integer) viewState.getMetaObject().getObject() : null;
        VariantBean bean = new VariantBean();
        bean.setInteger(value);
        final Project project = getProject(request);
        if (bean.getInteger() != null) {
            final List<ProjectSubmission> projectSubmissions =
                    new ArrayList<ProjectSubmission>(project.getLastProjectSubmissionForEachStudentGroup());
            Collections.sort(projectSubmissions, ProjectSubmission.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);
            setRequestParameters(request, project, projectSubmissions, null);
        } else {
            setRequestParameters(request, project, null, null);
        }

        request.setAttribute("bean", bean);
        return doForward(request, "/teacher/evaluation/selectiveDownload.jsp");
    }

    public ActionForward selectiveDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, IOException, ServletException {

        final Project project = getProject(request);
        final Integer startIndex = Integer.valueOf(request.getParameter("startIndex"));
        final Integer pageSize = Integer.valueOf(request.getParameter("size"));
        final List<ProjectSubmission> projectSubmissions =
                new ArrayList<ProjectSubmission>(project.getLastProjectSubmissionForEachStudentGroup());
        Collections.sort(projectSubmissions, ProjectSubmission.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);
        Integer finishIndex = Math.min(projectSubmissions.size(), startIndex + pageSize);

        final List<ProjectSubmission> subList = projectSubmissions.subList(startIndex, finishIndex);

        try (ZipOutputStream stream = new ZipOutputStream(response.getOutputStream())) {
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + project.getName() + "-" + (startIndex + 1)
                    + "-" + finishIndex + ".zip\"");

            for (ProjectSubmission submission : subList) {
                StudentGroup group = submission.getStudentGroup();
                ZipEntry zipEntry =
                        new ZipEntry(group.getGroupNumber() + getStudentsISTID(group) + "/"
                                + submission.getProjectSubmissionFile().getFilename());
                stream.putNextEntry(zipEntry);
                stream.write(submission.getProjectSubmissionFile().getContent());
                stream.closeEntry();
            }

        }

        return null;
    }

    public ActionForward prepareGroupComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Project project = getProject(request);

        request.setAttribute("projectSubmission", project.getLastProjectSubmissionForStudentGroup(getStudentGroup(request)));
        setRequestParameters(request, project, null, null);

        return doForward(request, "/teacher/evaluation/editProjectObservations.jsp");
    }

    public ActionForward sendCommentThroughEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final Project project = getProject(request);

        ExecutionCourse course = (ExecutionCourse) FenixFramework.getDomainObject(getExecutionCourseID(request));

        NotifyStudentGroup.run(project.getLastProjectSubmissionForStudentGroup(getStudentGroup(request)), course,
                getLoggedPerson(request));

        return prepareGroupComment(mapping, form, request, response);
    }

    private void setRequestParameters(HttpServletRequest request, Project project, List<ProjectSubmission> projectSubmissions,
            List<ProjectSubmissionLog> projectSubmissionLogs) {

        request.setAttribute("executionCourseID", getExecutionCourseID(request));
        request.setAttribute("project", project);
        request.setAttribute("projectSubmissions", projectSubmissions);
        request.setAttribute("projectSubmissionLogs", projectSubmissionLogs);
    }

    private StudentGroup getStudentGroup(HttpServletRequest request) {
        return getDomainObject(request, "studentGroupID");
    }

    private Project getProject(HttpServletRequest request) {
        final String projectExtId = request.getParameter("projectOID");
        if (projectExtId != null) {
            final Project project = FenixFramework.getDomainObject(projectExtId);
            request.setAttribute("projectID", project.getExternalId().toString());
            return project;
        }
        final Person person = getUserView(request).getPerson();
        final String projectId = request.getParameter("projectID");
        for (final Professorship professorship : person.getProfessorshipsSet()) {
            for (final Project project : professorship.getExecutionCourse().getAssociatedProjects()) {
                if (project.getExternalId().equals(projectId)) {
                    request.setAttribute("projectOID", project.getExternalId());
                    return project;
                }
            }
        }

        return null;
    }

    private String getExecutionCourseID(HttpServletRequest request) {
        return request.getParameter("executionCourseID");
    }

    private String getStudentsISTID(StudentGroup group) {
        ArrayList<Attends> sortedAttends = new ArrayList(group.getAttendsSet());
        Collections.sort(sortedAttends, Attends.COMPARATOR_BY_STUDENT_NUMBER);

        String studentsISTID = "";
        for (Attends attends : sortedAttends) {
            studentsISTID += "-" + attends.getAluno().getStudent().getPerson().getUsername();
        }
        return studentsISTID;
    }

}
