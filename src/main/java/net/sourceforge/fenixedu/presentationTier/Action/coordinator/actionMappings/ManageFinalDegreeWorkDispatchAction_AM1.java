package net.sourceforge.fenixedu.presentationTier.Action.coordinator.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.ManageFinalDegreeWorkDispatchAction;


@Mapping(path = "/manageFinalDegreeWork", module = "coordinator")
@Forwards(value = {
	@Forward(name = "error", path = "/indexCoordinator.jsp"),
	@Forward(name = "show-choose-execution-degree-page", path = "df.page.showFinalDegreeChooseExecutionDegree"),
	@Forward(name = "show-final-degree-work-list", path = "df.page.showFinalDegreeWorkList"),
	@Forward(name = "show-final-degree-work-info", path = "show-final-degree-work-info"),
	@Forward(name = "show-proposals", path = "show-proposals"),
	@Forward(name = "show-proposal", path = "show-proposal"),
	@Forward(name = "edit-proposal", path = "edit-proposal"),
	@Forward(name = "show-candidates", path = "show-candidates"),
	@Forward(name = "show-final-degree-work-proposal", path = "df.page.showFinalDegreeWorkProposal"),
	@Forward(name = "prepare-show-final-degree-work-proposal", path = "/manageFinalDegreeWork.do?method=prepare&page=0"),
	@Forward(name = "show-student-curricular-plan", path = "/viewCurriculum.do?method=prepare&page=0"),
	@Forward(name = "edit-final-degree-periods", path = "edit-final-degree-periods"),
	@Forward(name = "edit-final-degree-requirements", path = "edit-final-degree-requirements"),
	@Forward(name = "detailed-proposal-list", path = "detailed-proposal-list")})
@Exceptions(value = {
	@ExceptionHandling(key = "error.final.degree.work.scheduling.has.proposals", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.applicationTier.Servico.coordinator.AddExecutionDegreeToScheduling.SchedulingContainsProposalsException.class),
	@ExceptionHandling(key = "error.final.degree.work.scheduling.has.proposals", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, type = net.sourceforge.fenixedu.applicationTier.Servico.coordinator.RemoveExecutionDegreeToScheduling.SchedulingContainsProposalsException.class)})
public class ManageFinalDegreeWorkDispatchAction_AM1 extends ManageFinalDegreeWorkDispatchAction {

}
