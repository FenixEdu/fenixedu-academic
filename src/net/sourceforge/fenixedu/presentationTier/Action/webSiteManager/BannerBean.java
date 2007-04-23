package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.UnitSiteBanner;

public class BannerBean implements Serializable {

    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private SimpleFileBean mainImage;
    private SimpleFileBean backgroundImage;
    private String color;

    public BannerBean() {
        super();
        
        this.mainImage = new SimpleFileBean();
        this.backgroundImage = new SimpleFileBean();
    }

    public BannerBean(UnitSiteBanner banner) {
        this();
        
        this.color = banner.getColor();
    }
    
    public SimpleFileBean getBackgroundImage() {
        return this.backgroundImage;
    }

    public void setBackgroundImage(SimpleFileBean backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public SimpleFileBean getMainImage() {
        return this.mainImage;
    }

    public void setMainImage(SimpleFileBean mainImage) {
        this.mainImage = mainImage;
    }

}
