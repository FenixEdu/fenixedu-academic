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
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
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

    private static Registration student;
    
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        student = SessionUtils.getUserView(request).getPerson().getStudentByUsername();

        if (!student.getPayedTuition()) {
            addActionMessage(request, "error.message.tuitionNotPayed");
        } else {
            if (!student.hasAnyStudentCurricularPlans()) {
                addActionMessage(request, "error.student.curricularPlan.nonExistent");
            } else {
                request.setAttribute("student", student);

                if (student.getStudentCurricularPlansCount() > 1) {
                    request.setAttribute("studentCurricularPlans", student.getStudentCurricularPlans());
                }

                getAndSetExecutionYears(request, student.getActiveStudentCurricularPlan());
            }
        }

        return mapping.findForward("createDocumentRequests");
    }

    private void getAndSetExecutionYears(HttpServletRequest request, StudentCurricularPlan studentCurricularPlan) {
        if (studentCurricularPlan.getEnrolmentsExecutionYears().isEmpty()) {
            addActionMessage(request, "message.no.enrolments");
        } else {
            request.setAttribute("executionYears", studentCurricularPlan.getEnrolmentsExecutionYears());
        }
    }

    public ActionForward onSCPChange(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        getAndSetStudent(request);
        getAndSetExecutionYears(request, getAndSetStudentCurricularPlans(request, (DynaActionForm) actionForm));

        return mapping.findForward("createDocumentRequests");
    }

    private void getAndSetStudent(HttpServletRequest request) {
        student = SessionUtils.getUserView(request).getPerson().getStudentByUsername();
        request.setAttribute("student", student);
    }

    private StudentCurricularPlan getAndSetStudentCurricularPlans(HttpServletRequest request, final DynaActionForm dynaActionForm) {
        final StudentCurricularPlan studentCurricularPlan;
        if (student.getStudentCurricularPlansCount() > 1) {
            request.setAttribute("studentCurricularPlans", student.getStudentCurricularPlans());

            studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID((Integer) dynaActionForm.get("scpId"));
        } else {
            studentCurricularPlan = student.getActiveStudentCurricularPlan();
        }
        return studentCurricularPlan;
    }

    public ActionForward viewDocumentRequestsToCreate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        getAndSetStudent(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        
        getAndSetExecutionYears(request, getAndSetStudentCurricularPlans(request, dynaActionForm));

        final List<String> chosenDocumentRequestTypes = getAndSetChosenDocumentRequestTypes(request, dynaActionForm);
        final DocumentPurposeType chosenDocumentPurposeType = getAndSetChoseDocumentPurposeType(request, dynaActionForm);
        final String otherPurpose = getAndSetOtherPurpose(request, dynaActionForm, chosenDocumentPurposeType);
        if (hasActionMessage(request)) {
            return mapping.findForward("createDocumentRequests");
        }

        warningsToReport.clear();
        getAndSetDocumentRequestCreateBeans(request, dynaActionForm, getAndSetStudentCurricularPlans(request, dynaActionForm), chosenDocumentRequestTypes, chosenDocumentPurposeType, otherPurpose, dynaActionForm.getString("notes"), Boolean.valueOf(dynaActionForm.getString("urgentRequest")));

        return mapping.findForward("viewDocumentRequestsToCreate");
    }

    private List<String> getAndSetChosenDocumentRequestTypes(HttpServletRequest request, final DynaActionForm dynaActionForm) {
        final List<String> chosenDocumentRequestTypes = Arrays.asList((String[]) dynaActionForm.get("chosenDocumentRequestTypes"));
        if (chosenDocumentRequestTypes.isEmpty()) {
            addActionMessage(request, "error.choose.one.type.of.document");
        }
        request.setAttribute("chosenDocumentRequestTypes", chosenDocumentRequestTypes);
        return chosenDocumentRequestTypes;
    }

    private DocumentPurposeType getAndSetChoseDocumentPurposeType(HttpServletRequest request, final DynaActionForm dynaActionForm) {
        final DocumentPurposeType chosenDocumentPurposeType = (!StringUtils.isEmpty(dynaActionForm.getString("chosenDocumentPurposeType"))) ? DocumentPurposeType.valueOf(dynaActionForm.getString("chosenDocumentPurposeType")) : null;
        request.setAttribute("chosenDocumentPurposeType", chosenDocumentPurposeType);
        return chosenDocumentPurposeType;
    }

    private String getAndSetOtherPurpose(HttpServletRequest request, final DynaActionForm dynaActionForm, final DocumentPurposeType chosenDocumentPurposeType) {
        final String otherPurpose = dynaActionForm.getString("otherPurpose");
        if (chosenDocumentPurposeType == DocumentPurposeType.OTHER) {
            if (otherPurpose == null) {
                addActionMessage(request, "error.fill.notes");
            }
        } else if (chosenDocumentPurposeType != null) {
            if (!StringUtils.isEmpty(otherPurpose)) {
                addActionMessage(request, "error.only.one.purpose");
            }
        }
        request.setAttribute("otherPurpose", otherPurpose);
        return otherPurpose;
    }

    private static final List<String> warningsToReport = new ArrayList<String>();
    
    private void getAndSetDocumentRequestCreateBeans(HttpServletRequest request, final DynaActionForm dynaActionForm, StudentCurricularPlan studentCurricularPlan, final List<String> chosenDocumentRequestTypes, final DocumentPurposeType chosenDocumentPurposeType, final String otherPurpose, final String notes, final Boolean urgentRequest) {
        final List<DocumentRequestCreateBean> documentRequestCreateBeans = new ArrayList<DocumentRequestCreateBean>();
        
        for (final String chosenDocumentRequestType : chosenDocumentRequestTypes) {
            final DocumentRequestType documentRequestType = DocumentRequestType.valueOf(chosenDocumentRequestType);

            final DocumentRequestCreateBean documentRequestCreateBean = buildDocumentRequestCreateBean(dynaActionForm, studentCurricularPlan, chosenDocumentPurposeType, otherPurpose, notes, urgentRequest, documentRequestType);

            documentRequestCreateBeans.add(documentRequestCreateBean);
        }

        request.setAttribute("warningsToReport", warningsToReport);
        request.setAttribute("documentRequestCreateBeans", documentRequestCreateBeans);
    }

    private DocumentRequestCreateBean buildDocumentRequestCreateBean(
            final DynaActionForm dynaActionForm, final StudentCurricularPlan studentCurricularPlan,
            final DocumentPurposeType chosenDocumentPurposeType, final String otherPurpose,
            final String notes, final Boolean urgentRequest,
            final DocumentRequestType documentRequestType) {

        final DocumentRequestCreateBean documentRequestCreateBean = new DocumentRequestCreateBean();

        documentRequestCreateBean.setToBeCreated(Boolean.TRUE);
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
        
        if (documentRequestCreateBean.hasWarningsToReport()) {
            warningsToReport.addAll(documentRequestCreateBean.getWarningsToReport());
        }

        return documentRequestCreateBean;
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        final Object[] args = { getConfirmedDocumentRequestCreateBeans() };
        final List<String> messages = (List<String>) executeService(request, "CreateDocumentRequests", args);

        for (final String message : messages) {
            final ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(message, new ActionMessage(message));
            saveMessages(request, actionMessages);
        }

        return mapping.findForward("createSuccess");
    }

    private List<DocumentRequestCreateBean> getConfirmedDocumentRequestCreateBeans() {
        final List<DocumentRequestCreateBean> result = new ArrayList<DocumentRequestCreateBean>();

        for (final DocumentRequestCreateBean documentRequestCreateBean : (List<DocumentRequestCreateBean>) RenderUtils.getViewState().getMetaObject().getObject()) {
            if (documentRequestCreateBean.getToBeCreated()) {
                result.add(documentRequestCreateBean);
            }
        }
        return result;
    }

    public ActionForward viewDocumentRequests(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        getAndSetStudent(request);
        request.setAttribute("documentRequests", student.getDocumentRequests());

        return mapping.findForward("viewDocumentRequests");
    }

    public ActionForward viewDocumentRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("documentRequest", rootDomainObject.readAcademicServiceRequestByOID(getRequestParameterAsInteger(request, "documentRequestId")));

        return mapping.findForward("viewDocumentRequest");
    }

}
