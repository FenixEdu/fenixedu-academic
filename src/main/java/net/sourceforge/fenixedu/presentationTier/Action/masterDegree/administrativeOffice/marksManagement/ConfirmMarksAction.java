package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement.ConfirmStudentsFinalEvaluation;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement.ReadStudentsFinalEvaluationForConfirmation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEnrolmentEvaluation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.security.Authenticate;

/**
 * 
 * @author Fernanda Quitério 10/07/2003
 * 
 */
public class ConfirmMarksAction extends FenixDispatchAction {

    public ActionForward prepareMarksConfirmation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String curricularCourseCode = MarksManagementDispatchAction.getFromRequest("courseId", request);
        MarksManagementDispatchAction.getFromRequest("objectCode", request);
        MarksManagementDispatchAction.getFromRequest("degreeId", request);

        // Get students final evaluation

        User userView = Authenticate.getUser();
        InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = null;
        try {
            infoSiteEnrolmentEvaluation = ReadStudentsFinalEvaluationForConfirmation.run(curricularCourseCode, null);
        } catch (NonExistingServiceException e) {
            sendErrors(request, "nonExisting", "message.masterDegree.notfound.students");
            return mapping.findForward("ShowMarksManagementMenu");
        } catch (ExistingServiceException e) {
            sendErrors(request, "existing", "message.masterDegree.evaluation.alreadyConfirmed");
            return mapping.findForward("ShowMarksManagementMenu");
        } catch (InvalidSituationServiceException e) {
            sendErrors(request, "invalidSituation", "error.masterDegree.studentsWithoutGrade");
            return mapping.findForward("ShowMarksManagementMenu");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(infoSiteEnrolmentEvaluation.getEnrolmentEvaluations(), new BeanComparator(
                "infoEnrolment.infoStudentCurricularPlan.infoStudent.number"));

        request.setAttribute("infoSiteEnrolmentEvaluation", infoSiteEnrolmentEvaluation);

        String forward = findForward(request, infoSiteEnrolmentEvaluation);
        return mapping.findForward(forward);

    }

    private String findForward(HttpServletRequest request, InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation) {
        String useCase = (String) getFromRequest("useCase", request);
        String forward = "MarksConfirmationMenu";
        if (useCase != null && useCase.equals("confirm")) {
            forward = "MarksConfirmation";
        } else if (useCase != null && useCase.equals("print")) {
            forward = "MarksPrint";
        }
        return forward;
    }

    private void sendErrors(HttpServletRequest request, String arg0, String arg1) {
        ActionErrors errors = new ActionErrors();
        errors.add(arg0, new ActionError(arg1));
        saveErrors(request, errors);
    }

    public ActionForward confirm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String curricularCourseCode = MarksManagementDispatchAction.getFromRequest("courseId", request);
        MarksManagementDispatchAction.getFromRequest("objectCode", request);
        MarksManagementDispatchAction.getFromRequest("degreeId", request);

        // set final evaluation to final state
        User userView = Authenticate.getUser();

        ConfirmStudentsFinalEvaluation.run(curricularCourseCode, null, userView);

        return mapping.findForward("ShowMarksManagementMenu");
    }

    private Object getFromRequest(String parameter, HttpServletRequest request) {
        Object parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = request.getAttribute(parameter);
        }
        return parameterString;
    }
}