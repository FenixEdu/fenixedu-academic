package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoTutor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

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

        IUserView userView = getUserView(request);

        Object[] args = { userView.getUtilizador() };
        List<InfoTutor> infoStudents = (List<InfoTutor>) ServiceUtils.executeService(userView, "ViewStudentsByTutor", args);

        if (!infoStudents.isEmpty()) {
            // order list by number
            Collections.sort(infoStudents, new BeanComparator("infoStudent.number"));
            request.setAttribute("studentsOfTutor", infoStudents);
        }
        return mapping.findForward("viewStudentsByTutor");
    }
}