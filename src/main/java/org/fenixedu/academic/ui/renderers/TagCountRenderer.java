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

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.UnitFileTag;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class TagCountRenderer extends OutputRenderer {

    private String linkFormat;

    private String classes;

    private String styles;

    private boolean moduleRelative;

    private boolean contextRelative;

    private String separator;

    private String showAllUrl;

    private String sortBy;

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getShowAllUrl() {
        return showAllUrl;
    }

    public void setShowAllUrl(String showAllUrl) {
        this.showAllUrl = showAllUrl;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public boolean isContextRelative() {
        return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }

    public boolean isModuleRelative() {
        return moduleRelative;
    }

    public void setModuleRelative(boolean moduleRelative) {
        this.moduleRelative = moduleRelative;
    }

    @Override
    public String getClasses() {
        return classes;
    }

    @Override
    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getLinkFormat() {
        return linkFormat;
    }

    public void setLinkFormat(String linkFormat) {
        this.linkFormat = linkFormat;
    }

    public String getStyles() {
        return styles;
    }

    public void setStyles(String styles) {
        this.styles = styles;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Collection<UnitFileTag> tags =
                        (getSortBy() != null) ? RenderUtils.sortCollectionWithCriteria((Collection<UnitFileTag>) object,
                                getSortBy()) : new ArrayList<UnitFileTag>((Collection<UnitFileTag>) object);

                HtmlInlineContainer container = new HtmlInlineContainer();
                int i = 0;
                for (UnitFileTag tag : tags) {
                    if (tag.isTagAccessibleToUser(AccessControl.getPerson())) {
                        if (i > 0) {
                            container.addChild(new HtmlText(" " + getSeparator() + " "));
                        }

                        HtmlComponent component = null;
                        if (getLinkFormat() != null) {
                            HtmlLink link = new HtmlLink();
                            link.setModuleRelative(isModuleRelative());
                            link.setContextRelative(isContextRelative());
                            link.setUrl(RenderUtils.getFormattedProperties(getLinkFormat(), tag));
                            link.setBody(getText(tag));
                            component = link;
                        } else {
                            component = getText(tag);
                        }
                        container.addChild(component);
                        i++;
                    }
                }
                if (getShowAllUrl() != null) {
                    HtmlLink link = new HtmlLink();
                    link.setModuleRelative(isModuleRelative());
                    link.setContextRelative(isContextRelative());
                    link.setUrl(getShowAllUrl());
                    link.setBody(new HtmlText(RenderUtils.getResourceString("RENDERER_RESOURCES", "renderers.show.all")));
                    container.addChild(new HtmlText(" " + getSeparator() + " "));
                    container.addChild(link);
                }
                return container;
            }

            private HtmlComponent getText(UnitFileTag tag) {
                return new HtmlText(tag.getName() + "(" + tag.getFileTagCount(AccessControl.getPerson()) + ") ");
            }

        };
    }
}
