package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author Fernanda Quitério
 *  
 */
public class TeacherAdministrationSiteView extends SiteView {

    public TeacherAdministrationSiteView() {
    }

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

    public boolean equals(Object obj) {
        if (obj instanceof TeacherAdministrationSiteView) {
            TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) obj;
            boolean result1 = getCommonComponent().equals(siteView.getCommonComponent());
            boolean result2 = getComponent().equals(siteView.getComponent());

            return result1 && result2;
        }
        return false;
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