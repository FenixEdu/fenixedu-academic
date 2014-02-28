package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.SummariesControlAction;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.PedagogicalControlApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = PedagogicalControlApp.class, path = "summaries", titleKey = "link.summaries.control")
@Mapping(module = "pedagogicalCouncil", path = "/summariesControl", input = "/index.do")
@Forwards(@Forward(name = "success", path = "/pedagogicalCouncil/control/teachersSummariesControlList.jsp"))
public class SummariesControlActionForPedagogicalCouncil extends SummariesControlAction {
}