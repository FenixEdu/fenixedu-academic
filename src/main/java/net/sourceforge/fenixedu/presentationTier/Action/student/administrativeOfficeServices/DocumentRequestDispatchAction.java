package net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.factoryExecutors.DocumentRequestCreator;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = StudentAcademicOfficeServices.class, path = "create-document-request",
        titleKey = "documents.requirement")
@Mapping(module = "student", path = "/documentRequest", formBean = "documentRequestCreateForm")
@Forwards(value = {
        @Forward(name = "createDocumentRequests",
                path = "/student/administrativeOfficeServices/documentRequest/createDocumentRequests.jsp"),
        @Forward(name = "createSuccess", path = "/student/administrativeOfficeServices/documentRequest/createSuccess.jsp"),
        @Forward(name = "viewDocumentRequestsToCreate",
                path = "/student/administrativeOfficeServices/documentRequest/viewDocumentRequestsToCreate.jsp"),
        @Forward(name = "chooseRegistration",
                path = "/student/administrativeOfficeServices/documentRequest/chooseRegistration.jsp") })
public class DocumentRequestDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward chooseRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("registrations", getLoggedPerson(request).getStudent().getRegistrationsSet());

        return mapping.findForward("chooseRegistration");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("documentRequestCreateBean", new DocumentRequestCreator(getRegistration(request, actionForm)));

        return mapping.findForward("createDocumentRequests");
    }

    private Registration getRegistration(final HttpServletRequest request, final ActionForm actionForm) {
        String registrationId = getStringFromRequest(request, "registrationId");
        if (registrationId == null) {
            registrationId = ((DynaActionForm) actionForm).getString("registrationId");
        }
        return FenixFramework.getDomainObject(registrationId);
    }

    public ActionForward prepareCreateDocumentRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("documentRequestCreateBean", new DocumentRequestCreator(getRegistration(request, actionForm)));

        return mapping.findForward("createDocumentRequests");
    }

    public ActionForward documentRequestTypeInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DocumentRequestCreateBean requestCreateBean =
                (DocumentRequestCreateBean) RenderUtils.getViewState().getMetaObject().getObject();

        setAdditionalInformationSchemaName(request, requestCreateBean);
        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        return mapping.findForward("createDocumentRequests");
    }

    public ActionForward documentRequestTypeChosenPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final DocumentRequestCreateBean requestCreateBean =
                (DocumentRequestCreateBean) RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();

        setAdditionalInformationSchemaName(request, requestCreateBean);
        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        return mapping.findForward("createDocumentRequests");
    }

    private void setAdditionalInformationSchemaName(HttpServletRequest request, final DocumentRequestCreateBean requestCreateBean) {
        if (requestCreateBean.getHasAdditionalInformation()) {
            request.setAttribute("additionalInformationSchemaName", "DocumentRequestCreateBean."
                    + requestCreateBean.getChosenDocumentRequestType().name() + ".AdditionalInformation");
        }
    }

    public ActionForward viewDocumentRequestToCreate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DocumentRequestCreateBean requestCreateBean =
                (DocumentRequestCreateBean) RenderUtils.getViewState().getMetaObject().getObject();

        setAdditionalInformationSchemaName(request, requestCreateBean);
        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        return mapping.findForward("viewDocumentRequestsToCreate");
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeFactoryMethod();
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return viewDocumentRequestToCreate(mapping, actionForm, request, response);
        }

        request.setAttribute("documentRequestCreateBean", ((DocumentRequestCreateBean) getRenderedObject()).getRegistration());

        return mapping.findForward("createSuccess");
    }

}
