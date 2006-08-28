package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Fernanda Quitério 30/06/2003
 *  
 */

public class ShowMarkDispatchAction extends FenixDispatchAction {

    public ActionForward prepareShowMark(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();

        String curricularCourseId = MarksManagementDispatchAction.getFromRequest("courseId", request);
        MarksManagementDispatchAction.getFromRequest("objectCode", request);
        MarksManagementDispatchAction.getFromRequest("degreeId", request);
        // Get students List
        IUserView userView = SessionUtils.getUserView(request);
        Object args[] = { userView, Integer.valueOf(curricularCourseId), null };
        List listEnrolmentEvaluation = null;
        try {
            listEnrolmentEvaluation = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentMarksListByCurricularCourse", args);
        } catch (NotAuthorizedException e) {
            return mapping.findForward("NotAuthorized");
        } catch (NonExistingServiceException e) {
            errors.add("nonExisting", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);
            return mapping.findForward("NoStudents");
        }
        if (listEnrolmentEvaluation.size() == 0) {
            errors.add("StudentNotEnroled", new ActionError("error.students.Mark.NotAvailable"));
            saveErrors(request, errors);
            return mapping.findForward("NoStudents");
        }

        request.setAttribute("showMarks", "showMarks");
        request.setAttribute("studentList", listEnrolmentEvaluation);

        return mapping.findForward("displayStudentList");
    }
}