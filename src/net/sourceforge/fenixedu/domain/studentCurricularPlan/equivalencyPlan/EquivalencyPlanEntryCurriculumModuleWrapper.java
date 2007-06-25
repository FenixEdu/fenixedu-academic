package net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class EquivalencyPlanEntryCurriculumModuleWrapper {

    private static final Comparator<EquivalencyPlanEntryCurriculumModuleWrapper> COMPARATOR_BY_DEGREE_MODULE_NAME = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_DEGREE_MODULE_NAME).addComparator(new BeanComparator("curriculumModule.degreeModule.name", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_DEGREE_MODULE_NAME).addComparator(new BeanComparator("curriculumModule.degreeModule.idInternal"));
    }

    private final CurriculumModule curriculumModule;

    private final Set<EquivalencePlanEntry> equivalencePlanEntriesToApply = new HashSet<EquivalencePlanEntry>();

    private final Set<EquivalencePlanEntry> removedEquivalencePlanEntries = new HashSet<EquivalencePlanEntry>();

    private final Set<EquivalencyPlanEntryCurriculumModuleWrapper> children = new TreeSet<EquivalencyPlanEntryCurriculumModuleWrapper>(COMPARATOR_BY_DEGREE_MODULE_NAME);

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
