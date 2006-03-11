package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Homepage extends Homepage_Base {

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
