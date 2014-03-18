package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.candidate.CandidateRegistrationDispatchAction;


@Mapping(path = "/candidateRegistration", module = "masterDegreeAdministrativeOffice", input = "/candidate/chooseMasterDegreeToSelectCandidates.jsp", formBean = "candidateRegistrationForm")
@Forwards(value = {
	@Forward(name = "ListCandidates", path = "/candidate/displayCandidateListForRegistration.jsp"),
	@Forward(name = "ShowConfirmation", path = "/candidate/confirmCandidateRegistration.jsp"),
	@Forward(name = "PrepareCandidateList", path = "/candidateRegistration.do?method=getCandidateList"),
	@Forward(name = "ShowResult", path = "/candidate/candidateRegistered.jsp"),
	@Forward(name = "Print", path = "/candidate/candidateRegistrationTemplatePrint.jsp")})
@Exceptions(value = {
	@ExceptionHandling(key = "resources.Action.exceptions.InvalidChangeActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidChangeActionException.class),
	@ExceptionHandling(key = "resources.Action.exceptions.ExistingActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class),
	@ExceptionHandling(key = "resources.Action.exceptions.ActiveStudentCurricularPlanAlreadyExistsActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ActiveStudentCurricularPlanAlreadyExistsActionException.class),
	@ExceptionHandling(key = "resources.Action.exceptions.InvalidStudentNumberActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidStudentNumberActionException.class)})
public class CandidateRegistrationDispatchAction_AM1 extends CandidateRegistrationDispatchAction {

}
