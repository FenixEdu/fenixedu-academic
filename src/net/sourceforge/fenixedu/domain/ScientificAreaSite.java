package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.commons.lang.StringUtils;

public class ScientificAreaSite extends ScientificAreaSite_Base {

    public ScientificAreaSite() {
	super();
    }

    public ScientificAreaSite(ScientificAreaUnit unit) {
	this();
	if (unit.hasSite()) {
	    throw new DomainException("site.department.unit.already.has.site");
	}
	if (StringUtils.isEmpty(unit.getAcronym())) {
	    throw new DomainException("unit.acronym.cannot.be.null");
	}
	this.setUnit(unit);
    }

    @Override
	public ScientificAreaUnit getUnit() {
		return (ScientificAreaUnit) super.getUnit();
	}

	@Override
	public IGroup getOwner() {
		return new FixedSetGroup(getManagers());
	}

}
