package net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices.student;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "student", path = "/documentRequest", input = "/administrativeOfficeServicesSection.do", attribute = "documentRequestCreateForm", formBean = "documentRequestCreateForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "createDocumentRequests", path = "df.page.documentRequest.createDocumentRequests"),
		@Forward(name = "viewDocumentRequests", path = "df.page.documentRequest.viewDocumentRequests", redirect = true),
		@Forward(name = "createSuccess", path = "df.page.documentRequest.createSuccess"),
		@Forward(name = "viewDocumentRequestsToCreate", path = "df.page.documentRequest.viewDocumentRequestsToCreate"),
		@Forward(name = "chooseRegistration", path = "df.page.documentRequest.chooseRegistration"),
		@Forward(name = "prepareCancelAcademicServiceRequest", path = "df.page.documentRequest.prepareCancelAcademicServiceRequest"),
		@Forward(name = "cancelSuccess", path = "df.page.documentRequest.cancelSuccess"),
		@Forward(name = "viewDocumentRequest", path = "df.page.documentRequest.viewDocumentRequest") })
public class DocumentRequestDispatchActionForStudent extends net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices.DocumentRequestDispatchAction {
}