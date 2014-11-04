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
package org.fenixedu.academic.ui.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.rendererExtensions.TreeRenderer;

public abstract class AbstractUnitFunctionsTreeRenderer extends TreeRenderer {

    private String systemFunctionImage;
    private String virtualFunctionImage;

    public AbstractUnitFunctionsTreeRenderer() {
        super();

        setExpandable(true);
    }

    public String getSystemFunctionImage() {
        return systemFunctionImage;
    }

    public void setSystemFunctionImage(String systemFunctionImage) {
        this.systemFunctionImage = systemFunctionImage;
    }

    public String getVirtualFunctionImage() {
        return virtualFunctionImage;
    }

    public void setVirtualFunctionImage(String virtualFunctionImage) {
        this.virtualFunctionImage = virtualFunctionImage;
    }

    @Override
    protected String getLinksFor(Object object) {
        if (object instanceof Unit) {
            return getLinkSequenceFor((Unit) object);
        } else if (object instanceof Function) {
            return getLinkSequenceFor((Function) object);
        } else if (object instanceof PersonFunction) {
            return getLinkSequenceFor((PersonFunction) object);
        } else {
            return getLinksFor(object);
        }
    }

    protected String getLinkSequenceFor(Unit unit) {
        return null;
    }

    protected String getLinkSequenceFor(Function function) {
        return null;
    }

    protected String getLinkSequenceFor(PersonFunction personFunction) {
        return null;
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
        } else if (object instanceof Function) {
            return getChildrenCollectionFor((Function) object);
        } else if (object instanceof PersonFunction) {
            return getChildrenCollectionFor((PersonFunction) object);
        } else {
            return null;
        }
    }

    protected Collection getChildrenCollectionFor(Unit unit) {
        List<Object> result = new ArrayList<Object>();

        SortedSet<Unit> units = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
        units.addAll(unit.getSubUnits());

        result.addAll(unit.getOrderedActiveFunctions());
        result.addAll(units);

        return result;
    }

    protected Collection getChildrenCollectionFor(Function function) {
        return null;
    }

    protected Collection getChildrenCollectionFor(PersonFunction personFunction) {
        return null;
    }

    @Override
    public boolean isIncludeImage() {
        return super.isIncludeImage() && getSystemFunctionImage() == null && getVirtualFunctionImage() == null;
    }

    @Override
    protected String getImageFor(Object object) {
        String image = null;

        if (object instanceof Unit) {
            image = getImagePathFor((Unit) object);
        } else if (object instanceof Function) {
            image = getImagePathFor((Function) object);
        } else if (object instanceof PersonFunction) {
            image = getImagePathFor((PersonFunction) object);
        }

        if (image != null) {
            return image;
        } else {
            return super.getImageFor(object);
        }
    }

    protected String getImagePathFor(Unit unit) {
        return null;
    }

    protected String getImagePathFor(Function function) {
        return function.isVirtual() ? getVirtualFunctionImage() : getSystemFunctionImage();
    }

    protected String getImagePathFor(PersonFunction personFunction) {
        return null;
    }
}
