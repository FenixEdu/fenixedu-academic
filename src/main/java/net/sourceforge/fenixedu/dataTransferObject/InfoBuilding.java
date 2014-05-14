package net.sourceforge.fenixedu.dataTransferObject;

import org.fenixedu.spaces.domain.Space;

/**
 * @author Luis Cruz
 * 
 * 
 */
public class InfoBuilding extends InfoObject {

    private String name;

    public void copyFromDomain(Space building) {
        super.copyFromDomain(building);
        if (building != null) {
            setName(building.getName());
        }
    }

    public static InfoBuilding newInfoFromDomain(Space building) {
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