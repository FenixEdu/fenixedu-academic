package net.sourceforge.fenixedu.presentationTier.Action.coordinator.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.CoordinationTeamDispatchAction;


@Mapping(path = "/viewCoordinationTeam", module = "coordinator", formBean = "removeCoordinators")
@Forwards(value = {
	@Forward(name = "chooseExecutionYear", path = "chooseExecutionYear"),
	@Forward(name = "coordinationTeam", path = "coordinationTeam"),
	@Forward(name = "addCoordinator", path = "addCoordinator"),
	@Forward(name = "noAuthorization", path = "df.page.notAuthorized")})
public class CoordinationTeamDispatchAction_AM1 extends CoordinationTeamDispatchAction {

}
