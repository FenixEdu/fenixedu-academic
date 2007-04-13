package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class SetRootUnit extends Service{

    public void run(final Unit unit, final Boolean institutionUnit ){
	
	if (unit.isPlanetUnit()) {
	    rootDomainObject.setEarthUnit(unit);
	    
	} else if(institutionUnit) {
	    rootDomainObject.setInstitutionUnit(unit);
	   	    
	} else if(!institutionUnit){
	    rootDomainObject.setExternalInstitutionUnit(unit);
	}
    }    
}
