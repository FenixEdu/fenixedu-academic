package DataBeans;


/**
 * @author Fernanda Quitério
 *
 */
public class TeacherAdministrationSiteView extends SiteView {

/**
	 * @param commonComponent
	 * @param bodyComponent
	 */
	public TeacherAdministrationSiteView(ISiteComponent commonComponent, ISiteComponent bodyComponent) {
		setCommonComponent(commonComponent);
		setComponent(bodyComponent);
	}

	private ISiteComponent commonComponent; 


	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[TeacherAdministrationSiteView";
		result += ", commonComponent=" + getCommonComponent();
		result += ", bodyComponent=" + getComponent();
		result += "]";
		return result;
	}


	public boolean equals(Object siteView){
	return (siteView instanceof TeacherAdministrationSiteView)&&
	getCommonComponent().equals(((TeacherAdministrationSiteView)siteView).getCommonComponent())
	&& getComponent().equals(((TeacherAdministrationSiteView)siteView).getComponent());
			
		
	}

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
