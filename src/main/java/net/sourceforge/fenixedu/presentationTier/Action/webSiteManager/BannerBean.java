/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        this.color = banner.getColor();
        this.link = banner.getLink();
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
