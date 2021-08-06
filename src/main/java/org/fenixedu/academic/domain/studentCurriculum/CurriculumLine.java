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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularCourseEquivalence;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.EnrolmentAction;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.academic.util.predicates.ResultCollection;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.YearMonthDay;

abstract public class CurriculumLine extends CurriculumLine_Base {

    static final public Comparator<CurriculumLine> COMPARATOR_BY_APPROVEMENT_DATE_AND_ID = new Comparator<CurriculumLine>() {
        @Override
        public int compare(CurriculumLine o1, CurriculumLine o2) {
            int result =
                    (o1.getApprovementDate() != null && o2.getApprovementDate() != null) ? o1.getApprovementDate().compareTo(
                            o2.getApprovementDate()) : 0;
            return result == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : result;
        }
    };

    public CurriculumLine() {
        super();
        setCreatedBy(getCurrentUser());
        setUsedInSeparationCycle(false);
    }

    final public ExecutionYear getExecutionYear() {
        final ExecutionSemester executionSemester = getExecutionPeriod();
        return executionSemester == null ? null : executionSemester.getExecutionYear();
    }

    public YearMonthDay getApprovementDate() {
        return isApproved() ? calculateConclusionDate() : null;
    }

    @Override
    final public boolean isLeaf() {
        return true;
    }

    @Override
    final public boolean isRoot() {
        return false;
    }

    @Override
    public boolean isApproved(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
        return false;
    }

    @Override
    public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
        return false;
    }

    @Override
    public boolean isPropaedeutic() {
        return getCurriculumGroup().isPropaedeutic();
    }

    public boolean isExtraCurricular() {
        return getCurriculumGroup().isExtraCurriculum();
    }

    public boolean isOptional() {
        return false;
    }

    public boolean isStandalone() {
        return getCurriculumGroup().isStandalone();
    }

    final protected void validateDegreeModuleLink(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
        if (!curriculumGroup.getDegreeModule().validate(curricularCourse)) {
            throw new DomainException("error.studentCurriculum.curriculumLine.invalid.curriculum.group");
        }
    }

    @Override
    public List<Enrolment> getEnrolments() {
        return Collections.emptyList();
    }

    @Override
    public boolean hasAnyEnrolments() {
        return false;
    }

    @Override
    final public void addApprovedCurriculumLines(final Collection<CurriculumLine> result) {
        if (isApproved()) {
            result.add(this);
        }
    }

    @Override
    final public boolean hasAnyApprovedCurriculumLines() {
        return isApproved();
    }

    @Override
    public void collectDismissals(final List<Dismissal> result) {
        // nothing to do
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
        return getCurriculumGroup() != null ? getCurriculumGroup().getStudentCurricularPlan() : null;
    }

    @Override
    public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        return false;
    }

    @Override
    public ExecutionYear getIEnrolmentsLastExecutionYear() {
        return getExecutionYear();
    }

    @Override
    public void getCurriculumModules(final ResultCollection<CurriculumModule> collection) {
        collection.condicionalAdd(this);
    }

    public CurricularCourse getCurricularCourse() {
        return (CurricularCourse) getDegreeModule();
    }

    final public void setCurricularCourse(CurricularCourse curricularCourse) {
        setDegreeModule(curricularCourse);
    }

    @Override
    public void setDegreeModule(final DegreeModule degreeModule) {
        if (degreeModule != null && !(degreeModule instanceof CurricularCourse)) {
            throw new DomainException("error.curriculumLine.DegreeModuleCanOnlyBeCurricularCourse");
        }
        super.setDegreeModule(degreeModule);
    }

    final public boolean hasCurricularCourse() {
        return getDegreeModule() != null;
    }

    @Override
    public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        return null;
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionSemester executionSemester) {
        return Collections.emptySet();
    }

    @Override
    public Enrolment getApprovedEnrolment(final CurricularCourse curricularCourse) {
        return null;
    }

    @Override
    public Dismissal getDismissal(final CurricularCourse curricularCourse) {
        return null;
    }

    @Override
    public Collection<Enrolment> getSpecialSeasonEnrolments(ExecutionYear executionYear) {
        return Collections.emptySet();
    }

    @Override
    public Collection<Enrolment> getSpecialSeasonEnrolments(ExecutionSemester executionSemester) {
        return Collections.emptySet();
    }
    
    @Override
    public Collection<Enrolment> getExtraordinarySeasonEnrolments(ExecutionYear executionYear) {
        return Collections.emptySet();
    }

    @Override
    public Collection<Enrolment> getExtraordinarySeasonEnrolments(ExecutionSemester executionSemester) {
        return Collections.emptySet();
    }

    @Override
    final public void getAllDegreeModules(final Collection<DegreeModule> degreeModules) {
        degreeModules.add(getDegreeModule());
    }

    @Override
    public Set<CurriculumLine> getAllCurriculumLines() {
        return Collections.singleton(this);
    }

    @Override
    public LocalizedString getName() {
        ExecutionSemester period = getExecutionPeriod();
        CurricularCourse course = getCurricularCourse();
        return new LocalizedString().with(org.fenixedu.academic.util.LocaleUtils.PT, course.getName(period)).with(org.fenixedu.academic.util.LocaleUtils.EN,
                course.getNameEn(period));
    }

    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    protected boolean hasCurricularCourse(final CurricularCourse own, final CurricularCourse other,
            final ExecutionSemester executionSemester) {
        return own.isEquivalent(other) || hasCurricularCourseEquivalence(own, other, executionSemester);
    }

    private boolean hasCurricularCourseEquivalence(final CurricularCourse sourceCurricularCourse,
            final CurricularCourse equivalentCurricularCourse, final ExecutionSemester executionSemester) {
        for (final CurricularCourseEquivalence curricularCourseEquivalence : sourceCurricularCourse
                .getCurricularCourseEquivalencesFor(equivalentCurricularCourse)) {
            if (oldCurricularCoursesAreApproved(curricularCourseEquivalence, executionSemester)) {
                return true;
            }
        }
        return false;
    }

    private boolean oldCurricularCoursesAreApproved(final CurricularCourseEquivalence curricularCourseEquivalence,
            final ExecutionSemester executionSemester) {
        for (final CurricularCourse curricularCourse : curricularCourseEquivalence.getOldCurricularCoursesSet()) {
            if (!getStudentCurricularPlan().isApproved(curricularCourse, executionSemester)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasConcluded(final DegreeModule degreeModule, final ExecutionYear executionYear) {
        return getDegreeModule() == degreeModule && isConcluded(executionYear).value();
    }

    @Override
    public CurriculumLine getApprovedCurriculumLine(CurricularCourse curricularCourse) {
        return isApproved(curricularCourse) ? this : null;
    }

    protected String getCurrentUser() {
        return Authenticate.getUser() != null ? Authenticate.getUser().getUsername() : null;
    }

    abstract public boolean isApproved();

    abstract public boolean isValid(ExecutionSemester executionSemester);

    abstract public ExecutionSemester getExecutionPeriod();

    @Override
    public boolean hasEnrolment(ExecutionSemester executionSemester) {
        return false;
    }

    @Override
    public boolean hasEnrolment(ExecutionYear executionYear) {
        return false;
    }

    @Override
    public boolean isEnroledInSpecialSeason(ExecutionSemester executionSemester) {
        return false;
    }

    @Override
    public boolean isEnroledInSpecialSeason(ExecutionYear executionYear) {
        return false;
    }

    public boolean hasCreatedBy() {
        return getCreatedBy() != null && !getCreatedBy().isEmpty();
    }

    @Override
    public int getNumberOfAllApprovedEnrolments(final ExecutionSemester executionSemester) {
        return 0;
    }

    @Override
    public Set<CurriculumGroup> getAllCurriculumGroups() {
        return Collections.emptySet();
    }

    @Override
    public Set<CurriculumGroup> getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups() {
        return Collections.emptySet();
    }

    abstract protected void createCurriculumLineLog(final EnrolmentAction action);

    abstract public BigDecimal getEctsCreditsForCurriculum();

    abstract public double getAccumulatedEctsCredits(final ExecutionSemester executionSemester);

    abstract public String getModuleTypeName();

	@Override
	public Stream<CurriculumLine> getCurriculumLineStream() {
		return Stream.of(this);
	}

}
