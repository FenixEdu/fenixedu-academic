package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.ForunsManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @author naat
 * 
 */
public class ViewExecutionCourseForunsDispatchAction extends ForunsManagement {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException,
            FenixServiceException {

        SortedSet<Attends> attendsForCurrentExecutionPeriod = new TreeSet<Attends>(
                Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
        attendsForCurrentExecutionPeriod.addAll(getUserView(request).getPerson().getCurrentAttends());

        request.setAttribute("attendsForExecutionPeriod", attendsForCurrentExecutionPeriod);

        return mapping.findForward("viewForuns");

    }

    @Override
    protected String getContextInformation(HttpServletRequest request) {
        return "/viewExecutionCourseForuns.do";
    }

}