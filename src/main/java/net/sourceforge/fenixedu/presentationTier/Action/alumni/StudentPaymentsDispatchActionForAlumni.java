package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import net.sourceforge.fenixedu.presentationTier.Action.alumni.AlumniApplication.AlumniAcademicServicesApp;
import net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices.StudentPaymentsDispatchAction;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AlumniAcademicServicesApp.class, path = "payments", titleKey = "link.academic.services.payments")
@Mapping(module = "alumni", path = "/payments")
@Forwards({ @Forward(name = "showEvents", path = "/alumni/administrativeOfficeServices/payments/showEvents.jsp"),
        @Forward(name = "showEventDetails", path = "/alumni/administrativeOfficeServices/payments/showEventDetails.jsp") })
public class StudentPaymentsDispatchActionForAlumni extends StudentPaymentsDispatchAction {
}