package net.sourceforge.fenixedu.presentationTier.Action.coordinator.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.ManageFinalDegreeWorkDispatchAction;


@Mapping(path = "/finalDegreeWorkProposal", module = "coordinator", input = "/finalDegreeWorkProposal.do?method=createNewFinalDegreeWorkProposal&page=0", formBean = "finalDegreeWorkProposal")
@Forwards(value = {
	@Forward(name = "show-final-degree-work-proposal", path = "df.page.showFinalDegreeWorkProposal"),
	@Forward(name = "show-final-degree-work-list", path = "/manageFinalDegreeWork.do?method=showProposal&page=0"),
	@Forward(name = "detailed-proposal-list", path = "detailed-proposal-list")})
public class ManageFinalDegreeWorkDispatchAction_AM3 extends ManageFinalDegreeWorkDispatchAction {

}
