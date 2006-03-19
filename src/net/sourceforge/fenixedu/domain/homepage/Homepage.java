package net.sourceforge.fenixedu.domain.homepage;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Homepage extends Homepage_Base {

	public Homepage() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	@Override
	public void setMyUrl(String myUrl) {
		for (final Person person : RootDomainObject.readAllPersons()) {
			final Homepage homepage = person.getHomepage();
			if (homepage != null && homepage.getMyUrl().equalsIgnoreCase(myUrl)) {
				throw new DomainException("homepage.url.exists");
			}
		}
		super.setMyUrl(myUrl);
	}

}
