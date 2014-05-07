package net.sourceforge.fenixedu.presentationTier.Action.research;

import org.fenixedu.bennu.portal.StrutsApplication;

public class ResearcherApplication {

    private static final String ACCESS_GROUP = "role(RESEARCHER)";
    private static final String HINT = "Researcher";

    @StrutsApplication(bundle = "ResearcherResources", path = "research-units", titleKey = "label.researchUnits",
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ResearcherResearchUnitApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "final-work", titleKey = "link.manage.finalWork",
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class ResearcherFinalWorkApp {
    }

    @StrutsApplication(bundle = "ResearcherResources", path = "curriculum", titleKey = "link.viewCurriculum",
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class CurriculumApp {
    }
}
