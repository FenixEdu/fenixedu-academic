package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.alumni.AlumniApplication.AlumniAcademicServicesApp;
import net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices.DocumentRequestDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AlumniAcademicServicesApp.class, path = "document-requests",
        titleKey = "link.academic.services.document.requirements")
@Mapping(module = "alumni", path = "/documentRequest", formBean = "documentRequestCreateForm")
@Forwards({
        @Forward(name = "createDocumentRequests",
                path = "/alumni/administrativeOfficeServices/documentRequests/createDocumentRequests.jsp"),
        @Forward(name = "viewDocumentRequests",
                path = "/alumni/administrativeOfficeServices/documentRequests/viewDocumentRequests.jsp"),
        @Forward(name = "createSuccess", path = "/alumni/administrativeOfficeServices/documentRequests/createSuccess.jsp"),
        @Forward(name = "viewDocumentRequestsToCreate",
                path = "/alumni/administrativeOfficeServices/documentRequests/viewDocumentRequestsToCreate.jsp"),
        @Forward(name = "chooseRegistration",
                path = "/alumni/administrativeOfficeServices/documentRequests/chooseRegistration.jsp"),
        @Forward(name = "prepareCancelAcademicServiceRequest",
                path = "/alumni/administrativeOfficeServices/documentRequests/prepareCancelAcademicServiceRequest.jsp"),
        @Forward(name = "cancelSuccess", path = "/alumni/administrativeOfficeServices/documentRequests/cancelSuccess.jsp"),
        @Forward(name = "viewDocumentRequest",
                path = "/alumni/administrativeOfficeServices/documentRequests/viewDocumentRequest.jsp") })
public class DocumentRequestDispatchActionForAlumni extends DocumentRequestDispatchAction {

    @StrutsFunctionality(app = AlumniAcademicServicesApp.class, path = "view-document-requests",
            titleKey = "link.academic.services.pending.requests")
    @Mapping(module = "alumni", path = "/viewDocumentRequests", formBean = "documentRequestCreateForm")
    public static class ViewDocumentRequestForAlumni extends DocumentRequestDispatchActionForAlumni {
        @Override
        @EntryPoint
        public ActionForward viewDocumentRequests(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
                HttpServletResponse response) {
            return super.viewDocumentRequests(mapping, actionForm, request, response);
        }
    }

}