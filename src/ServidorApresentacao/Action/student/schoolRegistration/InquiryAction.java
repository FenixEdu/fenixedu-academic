/*
 * Created on Jul 13, 2004
 *
 */
package ServidorApresentacao.Action.student.schoolRegistration;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class InquiryAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);        

        DynaActionForm inquiryForm = (DynaActionForm) form;
        HashMap answersMap = (HashMap) inquiryForm.get("answersMap");
        
        Object args[] = {userView,answersMap};
        ServiceUtils.executeService(userView, "WriteInquiryAnswers", args);

        return mapping.findForward("viewQuestions");
    }
}