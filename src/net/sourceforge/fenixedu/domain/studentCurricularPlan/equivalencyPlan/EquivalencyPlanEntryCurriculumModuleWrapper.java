package net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EquivalencyPlanEntryCurriculumModuleWrapper {

    private static final Comparator<EquivalencyPlanEntryCurriculumModuleWrapper> COMPARATOR_BY_DEGREE_MODULE_NAME = new Comparator<EquivalencyPlanEntryCurriculumModuleWrapper>() {

	@Override
	public int compare(EquivalencyPlanEntryCurriculumModuleWrapper o1, EquivalencyPlanEntryCurriculumModuleWrapper o2) {
	    final DegreeModule d1 = o1.getCurriculumModule().getDegreeModule();
	    final DegreeModule d2 = o2.getCurriculumModule().getDegreeModule();
	    final int c = Collator.getInstance().compare(d1.getName(), d2.getName());
	    return c == 0 ? DomainObject.COMPARATOR_BY_ID.compare(d1, d2) : c;
	}
	
    };

    private final CurriculumModule curriculumModule;

    private final Set<EquivalencePlanEntry> equivalencePlanEntriesToApply = new HashSet<EquivalencePlanEntry>();

    private final Set<EquivalencePlanEntry> removedEquivalencePlanEntries = new HashSet<EquivalencePlanEntry>();

    private final Set<EquivalencyPlanEntryCurriculumModuleWrapper> children = new TreeSet<EquivalencyPlanEntryCurriculumModuleWrapper>(
	    COMPARATOR_BY_DEGREE_MODULE_NAME);

    public EquivalencyPlanEntryCurriculumModuleWrapper(final CurriculumModule curriculumModule) {
	this.curriculumModule = curriculumModule;
    }

    public CurriculumModule getCurriculumModule() {
	return curriculumModule;
    }

    public Set<EquivalencePlanEntry> getEquivalencePlanEntriesToApply() {
	return equivalencePlanEntriesToApply;
    }

    public Set<EquivalencePlanEntry> getRemovedEquivalencePlanEntries() {
	return removedEquivalencePlanEntries;
    }

    public Set<EquivalencyPlanEntryCurriculumModuleWrapper> getChildren() {
	return children;
    }

    public void addEquivalencePlanEntriesToApply(final EquivalencePlanEntry equivalencePlanEntry) {
	equivalencePlanEntriesToApply.add(equivalencePlanEntry);
    }

    public void addRemovedEquivalencePlanEntries(final EquivalencePlanEntry equivalencePlanEntry) {
	removedEquivalencePlanEntries.add(equivalencePlanEntry);
    }

    public void addChildren(final EquivalencyPlanEntryCurriculumModuleWrapper curriculumModule) {
	children.add(curriculumModule);
    }

}
