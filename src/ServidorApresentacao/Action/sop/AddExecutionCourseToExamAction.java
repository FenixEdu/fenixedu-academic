package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.base.FenixDateAndTimeContextAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

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