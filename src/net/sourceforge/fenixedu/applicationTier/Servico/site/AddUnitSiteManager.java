package net.sourceforge.fenixedu.applicationTier.Servico.site;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;

/**
 * Adds a new person to the managers of a UnitSite.
 * 
 * @author cfgi
 */
public class AddUnitSiteManager extends Service {

    public void run (UnitSite site, Person person) {
        site.addManagers(person);
    }
    
}
