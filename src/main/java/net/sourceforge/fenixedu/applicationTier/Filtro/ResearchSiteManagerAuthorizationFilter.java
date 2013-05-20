package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ResearchSiteManagerAuthorizationFilter extends Filtro {

    public void execute(Object[] parameters) throws Exception {
        IUserView userView = AccessControl.getUserView();
        Person person = userView.getPerson();

        UnitSite site = (UnitSite) parameters[0];

        if (!site.hasManagers(person)) {
            throw new NotAuthorizedFilterException("error.person.not.manager.of.site");
        }
    }

}
