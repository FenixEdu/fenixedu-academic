package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.ForunsManagement;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentParticipateApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author naat
 * @author pcma
 * 
 */
@StrutsFunctionality(app = StudentParticipateApp.class, path = "forums", titleKey = "link.viewExecutionCourseForuns")
@Mapping(module = "student", path = "/viewExecutionCourseForuns")
@Forwards({ @Forward(name = "viewForum", path = "/commons/forums/viewForum.jsp"),
        @Forward(name = "viewThread", path = "/commons/forums/viewThread.jsp"),
        @Forward(name = "createThreadAndMessage", path = "/commons/forums/createThreadAndMessage.jsp"),
        @Forward(name = "viewForuns", path = "/student/forums/viewExecutionCourseForuns.jsp") })
public class ViewExecutionCourseForunsDispatchAction extends ForunsManagement {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        SortedSet<Attends> attendsForCurrentExecutionPeriod =
                new TreeSet<Attends>(Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
        attendsForCurrentExecutionPeriod.addAll(getLoggedPerson(request).getCurrentAttends());

        request.setAttribute("attendsForExecutionPeriod", attendsForCurrentExecutionPeriod);

        return mapping.findForward("viewForuns");

    }

}