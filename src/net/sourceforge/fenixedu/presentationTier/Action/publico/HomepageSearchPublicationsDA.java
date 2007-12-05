package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.homepage.Homepage;

public class HomepageSearchPublicationsDA  extends SearchPublicationsDA {

    @Override
    protected void setRequestDomainObject(HttpServletRequest request) {
	
	Integer homepageId = getIntegerFromRequest(request, "homepageID");

	DomainObject possibleHomepage = readDomainObject(request, Homepage.class, homepageId);
	if (possibleHomepage instanceof Homepage) {
	    Homepage homepage = (Homepage) possibleHomepage;
	    if (homepage.getActivated() != null && homepage.getActivated()) {
		    request.setAttribute("homepage", homepage);
		}
	    }
    }
}
