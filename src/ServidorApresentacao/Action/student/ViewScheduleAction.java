package ServidorApresentacao.Action.student;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author tfc130
 *  
 */
public class ViewScheduleAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        HttpSession session = request.getSession(false);

        Object argsReadStudentLessons[] = { (InfoStudent) session.getAttribute("infoStudent") };

        List lessons = (ArrayList) ServiceUtils.executeService(userView, "ReadStudentLessons",
                argsReadStudentLessons);

        if (lessons != null) {
            session.setAttribute(SessionConstants.LESSON_LIST_ATT, lessons);
        }

        return mapping.findForward("sucess");

    }

}