package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.directiveCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.DirectiveCouncilApplication.DirectiveCouncilControlApp;
import net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.SummariesControlAction;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DirectiveCouncilControlApp.class, path = "summaries", titleKey = "link.summaries.control")
@Mapping(module = "directiveCouncil", path = "/summariesControl", input = "/index.do")
@Forwards(@Forward(name = "success", path = "/directiveCouncil/summariesControl/listTeacherSummariesControl.jsp"))
public class SummariesControlActionForDirectiveCouncil extends SummariesControlAction {
}