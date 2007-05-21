package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.homepage.Homepage;

public class ShowResearchResultInHomePage extends ShowResearchResult {
	
	@Override
	protected void putSiteOnRequest(HttpServletRequest request) {
		String homepageID = request.getParameter("homepageID");
		if(homepageID!=null) {
			Homepage homepage = (Homepage) RootDomainObject.readDomainObjectByOID(Homepage.class, Integer.valueOf(homepageID));
			request.setAttribute("homepage", homepage);
		}
	}
	

}
