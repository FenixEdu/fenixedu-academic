/*
 * Created on 5/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;

/**
 * @author jmota
 * @author Fernanda Quitério
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SiteView {
	
	private ISiteComponent bodyComponent;
	
	

	/**
	 * @return
	 */
	public ISiteComponent getComponent() {
		return bodyComponent;
	}

	/**
	 * @param component
	 */
	public void setComponent(ISiteComponent component) {
		this.bodyComponent = component;
	}

}
