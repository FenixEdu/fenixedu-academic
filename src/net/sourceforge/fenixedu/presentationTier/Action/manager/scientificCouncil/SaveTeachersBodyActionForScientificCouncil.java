package net.sourceforge.fenixedu.presentationTier.Action.manager.scientificCouncil;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "scientificCouncil",
		path = "/saveTeachersBody",
		input = "/readTeacherInCharge.do",
		attribute = "masterDegreeCreditsForm",
		formBean = "masterDegreeCreditsForm",
		scope = "request")
@Forwards(value = { @Forward(name = "readCurricularCourse", path = "/masterDegreeCreditsManagement.do?method=prepareEdit") })
@Exceptions(value = {
		@ExceptionHandling(
				type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class,
				handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class,
				scope = "request"),
		@ExceptionHandling(
				type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException.class,
				key = "presentationTier.Action.exceptions.InvalidArgumentsActionException",
				handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
				scope = "request") })
public class SaveTeachersBodyActionForScientificCouncil extends
		net.sourceforge.fenixedu.presentationTier.Action.manager.SaveTeachersBodyAction {
}