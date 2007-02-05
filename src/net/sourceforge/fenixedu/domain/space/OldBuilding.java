package net.sourceforge.fenixedu.domain.space;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class OldBuilding extends OldBuilding_Base {

    public OldBuilding() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	if (!hasAnyRooms()) {
	    removeCampus();
	    removeRootDomainObject();
	    deleteDomainObject();
	} else {
	    throw new DomainException("");
	}
    }
    
    public static Set<OldBuilding> getOldBuildings() {
	final Set<OldBuilding> oldBuildings = new HashSet<OldBuilding>();
	for (Space space : RootDomainObject.getInstance().getSpacesSet()) {
	    if (space instanceof OldBuilding) {
		OldBuilding oldBuilding = (OldBuilding) space;
		oldBuildings.add(oldBuilding);
	    }
	}
	return oldBuildings;
    }

}
