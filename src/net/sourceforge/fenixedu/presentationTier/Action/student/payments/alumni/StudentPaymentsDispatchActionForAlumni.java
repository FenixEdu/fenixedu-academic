package net.sourceforge.fenixedu.presentationTier.Action.student.payments.alumni;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "alumni", path = "/payments", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "showEvents", path = "/alumni/administrativeOfficeServices/payments/showEvents.jsp",
                tileProperties = @Tile(title = "private.alumni.academicservices.payments")),
        @Forward(name = "showEventDetails", path = "/alumni/administrativeOfficeServices/payments/showEventDetails.jsp",
                tileProperties = @Tile(title = "private.alumni.academicservices.payments")) })
public class StudentPaymentsDispatchActionForAlumni extends
        net.sourceforge.fenixedu.presentationTier.Action.student.payments.StudentPaymentsDispatchAction {
}