package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.serviceRequests.documentRequests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestEditBean;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class DocumentRequestsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward showOperations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("showOperations");
    }

    public ActionForward viewNewDocumentRequests(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("viewNewDocumentRequests");
    }

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("prepareSearch");
    }

    public ActionForward search(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        DynaActionForm form = (DynaActionForm) actionForm;

        DocumentRequestType documentRequestType = (DocumentRequestType) getEnum(form,
                "documentRequestType", DocumentRequestType.class);

        AcademicServiceRequestSituationType requestSituationType = (AcademicServiceRequestSituationType) getEnum(
                form, "requestSituationType", AcademicServiceRequestSituationType.class);

        Boolean isUrgent = (Boolean) form.get("isUrgent");
        Student student = getStudent(form.getString("studentNumber"));

        request.setAttribute("documentRequestsResult", getAdministrativeOffice().searchDocumentsBy(
                documentRequestType, requestSituationType, isUrgent, student));

        return mapping.findForward("showDocumentRequests");
    }

    public ActionForward viewDocumentRequest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("documentRequest", getDocumentRequest(request));

        return mapping.findForward("viewDocumentRequest");
    }

    private Student getStudent(String studentNumberString) {
        try {
            return Student.readStudentByNumberAndDegreeType(Integer.valueOf(studentNumberString),
                    DegreeType.DEGREE);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public AdministrativeOffice getAdministrativeOffice() {
        return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

    private Enum getEnum(DynaActionForm form, String name, Class type) {
        return (form.get(name) == null || form.getString(name).length() == 0) ? null : Enum.valueOf(
                type, form.getString(name));
    }

    public ActionForward prepareEditDocumentRequest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final DocumentRequest documentRequest = getDocumentRequest(request);
        final DocumentRequestEditBean documentRequestEditBean = new DocumentRequestEditBean(
                documentRequest, getUserView(request).getPerson().getEmployee(), documentRequest
                        .getActiveSituation().getAcademicServiceRequestSituationType(), documentRequest
                        .getNumberOfPages());
        request.setAttribute("documentRequestEditBean", documentRequestEditBean);

        return mapping.findForward("editDocumentRequest");
    }

    public ActionForward editDocumentRequest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
        final DocumentRequestEditBean documentRequestEditBean = (DocumentRequestEditBean) RenderUtils
                .getViewState("documentRequestEdit").getMetaObject().getObject();

        try {
            ServiceManagerServiceFactory.executeService(getUserView(request), "EditDocumentRequest",
                    new Object[] { documentRequestEditBean });

            return viewDocumentRequest(mapping, form, request, response);

        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
                    .getLabelFormatterArgs()));
            request.setAttribute("documentRequestEditBean", documentRequestEditBean);

            return mapping.findForward("editDocumentRequest");
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
            request.setAttribute("documentRequestEditBean", documentRequestEditBean);

            return mapping.findForward("editDocumentRequest");
        }

    }

    private DocumentRequest getDocumentRequest(HttpServletRequest request) {
        return (DocumentRequest) rootDomainObject
                .readAcademicServiceRequestByOID(getRequestParameterAsInteger(request,
                        "documentRequestId"));
    }
    
}
