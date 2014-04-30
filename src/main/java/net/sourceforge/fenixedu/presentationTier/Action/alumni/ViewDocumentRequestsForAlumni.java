package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import net.sourceforge.fenixedu.presentationTier.Action.alumni.AlumniApplication.AlumniAcademicServicesApp;
import net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices.ViewDocumentRequestsDA;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AlumniAcademicServicesApp.class, path = "view-document-requests",
        titleKey = "link.academic.services.pending.requests")
@Mapping(module = "alumni", path = "/viewDocumentRequests", formBean = "documentRequestCreateForm")
@Forwards({
        @Forward(name = "viewDocumentRequests",
                path = "/alumni/administrativeOfficeServices/documentRequests/viewDocumentRequests.jsp"),
        @Forward(name = "prepareCancelAcademicServiceRequest",
                path = "/alumni/administrativeOfficeServices/documentRequests/prepareCancelAcademicServiceRequest.jsp"),
        @Forward(name = "cancelSuccess", path = "/alumni/administrativeOfficeServices/documentRequests/cancelSuccess.jsp"),
        @Forward(name = "viewDocumentRequest",
                path = "/alumni/administrativeOfficeServices/documentRequests/viewDocumentRequest.jsp") })
public class ViewDocumentRequestsForAlumni extends ViewDocumentRequestsDA {
}
