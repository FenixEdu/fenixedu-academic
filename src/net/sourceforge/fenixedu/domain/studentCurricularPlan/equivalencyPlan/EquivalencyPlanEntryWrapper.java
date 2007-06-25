package net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;

public class EquivalencyPlanEntryWrapper {

    public static final Comparator<EquivalencyPlanEntryWrapper> COMPARATOR = new Comparator<EquivalencyPlanEntryWrapper>() {

	public int compare(EquivalencyPlanEntryWrapper o1, EquivalencyPlanEntryWrapper o2) {
	    return EquivalencePlanEntry.COMPARATOR.compare(o1.equivalencePlanEntry, o2.equivalencePlanEntry);
	}
	
    };

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
