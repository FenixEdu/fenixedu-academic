package net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan;

import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;

public class EquivalencyPlanEntryWrapper {

    private final EquivalencePlanEntry equivalencePlanEntry;

    private final boolean isRemovalEntry;

    public EquivalencyPlanEntryWrapper(final EquivalencePlanEntry equivalencePlanEntry, final boolean isRemovalEntry) {
	this.equivalencePlanEntry = equivalencePlanEntry;
	this.isRemovalEntry = isRemovalEntry;
    }

    public EquivalencePlanEntry getEquivalencePlanEntry() {
        return equivalencePlanEntry;
    }

    public boolean isRemovalEntry() {
        return isRemovalEntry;
    }

}
