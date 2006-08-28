package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author joaosa and rmalo
 * 
 */
public class ViewStudentsByTutorDispatchAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);

        Object[] args = { userView.getUtilizador() };
        List infoStudents = null;

        infoStudents = (List) ServiceUtils.executeService(userView, "ViewStudentsByTutor", args);

        if (!infoStudents.isEmpty()) {
            // order list by number
            Collections.sort(infoStudents, new BeanComparator("infoStudent.number"));
            request.setAttribute("studentsOfTutor", infoStudents);
        }
        return mapping.findForward("viewStudentsByTutor");
    }
}