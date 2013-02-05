/**
 * 
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

import net.sourceforge.fenixedu.util.BundleUtil;

public enum CandidacyAttributionType {
    // order is important (it is used to return the most relevant element)
    ATTRIBUTED_BY_CORDINATOR, ATTRIBUTED, ATTRIBUTED_NOT_CONFIRMED, NOT_ATTRIBUTED, TOTAL;

    public boolean isFinalAttribution() {
        return this == ATTRIBUTED || this == ATTRIBUTED_BY_CORDINATOR;
    }

    public String getDescription() {
        return BundleUtil.getMessageFromModuleOrApplication("Application", this.getClass().getSimpleName() + "." + this + "."
                + "description");
    }

    public String getDescription(int count) {
        return String.format("%s (%s)", getDescription(), count);
    }

    public String getSimpleLabel() {
        return BundleUtil.getEnumName(this);
    }
}