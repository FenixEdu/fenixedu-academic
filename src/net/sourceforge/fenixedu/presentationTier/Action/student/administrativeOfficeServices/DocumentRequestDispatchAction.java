package net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest.DocumentRequestCreator;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class DocumentRequestDispatchAction extends FenixDispatchAction {

    public ActionForward chooseRegistration(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("registrations", getLoggedPerson(request).getStudent().getRegistrations());

        return mapping.findForward("chooseRegistration");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("documentRequestCreateBean", new DocumentRequestCreator(getRegistration(request, actionForm)));
        
	return mapping.findForward("createDocumentRequests");
    }

    private Registration getRegistration(final HttpServletRequest request, final ActionForm actionForm) {
        return rootDomainObject.readRegistrationByOID(getIntegerFromRequestOrForm(request,
                (DynaActionForm) actionForm, "registrationId"));
    }

    public ActionForward viewDocumentRequests(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("student", getLoggedPerson(request).getStudent());
        request.setAttribute("documentRequests", getDocumentRequest(request));
        return mapping.findForward("viewDocumentRequests");
    }

    private List<DocumentRequest> getDocumentRequest(final HttpServletRequest request) {

        final List<DocumentRequest> result = new ArrayList<DocumentRequest>();
        for (final Registration registration : getLoggedPerson(request).getStudent()
                .getRegistrationsSet()) {
            result.addAll(registration.getDocumentRequests());
        }
        return result;
    }

    public ActionForward viewDocumentRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("documentRequest", rootDomainObject
                .readAcademicServiceRequestByOID(getRequestParameterAsInteger(request,
                        "documentRequestId")));

        return mapping.findForward("viewDocumentRequest");
    }

    public ActionForward prepareCancelAcademicServiceRequest(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
            throws FenixFilterException, FenixServiceException {

        getAndSetAcademicServiceRequest(request);
        return mapping.findForward("prepareCancelAcademicServiceRequest");
    }

    public ActionForward cancelAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
        final String justification = ((DynaActionForm) actionForm).getString("justification");
        try {
            executeService("CancelAcademicServiceRequest", academicServiceRequest, justification);
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
                    .getLabelFormatterArgs()));
            return mapping.findForward("prepareCancelAcademicServiceRequest");
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
            return mapping.findForward("prepareCancelAcademicServiceRequest");
        }

        return mapping.findForward("cancelSuccess");
    }

    private AcademicServiceRequest getAndSetAcademicServiceRequest(final HttpServletRequest request) {
        final AcademicServiceRequest academicServiceRequest = rootDomainObject
                .readAcademicServiceRequestByOID(getRequestParameterAsInteger(request,
                        "academicServiceRequestId"));
        request.setAttribute("academicServiceRequest", academicServiceRequest);
        return academicServiceRequest;
    }

    public ActionForward prepareCreateDocumentRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("documentRequestCreateBean", new DocumentRequestCreator(getRegistration(
                request, actionForm)));

        return mapping.findForward("createDocumentRequests");
    }

    public ActionForward documentRequestTypeChoosedPostBack(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final DocumentRequestCreateBean requestCreateBean = (DocumentRequestCreateBean) RenderUtils
                .getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();

        setAdditionalInformationSchemaName(request, requestCreateBean);
        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        return mapping.findForward("createDocumentRequests");
    }

    private void setAdditionalInformationSchemaName(HttpServletRequest request,
            final DocumentRequestCreateBean requestCreateBean) {
        if (requestCreateBean.getChosenDocumentRequestType().getHasAdditionalInformation()) {
            request
                    .setAttribute("additionalInformationSchemaName", "DocumentRequestCreateBean."
                            + requestCreateBean.getChosenDocumentRequestType().name()
                            + ".AdditionalInformation");
        }
    }

    public ActionForward viewDocumentRequestToCreate(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final DocumentRequestCreateBean requestCreateBean = (DocumentRequestCreateBean) RenderUtils
                .getViewState().getMetaObject().getObject();

        setAdditionalInformationSchemaName(request, requestCreateBean);
        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        return mapping.findForward("viewDocumentRequestsToCreate");
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
        try {
            executeFactoryMethod(request);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return viewDocumentRequestToCreate(mapping, actionForm, request, response);
        }

        request.setAttribute("documentRequestCreateBean",
                ((DocumentRequestCreateBean) getRenderedObject()).getRegistration());

        return mapping.findForward("createSuccess");
    }

}
