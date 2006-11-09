package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class SetRootUnit extends Service{

    public void run(Unit unit, PartyTypeEnum partyTypeEnum){
	if(partyTypeEnum == null) {
	    rootDomainObject.setInstitutionUnit(unit);
	} else if(partyTypeEnum.equals(PartyTypeEnum.EXTERNAL_INSTITUTION)) {
	    rootDomainObject.setExternalInstitutionUnit(unit);
	}
    }    
}
