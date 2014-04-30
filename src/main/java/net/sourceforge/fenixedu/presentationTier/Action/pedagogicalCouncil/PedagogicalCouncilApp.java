package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.commons.FacesEntryPoint;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = PedagogicalCouncilApp.BUNDLE, path = "pedagogical-council", titleKey = "pedagogical.council",
        accessGroup = "(role(PEDAGOGICAL_COUNCIL) | role(TUTORSHIP))", hint = PedagogicalCouncilApp.HINT)
@Mapping(path = "/index", module = "pedagogicalCouncil", parameter = "/pedagogicalCouncil/index.jsp")
public class PedagogicalCouncilApp extends ForwardAction {

    static final String BUNDLE = "PedagogicalCouncilResources";
    static final String ACCESS_GROUP = "role(PEDAGOGICAL_COUNCIL)";
    static final String HINT = "Pedagogical Council";

    @StrutsApplication(bundle = BUNDLE, path = "bolonha-process", titleKey = "bolonha.process", accessGroup = ACCESS_GROUP,
            hint = HINT)
    public static class PedagogicalBolonhaProcessApp {
    }

    @StrutsApplication(bundle = "ResearcherResources", path = "communication", titleKey = "title.unit.communication.section",
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class PedagogicalCommunicationApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "delegates", titleKey = "delegates.section", accessGroup = ACCESS_GROUP,
            hint = HINT)
    public static class PedagogicalDelegatesApp {
    }

    @StrutsApplication(bundle = "ApplicationResources", path = "control", titleKey = "link.control", accessGroup = ACCESS_GROUP,
            hint = HINT)
    public static class PedagogicalControlApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "tutorship", titleKey = "link.tutorship", accessGroup = "role(TUTORSHIP)",
            hint = HINT)
    public static class TutorshipApp {
    }

    // Faces Entry Points

    @StrutsFunctionality(app = PedagogicalBolonhaProcessApp.class, path = "competence-courses",
            titleKey = "navigation.competenceCoursesManagement")
    @Mapping(path = "/competenceCourses/competenceCoursesManagement", module = "pedagogicalCouncil")
    public static class PedagogicalCompetenceCoursesManagement extends FacesEntryPoint {
    }

    @StrutsFunctionality(app = PedagogicalBolonhaProcessApp.class, path = "curricular-plans",
            titleKey = "navigation.curricularPlansManagement")
    @Mapping(path = "/curricularPlans/curricularPlansManagement", module = "pedagogicalCouncil")
    public static class PedagogicalCurricularPlansManagement extends FacesEntryPoint {
    }

    @StrutsFunctionality(app = PedagogicalDelegatesApp.class, path = "elections", titleKey = "link.createEditVotingPeriods")
    @Mapping(path = "/delegateElections", module = "pedagogicalCouncil",
            parameter = "/pedagogicalCouncil/elections/firstPage.jsp")
    public static class PedagogicalDelegateElectionsEntryPoint extends ForwardAction {
    }

}
