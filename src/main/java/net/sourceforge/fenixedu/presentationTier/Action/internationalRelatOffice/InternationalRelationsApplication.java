package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "InternRelationOfficeResources", path = "international-relations",
        titleKey = "title.internationalrelations", hint = InternationalRelationsApplication.HINT,
        accessGroup = InternationalRelationsApplication.ACCESS_GROUP)
@Mapping(path = "/index", module = "internationalRelatOffice", parameter = "/internationalRelatOffice/index.jsp")
public class InternationalRelationsApplication extends ForwardAction {

    static final String BUNDLE = "InternRelationOfficeResources";
    static final String HINT = "International Relations";
    static final String ACCESS_GROUP = "role(INTERNATIONAL_RELATION_OFFICE)";

    @StrutsApplication(bundle = BUNDLE, path = "internships", titleKey = "label.internationalrelations.internship", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class InternRelationsInternshipApp {
    }

    @StrutsApplication(bundle = "AcademicAdminOffice", path = "consult", titleKey = "link.consult", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class InternRelationsConsultApp {
    }

    @StrutsApplication(bundle = BUNDLE, path = "mobility", titleKey = "title.mobility", hint = HINT, accessGroup = ACCESS_GROUP)
    public static class InternRelationsMobilityApp {
    }

}
