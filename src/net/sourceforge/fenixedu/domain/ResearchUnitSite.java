package net.sourceforge.fenixedu.domain;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class ResearchUnitSite extends ResearchUnitSite_Base {
    
    public  ResearchUnitSite() {
        super();
    }

    public ResearchUnitSite(ResearchUnit unit) {
	this();
	if(unit.hasSite()) {
	    throw new DomainException("site.department.unit.already.has.site");
	}
	if(StringUtils.isEmpty(unit.getAcronym())) {
	    throw new DomainException("unit.acronym.cannot.be.null");
	}
	this.setUnit(unit);
    }
    
    @Override
    public ResearchUnit getUnit() {
        return (ResearchUnit) super.getUnit();
    }
    
    @Override
    public IGroup getOwner() {
	return new FixedSetGroup(getManagers());
    }
    
}
