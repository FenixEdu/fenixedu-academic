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

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.LinkObject;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.ObjectLinkRenderer;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;

public class ListLinkRenderer extends OutputRenderer {

    private static String DEFAULT_SEPARATOR = " > ";

    private String linkFormat;
    private String separator;

    private boolean contextRelative;
    private boolean moduleRelative;

    public String getSeparator() {
        return hasSeparator() ? this.separator : DEFAULT_SEPARATOR;
    }

    /**
     * Indicates the separator between generated links. If not specified then
     * " > " default sting is used
     * 
     * @property
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    private boolean hasSeparator() {
        return !StringUtils.isEmpty(this.separator);
    }

    public boolean isContextRelative() {
        return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }

    public String getLinkFormat() {
        return linkFormat;
    }

    public void setLinkFormat(String linkFormat) {
        this.linkFormat = linkFormat;
    }

    public boolean isModuleRelative() {
        return moduleRelative;
    }

    public void setModuleRelative(boolean moduleRelative) {
        this.moduleRelative = moduleRelative;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {

                final HtmlBlockContainer blockContainer = new HtmlBlockContainer();

                final List<LinkObject> objects = (List<LinkObject>) object;
                final Iterator<LinkObject> iterator = objects.iterator();

                while (iterator.hasNext()) {
                    final LinkObject each = iterator.next();

                    final ObjectLinkRenderer linkRenderer = new ObjectLinkRenderer();

                    linkRenderer.setText(each.getLabel());
                    linkRenderer.setLinkFormat(getLinkFormat());
                    linkRenderer.setModuleRelative(isModuleRelative());
                    linkRenderer.setContextRelative(isContextRelative());

                    blockContainer.addChild(RenderKit.getInstance().renderUsing(linkRenderer, getContext(), each,
                            LinkObject.class));
                    blockContainer.addChild(iterator.hasNext() ? new HtmlText(getSeparator()) : new HtmlText());
                }
                return blockContainer;
            }
        };
    }
}
