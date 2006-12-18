package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.projectSubmission.CreateProjectSubmissionBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.ProjectSubmission;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.utl.ist.fenix.tools.file.FileManagerException;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;

/**
 * 
 * @author naat
 * 
 */
public class ProjectSubmissionDispatchAction extends FenixDispatchAction {

 
    public ActionForward viewProjectsWithOnlineSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {

        SortedSet<Attends> attendsForCurrentExecutionPeriod = new TreeSet<Attends>(
                Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
        attendsForCurrentExecutionPeriod.addAll(getUserView(request).getPerson().getCurrentAttends());

        request.setAttribute("attendsForCurrentExecutionPeriod", attendsForCurrentExecutionPeriod);

        return mapping.findForward("viewProjectsWithOnlineSubmission");

    }

    public ActionForward viewProjectSubmissions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {

        final Attends attends = getAttends(request);
        final Project project = getProject(request);
        final StudentGroup studentGroup = project.getGrouping().getStudentGroupByAttends(attends);

        request.setAttribute("project", project);

        if (studentGroup != null) {
            final List<ProjectSubmission> projectSubmissionsSortedByMostRecent = getProjectSubmissionsForStudentGroupSortedByMostRecent(
                    project, studentGroup);

            request.setAttribute("attends", attends);
            request.setAttribute("projectSubmissions", projectSubmissionsSortedByMostRecent);
        } else {
            request.setAttribute("noStudentGroupForGrouping", true);
        }

        return mapping.findForward("viewProjectSubmissions");

    }

    private List<ProjectSubmission> getProjectSubmissionsForStudentGroupSortedByMostRecent(
            final Project project, final StudentGroup studentGroup) {
        final List<ProjectSubmission> projectSubmissionsSortedByMostRecent = new ArrayList<ProjectSubmission>(
                project.getProjectSubmissionsByStudentGroup(studentGroup));
        Collections.sort(projectSubmissionsSortedByMostRecent,
                ProjectSubmission.COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE);
        return projectSubmissionsSortedByMostRecent;
    }

    public ActionForward prepareProjectSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {

        Attends attends = getAttends(request);
        Project project = getProject(request);
        StudentGroup studentGroup = project.getGrouping().getStudentGroupByAttends(attends);

        request.setAttribute("attends", attends);
        request.setAttribute("project", getProject(request));
        request.setAttribute("studentGroup", studentGroup);
        request.setAttribute("person", getUserView(request).getPerson());
        request.setAttribute("projectSubmission", new CreateProjectSubmissionBean());

        return mapping.findForward("submitProject");

    }

    public ActionForward submitProject(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {

        final IViewState viewState = RenderUtils.getViewState("createProjectSubmission");
        final CreateProjectSubmissionBean createProjectSubmissionBean = (CreateProjectSubmissionBean) viewState
                .getMetaObject().getObject();

        try {
            ServiceUtils.executeService(getUserView(request), "CreateProjectSubmission",
                    new Object[] { createProjectSubmissionBean });
        } catch (DomainException ex) {
            saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());

            return prepareProjectSubmission(mapping, form, request, response);

        } catch (FileManagerException ex) {
            saveActionMessageOnRequest(request, ex.getKey(), ex.getArgs());

            return prepareProjectSubmission(mapping, form, request, response);
        }

        return viewProjectSubmissions(mapping, form, request, response);
    }

    private void saveActionMessageOnRequest(HttpServletRequest request, String errorKey, String[] args) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add(errorKey, new ActionMessage(errorKey, args));
        saveMessages(request, actionMessages);
    }

    private Project getProject(HttpServletRequest request) {
        Integer projectId = getRequestParameterAsInteger(request, "projectId");

        if (projectId != null) {
            return (Project) rootDomainObject.readEvaluationByOID(projectId);
        } else {
            return null;
        }
    }

    private Attends getAttends(HttpServletRequest request) {
        Integer attendsId = getRequestParameterAsInteger(request, "attendsId");

        if (attendsId != null) {
            return rootDomainObject.readAttendsByOID(attendsId);
        } else {
            return null;
        }
    }

}