/*
 * Created on 20/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSessionActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author João Mota
 * 
 *  
 */
public class IndexTeacherAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

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