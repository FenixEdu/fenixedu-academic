/*
 * Created on 29/Ago/2003
 *
 * 
 */
package ServidorApresentacao.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 * 
 * 29/Ago/2003 fenix-head ServidorApresentacao.Action.student
 *  
 */
public class ViewStudentTimeTable extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Object[] args = { userView.getUtilizador() };
        List infoLessons;
        try {
            infoLessons = (List) ServiceUtils.executeService(userView, "ReadStudentTimeTable", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("infoLessons", infoLessons);

        return mapping.findForward("sucess");

    }

}