package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ResearchSiteManagerAuthorizationFilter {

    public static final ResearchSiteManagerAuthorizationFilter instance = new ResearchSiteManagerAuthorizationFilter();

    public void execute(UnitSite site, PersonFunction personFunction) throws Exception {
        IUserView userView = AccessControl.getUserView();
        Person person = userView.getPerson();

        if (!site.hasManagers(person)) {
            throw new NotAuthorizedFilterException("error.person.not.manager.of.site");
        }
    }

}
