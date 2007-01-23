package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class SetRootUnit extends Service{

    public void run(final Unit unit, final PartyTypeEnum partyTypeEnum){
	
	if (unit.getType().equals(PartyTypeEnum.PLANET)) {
	    rootDomainObject.setEarthUnit(unit);
	    
	} else if(partyTypeEnum.equals(PartyTypeEnum.EXTERNAL_INSTITUTION)) {
	    rootDomainObject.setExternalInstitutionUnit(unit);
	    
	} else if(partyTypeEnum == null) {
	    rootDomainObject.setInstitutionUnit(unit);
	}
    }    
}
