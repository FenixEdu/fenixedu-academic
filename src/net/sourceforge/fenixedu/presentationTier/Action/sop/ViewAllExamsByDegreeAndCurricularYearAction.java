package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewAllExamsByDegreeAndCurricularYearAction extends FenixContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            InfoExecutionPeriod infoExecutionPeriod = setExecutionContext(request);

            // 1. Read All ExecutionDegrees
            //			Object[] argsReadExecutionDegrees =
            // {infoExecutionPeriod.getInfoExecutionYear() };
            //			List infoExecutionDegreesList =
            //				(List) gestor.executar(
            //					userView,
            //					"ReadExecutionDegreesByExecutionYear",
            //					argsReadExecutionDegrees);

            // 2. For each ExecutionDegree and for each CurricularYear
            //    readExamsByExecutionDegreeAndCurricularYear
            //			int numberOfCurricularYears = 5;
            //			List infoViewAllExamsList = new ArrayList();
            //			
            //			for (int i = 0; i < infoExecutionDegreesList.size(); i++) {
            //				InfoExecutionDegree infoExecutionDegree =
            //					(InfoExecutionDegree) infoExecutionDegreesList.get(i);
            //				for (int j = 1; j < numberOfCurricularYears+1; j++) {
            //					InfoViewAllExams infoViewAllExams = new
            // InfoViewAllExams(infoExecutionDegree,new Integer(j),null);
            //					Object[] args = {infoExecutionDegree,infoExecutionPeriod, new
            // Integer(j) };
            //					List infoExecutionCourseAndExamsList =
            //						(List) gestor.executar(
            //							userView,"ReadExamsByExecutionDegreeAndCurricularYear", args);
            //					if(infoExecutionCourseAndExamsList!= null &&
            // infoExecutionCourseAndExamsList.isEmpty()) {
            //						infoViewAllExams.setInfoExecutionCourseAndExamsList(null);
            //					} else {
            //						infoViewAllExams.setInfoExecutionCourseAndExamsList(infoExecutionCourseAndExamsList);
            //					}
            //					infoViewAllExamsList.add(infoViewAllExams);
            //				}
            //			}

            Object[] args = { infoExecutionPeriod };
            List infoViewAllExamsList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadExamsSortedByExecutionDegreeAndCurricularYear", args);

            if (infoViewAllExamsList != null && infoViewAllExamsList.isEmpty()) {
                request.removeAttribute(SessionConstants.ALL_INFO_EXAMS_KEY);
            } else {
                request.setAttribute(SessionConstants.ALL_INFO_EXAMS_KEY, infoViewAllExamsList);
            }

            return mapping.findForward("Sucess");
        }
        throw new Exception();

    }

    /**
     * Method setExecutionContext.
     * 
     * @param request
     */
    private InfoExecutionPeriod setExecutionContext(HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession(false);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session
                .getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
        if (infoExecutionPeriod == null) {
            IUserView userView = SessionUtils.getUserView(request);
            infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView,
                    "ReadCurrentExecutionPeriod", new Object[0]);

            request.setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);
        }
        return infoExecutionPeriod;
    }

}