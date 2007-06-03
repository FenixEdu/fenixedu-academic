package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

public class CurricularCourseEquivalencePlanEntry extends CurricularCourseEquivalencePlanEntry_Base {

    public static class CurricularCourseEquivalencePlanEntryCreator implements FactoryExecutor, Serializable {
	private DomainReference<EquivalencePlan> equivalencePlan;
	private DomainReference<CurricularCourse> curricularCourse;
	private Set<DomainReference<CurricularCourse>> curricularCourses = new HashSet<DomainReference<CurricularCourse>>();
	private DomainReference<CurricularCourse> curricularCourseToAdd;

	public CurricularCourseEquivalencePlanEntryCreator(final EquivalencePlan equivalencePlan, final CurricularCourse curricularCourse) {
	    setEquivalencePlan(equivalencePlan);
	    setCurricularCourse(curricularCourse);
	}

	public Object execute() {
	    return new CurricularCourseEquivalencePlanEntry(getEquivalencePlan(), getCurricularCourses(), getCurricularCourse());
	}

	public CurricularCourse getCurricularCourse() {
	    return curricularCourse == null ? null : curricularCourse.getObject();
	}

	public void setCurricularCourse(CurricularCourse curricularCourse) {
	    this.curricularCourse = curricularCourse == null ? null : new DomainReference<CurricularCourse>(curricularCourse);
	}

	public EquivalencePlan getEquivalencePlan() {
	    return equivalencePlan == null ? null : equivalencePlan.getObject();
	}

	public void setEquivalencePlan(EquivalencePlan equivalencePlan) {
	    this.equivalencePlan = equivalencePlan == null ? null : new DomainReference<EquivalencePlan>(equivalencePlan);
	}

	public Set<CurricularCourse> getCurricularCourses() {
	    final Set<CurricularCourse> curricularCourses = new HashSet<CurricularCourse>();
	    for (final DomainReference<CurricularCourse> curricularCourse : this.curricularCourses) {
		curricularCourses.add(curricularCourse.getObject());
	    }
	    return curricularCourses;
	}

	public CurricularCourse getCurricularCourseToAdd() {
	    return curricularCourseToAdd == null ? null : curricularCourseToAdd.getObject();
	}

	public void setCurricularCourseToAdd(CurricularCourse curricularCourseToAdd) {
	    this.curricularCourseToAdd = curricularCourseToAdd == null ? null : new DomainReference<CurricularCourse>(curricularCourseToAdd);
	}

	public void add(CurricularCourse curricularCourseToAdd) {
	    if (curricularCourseToAdd != null) {
		curricularCourses.add(new DomainReference<CurricularCourse>(curricularCourseToAdd));
	    }
	}
    }

    protected CurricularCourseEquivalencePlanEntry() {
	super();
    }

    public CurricularCourseEquivalencePlanEntry(final EquivalencePlan equivalencePlan,
	    final Collection<CurricularCourse> oldCurricularCourses, final CurricularCourse newCurricularCourse) {
	this();
	init(equivalencePlan, oldCurricularCourses, newCurricularCourse);
    }

    private void init(final EquivalencePlan equivalencePlan,
	    final Collection<CurricularCourse> oldCurricularCourses, final CurricularCourse newCurricularCourse) {
	super.init(equivalencePlan);
	checkParameters(oldCurricularCourses, newCurricularCourse);
	super.getOldCurricularCourses().addAll(oldCurricularCourses);
	super.setNewCurricularCourse(newCurricularCourse);
    }

    public void checkParameters(Collection<CurricularCourse> oldCurricularCourses,
	    CurricularCourse newCurricularCourse) {
	if (oldCurricularCourses.isEmpty()) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry.oldCurricularCourses.cannot.be.empty");
	}

	if (newCurricularCourse == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry.newCurricularCourse.cannot.be.null");
	}

    }

}
