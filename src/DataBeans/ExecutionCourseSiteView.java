/*
 * Created on 5/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;

/**
 * @author João Mota
 *
 * 
 */
public class ExecutionCourseSiteView extends SiteView {

/**
	 * @param commonComponent
	 * @param bodyComponent
	 */
	public ExecutionCourseSiteView(ISiteComponent commonComponent, ISiteComponent bodyComponent) {
		setCommonComponent(commonComponent);
		setComponent(bodyComponent);
	}

	private ISiteComponent commonComponent; 


/**
 * @return
 */
public ISiteComponent getCommonComponent() {

	return commonComponent;
}

/**
 * @param component
 */
public void setCommonComponent(ISiteComponent component) {
	commonComponent = component;
}

}
