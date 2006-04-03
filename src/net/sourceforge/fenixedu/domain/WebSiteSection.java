package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fernanda Quit�rio 23/09/2003
 *  
 */
public class WebSiteSection extends WebSiteSection_Base {

	public WebSiteSection() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
	
	public List<WebSiteItem> getPublishedWebSiteItems(){
		List<WebSiteItem> result = new ArrayList<WebSiteItem>();
		for (WebSiteItem webSiteItem : this.getIncludedWebSiteItems()) {
			if(webSiteItem.getPublished()) {
				result.add(webSiteItem);
			}
		}
		return result;
	}
	
	public static WebSiteSection readByName(String name) {
		for (WebSiteSection webSiteSection : RootDomainObject.getInstance().getWebSiteSections()) {
			if(webSiteSection.getName().equalsIgnoreCase(name)) {
				return webSiteSection;
			}
		}
		return null;
	}

}
