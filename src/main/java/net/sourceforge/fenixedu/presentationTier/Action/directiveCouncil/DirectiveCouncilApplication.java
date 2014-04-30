package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "ApplicationResources", path = "directive-council", titleKey = "role.directiveCouncil",
        hint = DirectiveCouncilApplication.HINT, accessGroup = DirectiveCouncilApplication.ACCESS_GROUP)
@Mapping(path = "/index", module = "directiveCouncil", parameter = "/directiveCouncil/index.jsp")
public class DirectiveCouncilApplication extends ForwardAction {

    protected static final String HINT = "Directive Council";
    protected static final String ACCESS_GROUP = "role(DIRECTIVE_COUNCIL)";

    @StrutsApplication(bundle = "ApplicationResources", path = "control", titleKey = "link.control", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class DirectiveCouncilControlApp {

    }

    @StrutsApplication(bundle = "ApplicationResources", path = "external-supervision",
            titleKey = "link.directiveCouncil.externalSupervision", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class DirectiveCouncilExternalSupervision {

    }

    @StrutsApplication(bundle = "DirectiveCouncilResources", path = "career-workshops",
            titleKey = "label.title.careerWorkshop.simple", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class DirectiveCouncilCareerWorkshops {

    }

}
