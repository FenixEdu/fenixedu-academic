package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import org.fenixedu.bennu.portal.StrutsApplication;

public class AlumniApplication {

    private static final String HINT = "Alumni";
    private static final String ACCESS_GROUP = "role(ALUMNI)";
    private static final String BUNDLE = "AlumniResources";

    @StrutsApplication(bundle = BUNDLE, path = "academic-path", titleKey = "academic.path", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class AlumniAcademicPathApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "professional-info", titleKey = "professional.info", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class AlumniProfessionalInfoApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "formation", titleKey = "label.formation", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class AlumniFormationApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "academic-services", titleKey = "academic.services", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class AlumniAcademicServicesApp {
    }

}
