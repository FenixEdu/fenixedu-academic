/*
 * Created on 28/Mai/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

/**
 * @author Tânia Nunes
 *  
 */
public class ViewExamsAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

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