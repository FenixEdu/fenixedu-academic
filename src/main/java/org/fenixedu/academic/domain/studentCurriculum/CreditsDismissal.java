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
package org.fenixedu.academic.domain.studentCurriculum;

import java.util.Collection;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.log.CreditsDismissalLog;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.EnrolmentAction;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

public class CreditsDismissal extends CreditsDismissal_Base {

    public CreditsDismissal() {
        super();
    }

    public CreditsDismissal(Credits credits, CurriculumGroup curriculumGroup,
            Collection<CurricularCourse> noEnrolCurricularCourses) {
        checkIfCanCreate(credits, noEnrolCurricularCourses, curriculumGroup);
        init(credits, curriculumGroup);
        checkParameters(credits);
        if (noEnrolCurricularCourses != null) {
            getNoEnrolCurricularCoursesSet().addAll(noEnrolCurricularCourses);
        }
        createCurriculumLineLog(EnrolmentAction.ENROL);
    }

    private void checkIfCanCreate(final Credits credits, final Collection<CurricularCourse> noEnrolCurricularCourses,
            final CurriculumGroup curriculumGroup) {

        for (final Dismissal dismissal : curriculumGroup.getChildDismissals()) {
            if (dismissal.isCreditsDismissal()) {
                final CreditsDismissal creditsDismissal = (CreditsDismissal) dismissal;
                if (isSimilar(credits, noEnrolCurricularCourses, creditsDismissal)) {
                    throw new DomainException("error.CreditsDismissal.already.exists.similar",
                            curriculumGroup.getName().getContent());
                }
            }
        }
    }

    private boolean isSimilar(final Credits credits, final Collection<CurricularCourse> curricularCourses,
            final CreditsDismissal creditsDismissalToCheck) {
        boolean result = true;
        result &= hasSameEctsCredits(credits.getGivenCredits(), creditsDismissalToCheck);
        result &= hasSameSourceIEnrolments(credits.getIEnrolments(), creditsDismissalToCheck);
        result &= curricularCourses == null || hasSameNoEnrolCurricularCourses(curricularCourses, creditsDismissalToCheck);
        return result;
    }

    private void checkParameters(final Credits credits) {
        if (credits.getGivenCredits() == null) {
            throw new DomainException("error.CreditsDismissal.invalid.credits");
        }
    }

    @Override
    public boolean isApproved(CurricularCourse curricularCourse, ExecutionInterval executionInterval) {
        return (executionInterval == null || getExecutionInterval().isBeforeOrEquals(executionInterval))
                && hasEquivalentNoEnrolCurricularCourse(curricularCourse);
    }

    private boolean hasEquivalentNoEnrolCurricularCourse(CurricularCourse curricularCourse) {
        for (CurricularCourse course : getNoEnrolCurricularCoursesSet()) {
            if (course.isEquivalent(curricularCourse)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Double getEctsCredits() {
        return getCredits().getGivenCredits();
    }

    @Override
    public boolean hasDegreeModule(DegreeModule degreeModule) {
        return false;
    }

    @Override
    public LocalizedString getName() {
        return BundleUtil.getLocalizedString(Bundle.ACADEMIC, "label.group.credits");
    }

    @Override
    public Dismissal getDismissal(final CurricularCourse curricularCourse) {
        return getNoEnrolCurricularCoursesSet().contains(curricularCourse) ? this : null;
    }

    @Override
    public void delete() {
        getNoEnrolCurricularCoursesSet().clear();
        super.delete();
    }

    @Override
    void deleteFromCredits() {
        getNoEnrolCurricularCoursesSet().clear();
        super.deleteFromCredits();
    }

    @Override
    public boolean isCreditsDismissal() {
        return true;
    }

    @Override
    public boolean isSimilar(final Dismissal dismissal) {
        return dismissal.isCreditsDismissal() && hasSameSourceIEnrolments(getSourceIEnrolments(), dismissal)
                && hasSameNoEnrolCurricularCourses(getNoEnrolCurricularCoursesSet(), (CreditsDismissal) dismissal)
                && hasSameEctsCredits(getEctsCredits(), (CreditsDismissal) dismissal);
    }

    private boolean hasSameNoEnrolCurricularCourses(final Collection<CurricularCourse> curricularCourses,
            final CreditsDismissal dismissal) {
        return curricularCourses.containsAll(dismissal.getNoEnrolCurricularCoursesSet())
                && curricularCourses.size() == dismissal.getNoEnrolCurricularCoursesSet().size();
    }

    private boolean hasSameEctsCredits(final Double ectsCredits, final CreditsDismissal dismissal) {
        return ectsCredits.equals(dismissal.getEctsCredits());
    }

    @Override
    protected void createCurriculumLineLog(final EnrolmentAction action) {
        new CreditsDismissalLog(action, getRegistration(), getCurriculumGroup(), getCredits(), getExecutionPeriod(),
                getCurrentUser());
    }

}
