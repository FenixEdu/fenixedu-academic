/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.util.Collection;
import java.util.Comparator;

import org.apache.commons.collections.CollectionUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author David Santos in Jun 29, 2004
 */

public class CurricularCourseEquivalence extends CurricularCourseEquivalence_Base {

    static final public Comparator<CurricularCourseEquivalence> COMPARATOR_BY_EQUIVALENT_COURSE_NAME =
            new Comparator<CurricularCourseEquivalence>() {
                @Override
                public int compare(CurricularCourseEquivalence o1, CurricularCourseEquivalence o2) {
                    final String name1 = o1.getEquivalentCurricularCourse().getName();
                    final String name2 = o2.getEquivalentCurricularCourse().getName();
                    return String.CASE_INSENSITIVE_ORDER.compare(name1, name2);
                }
            };

    static final public Comparator<CurricularCourseEquivalence> COMPARATOR_BY_EQUIVALENT_COURSE_CODE =
            new Comparator<CurricularCourseEquivalence>() {
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
        setRootDomainObject(Bennu.getInstance());
    }

    public CurricularCourseEquivalence(final DegreeCurricularPlan degreeCurricularPlan,
            final CurricularCourse equivalentCurricularCourse, final Collection<CurricularCourse> oldCurricularCourses) {
        this();
        checkIfEquivalenceAlreadyExists(equivalentCurricularCourse, oldCurricularCourses);

        setDegreeCurricularPlan(degreeCurricularPlan);
        setEquivalentCurricularCourse(equivalentCurricularCourse);
        getOldCurricularCoursesSet().addAll(oldCurricularCourses);
    }

    private void checkIfEquivalenceAlreadyExists(CurricularCourse curricularCourse,
            Collection<CurricularCourse> oldCurricularCourses) {
        int size = oldCurricularCourses.size();
        for (final CurricularCourseEquivalence curricularCourseEquivalence : curricularCourse
                .getCurricularCourseEquivalencesSet()) {
            int sizeOld = curricularCourseEquivalence.getOldCurricularCoursesSet().size();
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
        getOldCurricularCoursesSet().clear();

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean isSatisfied(final Registration registration) {
        boolean result = true;

        final Collection<CurricularCourse> curricularCoursesApprovedByEnrolment =
                registration.getCurricularCoursesApprovedByEnrolment();
        for (final CurricularCourse oldCurricularCourse : getOldCurricularCoursesSet()) {
            result &= curricularCoursesApprovedByEnrolment.contains(oldCurricularCourse);
        }

        return result;
    }

    public boolean isFrom(DegreeCurricularPlan degreeCurricularPlan) {
        return getDegreeCurricularPlan() == degreeCurricularPlan;
    }

}
