package ServidorApresentacao.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author tfc130
 *  
 */
public class ViewEnrolmentAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        HttpSession session = request.getSession(false);

        Object argsReadShiftEnrolment[] = { (InfoStudent) session.getAttribute("infoStudent") };

        InfoShiftEnrolment iSE = (InfoShiftEnrolment) ServiceUtils.executeService(userView,
                "ReadShiftEnrolment", argsReadShiftEnrolment);

        session.removeAttribute("infoShiftEnrolment");
        session.removeAttribute("index");
        if (iSE != null) {
            if (iSE.getInfoEnrolmentWithOutShift() != null
                    && iSE.getInfoEnrolmentWithOutShift().isEmpty())
                iSE.setInfoEnrolmentWithOutShift(null);
            session.setAttribute("infoShiftEnrolment", iSE);
        }

        return mapping.findForward("sucess");

    }

}