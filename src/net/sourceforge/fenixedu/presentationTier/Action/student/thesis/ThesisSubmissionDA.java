package net.sourceforge.fenixedu.presentationTier.Action.student.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ThesisSubmissionDA extends FenixDispatchAction {

    public Student getStudent(HttpServletRequest request) {
        return getUserView(request).getPerson().getStudent();
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("thesis", getThesis(request));

        return super.execute(mapping, actionForm, request, response);
    }

    public Thesis getThesis(HttpServletRequest request) {
        Thesis thesis = null;
        
        String idString = request.getParameter("thesisID");
        if (idString == null) {
            thesis = (Thesis) request.getAttribute("thesis");

            if (thesis == null) {
                Student student = getStudent(request);
                
                Enrolment enrolment = student.getDissertationEnrolment();
                if (enrolment != null) {
                    thesis = enrolment.getThesis();
                }
            }
        }
        else {
            thesis = RootDomainObject.getInstance().readThesisByOID(new Integer(idString));
        }
        
        return thesis;
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
            request.setAttribute("fileBean", new ThesisFileBean());
            return mapping.findForward("thesis-submit");
        }
        else {
            if (thesis.isConfirmed()) {
                return mapping.findForward("thesis-showState");
            }
            else {
                return mapping.findForward("thesis-showUnavailable");
            }
        }
        
    }

    public ActionForward editAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return editASCII(mapping, request, "thesis-edit-abstract");
    }
    
    public ActionForward editKeywords(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return editASCII(mapping, request, "thesis-edit-keywords");
    }
    
    public ActionForward editASCII(ActionMapping mapping, HttpServletRequest request, String forward) throws Exception {
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
        return mapping.findForward(forward);
    }

    public ActionForward uploadDissertation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisFileBean bean = (ThesisFileBean) getRenderedObject();
        RenderUtils.invalidateViewState();
        
        if (bean != null && bean.getFile() != null) {
            executeService("CreateThesisDissertationFile", getThesis(request), bean.getFile(), bean.getSimpleFileName());
        }
        
        return prepareThesisSubmission(mapping, actionForm, request, response);
    }

    public ActionForward removeDissertation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        executeService("CreateThesisDissertationFile", getThesis(request), null, null);
        
        return prepareThesisSubmission(mapping, actionForm, request, response);
    }
    
    public ActionForward uploadAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        executeService("CreateThesisAbstractFile", getThesis(request), null, null);
        
        return prepareThesisSubmission(mapping, actionForm, request, response);
    }

    public ActionForward removeAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisFileBean bean = (ThesisFileBean) getRenderedObject();
        RenderUtils.invalidateViewState();
        
        if (bean != null && bean.getFile() != null) {
            executeService("CreateThesisAbstractFile", getThesis(request), bean.getFile(), bean.getSimpleFileName());
        }
        
        return prepareThesisSubmission(mapping, actionForm, request, response);
    }
}
