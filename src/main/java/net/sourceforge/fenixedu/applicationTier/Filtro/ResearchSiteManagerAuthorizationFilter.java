package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

public class ResearchSiteManagerAuthorizationFilter {

    public static final ResearchSiteManagerAuthorizationFilter instance = new ResearchSiteManagerAuthorizationFilter();

    public void execute(UnitSite site) throws NotAuthorizedException {
        User userView = Authenticate.getUser();
        Person person = userView.getPerson();

        if (!site.getManagersSet().contains(person)) {
            throw new NotAuthorizedException("error.person.not.manager.of.site");
        }
    }

}
