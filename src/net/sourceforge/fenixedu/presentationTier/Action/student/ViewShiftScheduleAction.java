package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author tfc130
 *  
 */
public class ViewShiftScheduleAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaValidatorForm enrolForm = (DynaValidatorForm) form;
        String shiftName = (String) enrolForm.get("shiftName");

        HttpSession session = request.getSession(false);

        InfoShift infoShift = new InfoShift(shiftName, null, null, null);

        IUserView userView = SessionUtils.getUserView(request);

        Object argsReadStudentLessons[] = { infoShift };

        List lessons = (ArrayList) ServiceUtils.executeService(userView, "ReadShiftLessons",
                argsReadStudentLessons);

        if (lessons != null) {
            session.setAttribute(SessionConstants.LESSON_LIST_ATT, lessons);
        }

        return mapping.findForward("sucess");

    }

}