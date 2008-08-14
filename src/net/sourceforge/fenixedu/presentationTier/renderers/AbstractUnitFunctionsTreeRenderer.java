package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

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
