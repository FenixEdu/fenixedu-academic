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
import java.util.Collections;
import java.util.HashSet;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;

public class InternalSubstitution extends InternalSubstitution_Base {

    protected InternalSubstitution() {
        super();
    }

    public InternalSubstitution(final StudentCurricularPlan studentCurricularPlan,
            final Collection<SelectedCurricularCourse> dismissals, final Collection<IEnrolment> enrolments,
            ExecutionSemester executionSemester) {

        this();

        checkEnrolments(studentCurricularPlan, enrolments);
        changeParentCurriculumGroup(studentCurricularPlan, enrolments);

        init(studentCurricularPlan, dismissals, enrolments, executionSemester);
    }

    public InternalSubstitution(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
            Collection<IEnrolment> enrolments, Collection<CurricularCourse> noEnrolCurricularCourses, Double credits,
            ExecutionSemester executionSemester) {

        this();

        checkEnrolments(studentCurricularPlan, enrolments);
        changeParentCurriculumGroup(studentCurricularPlan, enrolments);

        init(studentCurricularPlan, courseGroup, enrolments, noEnrolCurricularCourses, credits, executionSemester);
    }

    public InternalSubstitution(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            Collection<IEnrolment> enrolments, Double credits, ExecutionSemester executionSemester) {

        this();

        checkEnrolments(studentCurricularPlan, enrolments);
        changeParentCurriculumGroup(studentCurricularPlan, enrolments);

        init(studentCurricularPlan, curriculumGroup, enrolments, new HashSet<CurricularCourse>(0), credits, executionSemester);
    }

    private void checkEnrolments(final StudentCurricularPlan studentCurricularPlan, final Collection<IEnrolment> enrolments) {
        for (final IEnrolment iEnrolment : enrolments) {

            if (iEnrolment.isExternalEnrolment()) {
                continue;
            }

            final Enrolment enrolment = (Enrolment) iEnrolment;
            if (enrolment.getStudentCurricularPlan() != studentCurricularPlan) {
                throw new DomainException("error.InternalSubstitution.invalid.enrolment");
            }
        }
    }

    private void changeParentCurriculumGroup(final StudentCurricularPlan studentCurricularPlan,
            final Collection<IEnrolment> enrolments) {

        ensureSourceNoCourseGroupCurriculumGroup(studentCurricularPlan);
        final NoCourseGroupCurriculumGroup curriculumGroup = getInternalCreditsSourceGroup(studentCurricularPlan);

        for (final IEnrolment iEnrolment : enrolments) {
            final Enrolment enrolment = (Enrolment) iEnrolment;
            enrolment.setCurriculumGroup(curriculumGroup);
        }

    }

    private NoCourseGroupCurriculumGroup getInternalCreditsSourceGroup(final StudentCurricularPlan studentCurricularPlan) {
        return studentCurricularPlan
                .getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.INTERNAL_CREDITS_SOURCE_GROUP);
    }

    private void ensureSourceNoCourseGroupCurriculumGroup(final StudentCurricularPlan studentCurricularPlan) {
        if (getInternalCreditsSourceGroup(studentCurricularPlan) == null) {
            studentCurricularPlan
                    .createNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.INTERNAL_CREDITS_SOURCE_GROUP);
        }
    }

    @Override
    protected void disconnect() {
        moveExistingEnrolmentsToExtraCurriculumGroup();
        deleteSourceGroupIfEmpty();
        super.disconnect();
    }

    /**
     * When deleting existing internal substitution all source enrolments are
     * moved to extra curriculum group. In this case, admin office can move to
     * correct group or use it in another credits. Note: wrapper can not have
     * external enrolments so we can cast to enrolment
     */
    private void moveExistingEnrolmentsToExtraCurriculumGroup() {
        final ExtraCurriculumGroup extraCurriculumGroup = ensureExtraCurriculumGroup();

        for (final EnrolmentWrapper wrapper : getEnrolmentsSet()) {
            final Enrolment enrolment = (Enrolment) wrapper.getIEnrolment();
            enrolment.setCurriculumGroup(extraCurriculumGroup);
        }
    }

    private ExtraCurriculumGroup ensureExtraCurriculumGroup() {
        ExtraCurriculumGroup extraCurriculumGroup = getStudentCurricularPlan().getExtraCurriculumGroup();
        if (extraCurriculumGroup == null) {
            extraCurriculumGroup = getStudentCurricularPlan().createExtraCurriculumGroup();
        }
        return extraCurriculumGroup;
    }

    private void deleteSourceGroupIfEmpty() {
        final NoCourseGroupCurriculumGroup group =
                getStudentCurricularPlan().getNoCourseGroupCurriculumGroup(
                        NoCourseGroupCurriculumGroupType.INTERNAL_CREDITS_SOURCE_GROUP);

        if (group != null && group.isDeletable()) {
            group.delete();
        }
    }

    @Override
    public boolean isInternalSubstitution() {
        return true;
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString("resources.StudentResources", I18N.getLocale(), "label.dismissal.InternalSubstitution");
    }

    @Override
    public Curriculum getCurriculum(final Dismissal dismissal, final DateTime when, final ExecutionYear year) {

        Curriculum curriculum = Curriculum.createEmpty(year);

        for (final EnrolmentWrapper wrapper : getEnrolmentsSet()) {
            final Enrolment enrolment = (Enrolment) wrapper.getIEnrolment();

            if (enrolment.wasCreated(when) && isBefore(enrolment, year)) {
                curriculum.add(new Curriculum(dismissal, year, Collections.singleton((ICurriculumEntry) enrolment), Collections
                        .<ICurriculumEntry> emptySet(), Collections.singleton((ICurriculumEntry) enrolment)));
            }
        }

        return curriculum;

    }

    private boolean isBefore(final Enrolment enrolment, final ExecutionYear year) {
        return year == null || enrolment.getExecutionYear().isBefore(year);
    }

}
