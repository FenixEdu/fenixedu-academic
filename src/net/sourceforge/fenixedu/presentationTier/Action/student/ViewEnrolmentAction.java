package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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