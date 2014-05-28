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
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.rendererExtensions.TreeRenderer;

public class UnitSitesTreeRenderer extends TreeRenderer {

    private String createSiteLink;
    private String chooseManagersLink;

    private String unitWithSiteImage;
    private String unitWithoutSiteImage;

    public UnitSitesTreeRenderer() {
        super();

        setExpandable(true);
    }

    public String getChooseManagersLink() {
        return chooseManagersLink;
    }

    public void setChooseManagersLink(String chooseManagersLink) {
        this.chooseManagersLink = chooseManagersLink;
    }

    public String getCreateSiteLink() {
        return createSiteLink;
    }

    public void setCreateSiteLink(String createSiteLink) {
        this.createSiteLink = createSiteLink;
    }

    public String getUnitWithoutSiteImage() {
        return unitWithoutSiteImage;
    }

    public void setUnitWithoutSiteImage(String unitWithoutSiteImage) {
        this.unitWithoutSiteImage = unitWithoutSiteImage;
    }

    public String getUnitWithSiteImage() {
        return unitWithSiteImage;
    }

    public void setUnitWithSiteImage(String unitWithSiteImage) {
        this.unitWithSiteImage = unitWithSiteImage;
    }

    @Override
    protected String getLinksFor(Object object) {
        if (object instanceof Unit) {
            return getLinkSequenceFor((Unit) object);
        } else {
            return getLinksFor(object);
        }
    }

    protected String getLinkSequenceFor(Unit unit) {
        if (unit.hasSite()) {
            return createLinkSequence(getChooseManagersLink());
        } else if (isAllowedToHaveSite(unit)) {
            return createLinkSequence(getCreateSiteLink());
        } else {
            return null;
        }
    }

    private boolean isAllowedToHaveSite(Unit unit) {
        return unit.getType() != PartyTypeEnum.AGGREGATE_UNIT;
    }

    protected static String createLinkSequence(String... links) {
        StringBuilder builder = new StringBuilder();

        for (String link : links) {
            if (link == null) {
                continue;
            }

            if (builder.length() > 0) {
                builder.append(", ");
            }

            builder.append(link.trim());
        }

        if (builder.length() > 0) {
            return builder.toString();
        } else {
            return null;
        }
    }

    @Override
    protected String getChildrenFor(Object object) {
        return ""; // not null, must specify children directly
    }

    @Override
    protected Collection getChildrenObjects(Object object, String children) {
        if (object instanceof Unit) {
            return getChildrenCollectionFor((Unit) object);
        } else {
            return null;
        }
    }

    protected Collection getChildrenCollectionFor(Unit unit) {
        List<Object> result = new ArrayList<Object>();

        SortedSet<Unit> units = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
        units.addAll(unit.getSubUnits());

        result.addAll(units);

        return result;
    }

    @Override
    public boolean isIncludeImage() {
        return super.isIncludeImage() && getUnitWithSiteImage() == null && getUnitWithoutSiteImage() == null;
    }

    @Override
    protected String getImageFor(Object object) {
        String image = null;

        if (object instanceof Unit) {
            image = getImagePathFor((Unit) object);
        }

        return image != null ? image : super.getImageFor(object);
    }

    private String getImagePathFor(Unit unit) {
        if (unit.hasSite()) {
            return getUnitWithSiteImage();
        } else {
            return getUnitWithoutSiteImage();
        }
    }
}
