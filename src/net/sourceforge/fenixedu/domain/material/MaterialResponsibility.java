package net.sourceforge.fenixedu.domain.material;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.resource.Resource;

public class MaterialResponsibility extends MaterialResponsibility_Base {
    
    public MaterialResponsibility(Party owner, Material material) {
        super();
        setResource(material);
        setParty(owner);
        setBegin(material.getAcquisition());
        setEnd(material.getCease());
    }
    
    @Override
    public void delete() {
        super.delete();
    }
    
    @Override
    public void setResource(Resource resource) {
        if(resource == null || !resource.isMaterial()) {
            throw new DomainException("error.MaterialResponsibility.invalid.resource");
        }
	super.setResource(resource);
    }
    
    @Override
    public void setParty(Party party) {
	if(party == null || !party.isUnit() || party.isAggregateUnit()) {
	    throw new DomainException("error.MaterialResponsibility.invalid.owner");
	}
	super.setParty(party);
    }
    
    @Override
    public boolean isMaterialResponsibility() {
        return true;
    }
}
