package net.sourceforge.fenixedu.presentationTier.Action.coordinator.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.ManageFinalDegreeWorkDispatchAction;


@Mapping(path = "/setFinalDegreeWorkCandidacyRequirements", module = "coordinator", input = "/manageFinalDegreeWork.do?method=prepare&page=0", attribute = "finalDegreeWorkCandidacyRequirements", formBean = "finalDegreeWorkCandidacyRequirements")
@Forwards(value = {
	@Forward(name = "Sucess", path = "/manageFinalDegreeWork.do?method=prepare&page=0")})
public class ManageFinalDegreeWorkDispatchAction_AM2 extends ManageFinalDegreeWorkDispatchAction {

}
