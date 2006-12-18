package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.ProjectSubmission;
import net.sourceforge.fenixedu.domain.ProjectSubmissionLog;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;

public class ProjectSubmissionsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward viewLastProjectSubmissionForEachGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        final Project project = getProject(request);
        final List<ProjectSubmission> projectSubmissions = new ArrayList<ProjectSubmission>(project
                .getLastProjectSubmissionForEachStudentGroup());
        Collections.sort(projectSubmissions,
                ProjectSubmission.COMPARATOR_BY_GROUP_NUMBER_AND_MOST_RECENT_SUBMISSION_DATE);
        setRequestParameters(request, project, projectSubmissions, null);

        return mapping.findForward("viewLastProjectSubmissionForEachGroup");

    }

    public ActionForward viewProjectSubmissionsByGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        final Project project = getProject(request);
        final List<ProjectSubmission> projectSubmissions = new ArrayList<ProjectSubmission>(project
                .getProjectSubmissionsByStudentGroup(getStudentGroup(request)));
        Collections
                .sort(projectSubmissions, ProjectSubmission.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);
        setRequestParameters(request, project, projectSubmissions, null);

        return mapping.findForward("viewProjectSubmissionsByGroup");

    }

    public ActionForward viewProjectSubmissionLogsByGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        final Project project = getProject(request);
        final List<ProjectSubmissionLog> projectSubmissionLogs = new ArrayList<ProjectSubmissionLog>(
                project.getProjectSubmissionLogsByStudentGroup(getStudentGroup(request)));
        Collections.sort(projectSubmissionLogs,
                ProjectSubmissionLog.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);
        setRequestParameters(request, project, null, projectSubmissionLogs);

        return mapping.findForward("viewProjectSubmissionLogsByGroup");

    }

    private void setRequestParameters(HttpServletRequest request, Project project,
            List<ProjectSubmission> projectSubmissions, List<ProjectSubmissionLog> projectSubmissionLogs) {
        
        request.setAttribute("executionCourseID", getExecutionCourseID(request));
        request.setAttribute("project", project);
        request.setAttribute("projectSubmissions", projectSubmissions);
        request.setAttribute("projectSubmissionLogs", projectSubmissionLogs);
    }

    private StudentGroup getStudentGroup(HttpServletRequest request) {
        return rootDomainObject.readStudentGroupByOID(getRequestParameterAsInteger(request,
                "studentGroupID"));
    }

    private Project getProject(HttpServletRequest request) {
        final Teacher teacher = getUserView(request).getPerson().getTeacher();
        final Integer projectId = getRequestParameterAsInteger(request, "projectID");
        for (final Professorship professorship : teacher.getProfessorships()) {
            for (final Project project : professorship.getExecutionCourse().getAssociatedProjects()) {
                if (project.getIdInternal().equals(projectId)) {
                    return project;
                }
            }
        }

        return null;
    }

    private Integer getExecutionCourseID(HttpServletRequest request) {
        return getRequestParameterAsInteger(request, "executionCourseID");
    }

}
