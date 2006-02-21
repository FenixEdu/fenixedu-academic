package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class WeeklyWorkLoadDA extends FenixDispatchAction {

	private static final Comparator<Attends> ATTENDS_COMPARATOR = new Comparator<Attends>(){
		public int compare(final Attends attends1, final Attends attends2) {
			final ExecutionCourse executionCourse1 = attends1.getDisciplinaExecucao();
			final ExecutionCourse executionCourse2 = attends2.getDisciplinaExecucao();
			return executionCourse1.getNome().compareTo(executionCourse2.getNome());
		}};

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixFilterException, FenixServiceException {

        final String executionCourseIDString = request.getParameter("executionCourseID");
        final Integer executionCourseID = (executionCourseIDString == null || executionCourseIDString.length() == 0) ?
                null : Integer.valueOf(executionCourseIDString);

        final Object[] args = { ExecutionCourse.class, executionCourseID };
        final ExecutionCourse executionCourse = (ExecutionCourse) executeService(request, "ReadDomainObject", args);

        final InfoSiteCommon infoSiteCommon = new InfoSiteCommon();
        infoSiteCommon.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        final ExecutionCourseSiteView executionCourseSiteView = new ExecutionCourseSiteView(infoSiteCommon, null);
        request.setAttribute("siteView", executionCourseSiteView);

        final Set<Attends> attends = new TreeSet<Attends>(ATTENDS_COMPARATOR);
        attends.addAll(executionCourse.getAttends());
        request.setAttribute("attends", attends);

        return mapping.findForward("showWeeklyWorkLoad");
    }

}
