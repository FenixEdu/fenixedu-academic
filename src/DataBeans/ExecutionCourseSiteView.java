/*
 * Created on 5/Mai/2003
 *
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

    public boolean equals(Object obj) {

        boolean resultado = false;

        if (obj instanceof ExecutionCourseSiteView) {
            ExecutionCourseSiteView siteView = (ExecutionCourseSiteView) obj;

            resultado = getCommonComponent().equals(siteView.getCommonComponent())
                    && getComponent().equals(siteView.getComponent());
        }

        return resultado;
    }

}