/*
 * Created on 20/Mar/2003
 *
 * 
 */
package ServidorApresentacao.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidSessionActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author João Mota
 * 
 *  
 */
public class IndexTeacherAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        HttpSession session = getSession(request);
        IUserView userView = SessionUtils.getUserView(request);

        InfoTeacher teacher = null;
        try {
            Object args[] = { userView.getUtilizador() };
            teacher = (InfoTeacher) ServiceUtils.executeService(userView, "ReadTeacherByUsername", args);
            //TODO: fix the situation where the teacher is null
            if (teacher == null) {
                throw new InvalidSessionActionException();
            }

            session.setAttribute(SessionConstants.INFO_TEACHER, teacher);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("infoTeacher", teacher);
        return mapping.findForward("success");
    }

}