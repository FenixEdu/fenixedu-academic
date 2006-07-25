package net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class DocumentRequestDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        final Person person = SessionUtils.getUserView(request).getPerson();
        final Student student = person.getStudentByUsername();
        
        if (student.getStudentCurricularPlansCount() > 1) {
            request.setAttribute("student", student);
            request.setAttribute("studentCurricularPlans", student.getStudentCurricularPlans());
            
            if (student.getActiveStudentCurricularPlan().getEnrolmentsExecutionYears().isEmpty()) {
                final ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("message.no.enrolments", new ActionMessage("message.no.enrolments"));
                saveMessages(request, actionMessages);
            } else {
                request.setAttribute("executionYears", student.getActiveStudentCurricularPlan().getEnrolmentsExecutionYears());    
            }
        } else if (!student.hasAnyStudentCurricularPlans()) {
            final ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error.student.curricularPlan.nonExistent", new ActionMessage("error.student.curricularPlan.nonExistent"));
            saveMessages(request, actionMessages);
        }

        return mapping.findForward("createDocumentRequests");
    }
    
    public ActionForward onSCPChange(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        final Person person = SessionUtils.getUserView(request).getPerson();
        final Student student = person.getStudentByUsername();
        request.setAttribute("student", student);
        request.setAttribute("studentCurricularPlans", student.getStudentCurricularPlans());

        final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        final Integer scpId = (Integer) dynaActionForm.get("scpId");
        final StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(scpId);
        
        if (studentCurricularPlan.getEnrolmentsExecutionYears().isEmpty()) {
            final ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("message.no.enrolments", new ActionMessage("message.no.enrolments"));
            saveMessages(request, actionMessages);
        } else {
            request.setAttribute("executionYears", studentCurricularPlan.getEnrolmentsExecutionYears());    
        }
        
        return mapping.findForward("createDocumentRequests");
    }
    
    public ActionForward view(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        final Person person = SessionUtils.getUserView(request).getPerson();
        final Student student = person.getStudentByUsername();
        request.setAttribute("student", student);
        
        final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        final StudentCurricularPlan studentCurricularPlan;
        if (student.getStudentCurricularPlansCount() > 1) {
            request.setAttribute("studentCurricularPlans", student.getStudentCurricularPlans());
            
            final Integer scpId = (Integer) dynaActionForm.get("scpId");
            studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(scpId);
        } else {
            studentCurricularPlan = student.getActiveStudentCurricularPlan();
        }
        request.setAttribute("executionYears", studentCurricularPlan.getEnrolmentsExecutionYears());

        final List<String> chosenDocumentRequestTypes = Arrays.asList((String[])dynaActionForm.get("chosenDocumentRequestTypes"));
        if (chosenDocumentRequestTypes.isEmpty()) {
            final ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error.choose.one.type.of.document", new ActionMessage("error.choose.one.type.of.document"));
            saveMessages(request, actionMessages);
            
            return mapping.findForward("createDocumentRequests");
        }
        
        final DocumentPurposeType chosenDocumentPurposeType;
        if (!StringUtils.isEmpty(dynaActionForm.getString("chosenDocumentPurposeType"))) {
            chosenDocumentPurposeType = DocumentPurposeType.valueOf(dynaActionForm.getString("chosenDocumentPurposeType")); 
        } else {
            chosenDocumentPurposeType = null;
        }

        final String otherPurpose = dynaActionForm.getString("otherPurpose");
        if (chosenDocumentPurposeType == DocumentPurposeType.OTHER) {
            if (otherPurpose == null) {
                final ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("error.fill.notes", new ActionMessage("error.fill.notes"));
                saveMessages(request, actionMessages);
                
                return mapping.findForward("createDocumentRequests");
            }
        } else if (chosenDocumentPurposeType != null) {
            if (otherPurpose != null) {
                final ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("error.only.one.purpose", new ActionMessage("error.only.one.purpose"));
                saveMessages(request, actionMessages);
                
                return mapping.findForward("createDocumentRequests");
            }
        }
        
        final String notes = dynaActionForm.getString("notes");
        final Boolean urgentRequest = Boolean.valueOf(dynaActionForm.getString("urgentRequest"));

        final List<DocumentRequestCreateBean> documentRequestCreateBeans = new ArrayList<DocumentRequestCreateBean>();
        for (final String chosenDocumentRequestType : chosenDocumentRequestTypes) {
            final DocumentRequestType documentRequestType = DocumentRequestType.valueOf(chosenDocumentRequestType);
            
            final DocumentRequestCreateBean documentRequestCreateBean = buildDocumentRequestCreateBean(
                    dynaActionForm, studentCurricularPlan, chosenDocumentPurposeType, 
                    otherPurpose, notes, urgentRequest, documentRequestType);

            documentRequestCreateBeans.add(documentRequestCreateBean);
        }

        request.setAttribute("documentRequestCreateBeans", documentRequestCreateBeans);
        
        return mapping.findForward("viewDocumentRequestsToCreate");
    }
    
    private DocumentRequestCreateBean buildDocumentRequestCreateBean(final DynaActionForm dynaActionForm, 
            final StudentCurricularPlan studentCurricularPlan, final DocumentPurposeType chosenDocumentPurposeType, 
            final String otherPurpose, final String notes, final Boolean urgentRequest, 
            final DocumentRequestType documentRequestType) {
        
        final DocumentRequestCreateBean documentRequestCreateBean = new DocumentRequestCreateBean();
        
        documentRequestCreateBean.setStudentCurricularPlan(studentCurricularPlan);
        documentRequestCreateBean.setChosenDocumentRequestType(documentRequestType);
        documentRequestCreateBean.setChosenDocumentPurposeType(chosenDocumentPurposeType);
        documentRequestCreateBean.setOtherPurpose(otherPurpose);
        documentRequestCreateBean.setNotes(notes);
        documentRequestCreateBean.setUrgentRequest(urgentRequest);
        
        final Boolean average;
        final Boolean detailed;
        final ExecutionYear executionYear;
        if (documentRequestType == DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE) {
            average = null;
            detailed = null;

            Integer executionYearId = (Integer) dynaActionForm.get("schoolRegistrationExecutionYearId");
            executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);
        } else if (documentRequestType == DocumentRequestType.ENROLMENT_CERTIFICATE) {
            average = null;
            detailed = Boolean.valueOf(dynaActionForm.getString("enrolmentDetailed"));
            
            Integer executionYearId = (Integer) dynaActionForm.get("enrolmentExecutionYearId");
            executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);
        } else if (documentRequestType == DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE) {
            average = Boolean.valueOf(dynaActionForm.getString("degreeFinalizationAverage"));
            detailed = Boolean.valueOf(dynaActionForm.getString("degreeFinalizationDetailed"));
            executionYear = null;
        } else {
            average = null;
            executionYear = null;
            detailed = null;
        }
        documentRequestCreateBean.setAverage(average);
        documentRequestCreateBean.setDetailed(detailed);
        documentRequestCreateBean.setExecutionYear(executionYear);
        
        return documentRequestCreateBean;
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        final List<DocumentRequestCreateBean> toBeCreated = new ArrayList<DocumentRequestCreateBean>();
        
        final List<DocumentRequestCreateBean> documentRequestCreateBeans = (List<DocumentRequestCreateBean>) RenderUtils.getViewState().getMetaObject().getObject();
        for (final DocumentRequestCreateBean documentRequestCreateBean : documentRequestCreateBeans) {
            if (documentRequestCreateBean.getToBeCreated()) {
                toBeCreated.add(documentRequestCreateBean);
            }
        }
        
        final List<String> messages;
        final Object[] args = { toBeCreated };
        try {
            messages = (List<String>) executeService(request, "CreateDocumentRequests", args);
        } catch (FenixFilterException e) {
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            return mapping.getInputForward();
        }
        
        for (final String message : messages) {
            final ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(message, new ActionMessage(message));
            saveMessages(request, actionMessages);
        }
        
        return mapping.findForward("createSuccess");
    }

}
