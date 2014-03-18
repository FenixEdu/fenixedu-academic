package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide.CreateGuideDispatchAction;


@Mapping(path = "/createGuideDispatchAction", module = "masterDegreeAdministrativeOffice", input = "/createGuideDispatchAction.do?method=prepare&page=0", attribute = "createGuideForm", formBean = "createGuideForm")
@Forwards(value = {
	@Forward(name = "PrepareSuccess", path = "/guide/createGuide.jsp"),
	@Forward(name = "CreateCandidateGuide", path = "/guide/createGuideReady.jsp"),
	@Forward(name = "CreateStudentGuide", path = "/guide/chooseStudentGuide.jsp")})
@Exceptions(value = {
	@ExceptionHandling(key = "resources.Action.exceptions.ExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class),
	@ExceptionHandling(key = "resources.Action.exceptions.NoActiveStudentCurricularPlanActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NoActiveStudentCurricularPlanActionException.class)})
public class CreateGuideDispatchAction_AM1 extends CreateGuideDispatchAction {

}
