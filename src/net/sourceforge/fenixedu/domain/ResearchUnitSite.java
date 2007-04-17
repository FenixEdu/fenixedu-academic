package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class ResearchUnitSite extends ResearchUnitSite_Base {
    
    public  ResearchUnitSite() {
        super();
    }

    public ResearchUnitSite(ResearchUnit unit) {
	this();
	this.setResearchUnit(unit);
    }
    
    @Override
    public IGroup getOwner() {
	return null;
    }
    
}
