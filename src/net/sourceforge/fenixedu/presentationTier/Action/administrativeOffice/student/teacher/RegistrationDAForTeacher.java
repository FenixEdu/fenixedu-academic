package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.teacher;

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

@Mapping(module = "teacher", path = "/registration", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "view-registration-curriculum", path = "view-registration-curriculum"),
		@Forward(name = "chooseCycleForViewRegistrationCurriculum", path = "chooseCycleForViewRegistrationCurriculum") })
public class RegistrationDAForTeacher extends net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.RegistrationDA {
}