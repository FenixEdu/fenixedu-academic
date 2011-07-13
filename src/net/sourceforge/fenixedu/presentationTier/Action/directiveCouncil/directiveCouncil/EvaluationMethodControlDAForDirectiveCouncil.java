package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.directiveCouncil;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "directiveCouncil", path = "/evaluationMethodControl", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "search", path = "/directiveCouncil/evaluationMethodControl.jsp") })
public class EvaluationMethodControlDAForDirectiveCouncil extends net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.EvaluationMethodControlDA {
}