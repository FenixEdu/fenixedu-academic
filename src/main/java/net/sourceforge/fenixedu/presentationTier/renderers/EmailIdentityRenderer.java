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
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers;

import org.apache.commons.beanutils.BeanUtils;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 *         Created on May 8, 2006, 4:13:15 PM
 * 
 */
public class EmailIdentityRenderer extends OutputRenderer {

    private boolean collapsed = true;

    private String text;

    private String address;

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {
            @Override
            public HtmlComponent createComponent(Object object, Class type) {

                if (object == null) {
                    return new HtmlText();
                }

                String email = null;
                String name = null;
                try {
                    email = BeanUtils.getProperty(object, getAddress());
                    name = BeanUtils.getProperty(object, getText());
                } catch (Exception e) {

                    throw new RuntimeException(e);
                }

                HtmlText nameHtml = new HtmlText(name);

                HtmlLink emailHtml = new HtmlLink();
                emailHtml.setContextRelative(false);
                emailHtml.setUrl("mailto:" + email);

                if (!isCollapsed()) {
                    emailHtml.setText(email);
                    HtmlInlineContainer container = new HtmlInlineContainer();
                    container.addChild(nameHtml);
                    container.addChild(new HtmlText(" <", true));
                    container.addChild(emailHtml);
                    container.addChild(new HtmlText(">", true));
                    container.setIndented(false);

                    return container;
                } else {
                    emailHtml.setText(name);
                    return emailHtml;
                }

            }

        };

    }

    public boolean isCollapsed() {
        return this.collapsed;
    }

    /**
     * 
     * @property
     */
    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

}
