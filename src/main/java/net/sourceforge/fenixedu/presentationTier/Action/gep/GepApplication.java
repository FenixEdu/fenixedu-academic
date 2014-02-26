package net.sourceforge.fenixedu.presentationTier.Action.gep;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "GEPResources", path = "gep", titleKey = "label.gep.fullName", hint = "Gep",
        accessGroup = "role(GEP)")
@Mapping(path = "/index", module = "gep", parameter = "/gep/index.jsp")
public class GepApplication extends ForwardAction {

    static final String HINT = "Gep";
    static final String ACCESS_GROUP = "role(GEP)";
    static final String BUNDLE = "GEPResources";

    @StrutsApplication(bundle = BUNDLE, path = "gep", titleKey = "label.gep.portal.tilte", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class GepPortalApp {
    }

    @StrutsApplication(bundle = "InquiriesResources", path = "inquiries", titleKey = "label.inquiries", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class GepInquiriesApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "alumni", titleKey = "label.alumni", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class GepAlumniApp {
    }

    @StrutsApplication(bundle = "AcademicAdminOffice", path = "registered-degree-candidacies",
            titleKey = "label.registeredDegreeCandidacies.first.time.student.registration", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class GepRegisteredDegreeCandidaciesApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "raides", titleKey = "title.personal.ingression.data.viewer", hint = HINT,
            accessGroup = "#managers")
    public static class GepRAIDESApp {
    }

}
