package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.pedagogicalCouncil;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "pedagogicalCouncil", path = "/evaluationMethodControl", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "search", path = "/pedagogicalCouncil/control/evaluationMethodControl.jsp", tileProperties = @Tile(title = "private.pedagogiccouncil.control.controlassessmentmethods")) })
public class EvaluationMethodControlDAForPedagogicalCouncil extends
	net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.EvaluationMethodControlDA {
}