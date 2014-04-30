package net.sourceforge.fenixedu.presentationTier.Action.nape;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "PortalResources", path = "nape", titleKey = "portal.nape.name", hint = "NAPE",
        accessGroup = "role(NAPE)")
@Mapping(module = "nape", path = "/index", parameter = "/nape/index.jsp")
public class NapeApplication extends ForwardAction {

    static final String HINT = "NAPE";
    static final String ACCESS_GROUP = "role(NAPE)";

    @StrutsApplication(bundle = "AcademicAdminOffice", path = "registered-candidacies",
            titleKey = "label.registeredDegreeCandidacies.first.time.student.registration", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class NapeRegisteredCandidaciesApp {
    }

    @StrutsApplication(bundle = "CandidateResources", path = "candidacies", titleKey = "title.applications", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class NapeCandidaciesApp {
    }

}
