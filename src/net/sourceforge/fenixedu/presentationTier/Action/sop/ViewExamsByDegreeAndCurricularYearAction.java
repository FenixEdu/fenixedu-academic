package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionDegreeAndCurricularYearContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewExamsByDegreeAndCurricularYearAction extends
        FenixExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                    .getAttribute(SessionConstants.EXECUTION_DEGREE);

            Integer curricularYear = (Integer) request
                    .getAttribute(SessionConstants.CURRICULAR_YEAR_KEY);
            request.setAttribute(SessionConstants.CURRICULAR_YEAR_OID, curricularYear.toString());
            ContextUtils.setCurricularYearContext(request);

            Object[] args = { infoExecutionDegree, infoExecutionPeriod, curricularYear };
            List infoExecutionCourseAndExamsList = (List) ServiceManagerServiceFactory.executeService(
                    userView, "ReadExamsByExecutionDegreeAndCurricularYear", args);

            if (infoExecutionCourseAndExamsList != null && infoExecutionCourseAndExamsList.isEmpty()) {
                request.removeAttribute(SessionConstants.INFO_EXAMS_KEY);
            } else {
                request.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoExecutionCourseAndExamsList);
            }

            return mapping.findForward("Sucess");
        }
        throw new Exception();

    }
}