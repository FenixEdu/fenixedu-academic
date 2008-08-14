package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.space.Building;

/**
 * @author Luis Cruz
 * 
 * 
 */
public class InfoBuilding extends InfoObject {

    private String name;

    public void copyFromDomain(Building building) {
	super.copyFromDomain(building);
	if (building != null) {
	    setName(building.getName());
	}
    }

    public static InfoBuilding newInfoFromDomain(Building building) {
	InfoBuilding infoBuilding = null;
	if (building != null) {
	    infoBuilding = new InfoBuilding();
	    infoBuilding.copyFromDomain(building);
	}

	return infoBuilding;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

}