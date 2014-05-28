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
package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * Specialized renderer to show the weight and the corresponding percentage of a {@link UnitSiteBanner}.
 * 
 * @author cfgi
 */
public class UnitSiteBannerWeightRenderer extends OutputRenderer {

    private String percentClass;
    private String percentStyle;

    public String getPercentClass() {
        return percentClass;
    }

    /**
     * @property
     */
    public void setPercentClass(String percentClass) {
        this.percentClass = percentClass;
    }

    public String getPercentStyle() {
        return percentStyle;
    }

    /**
     * @property
     */
    public void setPercentStyle(String percentStyle) {
        this.percentStyle = percentStyle;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {

                Integer weight = (Integer) object;
                float percentage = calculatePercentage(weight);

                HtmlContainer container = new HtmlInlineContainer();

                container.addChild(getWeightComponent(object));
                container.addChild(getPercentageComponent(percentage));

                return container;
            }

            private HtmlComponent getWeightComponent(Object object) {
                if (object == null) {
                    return new HtmlText("-");
                } else {
                    return renderValue(object, null, null);
                }
            }

            private HtmlText getPercentageComponent(float percentage) {
                HtmlText text = new HtmlText(String.format("(%.1f%%)", percentage));

                text.setClasses(getPercentClass());
                text.setStyle(getPercentStyle());

                return text;
            }

            private float calculatePercentage(Integer weight) {
                UnitSiteBanner banner = getBanner();
                return banner.getWeightPercentage();
            }

            private UnitSiteBanner getBanner() {
                return (UnitSiteBanner) getContext().getParentContext().getMetaObject().getObject();
            }

        };
    }

}
