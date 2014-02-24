package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import net.sourceforge.fenixedu.presentationTier.Action.alumni.AlumniApplication.AlumniAcademicServicesApp;
import net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices.DocumentRequestDispatchAction;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AlumniAcademicServicesApp.class, path = "create-document-request",
        titleKey = "link.academic.services.document.requirements")
@Mapping(module = "alumni", path = "/documentRequest", formBean = "documentRequestCreateForm")
@Forwards({
        @Forward(name = "createDocumentRequests",
                path = "/alumni/administrativeOfficeServices/documentRequests/createDocumentRequests.jsp"),
        @Forward(name = "createSuccess", path = "/alumni/administrativeOfficeServices/documentRequests/createSuccess.jsp"),
        @Forward(name = "viewDocumentRequestsToCreate",
                path = "/alumni/administrativeOfficeServices/documentRequests/viewDocumentRequestsToCreate.jsp"),
        @Forward(name = "chooseRegistration",
                path = "/alumni/administrativeOfficeServices/documentRequests/chooseRegistration.jsp") })
public class DocumentRequestDispatchActionForAlumni extends DocumentRequestDispatchAction {

}