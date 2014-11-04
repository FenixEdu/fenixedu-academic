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
package org.fenixedu.academic.domain.degree.enrollment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularCourseEquivalence;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author David Santos in Jun 17, 2004
 */

public class NotNeedToEnrollInCurricularCourse extends NotNeedToEnrollInCurricularCourse_Base {

    public NotNeedToEnrollInCurricularCourse() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public NotNeedToEnrollInCurricularCourse(CurricularCourse curricularCourse, StudentCurricularPlan studentCurricularPlan) {
        this();
        setCurricularCourse(curricularCourse);
        setStudentCurricularPlan(studentCurricularPlan);
    }

    public void delete() {
        setStudentCurricularPlan(null);
        setCurricularCourse(null);
        setRootDomainObject(null);
        getEnrolmentsSet().clear();
        getExternalEnrolmentsSet().clear();
        super.deleteDomainObject();
    }

    public Double getEctsCredits() {
        if (isDueToEquivalence()) {
            return Double.valueOf(0d);
        }
        return getCurricularCourse().getEctsCredits();
    }

    private boolean isDueToEquivalence() {
        return isDueToOtherEnrolmentEquivalence() || isDueToGlobalEquivalence();
    }

    private boolean isDueToGlobalEquivalence() {
        for (final CurricularCourseEquivalence curricularCourseEquivalence : getCurricularCourse()
                .getCurricularCourseEquivalencesSet()) {
            if (curricularCourseEquivalence.isSatisfied(getRegistration())) {
                return true;
            }
        }

        return false;
    }

    private boolean isDueToOtherEnrolmentEquivalence() {
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (getRegistration().hasEnrolments(enrolment)) {
                return true;
            }
        }

        return false;
    }

    public Registration getRegistration() {
        return getStudentCurricularPlan().getRegistration();
    }

    public Collection<IEnrolment> getIEnrolments() {
        Set<IEnrolment> res = new HashSet<IEnrolment>(getEnrolmentsSet());
        res.addAll(getExternalEnrolmentsSet());
        return res;
    }

}
