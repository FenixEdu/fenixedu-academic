package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author David Santos in Jun 29, 2004
 */

public class CurricularCourseEquivalence extends CurricularCourseEquivalence_Base {

    static final public Comparator<CurricularCourseEquivalence> COMPARATOR_BY_EQUIVALENT_COURSE_NAME = new Comparator<CurricularCourseEquivalence>() {
	@Override
	public int compare(CurricularCourseEquivalence o1, CurricularCourseEquivalence o2) {
	    final String name1 = o1.getEquivalentCurricularCourse().getName();
	    final String name2 = o2.getEquivalentCurricularCourse().getName();
	    return String.CASE_INSENSITIVE_ORDER.compare(name1, name2);
	}
    };

    static final public Comparator<CurricularCourseEquivalence> COMPARATOR_BY_EQUIVALENT_COURSE_CODE = new Comparator<CurricularCourseEquivalence>() {
	@Override
	public int compare(CurricularCourseEquivalence o1, CurricularCourseEquivalence o2) {
	    final String code1 = o1.getEquivalentCurricularCourse().getCode();
	    final String code2 = o2.getEquivalentCurricularCourse().getCode();
	    if (code1 == null) {
		return -1;
	    }
	    if (code2 == null) {
		return 1;
	    }
	    return String.CASE_INSENSITIVE_ORDER.compare(code1, code2);
	}
    };

    public CurricularCourseEquivalence() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public CurricularCourseEquivalence(final DegreeCurricularPlan degreeCurricularPlan, final CurricularCourse curricularCourse,
	    final Collection<CurricularCourse> oldCurricularCourses) {
	this();
	checkIfEquivalenceAlreadyExists(curricularCourse, oldCurricularCourses);

	setDegreeCurricularPlan(degreeCurricularPlan);
	setEquivalentCurricularCourse(curricularCourse);
	getOldCurricularCourses().addAll(oldCurricularCourses);
    }

    private void checkIfEquivalenceAlreadyExists(CurricularCourse curricularCourse,
	    Collection<CurricularCourse> oldCurricularCourses) {
	int size = oldCurricularCourses.size();
	for (final CurricularCourseEquivalence curricularCourseEquivalence : curricularCourse.getCurricularCourseEquivalences()) {
	    int sizeOld = curricularCourseEquivalence.getOldCurricularCoursesCount();
	    if ((size == sizeOld)
		    && CollectionUtils.intersection(oldCurricularCourses,
			    curricularCourseEquivalence.getOldCurricularCoursesSet()).size() == size) {
		throw new DomainException("error.exists.curricular.course.equivalency");
	    }
	}
    }

    public void delete() {
	setDegreeCurricularPlan(null);
	setEquivalentCurricularCourse(null);
	getOldCurricularCourses().clear();

	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean isSatisfied(final Registration registration) {
	boolean result = true;

	final Collection<CurricularCourse> curricularCoursesApprovedByEnrolment = registration
		.getCurricularCoursesApprovedByEnrolment();
	for (final CurricularCourse oldCurricularCourse : getOldCurricularCoursesSet()) {
	    result &= curricularCoursesApprovedByEnrolment.contains(oldCurricularCourse);
	}

	return result;
    }

    public boolean isFrom(DegreeCurricularPlan degreeCurricularPlan) {
	return getDegreeCurricularPlan() == degreeCurricularPlan;
    }
}
