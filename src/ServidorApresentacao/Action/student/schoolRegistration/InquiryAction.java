/*
 * Created on Jul 13, 2004
 *
 */
package ServidorApresentacao.Action.student.schoolRegistration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class InquiryAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);     


        return mapping.findForward("viewQuestions");
    }
}