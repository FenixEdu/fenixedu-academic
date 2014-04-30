package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "ApplicationResources", path = "public-relations", titleKey = "label.publicRelationOffice",
        hint = PublicRelationsApplication.HINT, accessGroup = PublicRelationsApplication.ACCESS_GROUP)
@Mapping(path = "/index", module = "publicRelations", parameter = "/publicRelations/index.jsp")
public class PublicRelationsApplication extends ForwardAction {

    static final String HINT = "Public Relations";
    static final String ACCESS_GROUP = "role(PUBLIC_RELATIONS_OFFICE)";

    @StrutsApplication(bundle = "AlumniResources", path = "alumni", titleKey = "label.alumni.main.title", hint = HINT,
            accessGroup = ACCESS_GROUP)
    public static class PublicRelationsAlumniApp {
    }
}
