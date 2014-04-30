package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.directiveCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.DirectiveCouncilApplication.DirectiveCouncilControlApp;
import net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.EvaluationMethodControlDA;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DirectiveCouncilControlApp.class, path = "evaluation-method",
        titleKey = "label.evaluationMethodControl")
@Mapping(module = "directiveCouncil", path = "/evaluationMethodControl")
@Forwards(@Forward(name = "search", path = "/directiveCouncil/evaluationMethodControl.jsp"))
public class EvaluationMethodControlDAForDirectiveCouncil extends EvaluationMethodControlDA {
}