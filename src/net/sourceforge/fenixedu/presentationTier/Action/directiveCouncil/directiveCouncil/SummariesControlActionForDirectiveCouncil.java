package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.directiveCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "directiveCouncil", path = "/summariesControl", input = "/index.do", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "success", path = "/directiveCouncil/summariesControl/listTeacherSummariesControl.jsp", tileProperties = @Tile(title = "private.steeringcouncil.control.controlbriefs")) })
public class SummariesControlActionForDirectiveCouncil extends
	net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.SummariesControlAction {
}