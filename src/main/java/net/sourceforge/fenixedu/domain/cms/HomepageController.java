package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.homepage.Homepage;

public class HomepageController extends SiteTemplateController {

    @Override
    public Class<Homepage> getControlledClass() {
        return Homepage.class;
    }

    @Override
    public Site selectSiteForPath(String[] parts) {
        Person person = Person.findByUsername(parts[0]);
        if (person != null) {
            return person.isHomePageAvailable() ? person.getHomepage() : null;
        }
        return null;
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 1;
    }

}
