package net.sourceforge.fenixedu.presentationTier.Action.publico;

import org.fenixedu.bennu.portal.StrutsApplication;

@StrutsApplication(bundle = "ApplicationResources", path = "public", titleKey = "title.public", hint = "Public")
public class PublicApplication {

    @StrutsApplication(bundle = "PhdResources", path = "phd", titleKey = "label.php.program", hint = "Public")
    public static class PublicPhdApp {

    }

    @StrutsApplication(bundle = "CandidateResources", path = "candidacies", titleKey = "title.applications", hint = "Public")
    public static class PublicCandidaciesApp {

    }

}
