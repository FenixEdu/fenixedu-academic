package net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices.alumni;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(
		module = "alumni",
		path = "/documentRequest",
		input = "/administrativeOfficeServicesSection.do",
		attribute = "documentRequestCreateForm",
		formBean = "documentRequestCreateForm",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(
				name = "createDocumentRequests",
				path = "/alumni/administrativeOfficeServices/documentRequests/createDocumentRequests.jsp",
				tileProperties = @Tile(title = "private.alumni.academicservices.certificaterequests")),
		@Forward(
				name = "viewDocumentRequests",
				path = "/alumni/administrativeOfficeServices/documentRequests/viewDocumentRequests.jsp",
				tileProperties = @Tile(title = "private.alumni.academicservices.viewrequests"),
				redirect = true),
		@Forward(
				name = "createSuccess",
				path = "/alumni/administrativeOfficeServices/documentRequests/createSuccess.jsp",
				tileProperties = @Tile(title = "private.alumni.academicservices.certificaterequests")),
		@Forward(
				name = "viewDocumentRequestsToCreate",
				path = "/alumni/administrativeOfficeServices/documentRequests/viewDocumentRequestsToCreate.jsp",
				tileProperties = @Tile(title = "private.alumni.academicservices.certificaterequests")),
		@Forward(
				name = "chooseRegistration",
				path = "/alumni/administrativeOfficeServices/documentRequests/chooseRegistration.jsp",
				tileProperties = @Tile(title = "private.alumni.academicservices.certificaterequests")),
		@Forward(
				name = "prepareCancelAcademicServiceRequest",
				path = "/alumni/administrativeOfficeServices/documentRequests/prepareCancelAcademicServiceRequest.jsp"),
		@Forward(name = "cancelSuccess", path = "/alumni/administrativeOfficeServices/documentRequests/cancelSuccess.jsp"),
		@Forward(
				name = "viewDocumentRequest",
				path = "/alumni/administrativeOfficeServices/documentRequests/viewDocumentRequest.jsp",
				tileProperties = @Tile(title = "private.alumni.academicservices.viewrequests")) })
public class DocumentRequestDispatchActionForAlumni extends
		net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices.DocumentRequestDispatchAction {
}