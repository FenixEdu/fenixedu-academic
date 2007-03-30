package net.sourceforge.fenixedu.presentationTier.Action.student.thesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.docs.thesis.StudentThesisIdentificationDocument;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;

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
            setupStudentTodo(request, thesis);
            return mapping.findForward("thesis-submit");
        }
        else {
            if (thesis.isConfirmed() || thesis.isEvaluated()) {
                return mapping.findForward("thesis-showState");
            }
            else {
                return mapping.findForward("thesis-showUnavailable");
            }
        }
    }

    private void setupStudentTodo(HttpServletRequest request, Thesis thesis) {
        request.setAttribute("todo", thesis.getStudentConditions());
    }

    public ActionForward changeThesisDetails(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("changeDetails", true);
        
        return prepareThesisSubmission(mapping, actionForm, request, response);
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

    public ActionForward viewDeclaration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        request.setAttribute("bean", new DeclarationBean(thesis));

        if (thesis.isWaitingConfirmation()) {
            return mapping.findForward("thesis-declaration");
        }
        else {
            return mapping.findForward("thesis-declaration-view");
        }
    }
    
    public ActionForward changeDeclaration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        boolean confirmation = request.getParameter("confirmReject") != null;
        if (confirmation) {
            executeService("RejectThesisDeclaration", thesis);
        }
        else {
            DeclarationBean bean = (DeclarationBean) getRenderedObject("declarationBean");

            boolean accepted = request.getParameter("accept") != null;
            if (accepted) {
                if (bean.getVisibility() != null) {
                    executeService("AcceptThesisDeclaration", thesis, bean.getVisibility(), bean.getAvailableAfter());
                }
                else {
                    if (bean.getVisibility() == null) {
                        addActionMessage("error", request, "error.student.thesis.declaration.visibility.required");
                    }
                    
                    return mapping.findForward("thesis-declaration");
                }
            }
            else {
                if (thesis.hasDissertation() || thesis.hasExtendedAbstract()) {
                    request.setAttribute("confirmRejectWithFiles", true);
                    return mapping.findForward("thesis-declaration");
                }
                else {
                    executeService("RejectThesisDeclaration", thesis);
                }
            }
        }
        
        return prepareThesisSubmission(mapping, actionForm, request, response);
    }
    
    public ActionForward prepareUploadDissertation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("fileBean", new ThesisFileBean());
        return mapping.findForward("thesis-upload-dissertation");
    }
    
    public ActionForward uploadDissertation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisFileBean bean = (ThesisFileBean) getRenderedObject();
        RenderUtils.invalidateViewState();
        
        if (bean != null && bean.getFile() != null) {
            executeService("CreateThesisDissertationFile", getThesis(request), bean.getFile(), bean.getSimpleFileName(), bean.getTitle(), bean.getSubTitle(), bean.getLanguage());
        }
        
        return prepareThesisSubmission(mapping, actionForm, request, response);
    }

    public ActionForward removeDissertation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        executeService("CreateThesisDissertationFile", getThesis(request), null, null, null, null, null);
        
        return prepareThesisSubmission(mapping, actionForm, request, response);
    }
    
    public ActionForward prepareUploadAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("fileBean", new ThesisFileBean());
        return mapping.findForward("thesis-upload-abstract");
    }

    public ActionForward uploadAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ThesisFileBean bean = (ThesisFileBean) getRenderedObject();
        RenderUtils.invalidateViewState();
        
        if (bean != null && bean.getFile() != null) {
            executeService("CreateThesisAbstractFile", getThesis(request), bean.getFile(), bean.getSimpleFileName(), null, null, null);
        }
        
        return prepareThesisSubmission(mapping, actionForm, request, response);
    }

    public ActionForward removeAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        executeService("CreateThesisAbstractFile", getThesis(request), null, null, null, null, null);
        
        return prepareThesisSubmission(mapping, actionForm, request, response);
    }

    public ActionForward downloadIdentificationSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        
        try {
            StudentThesisIdentificationDocument document = new StudentThesisIdentificationDocument(thesis);
            byte[] data = ReportsUtils.exportToPdf(document);
            
            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));

            response.getOutputStream().write(data);
            
            return null;
        } catch (JRException e) {
            addActionMessage("error", request, "student.thesis.generate.identification.failed");
            return prepareThesisSubmission(mapping, actionForm, request, response);
        }
    }

}
