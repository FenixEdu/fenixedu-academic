package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class BusinessArea extends BusinessArea_Base {

    public BusinessArea(Integer level, String code, String description) {
	super();

	if (level == null) {
	    throw new DomainException("businessArea.creation.level.null");
	}

	if (code == null) {
	    throw new DomainException("businessArea.creation.code.null");
	}

	if (description == null) {
	    throw new DomainException("businessArea.creation.description.null");
	}

	setRootDomainObject(RootDomainObject.getInstance());
	setLevel(level);
	setCode(code);
	setDescription(description);
    }

    public BusinessArea(Integer level, String code, String description, BusinessArea parentArea) {
	this(level, code, description);
	setParentArea(parentArea);
    }

    public static List<BusinessArea> getParentBusinessAreas() {
	List<BusinessArea> parentAreas = new ArrayList<BusinessArea>();
	for (BusinessArea area : RootDomainObject.getInstance().getBusinessAreas()) {
	    if (!area.hasParentArea()) {
		parentAreas.add(area);
	    }
	}
	return parentAreas;
    }

    public static Object getChildBusinessAreas(BusinessArea parentArea) {
	List<BusinessArea> childAreas = new ArrayList<BusinessArea>();
	for (BusinessArea area : RootDomainObject.getInstance().getBusinessAreas()) {
	    if (area.hasParentArea() && area.getParentArea().equals(parentArea)) {
		childAreas.add(area);
	    }
	}
	return childAreas;
    }
}
