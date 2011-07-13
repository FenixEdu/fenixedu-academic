package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.gep;

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

@Mapping(module = "gep", path = "/student", attribute = "studentManagementForm", formBean = "studentManagementForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "viewStudentDetails", path = "/internationalRelatOffice/viewStudentDetails.jsp"),
		@Forward(name = "viewRegistrationDetails", path = "/internationalRelatOffice/viewRegistrationDetails.jsp"),
		@Forward(name = "viewPersonalData", path = "/internationalRelatOffice/viewPersonalData.jsp") })
public class StudentDAForGep extends net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.StudentDA {
}