package net.sourceforge.fenixedu.presentationTier.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixDateAndTimeContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class AddExecutionCourseToExamAction extends FenixDateAndTimeContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        //HttpSession session = request.getSession(false);
        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionCourse executionCourse = (InfoExecutionCourse) request
                .getAttribute(SessionConstants.EXECUTION_COURSE);

        InfoViewExamByDayAndShift infoViewExams = (InfoViewExamByDayAndShift) request.getSession()
                .getAttribute(SessionConstants.INFO_VIEW_EXAM);

        // Create new association between exam and executionCourse
        Object argsCreateExam[] = { infoViewExams, executionCourse };
        try {
            ServiceUtils.executeService(userView, "AssociateExecutionCourseToExam", argsCreateExam);
        } catch (ExistingServiceException ex) {
            throw new ExistingActionException("Para a disciplina escolhida, o exame de "
                    + infoViewExams.getInfoExam().getSeason(), ex);
        }

        request.getSession().removeAttribute(SessionConstants.INFO_VIEW_EXAM);
        return mapping.findForward("Sucess");
    }

}