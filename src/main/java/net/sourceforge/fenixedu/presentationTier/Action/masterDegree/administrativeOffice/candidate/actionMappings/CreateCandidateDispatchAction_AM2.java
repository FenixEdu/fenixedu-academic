package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate.CreateCandidateDispatchAction;


@Mapping(path = "/listMasterDegreesCandidate", module = "masterDegreeAdministrativeOffice", input = "/chooseExecutionYear.do?method=chooseDegreeFromList&page=0")
@Forwards(value = {
	@Forward(name = "DisplayMasterDegreeList", path = "/candidate/displayMasterDegrees.jsp"),
	@Forward(name = "PrepareSuccess", path = "/candidate/chooseExecutionYear.jsp"),
	@Forward(name = "MasterDegreeReady", path = "/candidate/displayCurricularPlanByChosenMasterDegree.jsp"),
	@Forward(name = "CreateReady", path = "/createCandidateDispatchAction.do?method=prepare&page=0"),
	@Forward(name = "NotAuthorized", path = "/student/notAuthorized.jsp")})
@Exceptions(value = {
	@ExceptionHandling(key = "resources.Action.exceptions.NonExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class)})
public class CreateCandidateDispatchAction_AM2 extends CreateCandidateDispatchAction {

}
