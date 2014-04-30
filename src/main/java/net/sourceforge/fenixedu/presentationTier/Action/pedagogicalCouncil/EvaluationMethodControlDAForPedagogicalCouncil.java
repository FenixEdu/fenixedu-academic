package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.EvaluationMethodControlDA;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.PedagogicalControlApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = PedagogicalControlApp.class, path = "evaluation-method", titleKey = "label.evaluationMethodControl")
@Mapping(module = "pedagogicalCouncil", path = "/evaluationMethodControl")
@Forwards(@Forward(name = "search", path = "/pedagogicalCouncil/control/evaluationMethodControl.jsp"))
public class EvaluationMethodControlDAForPedagogicalCouncil extends EvaluationMethodControlDA {
}