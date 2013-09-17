package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ResearchSiteManagerAuthorizationFilter {

    public static final ResearchSiteManagerAuthorizationFilter instance = new ResearchSiteManagerAuthorizationFilter();

    public void execute(UnitSite site) throws NotAuthorizedException {
        IUserView userView = AccessControl.getUserView();
        Person person = userView.getPerson();

        if (!site.getManagersSet().contains(person)) {
            throw new NotAuthorizedException("error.person.not.manager.of.site");
        }
    }

}
