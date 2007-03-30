package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author David Santos in Jun 29, 2004
 */

public class CurricularCourseEquivalence extends CurricularCourseEquivalence_Base {

    public CurricularCourseEquivalence() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public CurricularCourseEquivalence(final DegreeCurricularPlan degreeCurricularPlan,
            final CurricularCourse curricularCourse, final Collection<CurricularCourse> oldCurricularCourses) {
    	this();
    	checkIfEquivalenceAlreadyExists(curricularCourse, oldCurricularCourses);

        setDegreeCurricularPlan(degreeCurricularPlan);
        setEquivalentCurricularCourse(curricularCourse);
        getOldCurricularCourses().addAll(oldCurricularCourses);
    }

    private void checkIfEquivalenceAlreadyExists(CurricularCourse curricularCourse, Collection<CurricularCourse> oldCurricularCourses) {
	int size = oldCurricularCourses.size();
	for (final CurricularCourseEquivalence curricularCourseEquivalence : curricularCourse.getCurricularCourseEquivalences()) {
	    int sizeOld = curricularCourseEquivalence.getOldCurricularCoursesCount();
	    if((size == sizeOld) && CollectionUtils.intersection(oldCurricularCourses, curricularCourseEquivalence.getOldCurricularCoursesSet()).size() == size) {
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

	final Collection<CurricularCourse> curricularCoursesApprovedByEnrolment = registration.getCurricularCoursesApprovedByEnrolment();
	for (final CurricularCourse oldCurricularCourse : getOldCurricularCoursesSet()) {
	    result &= curricularCoursesApprovedByEnrolment.contains(oldCurricularCourse);
	}
	
	return result;
    }
    
}
