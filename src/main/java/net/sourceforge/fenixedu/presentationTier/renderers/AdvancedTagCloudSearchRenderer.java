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

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.UnitFileTag;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;

public class AdvancedTagCloudSearchRenderer extends TagCloudRenderer {

    private String selectedTags;

    private String parameter;

    private String selectedTagClass;

    private String nearByTagClass;

    private Collection<UnitFileTag> tags;

    public String getNearByTagClass() {
        return nearByTagClass;
    }

    public void setNearByTagClass(String nearByTagClass) {
        this.nearByTagClass = nearByTagClass;
    }

    public String getSelectedTagClass() {
        return selectedTagClass;
    }

    public void setSelectedTagClass(String selectedTagClass) {
        this.selectedTagClass = selectedTagClass;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(String selectedTags) {
        this.selectedTags = selectedTags;
    }

    @Override
    public String getExtraTagClasses(UnitFileTag tag) {

        if (isSelected(tag)) {
            return getSelectedTagClass();
        }

        if (isNeighbour(tag)) {
            return getNearByTagClass();
        }

        return super.getExtraTagClasses(tag);
    }

    @Override
    protected void addExtraParameters(HtmlLink link, UnitFileTag tag) {
        if (isSelected(tag)) {
            link.setParameter(getParameter(), removeSelectedTag(tag.getName()));
        } else {
            link.setParameter(getParameter(), (selectedTags.length() > 0) ? selectedTags + " " + tag.getName() : tag.getName());
        }
    }

    private String removeSelectedTag(String name) {
        String[] tags = selectedTags.split("\\p{Space}+");
        String newSelectedTags = "";
        for (int i = tags.length - 1; i >= 0; i--) {
            if (!tags[i].equals(name)) {
                newSelectedTags += tags[i];
                if (i > 0) {
                    newSelectedTags += " ";
                }
            }

        }
        return newSelectedTags.trim();
    }

    private boolean isSelected(UnitFileTag tag) {
        String selectedTags = getSelectedTags();

        if (selectedTags == null) {
            return false;
        }

        int j = 0;
        String[] tags = selectedTags.split("\\p{Space}+");
        for (String tag2 : tags) {
            if (tag2.equals(tag.getName())) {
                j++;
            }
        }
        return j == 1;
    }

    private boolean isNeighbour(UnitFileTag tag) {

        if (selectedTags.length() == 0) {
            return false;
        }

        for (UnitFile file : tag.getTaggedFiles()) {
            if (file.hasUnitFileTags(materializeSelected(tag.getUnit()))) {
                return true;
            }
        }
        return false;
    }

    private Collection<UnitFileTag> materializeSelected(Unit unit) {
        if (tags == null) {
            String tagArray[] = selectedTags.split("\\p{Space}+");
            this.tags = new HashSet<UnitFileTag>();
            for (String element : tagArray) {
                UnitFileTag tag = unit.getUnitFileTag(element);
                if (tag != null) {
                    this.tags.add(tag);
                }
            }

        }
        return tags;
    }

}
