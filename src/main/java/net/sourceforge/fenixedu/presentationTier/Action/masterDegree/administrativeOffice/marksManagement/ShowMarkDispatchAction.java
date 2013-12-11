package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement.ReadStudentMarksListByCurricularCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.security.Authenticate;

/**
 * @author Fernanda Quitério 30/06/2003
 * 
 */

public class ShowMarkDispatchAction extends FenixDispatchAction {

    public ActionForward prepareShowMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();

        String curricularCourseId = MarksManagementDispatchAction.getFromRequest("courseId", request);
        MarksManagementDispatchAction.getFromRequest("objectCode", request);
        MarksManagementDispatchAction.getFromRequest("degreeId", request);
        // Get students List
        User userView = Authenticate.getUser();
        List listEnrolmentEvaluation = null;
        try {
            listEnrolmentEvaluation =
                    ReadStudentMarksListByCurricularCourse.runReadStudentMarksListByCurricularCourse(userView,
                            curricularCourseId, null);
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