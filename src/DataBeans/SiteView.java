/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

/**
 * @author João Mota
 * @author Fernanda Quitério
 *  
 */
public class SiteView extends DataTranferObject {

    private ISiteComponent bodyComponent;

    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof SiteView) {
            SiteView siteView = (SiteView) arg0;
            if (getComponent() == null && siteView.getComponent() == null) {
                result = true;
            } else if (getComponent() != null && getComponent().equals(siteView.getComponent())) {
                result = true;
            }
        }

        return result;
    }

    /**
     *  
     */
    public SiteView() {
    }

    public SiteView(ISiteComponent bodyComponent) {
        setComponent(bodyComponent);
    }

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