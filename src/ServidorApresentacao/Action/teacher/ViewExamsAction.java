/*
 * Created on 28/Mai/2003
 *
 */
package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSite;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Tânia Nunes
 *  
 */
public class ViewExamsAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        HttpSession session = request.getSession(false);
        IUserView userView = SessionUtils.getUserView(request);
        Integer executionCourseCode = new Integer(request.getParameter("objectCode"));
        if (executionCourseCode == null) {

            InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
            InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
            infoExecutionCourse = infoSite.getInfoExecutionCourse();
            executionCourseCode = infoExecutionCourse.getIdInternal();
        }

        Object args[] = { executionCourseCode };

        List infoExamList;
        try {
            infoExamList = (List) ServiceUtils.executeService(userView, "ReadExams", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoExamList", infoExamList);
        request.setAttribute("objectCode", executionCourseCode);
        return mapping.findForward("viewExams");

    }
}