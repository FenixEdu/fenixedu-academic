package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.homepage.Homepage;

public class SubmitHomepage extends Service {

    public void run(final Person person, final Boolean activated, final String name, final Boolean showUnit,
    		final Boolean showPhoto, final Boolean showEmail, final Boolean showTelephone,
    		final Boolean showAlternativeHomepage, final Boolean showResearchUnitHomepage) {
    	Homepage homepage = person.getHomepage();
    	if (homepage == null) {
    		homepage = DomainFactory.makeHomepage();
    		homepage.setPerson(person);
    	}

    	homepage.setActivated(activated);
    	homepage.setName(name);
    	homepage.setShowUnit(showUnit);
    	homepage.setShowPhoto(showPhoto);
    	homepage.setShowEmail(showEmail);
    	homepage.setShowTelephone(showTelephone);
    	homepage.setShowAlternativeHomepage(showAlternativeHomepage);
    	homepage.setShowResearchUnitHomepage(showResearchUnitHomepage);
    }

}