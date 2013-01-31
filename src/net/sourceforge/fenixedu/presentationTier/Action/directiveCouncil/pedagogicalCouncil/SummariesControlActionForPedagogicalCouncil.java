package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.pedagogicalCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "pedagogicalCouncil", path = "/summariesControl", input = "/index.do", scope = "request", parameter = "method")
@Forwards(value = { @Forward(
		name = "success",
		path = "/pedagogicalCouncil/control/teachersSummariesControlList.jsp",
		tileProperties = @Tile(title = "private.pedagogiccouncil.control.controlbriefs")) })
public class SummariesControlActionForPedagogicalCouncil extends
		net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.SummariesControlAction {
}