package net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices.student;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "student", path = "/documentRequest", input = "/administrativeOfficeServicesSection.do",
        attribute = "documentRequestCreateForm", formBean = "documentRequestCreateForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "createDocumentRequests", path = "df.page.documentRequest.createDocumentRequests",
                tileProperties = @Tile(title = "private.student.view.academicservices.certificaterequest")),
        @Forward(name = "viewDocumentRequests", path = "df.page.documentRequest.viewDocumentRequests", tileProperties = @Tile(
                title = "private.student.view.academicservices.certificaterequest"), redirect = true),
        @Forward(name = "createSuccess", path = "df.page.documentRequest.createSuccess", tileProperties = @Tile(
                title = "private.student.view.academicservices.certificaterequest")),
        @Forward(name = "viewDocumentRequestsToCreate", path = "df.page.documentRequest.viewDocumentRequestsToCreate",
                tileProperties = @Tile(title = "private.student.view.academicservices.certificaterequest")),
        @Forward(name = "chooseRegistration", path = "df.page.documentRequest.chooseRegistration", tileProperties = @Tile(
                title = "private.student.view.academicservices.certificaterequest")),
        @Forward(name = "prepareCancelAcademicServiceRequest",
                path = "df.page.documentRequest.prepareCancelAcademicServiceRequest", tileProperties = @Tile(
                        title = "private.student.view.academicservices.certificaterequest")),
        @Forward(name = "cancelSuccess", path = "df.page.documentRequest.cancelSuccess", tileProperties = @Tile(
                title = "private.student.view.academicservices.certificaterequest")),
        @Forward(name = "viewDocumentRequest", path = "df.page.documentRequest.viewDocumentRequest", tileProperties = @Tile(
                title = "private.student.view.academicservices.certificaterequest")) })
public class DocumentRequestDispatchActionForStudent extends
        net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices.DocumentRequestDispatchAction {
}