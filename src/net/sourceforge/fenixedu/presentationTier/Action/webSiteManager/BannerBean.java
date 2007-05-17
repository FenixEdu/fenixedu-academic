package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteBannerRepeatType;

public class BannerBean implements Serializable {

    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private SimpleFileBean mainImage;
    private SimpleFileBean backgroundImage;
    private String color;
    private String link;
    private Integer weight;
    private UnitSiteBannerRepeatType repeat;
    
    public BannerBean() {
        super();
        
        this.mainImage = new SimpleFileBean();
        this.backgroundImage = new SimpleFileBean();
        this.repeat = UnitSiteBannerRepeatType.HORIZONTAL;
    }

    public BannerBean(UnitSiteBanner banner) {
        this();
        
        this.repeat = banner.getRepeatType();
        this.color  = banner.getColor();
        this.link   = banner.getLink();
        this.weight = banner.getWeight();
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public UnitSiteBannerRepeatType getRepeat() {
		return repeat;
	}

	public void setRepeat(UnitSiteBannerRepeatType repeat) {
		this.repeat = repeat;
	}
	
}
