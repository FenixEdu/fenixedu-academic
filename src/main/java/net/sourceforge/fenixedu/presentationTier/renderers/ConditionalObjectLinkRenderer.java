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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.ObjectLinkRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RendererPropertyUtils;

public class ConditionalObjectLinkRenderer extends ObjectLinkRenderer {
    /**
     * This render is used to create a link out of an object if a given boolean
     * property is true. You choose the link format and some properties can be
     * used to configure the link. You can also specify the link indirectly by
     * specifing a destination and then defining a destination with that name in
     * the place were ou use this renderer.
     * 
     * <p>
     * The link body is configured through a sub rendering of the object with the specified layout and schema.
     * 
     * <p>
     * Example: <a href="#">Jane Doe</a>
     * 
     * @author pcma
     */

    private static final Logger logger = LoggerFactory.getLogger(ConditionalObjectLinkRenderer.class);

    private String visibleIf;

    public String getVisibleIf() {
        return visibleIf;
    }

    public void setVisibleIf(String visibleIf) {
        this.visibleIf = visibleIf;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        final Layout layout = super.getLayout(object, type);

        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Boolean visible = Boolean.FALSE;
                try {
                    visible = (Boolean) RendererPropertyUtils.getProperty(getTargetObject(object), getVisibleIf(), false);
                } catch (ClassCastException e) {
                    logger.error(e.getMessage(), e);
                }

                if (visible) {
                    return layout.createComponent(object, type);
                } else {
                    String text = getText();
                    return (text != null) ? new HtmlText(text) : renderValue(object,
                            RenderKit.getInstance().findSchema(getSubSchema()), getSubLayout());
                }
            }

        };
    }
}
