package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.homepage.Homepage;

public class GetHomepage extends Service {

    public Homepage run(Person person, boolean create) {
        Homepage homepage = person.getHomepage();
        
        if (homepage != null) {
            return homepage;
        }
        else if (create) {
            homepage = new Homepage(person);
            homepage.setActivated(false);
            
            return homepage;
        }
        else {
            return null;
        }
    }
}
