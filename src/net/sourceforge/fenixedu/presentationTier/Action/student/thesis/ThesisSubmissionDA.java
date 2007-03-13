package net.sourceforge.fenixedu.presentationTier.Action.student.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ThesisSubmissionDA extends FenixDispatchAction {

    public Student getStudent(HttpServletRequest request) {
        return getUserView(request).getPerson().getStudent();
    }

    public ActionForward prepareThesisSubmission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Student student = getStudent(request);
        
        Enrolment enrolment = student.getDissertationEnrolment();
        if (enrolment == null) {
            return mapping.findForward("thesis-notFound");
        }
        
        Thesis thesis = enrolment.getThesis();
        if (thesis == null) {
            return mapping.findForward("thesis-notFound");
        }
        
        request.setAttribute("thesis", thesis);
        if (thesis.isWaitingConfirmation()) {
            return mapping.findForward("thesis-submit");
        }
        else {
            return mapping.findForward("thesis-showState");
        }
        
    }
    
}
