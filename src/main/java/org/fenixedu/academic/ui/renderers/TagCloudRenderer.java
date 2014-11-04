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

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitFileTag;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlList;
import pt.ist.fenixWebFramework.renderers.components.HtmlListItem;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class TagCloudRenderer extends OutputRenderer {

    private String linkFormat;

    private String classes;

    private String styles;

    private boolean moduleRelative;

    private boolean contextRelative;

    private int numberOfLevels = 6;

    private int popularCount = 20;

    private float minimumLevel = 0.4f;

    private String sortBy;

    private String onClick;

    private String onDblClick;

    public String getOnClick() {
        return onClick;
    }

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    public String getOnDblClick() {
        return onDblClick;
    }

    public void setOnDblClick(String onDoubleClick) {
        this.onDblClick = onDoubleClick;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public float getMinimumLevel() {
        return minimumLevel;
    }

    public void setMinimumLevel(float minimumLevel) {
        this.minimumLevel = minimumLevel;
    }

    public int getPopularCount() {
        return popularCount;
    }

    public void setPopularCount(int popularCount) {
        this.popularCount = popularCount;
    }

    public int getNumberOfLevels() {
        return numberOfLevels;
    }

    public void setNumberOfLevels(int numberOfLevels) {
        this.numberOfLevels = numberOfLevels;
    }

    @Override
    public String getClasses() {
        return classes;
    }

    @Override
    public void setClasses(String classes) {
        this.classes = classes;
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

    public String getStyles() {
        return styles;
    }

    public void setStyles(String styles) {
        this.styles = styles;
    }

    public String getExtraTagClasses(UnitFileTag tag) {
        return "";
    }

    protected void addExtraParameters(HtmlLink link, UnitFileTag tag) {

    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Collection<UnitFileTag> tags =
                        (getSortBy() != null) ? RenderUtils.sortCollectionWithCriteria((Collection<UnitFileTag>) object,
                                getSortBy()) : new ArrayList<UnitFileTag>((Collection<UnitFileTag>) object);

                Person person = AccessControl.getPerson();
                int maximum = getMaximum(tags, person);

                HtmlList container = new HtmlList();

                for (UnitFileTag tag : tags) {
                    if (tag.isTagAccessibleToUser(person)) {
                        HtmlLink link = new HtmlLink();
                        link.setModuleRelative(isModuleRelative());
                        link.setContextRelative(isContextRelative());
                        link.setUrl(RenderUtils.getFormattedProperties(getLinkFormat(), tag));
                        if (getOnClick() != null) {
                            link.setOnClick(RenderUtils.getFormattedProperties(getOnClick(), tag));
                        }
                        if (getOnDblClick() != null) {
                            link.setOnDblClick(RenderUtils.getFormattedProperties(getOnDblClick(), tag));
                        }
                        HtmlText text = new HtmlText(tag.getName());
                        text.setClasses(getHtmlClass(maximum, tag, person));
                        link.setBody(text);
                        HtmlListItem item = container.createItem();

                        addExtraParameters(link, tag);

                        item.addChild(link);
                    }
                }
                return container;
            }

            private String getHtmlClass(Integer maximum, UnitFileTag tag, Person person) {
                Double level = getLevel(tag, maximum);
                Double min = Math.min(level, getNumberOfLevels() - 1);

                return "tcloudlevel" + getNumberOfLevels() + "-" + (min.intValue() + 1) + " " + getExtraTagClasses(tag);
            }

            private double getLevel(UnitFileTag tag, Integer maxFrequency) {
                float level =
                        Math.min(getNumberOfLevels() - weight(maxFrequency), getNumberOfLevels() * maxFrequency
                                / getPopularCount());
                return Math.log10(tag.getFileTagCount(AccessControl.getPerson())) * level / Math.log10(1 + maxFrequency)
                        + weight(maxFrequency);
            }

            private float weight(int maximumFrequency) {
                float value = getMinimumLevel() * getNumberOfLevels();
                return Math.min(value, value * getPopularCount() / maximumFrequency);
            }

            private int getMaximum(Collection<UnitFileTag> tags, Person person) {
                int max = -1;
                for (UnitFileTag tag : tags) {
                    max = Math.max(max, tag.getFileTagCount(person));
                }
                return max;
            }
        };
    }

}
