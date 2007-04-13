package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.ProjectSubmission;
import net.sourceforge.fenixedu.domain.ProjectSubmissionLog;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Archive;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.DiskZipArchive;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Fetcher;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Resource;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
	Collections.sort(projectSubmissions, ProjectSubmission.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);

	final List<ProjectSubmissionLog> projectSubmissionLogs = new ArrayList<ProjectSubmissionLog>(project
		.getProjectSubmissionLogsByStudentGroup(getStudentGroup(request)));
	Collections.sort(projectSubmissionLogs,
		ProjectSubmissionLog.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);

	setRequestParameters(request, project, projectSubmissions, projectSubmissionLogs);

	return mapping.findForward("viewProjectSubmissionsByGroup");

    }

    public ActionForward downloadProjectsInZipFormat(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, IOException, ServletException {

	final Project project = getProject(request);
	final List<ProjectSubmission> projectSubmissions = new ArrayList<ProjectSubmission>(project
		.getLastProjectSubmissionForEachStudentGroup());

	Archive archive = new DiskZipArchive(response, project.getName());
	Fetcher fetcher = new Fetcher(archive, request, response);

	for (ProjectSubmission submission : projectSubmissions) {
	    fetcher.queue(new Resource(submission.getProjectSubmissionFile().getFilename(), submission
		    .getProjectSubmissionFile().getDownloadUrl()));
	}

	fetcher.process();
	archive.finish();

	return null;
    }

    public ActionForward prepareSelectiveDownload(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, IOException, ServletException {

	IViewState viewState = RenderUtils.getViewState("selectiveDownload");
	Integer value = (viewState != null) ? (Integer) viewState.getMetaObject().getObject() : null;
	VariantBean bean = new VariantBean();
	bean.setInteger(value);
	final Project project = getProject(request);
	if (bean.getInteger() != null) {
	    final List<ProjectSubmission> projectSubmissions = new ArrayList<ProjectSubmission>(project
		    .getLastProjectSubmissionForEachStudentGroup());
	    Collections.sort(projectSubmissions, ProjectSubmission.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);
	    setRequestParameters(request, project, projectSubmissions, null);
	} else {
	    setRequestParameters(request, project, null, null);
	}

	request.setAttribute("bean", bean);
	return mapping.findForward("selectiveDownload");
    }

    public ActionForward selectiveDownload(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, IOException, ServletException {

	final Project project = getProject(request);
	final Integer startIndex = Integer.valueOf(request.getParameter("startIndex"));
	final Integer pageSize = Integer.valueOf(request.getParameter("size"));
	final List<ProjectSubmission> projectSubmissions = new ArrayList<ProjectSubmission>(project
		.getLastProjectSubmissionForEachStudentGroup());
	Collections.sort(projectSubmissions, ProjectSubmission.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);
	Integer actualSize = Math.min(projectSubmissions.size(), startIndex + pageSize);

	final List<ProjectSubmission> subList = projectSubmissions.subList(startIndex, actualSize);

	Archive archive = new DiskZipArchive(response, project.getName() + "-" + (startIndex + 1) + "-"
		+ (startIndex + actualSize));
	Fetcher fetcher = new Fetcher(archive, request, response);

	for (ProjectSubmission submission : subList) {
	    fetcher.queue(new Resource(submission.getProjectSubmissionFile().getFilename(), submission
		    .getProjectSubmissionFile().getDownloadUrl()));
	}

	fetcher.process();
	archive.finish();

	return null;
    }

    public ActionForward prepareGroupComment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response)  {
	
	final Project project = getProject(request);
	
	request.setAttribute("projectSubmission",project.getLastProjectSubmissionForStudentGroup(getStudentGroup(request)));
	setRequestParameters(request, project, null, null);
	
	
	
	return mapping.findForward("editProjectObservations");
    }

    public ActionForward sendCommentThroughEmail(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException  {
	
	final Project project = getProject(request);
	
	ExecutionCourse course = (ExecutionCourse) RootDomainObject.readDomainObjectByOID(ExecutionCourse.class, getExecutionCourseID(request));
	Object[] args = {project.getLastProjectSubmissionForStudentGroup(getStudentGroup(request)), course, getLoggedPerson(request)};
	executeService("NotifyStudentGroup", args);
	
	return prepareGroupComment(mapping, form, request, response);
    }
    
    private void setRequestParameters(HttpServletRequest request, Project project,
	    List<ProjectSubmission> projectSubmissions, List<ProjectSubmissionLog> projectSubmissionLogs) {

	request.setAttribute("executionCourseID", getExecutionCourseID(request));
	request.setAttribute("project", project);
	request.setAttribute("projectSubmissions", projectSubmissions);
	request.setAttribute("projectSubmissionLogs", projectSubmissionLogs);
    }

    private StudentGroup getStudentGroup(HttpServletRequest request) {
	return rootDomainObject
		.readStudentGroupByOID(getRequestParameterAsInteger(request, "studentGroupID"));
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
