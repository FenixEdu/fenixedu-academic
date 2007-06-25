package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class EquivalencePlanEntry extends EquivalencePlanEntry_Base {

    public static Comparator<EquivalencePlanEntry> COMPARATOR = new Comparator<EquivalencePlanEntry>() {
	public int compare(EquivalencePlanEntry o1, EquivalencePlanEntry o2) {
	    return o1.getCompareString().compareTo(o2.getCompareString());
	}
    };

    protected EquivalencePlanEntry() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(this.getClass().getName());
    }

    public EquivalencePlanEntry(final EquivalencePlan equivalencePlan) {
	this();

	init(equivalencePlan);
    }

    protected void init(EquivalencePlan equivalencePlan) {
	checkParameters(equivalencePlan);

	super.setEquivalencePlan(equivalencePlan);

    }

    private void checkParameters(EquivalencePlan equivalencePlan) {
	if (equivalencePlan == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.EquivalencePlanEntry.equivalencePlan.cannot.be.null");
	}

    }

    public boolean isCourseGroupEntry() {
	return false;
    }

    public boolean isCurricularCourseEntry() {
	return false;
    }

    public void delete() {
	removeRootDomainObject();
	removeEquivalencePlan();
	getEquivalencePlansSet().clear();
	deleteDomainObject();
    }

    abstract public boolean hasAnyDestinationDegreeModuleFor(final DegreeCurricularPlan degreeCurricularPlan);

    public abstract boolean isFor(final DegreeCurricularPlan degreeCurricularPlan);

    public abstract boolean isFor(final DegreeModule degreeModule);

    protected abstract String getCompareString();
}
