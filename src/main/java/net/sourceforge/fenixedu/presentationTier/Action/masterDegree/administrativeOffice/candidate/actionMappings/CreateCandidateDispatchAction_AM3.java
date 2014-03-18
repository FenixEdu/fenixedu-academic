package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate.CreateCandidateDispatchAction;


@Mapping(path = "/createCandidateDispatchAction", module = "masterDegreeAdministrativeOffice", input = "/createCandidateDispatchAction.do?page=0&method=prepare", attribute = "createCandidateForm", formBean = "createCandidateForm")
@Forwards(value = {
	@Forward(name = "PrepareSuccess", path = "/createCandidate.jsp"),
	@Forward(name = "CreateSuccess", path = "/createCandidateSuccess.jsp")})
@Exceptions(value = {
	@ExceptionHandling(key = "resources.Action.exceptions.ExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class)})
public class CreateCandidateDispatchAction_AM3 extends CreateCandidateDispatchAction {

}
