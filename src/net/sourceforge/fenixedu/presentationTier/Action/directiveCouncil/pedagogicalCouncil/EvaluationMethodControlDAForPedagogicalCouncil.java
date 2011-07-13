package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.pedagogicalCouncil;

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

@Mapping(module = "pedagogicalCouncil", path = "/evaluationMethodControl", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "search", path = "/pedagogicalCouncil/control/evaluationMethodControl.jsp") })
public class EvaluationMethodControlDAForPedagogicalCouncil extends net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.EvaluationMethodControlDA {
}