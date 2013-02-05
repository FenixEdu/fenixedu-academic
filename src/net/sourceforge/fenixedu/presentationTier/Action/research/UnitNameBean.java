package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

public class UnitNameBean implements Serializable {

    boolean external;
    String rawName;
    UnitName unitName;

    public UnitNameBean() {
        external = false;
        setUnitName(null);
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public UnitName getUnitName() {
        return unitName;
    }

    public void setUnitName(UnitName name) {
        this.unitName = name;
    }
}
