package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import org.fenixedu.bennu.portal.StrutsApplication;

public class CoordinatorApplication {

    private static final String HINT = "Coordinator";
    private static final String ACCESS_GROUP = "role(COORDINATOR)";

    @StrutsApplication(bundle = "ApplicationResources", path = "manage", titleKey = "label.coordinator.management", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class CoordinatorManagementApp {
    }

    @StrutsApplication(bundle = "PhdResources", path = "phd", titleKey = "label.phds", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class CoordinatorPhdApp {
    }

}
