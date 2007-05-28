package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class EquivalencePlanEntry extends EquivalencePlanEntry_Base {

    protected EquivalencePlanEntry() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(this.getClass().getName());
    }

    public EquivalencePlanEntry(final EquivalencePlan equivalencePlan,
	    final List<CurricularCourse> oldCurricularCourses, final CurricularCourse newCurricularCourse) {
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

}
