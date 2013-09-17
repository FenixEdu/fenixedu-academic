package net.sourceforge.fenixedu.domain;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeCurricularPlanEquivalencePlan extends DegreeCurricularPlanEquivalencePlan_Base {

    public DegreeCurricularPlanEquivalencePlan(final DegreeCurricularPlan degreeCurricularPlan,
            final DegreeCurricularPlan sourceDegreeCurricularPlan) {
//        check(this, DegreeCurricularPlanEquivalencePlanPredicates.checkPermissionsToCreate);
        super();
        init(degreeCurricularPlan, sourceDegreeCurricularPlan);
    }

    protected void init(DegreeCurricularPlan degreeCurricularPlan, DegreeCurricularPlan sourceDegreeCurricularPlan) {
        checkParameters(degreeCurricularPlan, sourceDegreeCurricularPlan);

        super.setDegreeCurricularPlan(degreeCurricularPlan);
        super.setSourceDegreeCurricularPlan(sourceDegreeCurricularPlan);

    }

    private void checkParameters(DegreeCurricularPlan degreeCurricularPlan, DegreeCurricularPlan sourceDegreeCurricularPlan) {
        if (degreeCurricularPlan == null) {
            throw new DomainException("error.DegreeCurricularPlanEquivalencePlan.degreeCurricularPlan.cannot.be.null");
        }

        if (sourceDegreeCurricularPlan == null) {
            throw new DomainException("error.DegreeCurricularPlanEquivalencePlan.sourceDegreeCurricularPlan.cannot.be.null");
        }

        if (degreeCurricularPlan == sourceDegreeCurricularPlan) {
            throw new DomainException("error.DegreeCurricularPlanEquivalencePlan.source.and.target.cannot.be.the.same");
        }
    }

    public SortedSet<EquivalencePlanEntry> getOrderedEntries() {
        final SortedSet<EquivalencePlanEntry> entries = new TreeSet<EquivalencePlanEntry>(EquivalencePlanEntry.COMPARATOR);
        entries.addAll(getEntriesSet());
        return entries;
    }

    @Override
    public void delete() {
        setDegreeCurricularPlan(null);
        setSourceDegreeCurricularPlan(null);
        super.delete();
    }

    public Degree getSourceDegree() {
        return getSourceDegreeCurricularPlan().getDegree();
    }

    @Deprecated
    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasSourceDegreeCurricularPlan() {
        return getSourceDegreeCurricularPlan() != null;
    }

}
