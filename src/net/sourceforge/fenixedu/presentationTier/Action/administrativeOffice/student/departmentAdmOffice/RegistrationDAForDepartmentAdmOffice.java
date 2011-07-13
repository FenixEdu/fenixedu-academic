package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.departmentAdmOffice;

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

@Mapping(module = "departmentAdmOffice", path = "/registration", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "view-registration-curriculum", path = "/student/curriculum/viewRegistrationCurriculum.jsp"),
		@Forward(name = "chooseCycleForViewRegistrationCurriculum", path = "/student/curriculum/chooseCycleForViewRegistrationCurriculum.jsp") })
public class RegistrationDAForDepartmentAdmOffice extends net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.RegistrationDA {
}