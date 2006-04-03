package net.sourceforge.fenixedu.domain;


/**
 * @author Fernanda Quit�rio 23/09/2003
 * 
 */
public class WebSiteItem extends WebSiteItem_Base {

	public WebSiteItem() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
	
	public void delete() {
		removeRootDomainObject();
		removeEditor();
		removeWebSiteSection();
		super.deleteDomainObject();
	}

}
