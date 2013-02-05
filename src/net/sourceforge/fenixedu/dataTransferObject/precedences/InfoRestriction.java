package net.sourceforge.fenixedu.dataTransferObject.precedences;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;

/**
 * @author David Santos on Jul 27, 2004
 */

public abstract class InfoRestriction extends InfoObject {

    protected String restrictionKindResourceKey;

    public String getRestrictionKindResourceKey() {
        return restrictionKindResourceKey;
    }

    public void setRestrictionKindResourceKey(String restrictionKindResourceKey) {
        this.restrictionKindResourceKey = restrictionKindResourceKey;
    }

    public abstract String getArg();
}