package net.sourceforge.fenixedu.presentationTier.Action.teacher.credits;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PrepareTeacherCreditsSheet extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        Object args[] = { userView.getUtilizador() };
        InfoTeacher infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView,
                "ReadTeacherByUsername", args);
        request.setAttribute("infoTeacher", infoTeacher);

        args[0] = infoTeacher.getIdInternal();
        List infoCredits = (List) ServiceUtils.executeService(userView, "ReadTeacherCredits", args);

        request.setAttribute("infoCredits", infoCredits);

        BeanComparator descriptionComparator = new BeanComparator("infoExecutionPeriod.beginDate");
        Collections.sort(infoCredits, descriptionComparator);
        return mapping.findForward("successfull-prepare");
    }

}
