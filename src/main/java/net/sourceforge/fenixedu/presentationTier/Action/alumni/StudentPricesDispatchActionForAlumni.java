package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import net.sourceforge.fenixedu.presentationTier.Action.alumni.AlumniApplication.AlumniAcademicServicesApp;
import net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices.StudentPricesAction;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AlumniAcademicServicesApp.class, path = "prices", titleKey = "link.academic.services.prices")
@Mapping(module = "alumni", path = "/prices")
@Forwards(value = { @Forward(name = "viewPrices", path = "/alumni/administrativeOfficeServices/prices/viewPrices.jsp") })
public class StudentPricesDispatchActionForAlumni extends StudentPricesAction {
}