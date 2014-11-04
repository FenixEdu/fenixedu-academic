package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.FactoryExecutor;
import net.sourceforge.fenixedu.domain.EquivalencePlan;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.util.LogicOperator;

public class EquivalencePlanEntryCreator implements FactoryExecutor, Serializable {

    private EquivalencePlan equivalencePlan;

    private final Set<DegreeModule> originDegreeModules = new HashSet<DegreeModule>();

    private final Set<DegreeModule> destinationDegreeModules = new HashSet<DegreeModule>();

    private DegreeModule originDegreeModuleToAdd;

    private DegreeModule destinationDegreeModuleToAdd;

    private LogicOperator originLogicOperator = LogicOperator.AND;

    private LogicOperator destinationLogicOperator = LogicOperator.AND;

    private Boolean transitiveOrigin = true;

    private Double ectsCredits;

    private CourseGroup destinationDegreeModulesPreviousCourseGroup;

    public EquivalencePlanEntryCreator(final EquivalencePlan equivalencePlan) {
        setEquivalencePlan(equivalencePlan);
    }

    @Override
    public Object execute() {

        final Double ectsCredits;
        if (getEctsCredits() != null && getEctsCredits().doubleValue() > 0) {
            ectsCredits = getEctsCredits();
        } else {
            ectsCredits = null;
        }

        return new EquivalencePlanEntry(getEquivalencePlan(), getOriginDegreeModules(), getDestinationDegreeModules(),
                getDestinationDegreeModulesPreviousCourseGroup(), getOriginLogicOperator(), getDestinationLogicOperator(),
                getTransitiveOrigin(), ectsCredits);

    }

    public EquivalencePlan getEquivalencePlan() {
        return equivalencePlan;
    }

    public void setEquivalencePlan(EquivalencePlan equivalencePlan) {
        this.equivalencePlan = equivalencePlan;
    }

    public Set<DegreeModule> getOriginDegreeModules() {
        final Set<DegreeModule> degreeModules = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME);
        for (final DegreeModule degreeModule : this.originDegreeModules) {
            degreeModules.add(degreeModule);
        }
        return degreeModules;
    }

    public Set<DegreeModule> getDestinationDegreeModules() {
        final Set<DegreeModule> degreeModules = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME);
        for (final DegreeModule degreeModule : this.destinationDegreeModules) {
            degreeModules.add(degreeModule);
        }
        return degreeModules;
    }

    public DegreeModule getOriginDegreeModuleToAdd() {
        return originDegreeModuleToAdd;
    }

    public void setOriginDegreeModuleToAdd(DegreeModule degreeModule) {
        this.originDegreeModuleToAdd = degreeModule;
    }

    public DegreeModule getDestinationDegreeModuleToAdd() {
        return destinationDegreeModuleToAdd;
    }

    public void setDestinationDegreeModuleToAdd(DegreeModule degreeModule) {
        this.destinationDegreeModuleToAdd = degreeModule;
    }

    public void addOrigin(DegreeModule degreeModule) {
        if (degreeModule != null) {
            originDegreeModules.add(degreeModule);
        }
    }

    public void addDestination(DegreeModule degreeModule) {
        if (degreeModule != null) {
            destinationDegreeModules.add(degreeModule);
        }
    }

    public LogicOperator getOriginLogicOperator() {
        return originLogicOperator;
    }

    public void setOriginLogicOperator(LogicOperator originLogicOperator) {
        this.originLogicOperator = originLogicOperator;
    }

    public LogicOperator getDestinationLogicOperator() {
        return destinationLogicOperator;
    }

    public void setDestinationLogicOperator(LogicOperator destinationLogicOperator) {
        this.destinationLogicOperator = destinationLogicOperator;
    }

    public Boolean getTransitiveOrigin() {
        return transitiveOrigin;
    }

    public void setTransitiveOrigin(Boolean transitiveOrigin) {
        this.transitiveOrigin = transitiveOrigin;
    }

    public Double getEctsCredits() {
        return ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
        this.ectsCredits = ectsCredits;
    }

    public void setDestinationDegreeModulesPreviousCourseGroup(final CourseGroup previousCourseGroup) {
        this.destinationDegreeModulesPreviousCourseGroup = previousCourseGroup;
    }

    public CourseGroup getDestinationDegreeModulesPreviousCourseGroup() {
        return this.destinationDegreeModulesPreviousCourseGroup;
    }

}