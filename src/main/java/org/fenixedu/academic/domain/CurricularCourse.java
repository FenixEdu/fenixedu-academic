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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.branch.BranchType;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.curricularRules.AndRule;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curricularRules.OrRule;
import org.fenixedu.academic.domain.curricularRules.RestrictionDoneDegreeModule;
import org.fenixedu.academic.domain.curriculum.CurricularCourseType;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLevel;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLoad;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CurricularCourseFunctor;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.log.CurriculumLineLog;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import org.fenixedu.academic.predicate.MarkSheetPredicates;
import org.fenixedu.academic.util.DateFormatUtil;
import org.fenixedu.academic.util.EnrolmentEvaluationState;
import org.fenixedu.academic.util.ReportCache;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class CurricularCourse extends CurricularCourse_Base {

    static final public Comparator<CurricularCourse> CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME =
            new Comparator<CurricularCourse>() {
                @Override
                public int compare(CurricularCourse o1, CurricularCourse o2) {
                    final Degree degree1 = o1.getDegree();
                    final Degree degree2 = o2.getDegree();
                    final Collator collator = Collator.getInstance();
                    final int degreeTypeComp = degree1.getDegreeType().compareTo(degree2.getDegreeType());
                    if (degreeTypeComp != 0) {
                        return degreeTypeComp;
                    }
                    final int degreeNameComp = collator.compare(degree1.getNome(), degree2.getNome());
                    if (degreeNameComp == 0) {
                        return degreeNameComp;
                    }
                    return CurricularCourse.COMPARATOR_BY_NAME.compare(o1, o2);
                }
            };

    public static List<CurricularCourse> readCurricularCourses() {
        List<CurricularCourse> result = new ArrayList<CurricularCourse>();
        for (DegreeModule degreeModule : Bennu.getInstance().getDegreeModulesSet()) {
            if (degreeModule instanceof CurricularCourse) {
                result.add((CurricularCourse) degreeModule);
            }
        }
        return result;
    }

    protected CurricularCourse() {
        super();
        final Double d = Double.valueOf(0d);
        setTheoreticalHours(d);
        setTheoPratHours(d);
        setLabHours(d);
        setPraticalHours(d);
        setCredits(d);
        setEctsCredits(d);
        setWeigth(d);
    }

    protected CurricularCourse(DegreeCurricularPlan degreeCurricularPlan, String name, String code, String acronym,
            Boolean enrolmentAllowed, CurricularStage curricularStage) {
        this();
        checkParameters(name, code, acronym);
        checkForCurricularCourseWithSameAttributes(degreeCurricularPlan, name, code, acronym);
        setName(name);
        setCode(code);
        setAcronym(acronym);
        setEnrollmentAllowed(enrolmentAllowed);
        setCurricularStage(curricularStage);
        setDegreeCurricularPlan(degreeCurricularPlan);
    }

    private void checkParameters(final String name, final String code, final String acronym) {
        if (StringUtils.isEmpty(name)) {
            throw new DomainException("error.curricularCourse.invalid.name");
        }
        if (StringUtils.isEmpty(code)) {
            throw new DomainException("error.curricularCourse.invalid.code");
        }
        if (StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.curricularCourse.invalid.acronym");
        }
    }

    public CurricularCourse(Double weight, String prerequisites, String prerequisitesEn, CurricularStage curricularStage,
            CompetenceCourse competenceCourse, CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
            Integer term,  ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {

        this();
        setWeigth(weight);
        setPrerequisites(prerequisites);
        setPrerequisitesEn(prerequisitesEn);
        setCurricularStage(curricularStage);
        setCompetenceCourse(competenceCourse);
        setType(CurricularCourseType.NORMAL_COURSE);
        new Context(parentCourseGroup, this, curricularPeriod, term, beginExecutionPeriod, endExecutionPeriod);
    }

    public GradeScale getGradeScaleChain() {
        return super.getGradeScale() != null ? super.getGradeScale() : getDegreeCurricularPlan().getGradeScaleChain();
    }

    @Override
    public void print(StringBuilder dcp, String tabs, Context previousContext) {
        String tab = tabs + "\t";
        dcp.append(tab);
        dcp.append("[CC ").append(getExternalId()).append("][");
        dcp.append(previousContext.getCurricularPeriod().getOrderByType(AcademicPeriod.YEAR)).append("Y,");
        dcp.append(previousContext.getCurricularPeriod().getOrderByType(AcademicPeriod.SEMESTER)).append("S]\t");
        dcp.append("[B:").append(previousContext.getBeginExecutionPeriod().getBeginDateYearMonthDay());
        dcp.append(" E:").append(
                previousContext.getEndExecutionPeriod() != null ? previousContext.getEndExecutionPeriod()
                        .getEndDateYearMonthDay() : "          ");
        dcp.append("]\t");
        dcp.append(getName()).append("\n");
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public DegreeCurricularPlan getParentDegreeCurricularPlan() {
        return !getParentContextsSet().isEmpty() ? getParentContextsSet().iterator().next().getParentCourseGroup()
                .getParentDegreeCurricularPlan() : null;
    }

    @Override
    final public DegreeCurricularPlan getDegreeCurricularPlan() {
        return getParentDegreeCurricularPlan();
    }

    public void edit(Double weight, String prerequisites, String prerequisitesEn, CurricularStage curricularStage,
            CompetenceCourse competenceCourse) {

        setWeigth(weight);
        setPrerequisites(prerequisites);
        setPrerequisitesEn(prerequisitesEn);
        setCurricularStage(curricularStage);
        setCompetenceCourse(competenceCourse);
    }

    /**
     * - This method is used to edit a 'special' curricular course that will
     * represent any curricular course according to a rule
     * @deprecated This method sets attributes that are no longer used in regular CurricularCourse objects.
     * Use
     * {@link org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse#edit(String, String, CurricularStage) edit}
     * instead.
     */
    @Deprecated
    public void edit(String name, String nameEn, CurricularStage curricularStage) {
        setName(name);
        setNameEn(nameEn);
        setCurricularStage(curricularStage);
    }

    /**
     * - Edit Pre-Bolonha CurricularCourse
     * @deprecated This method sets attributes that are no longer used. Set the corresponding attributes in the
     * appropriate
     * {@link org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation CompetenceCourseInformation}
     * object.
     */
    public void edit(String name, String nameEn, String code, String acronym, Double weigth, Double credits, Double ectsCredits,
            Integer enrolmentWeigth, Integer minimumValueForAcumulatedEnrollments, Integer maximumValueForAcumulatedEnrollments,
            final Double theoreticalHours, final Double labHours, final Double praticalHours, final Double theoPratHours,
            final GradeScale gradeScale) {
        checkForCurricularCourseWithSameAttributes(getDegreeCurricularPlan(), name, code, acronym);
        setName(name);
        setNameEn(nameEn);
        setCode(code);
        setAcronym(acronym);
        setWeigth(weigth);
        setCredits(credits);
        setEctsCredits(ectsCredits);

        setEnrollmentWeigth(enrolmentWeigth);
        setMinimumValueForAcumulatedEnrollments(minimumValueForAcumulatedEnrollments);
        setMaximumValueForAcumulatedEnrollments(maximumValueForAcumulatedEnrollments);

        setTheoreticalHours(theoreticalHours);
        setLabHours(labHours);
        setPraticalHours(praticalHours);
        setTheoPratHours(theoPratHours);

        super.setGradeScale(gradeScale);
    }

    private void checkForCurricularCourseWithSameAttributes(DegreeCurricularPlan degreeCurricularPlan, String name, String code,
            String acronym) {
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            if (curricularCourse == this) {
                continue;
            }
            if (curricularCourse.getName().equals(name) && curricularCourse.getCode().equals(code)) {
                throw new DomainException("error.curricularCourseWithSameNameAndCode");
            }
            if (curricularCourse.getAcronym().equals(acronym)) {
                throw new DomainException("error.curricularCourseWithSameAcronym");
            }
        }
    }

    @Override
    public void delete() {
        super.delete();
        getCurriculumLineLogsSet().forEach(CurriculumLineLog::delete);
        setUniversity(null);
        setDegreeCurricularPlan(null);
        setCompetenceCourse(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean curricularCourseIsMandatory() {
        return getMandatory().booleanValue();
    }

    public List<CurricularCourseScope> getInterminatedScopes() {
        List<CurricularCourseScope> result = new ArrayList<CurricularCourseScope>();
        for (CurricularCourseScope curricularCourseScope : getScopesSet()) {
            if (curricularCourseScope.getEndDate() == null) {
                result.add(curricularCourseScope);
            }
        }

        return result;
    }

    public List<CurricularCourseScope> getActiveScopes() {
        final List<CurricularCourseScope> activeScopes = new ArrayList<CurricularCourseScope>();
        for (CurricularCourseScope scope : getScopesSet()) {
            if (scope.isActive()) {
                activeScopes.add(scope);
            }
        }
        return activeScopes;
    }

    public boolean hasAnyActiveDegreModuleScope(int year, int semester) {
        for (final DegreeModuleScope degreeModuleScope : getDegreeModuleScopes()) {
            if (degreeModuleScope.isActive(year, semester)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAnyActiveDegreModuleScope() {
        for (final DegreeModuleScope degreeModuleScope : getDegreeModuleScopes()) {
            if (degreeModuleScope.isActive()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAnyActiveDegreModuleScope(final ExecutionSemester executionSemester) {
        for (final DegreeModuleScope degreeModuleScope : getDegreeModuleScopes()) {
            if (degreeModuleScope.isActiveForExecutionPeriod(executionSemester)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAnyActiveDegreModuleScope(final ExecutionYear executionYear) {
        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            if (hasAnyActiveDegreModuleScope(executionSemester)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAnyActiveContext(final ExecutionSemester executionSemester) {
        for (final Context context : getParentContextsSet()) {
            if (context.isValid(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    public List<CurricularCourseScope> getActiveScopesInExecutionPeriod(final ExecutionSemester executionSemester) {
        final List<CurricularCourseScope> result = new ArrayList<CurricularCourseScope>();
        for (final CurricularCourseScope curricularCourseScope : getScopesSet()) {
            if (curricularCourseScope.isActiveForExecutionPeriod(executionSemester)) {
                result.add(curricularCourseScope);
            }
        }
        return result;
    }

    @Deprecated
    public List<DegreeModuleScope> getActiveDegreeModuleScopesInExecutionPeriod(final ExecutionSemester executionSemester) {
        final List<DegreeModuleScope> activeScopesInExecutionPeriod = new ArrayList<DegreeModuleScope>();
        for (final DegreeModuleScope scope : getDegreeModuleScopes()) {
            if (scope.isActiveForExecutionPeriod(executionSemester)) {
                activeScopesInExecutionPeriod.add(scope);
            }
        }
        return activeScopesInExecutionPeriod;
    }

    public List<DegreeModuleScope> getActiveDegreeModuleScopesInAcademicInterval(AcademicInterval academicInterval) {
        final List<DegreeModuleScope> activeScopesInExecutionPeriod = new ArrayList<DegreeModuleScope>();
        for (final DegreeModuleScope scope : getDegreeModuleScopes()) {
            if (scope.isActiveForAcademicInterval(academicInterval)) {
                activeScopesInExecutionPeriod.add(scope);
            }
        }
        return activeScopesInExecutionPeriod;
    }

    public boolean hasActiveScopesInExecutionPeriod(final ExecutionSemester executionSemester) {
        return !getActiveDegreeModuleScopesInExecutionPeriod(executionSemester).isEmpty();
    }

    public Set<CurricularCourseScope> getActiveScopesInExecutionYear(final ExecutionYear executionYear) {
        final Set<CurricularCourseScope> result = new HashSet<CurricularCourseScope>();
        for (final CurricularCourseScope scope : getScopesSet()) {
            if (scope.isActiveForExecutionYear(executionYear)) {
                result.add(scope);
            }
        }
        return result;
    }

    public List<CurricularCourseScope> getActiveScopesIntersectedByExecutionPeriod(final ExecutionSemester executionSemester) {
        final List<CurricularCourseScope> activeScopesInExecutionPeriod = new ArrayList<CurricularCourseScope>();
        for (final CurricularCourseScope scope : getScopesSet()) {
            if (scope.getBeginYearMonthDay().isBefore(executionSemester.getBeginDateYearMonthDay())) {
                if (!scope.hasEndYearMonthDay()
                        || !scope.getEndYearMonthDay().isBefore(executionSemester.getBeginDateYearMonthDay())) {
                    activeScopesInExecutionPeriod.add(scope);
                }
            } else if (!scope.getBeginYearMonthDay().isAfter(executionSemester.getEndDateYearMonthDay())) {
                activeScopesInExecutionPeriod.add(scope);
            }
        }
        return activeScopesInExecutionPeriod;
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes
    // -------------------------------------------------------------

    public boolean hasRestrictionDone(final CurricularCourse precedence) {
        if (!isBolonhaDegree()) {
            throw new DomainException("CurricularCourse.method.only.appliable.to.bolonha.structure");
        }

        final ExecutionSemester currentExecutionPeriod = ExecutionSemester.readActualExecutionSemester();

        for (final CurricularRule curricularRule : getCurricularRulesSet()) {
            if (curricularRule.isValid(currentExecutionPeriod) && curricularRule instanceof RestrictionDoneDegreeModule) {
                final RestrictionDoneDegreeModule restrictionDone = (RestrictionDoneDegreeModule) curricularRule;

                if (restrictionDone.getPrecedenceDegreeModule() == precedence) {
                    return true;
                }
            }
        }

        return false;
    }

    public CurricularYear getCurricularYearByBranchAndSemester(final Branch branch, final Integer semester) {
        return getCurricularYearByBranchAndSemester(branch, semester, new Date());
    }

    public CurricularYear getCurricularYearByBranchAndSemester(final Branch branch, final Integer semester, final Date date) {
        final List<CurricularCourseScope> scopesFound = new ArrayList<CurricularCourseScope>();
        for (CurricularCourseScope scope : getScopesSet()) {
            if (scope.getCurricularSemester().getSemester().equals(semester) && scope.isActive(date)
                    && (scope.getBranch().representsCommonBranch() || (branch != null && branch.equals(scope.getBranch())))) {
                scopesFound.add(scope);
            }
        }
        if (scopesFound.isEmpty()) {
            for (CurricularCourseScope scope : getScopesSet()) {
                if (scope.getCurricularSemester().getSemester().equals(semester) && scope.isActive(date)) {
                    scopesFound.add(scope);
                }
            }
        }

        return getCurricularYearWithLowerYear(scopesFound, date);
    }

    public String getCurricularCourseUniqueKeyForEnrollment() {
        final DegreeType degreeType =
                (getDegreeCurricularPlan() != null && getDegreeCurricularPlan().getDegree() != null) ? getDegreeCurricularPlan()
                        .getDegree().getDegreeType() : null;
        return constructUniqueEnrollmentKey(getCode(), getName(), degreeType);
    }

    public boolean hasActiveScopeInGivenSemester(final Integer semester) {
        for (CurricularCourseScope curricularCourseScope : getScopesSet()) {
            if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester)
                    && curricularCourseScope.isActive().booleanValue()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasScopeInGivenSemester(final Integer semester) {
        for (CurricularCourseScope curricularCourseScope : getScopesSet()) {
            if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasActiveScopeInGivenSemesterForGivenBranch(final CurricularSemester curricularSemester, final Branch branch) {
        final Collection<CurricularCourseScope> scopes = getScopesSet();
        for (final CurricularCourseScope curricularCourseScope : scopes) {
            if (curricularCourseScope.getCurricularSemester().equals(curricularSemester) && curricularCourseScope.isActive()
                    && curricularCourseScope.getBranch().equals(branch)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActiveScopeInGivenSemesterForGivenBranch(final Integer semester, final Branch branch) {
        final Collection<CurricularCourseScope> scopes = getScopesSet();
        for (final CurricularCourseScope curricularCourseScope : scopes) {
            if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester) && curricularCourseScope.isActive()
                    && curricularCourseScope.getBranch().equals(branch)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean hasActiveScopeInGivenSemesterForCommonAndGivenBranch(final Integer semester, final Branch branch) {

        Collection<CurricularCourseScope> scopes = getScopesSet();

        List<CurricularCourseScope> result = (List<CurricularCourseScope>) CollectionUtils.select(scopes, new Predicate() {
            @Override
            public boolean evaluate(Object obj) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) obj;
                return ((curricularCourseScope.getBranch().getBranchType().equals(BranchType.COMNBR) || curricularCourseScope
                        .getBranch().equals(branch))
                        && curricularCourseScope.getCurricularSemester().getSemester().equals(semester) && curricularCourseScope
                        .isActive().booleanValue());
            }
        });

        return !result.isEmpty();
    }

    private CurricularYear getCurricularYearWithLowerYear(List<CurricularCourseScope> listOfScopes, Date date) {

        if (listOfScopes.isEmpty()) {
            return null;
        }

        CurricularYear minCurricularYear = null;

        for (CurricularCourseScope curricularCourseScope : listOfScopes) {
            if (curricularCourseScope.isActive(date).booleanValue()) {
                CurricularYear actualCurricularYear = curricularCourseScope.getCurricularSemester().getCurricularYear();
                if (minCurricularYear == null
                        || minCurricularYear.getYear().intValue() > actualCurricularYear.getYear().intValue()) {
                    minCurricularYear = actualCurricularYear;
                }
            }
        }

        return minCurricularYear;
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes
    // -------------------------------------------------------------

    private String constructUniqueEnrollmentKey(String code, String name, DegreeType degreeType) {
        StringBuilder stringBuffer = new StringBuilder(50);
        stringBuffer.append(code);
        stringBuffer.append(name);
        if (degreeType != null) {
            stringBuffer.append(degreeType.toString());
        }
        return StringUtils.lowerCase(stringBuffer.toString());
    }

    public static class CurriculumFactory implements Serializable {
        private CurricularCourse curricularCourse;

        private String program;

        private String programEn;

        private String generalObjectives;

        private String generalObjectivesEn;

        private String operacionalObjectives;

        private String operacionalObjectivesEn;

        private DateTime lastModification;

        public CurriculumFactory(final CurricularCourse curricularCourse) {
            setCurricularCourse(curricularCourse);
        }

        public String getGeneralObjectives() {
            return generalObjectives;
        }

        public void setGeneralObjectives(String generalObjectives) {
            this.generalObjectives = generalObjectives;
        }

        public String getGeneralObjectivesEn() {
            return generalObjectivesEn;
        }

        public void setGeneralObjectivesEn(String generalObjectivesEn) {
            this.generalObjectivesEn = generalObjectivesEn;
        }

        public String getOperacionalObjectives() {
            return operacionalObjectives;
        }

        public void setOperacionalObjectives(String operacionalObjectives) {
            this.operacionalObjectives = operacionalObjectives;
        }

        public String getOperacionalObjectivesEn() {
            return operacionalObjectivesEn;
        }

        public void setOperacionalObjectivesEn(String operacionalObjectivesEn) {
            this.operacionalObjectivesEn = operacionalObjectivesEn;
        }

        public String getProgram() {
            return program;
        }

        public void setProgram(String program) {
            this.program = program;
        }

        public String getProgramEn() {
            return programEn;
        }

        public void setProgramEn(String programEn) {
            this.programEn = programEn;
        }

        public DateTime getLastModification() {
            return lastModification;
        }

        public void setLastModification(DateTime lastModification) {
            this.lastModification = lastModification;
        }

        public CurricularCourse getCurricularCourse() {
            return curricularCourse;
        }

        public void setCurricularCourse(final CurricularCourse curricularCourse) {
            this.curricularCourse = curricularCourse;
        }

        public String getObjectives() {
            if (!StringUtils.isEmpty(getGeneralObjectives()) && !StringUtils.isEmpty(getOperacionalObjectives())) {
                return getGeneralObjectives() + " " + getOperacionalObjectives();
            }
            return null;
        }

        public String getObjectivesEn() {
            if (!StringUtils.isEmpty(getGeneralObjectivesEn()) && !StringUtils.isEmpty(getOperacionalObjectivesEn())) {
                return getGeneralObjectivesEn() + " " + getOperacionalObjectivesEn();
            }
            return null;
        }
    }

    public Curriculum editCurriculum(String program, String programEn, String generalObjectives, String generalObjectivesEn,
            String operacionalObjectives, String operacionalObjectivesEn, DateTime lastModification) {
        Curriculum curriculum = findLatestCurriculum();
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        if (!curriculum.getLastModificationDateDateTime().isBefore(
                currentExecutionYear.getBeginDateYearMonthDay().toDateMidnight())
                && !curriculum.getLastModificationDateDateTime().isAfter(
                        currentExecutionYear.getEndDateYearMonthDay().toDateMidnight())) {
            curriculum.edit(generalObjectives, operacionalObjectives, program, generalObjectivesEn, operacionalObjectivesEn,
                    programEn);
        } else {
            curriculum =
                    insertCurriculum(program, programEn, operacionalObjectives, operacionalObjectivesEn, generalObjectives,
                            generalObjectivesEn, lastModification);
        }
        return curriculum;
    }

    public Curriculum insertCurriculum(String program, String programEn, String operacionalObjectives,
            String operacionalObjectivesEn, String generalObjectives, String generalObjectivesEn, DateTime lastModification) {

        Curriculum curriculum = new Curriculum();

        curriculum.setCurricularCourse(this);
        curriculum.setProgram(program);
        curriculum.setProgramEn(programEn);
        curriculum.setOperacionalObjectives(operacionalObjectives);
        curriculum.setOperacionalObjectivesEn(operacionalObjectivesEn);
        curriculum.setGeneralObjectives(generalObjectives);
        curriculum.setGeneralObjectivesEn(generalObjectivesEn);
        curriculum.setLastModificationDateDateTime(lastModification);

        return curriculum;
    }

    @SuppressWarnings("unchecked")
    public List<ExecutionCourse> getExecutionCoursesByExecutionPeriod(final ExecutionSemester executionSemester) {
        return (List<ExecutionCourse>) CollectionUtils.select(getAssociatedExecutionCoursesSet(), new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                ExecutionCourse executionCourse = (ExecutionCourse) o;
                return executionCourse.getExecutionPeriod().equals(executionSemester);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<ExecutionCourse> getExecutionCoursesByExecutionYear(final ExecutionYear executionYear) {
        return (List<ExecutionCourse>) CollectionUtils.select(getAssociatedExecutionCoursesSet(), new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                ExecutionCourse executionCourse = (ExecutionCourse) o;
                return executionCourse.getExecutionPeriod().getExecutionYear().equals(executionYear);
            }
        });
    }

    public Curriculum findLatestCurriculum() {
        Curriculum latestCurriculum = null;
        for (final Curriculum curriculum : getAssociatedCurriculumsSet()) {
            if (latestCurriculum == null
                    || latestCurriculum.getLastModificationDateDateTime().isBefore(curriculum.getLastModificationDateDateTime())) {
                latestCurriculum = curriculum;
            }
        }
        return latestCurriculum;
    }

    public Curriculum findLatestCurriculumModifiedBefore(Date date) {
        Curriculum latestCurriculum = null;

        for (Curriculum curriculum : getAssociatedCurriculumsSet()) {
            if (curriculum.getLastModificationDateDateTime().toDate().compareTo(date) == 1) {
                // modified after date
                continue;
            }

            if (latestCurriculum == null) {
                latestCurriculum = curriculum;
                continue;
            }

            DateTime currentLastModificationDate = latestCurriculum.getLastModificationDateDateTime();
            if (currentLastModificationDate.isBefore(curriculum.getLastModificationDateDateTime())) {
                latestCurriculum = curriculum;
            }
        }

        return latestCurriculum;
    }

    final public double getProblemsHours() {
        return getProblemsHours((CurricularPeriod) null, (ExecutionSemester) null);
    }

    final public Double getProblemsHours(final CurricularPeriod curricularPeriod) {
        return getProblemsHours(curricularPeriod, (ExecutionSemester) null);
    }

    final public double getProblemsHours(final ExecutionSemester executionSemester) {
        return getProblemsHours((CurricularPeriod) null, executionSemester);
    }

    final public Double getProblemsHours(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return getCompetenceCourse() != null ? getCompetenceCourse()
                .getProblemsHours(curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
    }

    final public double getLaboratorialHours() {
        return getLaboratorialHours((CurricularPeriod) null, (ExecutionSemester) null);
    }

    final public Double getLaboratorialHours(final CurricularPeriod curricularPeriod) {
        return getLaboratorialHours(curricularPeriod, (ExecutionSemester) null);
    }

    final public double getLaboratorialHours(final ExecutionSemester executionSemester) {
        return getLaboratorialHours((CurricularPeriod) null, executionSemester);
    }

    final public Double getLaboratorialHours(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return getCompetenceCourse() != null ? getCompetenceCourse()
                .getLaboratorialHours(curricularPeriod == null ? null : curricularPeriod.getChildOrder(),
                        executionSemester) : 0.0d;
    }

    final public Double getSeminaryHours() {
        return getSeminaryHours((CurricularPeriod) null, (ExecutionSemester) null);
    }

    final public Double getSeminaryHours(final CurricularPeriod curricularPeriod) {
        return getSeminaryHours(curricularPeriod, (ExecutionSemester) null);
    }

    final public double getSeminaryHours(final ExecutionSemester executionSemester) {
        return getSeminaryHours((CurricularPeriod) null, executionSemester);
    }

    final public Double getSeminaryHours(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return getCompetenceCourse() != null ? getCompetenceCourse()
                .getSeminaryHours(curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
    }

    final public double getFieldWorkHours() {
        return getFieldWorkHours((CurricularPeriod) null, (ExecutionSemester) null);
    }

    final public Double getFieldWorkHours(final CurricularPeriod curricularPeriod) {
        return getFieldWorkHours(curricularPeriod, (ExecutionSemester) null);
    }

    final public double getFieldWorkHours(final ExecutionSemester executionSemester) {
        return getFieldWorkHours((CurricularPeriod) null, executionSemester);
    }

    final public Double getFieldWorkHours(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return getCompetenceCourse() != null ? getCompetenceCourse()
                .getFieldWorkHours(curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
    }

    final public double getTrainingPeriodHours() {
        return getTrainingPeriodHours((CurricularPeriod) null, (ExecutionSemester) null);
    }

    final public Double getTrainingPeriodHours(final CurricularPeriod curricularPeriod) {
        return getTrainingPeriodHours(curricularPeriod, (ExecutionSemester) null);
    }

    final public double getTrainingPeriodHours(final ExecutionSemester executionSemester) {
        return getTrainingPeriodHours((CurricularPeriod) null, executionSemester);
    }

    final public Double getTrainingPeriodHours(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return getCompetenceCourse() != null ? getCompetenceCourse()
                .getTrainingPeriodHours(curricularPeriod == null ? null : curricularPeriod.getChildOrder(),
                        executionSemester) : 0.0d;
    }

    final public double getTutorialOrientationHours() {
        return getTutorialOrientationHours((CurricularPeriod) null, (ExecutionSemester) null);
    }

    final public Double getTutorialOrientationHours(final CurricularPeriod curricularPeriod) {
        return getTutorialOrientationHours(curricularPeriod, (ExecutionSemester) null);
    }

    final public double getTutorialOrientationHours(final ExecutionSemester executionSemester) {
        return getTutorialOrientationHours((CurricularPeriod) null, executionSemester);
    }

    final public Double getTutorialOrientationHours(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return getCompetenceCourse() != null ? getCompetenceCourse()
                .getTutorialOrientationHours(curricularPeriod == null ? null : curricularPeriod.getChildOrder(),
                        executionSemester) : 0.0d;
    }

    final public double getAutonomousWorkHours() {
        return getAutonomousWorkHours((CurricularPeriod) null, (ExecutionSemester) null);
    }

    final public Double getAutonomousWorkHours(final CurricularPeriod curricularPeriod) {
        return getAutonomousWorkHours(curricularPeriod, (ExecutionSemester) null);
    }

    final public double getAutonomousWorkHours(final ExecutionSemester executionSemester) {
        return getAutonomousWorkHours((CurricularPeriod) null, executionSemester);
    }

    final public Double getAutonomousWorkHours(final CurricularPeriod curricularPeriod, final ExecutionYear executionYear) {
        return getCompetenceCourse() != null ? getCompetenceCourse()
                .getAutonomousWorkHours(curricularPeriod.getChildOrder(), executionYear) : 0d;
    }

    final public Double getAutonomousWorkHours(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return getCompetenceCourse() != null ? getCompetenceCourse()
                .getAutonomousWorkHours(curricularPeriod == null ? null : curricularPeriod.getChildOrder(),
                        executionSemester) : 0.0d;
    }

    final public double getContactLoad() {
        return getContactLoad((CurricularPeriod) null, (ExecutionSemester) null);
    }

    final public Double getContactLoad(final CurricularPeriod curricularPeriod) {
        return getContactLoad(curricularPeriod, (ExecutionSemester) null);
    }

    final public double getContactLoad(final ExecutionSemester executionSemester) {
        return getContactLoad((CurricularPeriod) null, executionSemester);
    }

    final public Double getContactLoad(final CurricularPeriod curricularPeriod, final ExecutionYear executionYear) {
        return getContactLoad(curricularPeriod, executionYear == null ? null : executionYear.getLastExecutionPeriod());
    }

    final public Double getContactLoad(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return getCompetenceCourse() != null ? getCompetenceCourse()
                .getContactLoad(curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
    }

    final public double getTotalLoad() {
        return getTotalLoad((CurricularPeriod) null, (ExecutionSemester) null);
    }

    final public Double getTotalLoad(final CurricularPeriod curricularPeriod) {
        return getTotalLoad(curricularPeriod, (ExecutionSemester) null);
    }

    final public double getTotalLoad(final ExecutionSemester executionSemester) {
        return getTotalLoad((CurricularPeriod) null, executionSemester);
    }

    final public Double getTotalLoad(final CurricularPeriod curricularPeriod, final ExecutionYear executionYear) {
        return getTotalLoad(curricularPeriod, executionYear == null ? null : executionYear.getLastExecutionPeriod());
    }

    final public Double getTotalLoad(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return getCompetenceCourse() != null ? getCompetenceCourse()
                .getTotalLoad(curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
    }

    @Override
    final public Double getLabHours() {
        return getLabHours((CurricularPeriod) null, (ExecutionSemester) null);
    }

    final public Double getLabHours(final CurricularPeriod curricularPeriod) {
        return getLabHours(curricularPeriod, (ExecutionSemester) null);
    }

    final public double getLabHours(final ExecutionSemester executionSemester) {
        return getLabHours((CurricularPeriod) null, executionSemester);
    }

    final public Double getLabHours(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return getCompetenceCourse() != null ? getCompetenceCourse()
                .getLaboratorialHours(curricularPeriod == null ? null : curricularPeriod.getChildOrder(),
                        executionSemester) : 0.0d;
    }

    @Override
    final public Double getTheoreticalHours() {
        return getTheoreticalHours((CurricularPeriod) null, (ExecutionSemester) null);
    }

    final public Double getTheoreticalHours(final CurricularPeriod curricularPeriod) {
        return getTheoreticalHours(curricularPeriod, (ExecutionSemester) null);
    }

    final public double getTheoreticalHours(final ExecutionSemester executionSemester) {
        return getTheoreticalHours((CurricularPeriod) null, executionSemester);
    }

    final public Double getTheoreticalHours(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return getCompetenceCourse() != null ? getCompetenceCourse()
                .getTheoreticalHours(curricularPeriod == null ? null : curricularPeriod.getChildOrder(),
                        executionSemester) : 0.0d;
    }

    @Override
    final public Double getPraticalHours() {
        final Double praticalHours = super.getPraticalHours();
        return praticalHours == null ? 0.0d : praticalHours;
    }

    @Override
    final public Double getTheoPratHours() {
        final Double theoPratHours = super.getTheoPratHours();
        return theoPratHours == null ? 0.0d : theoPratHours;
    }

    @Override
    final public Double getCredits() {
        return getEctsCredits();
    }

    @Override
    public Double getEctsCredits() {
        return getEctsCredits((CurricularPeriod) null, (ExecutionSemester) null);
    }

    public Double getEctsCredits(final CurricularPeriod curricularPeriod) {
        return getEctsCredits(curricularPeriod, (ExecutionSemester) null);
    }

    public Double getEctsCredits(final ExecutionSemester executionSemester) {
        return getEctsCredits((CurricularPeriod) null, executionSemester);
    }

    public Double getEctsCredits(final ExecutionYear executionYear) {
        return getEctsCredits((CurricularPeriod) null, executionYear);
    }

    public Double getEctsCredits(final CurricularPeriod curricularPeriod, final ExecutionYear executionYear) {
        return getEctsCredits(curricularPeriod, executionYear == null ? null : executionYear.getLastExecutionPeriod());
    }

    public Double getEctsCredits(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return getEctsCredits(curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester);
    }

    protected Double calculateEctsCredits(final Integer order, final ExecutionSemester executionSemester) {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getEctsCredits(order, executionSemester);
        }
        throw new DomainException("CurricularCourse.with.no.ects.credits");
    }

    public Double getEctsCredits(final Integer order, final ExecutionSemester executionSemester) {
        if (ReportCache.isActive()) {
            final String key = getExternalId() + order + executionSemester.getExternalId();
            final Double cachedValue = ReportCache.read(key);
            if (cachedValue != null) {
                return cachedValue;
            }
            final Double result = calculateEctsCredits(order, executionSemester);
            ReportCache.cache(key, result);
            return result;
        }
        return calculateEctsCredits(order, executionSemester);
    }

    @Override
    public Double getMaxEctsCredits(final ExecutionSemester executionSemester) {
        return getEctsCredits(executionSemester);
    }

    @Override
    public Double getMinEctsCredits(final ExecutionSemester executionSemester) {
        return getEctsCredits(executionSemester);
    }

    @Override
    @Deprecated()
    // typo: use getWeight() instead
    final public Double getWeigth() {
        return getWeight();
    }

    final public Double getWeight() {
        return getEctsCredits();
    }

    final public Double getWeight(ExecutionSemester semester) {
        return getEctsCredits(semester);
    }

    public CurricularSemester getCurricularSemesterWithLowerYearBySemester(Integer semester, Date date) {
        CurricularSemester result = null;
        for (CurricularCourseScope curricularCourseScope : getScopesSet()) {
            if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester)
                    && curricularCourseScope.isActive(date)) {
                if (result == null) {
                    result = curricularCourseScope.getCurricularSemester();
                } else {
                    if (result.getCurricularYear().getYear() > curricularCourseScope.getCurricularSemester().getCurricularYear()
                            .getYear()) {
                        result = curricularCourseScope.getCurricularSemester();
                    }
                }
            }
        }
        return result;
    }

    private List<Enrolment> getActiveEnrollments(ExecutionSemester executionSemester, Registration registration) {
        final List<Enrolment> results = new ArrayList<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                Enrolment enrollment = (Enrolment) curriculumModule;
                boolean filters = true;
                filters &= !enrollment.isAnnulled();
                filters &= executionSemester == null || enrollment.getExecutionPeriod().equals(executionSemester);
                filters &= registration == null || enrollment.getStudentCurricularPlan().getRegistration().equals(registration);

                if (filters) {
                    results.add(enrollment);
                }
            }
        }
        return results;
    }

    public void addNotAnulledEnrolmentsForExecutionPeriod(final Collection<Enrolment> enrolments,
            final ExecutionSemester executionSemester) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (enrolment.isActive() && enrolment.getExecutionPeriod() == executionSemester) {
                    enrolments.add(enrolment);
                }
            }
        }
    }

    @Deprecated
    public void addActiveEnrollments(final Collection<Enrolment> enrolments, final ExecutionSemester executionSemester) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (!enrolment.isAnnulled() && enrolment.getExecutionPeriod() == executionSemester) {
                    enrolments.add(enrolment);
                }
            }
        }
    }

    /**
     * @deprecated {@link #getEnrolmentsByAcademicInterval(AcademicInterval)}
     */
    @Deprecated
    public List<Enrolment> getEnrolmentsByExecutionPeriod(final ExecutionSemester executionSemester) {
        List<Enrolment> result = new ArrayList<Enrolment>();
        addActiveEnrollments(result, executionSemester);
        return result;
    }

    public List<Enrolment> getEnrolmentsByAcademicInterval(AcademicInterval academicInterval) {
        List<Enrolment> result = new ArrayList<Enrolment>();
        addActiveEnrollments(result, academicInterval);
        return result;
    }

    private void addActiveEnrollments(List<Enrolment> enrolments, AcademicInterval academicInterval) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (!enrolment.isAnnulled()
                        && (enrolment.getExecutionPeriod().getAcademicInterval().equals(academicInterval) || enrolment
                                .getExecutionPeriod().getExecutionYear().getAcademicInterval().equals(academicInterval))) {
                    enrolments.add(enrolment);
                }
            }
        }
    }

    public List<Enrolment> getEnrolments() {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                result.add((Enrolment) curriculumModule);
            }
        }
        return result;
    }

    public int countEnrolmentsByExecutionPeriod(final ExecutionSemester executionSemester) {
        return getEnrolmentsByExecutionPeriod(executionSemester).size();
    }

    public List<Enrolment> getEnrolmentsByYear(String year) {
        return getEnrolmentsByExecutionYear(ExecutionYear.readExecutionYearByName(year));
    }

    public int getNumberOfStudentsWithFirstEnrolmentIn(ExecutionSemester executionSemester) {

        Map<Student, List<Enrolment>> students = new HashMap<Student, List<Enrolment>>();
        for (Enrolment enrolment : getAllEnrolmentsUntil(executionSemester)) {
            Student student = enrolment.getStudentCurricularPlan().getRegistration().getStudent();
            if (!students.containsKey(student)) {
                List<Enrolment> enrolments = new ArrayList<Enrolment>();
                enrolments.add(enrolment);
                students.put(student, enrolments);
            } else {
                students.get(student).add(enrolment);
            }
        }

        int count = 0;
        for (Student student : students.keySet()) {
            boolean enrolledInExecutionPeriod = false;
            for (Enrolment enrolment : students.get(student)) {
                if (enrolment.getExecutionPeriod().equals(executionSemester)) {
                    enrolledInExecutionPeriod = true;
                    break;
                }
            }
            if (enrolledInExecutionPeriod && students.get(student).size() == 1) {
                count++;
            }
        }

        return count;
    }

    private List<Enrolment> getAllEnrolmentsUntil(ExecutionSemester executionSemester) {
        List<Enrolment> result = new ArrayList<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                ExecutionSemester enrolmentExecutionPeriod = enrolment.getExecutionPeriod();
                if (!enrolment.isAnnulled() && enrolmentExecutionPeriod.isBeforeOrEquals(executionSemester)) {
                    result.add(enrolment);
                }
            }
        }
        return result;
    }

    public List<Enrolment> getEnrolmentsByExecutionYear(ExecutionYear executionYear) {
        List<Enrolment> result = new ArrayList<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                    result.add(enrolment);
                }
            }
        }
        return result;
    }

    public boolean hasEnrolmentsForExecutionYear(final ExecutionYear executionYear) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Enrolment getEnrolmentByStudentAndYear(Registration registration, String year) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (enrolment.getStudentCurricularPlan().getRegistration().equals(registration)
                        && enrolment.getExecutionPeriod().getExecutionYear().getYear().equals(year)) {
                    return enrolment;
                }
            }
        }
        return null;
    }

    public List<Enrolment> getActiveEnrollments(Registration registration) {
        return getActiveEnrollments(null, registration);
    }

    public List<Enrolment> getActiveEnrollments() {
        return getActiveEnrollments(null, null);
    }

    public List<Enrolment> getActiveEnrollments(ExecutionSemester executionSemester) {
        List<Enrolment> enrolments = new ArrayList<Enrolment>();
        addActiveEnrollments(enrolments, executionSemester);
        return enrolments;
    }

    public List<Dismissal> getDismissals(ExecutionSemester executionSemester) {
        List<Dismissal> dismissals = new ArrayList<Dismissal>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isDismissal()) {
                final Dismissal dismissal = (Dismissal) curriculumModule;
                if (dismissal.getExecutionPeriod() == executionSemester) {
                    dismissals.add(dismissal);
                }
            }
        }
        return dismissals;
    }

    public List<Enrolment> getActiveEnrollments(ExecutionYear executionYear) {
        List<Enrolment> results = new ArrayList<Enrolment>();

        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            results.addAll(getActiveEnrollments(executionSemester));
        }

        return results;
    }

    protected String getBaseName() {
    	return super.getName();
    }

    protected String getBaseNameEn() {
    	return super.getNameEn();
    }

    @Override
    public String getName(ExecutionSemester period) {
        return getCompetenceCourse() != null ? getCompetenceCourse().getName(period) : null;
    }

    @Override
    public String getName() {
        return getName(null);
    }

    @Override
    public String getNameEn(ExecutionSemester period) {
        return getCompetenceCourse() != null ? getCompetenceCourse().getNameEn(period) : null;
    }

    @Override
    public String getNameEn() {
        return getNameEn(null);
    }

    public String getAcronym(ExecutionSemester period) {
        return getCompetenceCourse() != null ? getCompetenceCourse().getAcronym(period) : null;
    }

    @Override
    public String getAcronym() {
        return getAcronym(null);
    }

    public DepartmentUnit getDepartmentUnit(ExecutionSemester semester) {
        return getCompetenceCourse().getDepartmentUnit(semester);
    }

    public DepartmentUnit getDepartmentUnit() {
        return getCompetenceCourse().getDepartmentUnit();
    }

    public Boolean getBasic(ExecutionSemester period) {
        return getCompetenceCourse() != null && getCompetenceCourse().isBasic(period);
    }

    @Override
    public Boolean getBasic() {
        return getBasic(null);
    }

    public String getObjectives(ExecutionSemester period) {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getObjectives(period);
        }
        Curriculum curriculum = findLatestCurriculumModifiedBefore(period.getExecutionYear().getEndDate());
        if (curriculum != null) {
            return curriculum.getFullObjectives();
        }
        return null;
    }

    public String getObjectives() {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getObjectives();
        }
        Curriculum curriculum = findLatestCurriculum();
        if (curriculum != null) {
            return curriculum.getFullObjectives();
        }
        return null;
    }

    public String getObjectivesEn(ExecutionSemester period) {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getObjectivesEn(period);
        }
        Curriculum curriculum = findLatestCurriculumModifiedBefore(period.getExecutionYear().getEndDate());
        if (curriculum != null) {
            return curriculum.getFullObjectivesEn();
        }
        return null;
    }

    public String getObjectivesEn() {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getObjectivesEn();
        }
        Curriculum curriculum = findLatestCurriculum();
        if (curriculum != null) {
            return curriculum.getFullObjectivesEn();
        }
        return null;
    }

    public LocalizedString getObjectivesI18N(ExecutionSemester period) {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getObjectivesI18N(period);
        }
        Curriculum curriculum = findLatestCurriculumModifiedBefore(period.getExecutionYear().getEndDate());
        if (curriculum != null) {
            return curriculum.getFullObjectivesI18N();
        }
        return new LocalizedString();
    }

    public String getProgram(ExecutionSemester period) {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getProgram(period);
        }
        Curriculum curriculum = findLatestCurriculumModifiedBefore(period.getExecutionYear().getEndDate());
        if (curriculum != null) {
            return curriculum.getProgram();
        }
        return null;
    }

    public String getProgram() {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getProgram();
        }
        Curriculum curriculum = findLatestCurriculum();
        if (curriculum != null) {
            return curriculum.getProgram();
        }
        return null;
    }

    public String getProgramEn(ExecutionSemester period) {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getProgramEn(period);
        }
        Curriculum curriculum = findLatestCurriculum();
        if (curriculum != null) {
            return curriculum.getProgramEn();
        }
        return null;
    }

    public String getProgramEn() {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getProgramEn();
        }
        Curriculum curriculum = findLatestCurriculum();
        if (curriculum != null) {
            return curriculum.getProgramEn();
        }
        return null;
    }

    public LocalizedString getProgramI18N(ExecutionSemester period) {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getProgramI18N(period);
        }
        Curriculum curriculum = findLatestCurriculumModifiedBefore(period.getExecutionYear().getEndDate());
        if (curriculum != null) {
            return curriculum.getProgramI18N();
        }
        return new LocalizedString();
    }
       
    public LocalizedString getCrossCompetenceComponentI18N(ExecutionSemester period) {
        if (getCompetenceCourse() != null ) {
            return getCompetenceCourse().getCrossCompetenceComponentI18N(period);
      }       
        return new LocalizedString();
    }
    
    public LocalizedString getLaboratorialComponentI18N(ExecutionSemester period) {
        if (getCompetenceCourse() != null ) {
            return getCompetenceCourse().getLaboratorialComponentI18N(period);
      }       
        return new LocalizedString();
    }
    
    
    public LocalizedString getProgrammingAndComputingComponentI18N(ExecutionSemester period) {
        if (getCompetenceCourse() != null ) {
            return getCompetenceCourse().getProgrammingAndComputingComponentI18N(period);
      }       
        return new LocalizedString();
    }
    
    public LocalizedString getEthicalPrinciplesI18N(ExecutionSemester period) {
        if (getCompetenceCourse() != null ) {
            return getCompetenceCourse().getEthicalPrinciplesI18N(period);
      }       
        return new LocalizedString();
    }
    
    public LocalizedString getPrerequisitesI18N(ExecutionSemester period) {
        if (getCompetenceCourse() != null ) {
            return getCompetenceCourse().getPrerequisitesI18N(period);
      }       
        return new LocalizedString();
    }
    
    
    public LocalizedString getPrerequisitesI18N() {
        return new LocalizedString(org.fenixedu.academic.util.LocaleUtils.PT, getPrerequisites()).with(org.fenixedu.academic.util.LocaleUtils.EN,
                getPrerequisitesEn());
    }

    public String getEvaluationMethod(ExecutionSemester period) {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getEvaluationMethod(period);
        }
        if (hasAnyExecutionCourseIn(period)) {
            return getExecutionCoursesByExecutionPeriod(period).iterator().next().getEvaluationMethodText();
        } else {
            return null;
        }
    }

    public String getEvaluationMethod() {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getEvaluationMethod();
        }
        return null;
    }

    public String getEvaluationMethodEn(ExecutionSemester period) {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getEvaluationMethodEn(period);
        }
        if (hasAnyExecutionCourseIn(period)) {
            return getExecutionCoursesByExecutionPeriod(period).iterator().next().getEvaluationMethodTextEn();
        } else {
            return null;
        }
    }

    public String getEvaluationMethodEn() {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getEvaluationMethodEn();
        }
        return null;
    }

    public LocalizedString getEvaluationMethodI18N(ExecutionSemester period) {
        if (getCompetenceCourse() != null) {
            return new LocalizedString(org.fenixedu.academic.util.LocaleUtils.PT, getCompetenceCourse().getEvaluationMethod(period))
                    .with(org.fenixedu.academic.util.LocaleUtils.EN, getCompetenceCourse().getEvaluationMethodEn(period));
        }
        List<ExecutionCourse> courses = getExecutionCoursesByExecutionPeriod(period);
        if (courses.isEmpty()) {
            return new LocalizedString();
        }
        return new LocalizedString(org.fenixedu.academic.util.LocaleUtils.PT, courses.iterator().next().getEvaluationMethodText()).with(
                org.fenixedu.academic.util.LocaleUtils.EN, courses.iterator().next().getEvaluationMethodTextEn());
    }

    public RegimeType getRegime(final ExecutionSemester period) {
        final CompetenceCourse competenceCourse = getCompetenceCourse();
        return competenceCourse == null ? null : competenceCourse.getRegime(period);
    }

    public RegimeType getRegime(final ExecutionYear executionYear) {
        final CompetenceCourse competenceCourse = getCompetenceCourse();
        return competenceCourse == null ? null : competenceCourse.getRegime(executionYear);
    }

    public RegimeType getRegime() {
        if (getCompetenceCourse() != null) {
            return getCompetenceCourse().getRegime();
        }
        return isOptionalCurricularCourse() ? RegimeType.SEMESTRIAL : null;
    }

    public boolean hasRegime() {
        return getRegime() != null;
    }

    public boolean hasRegime(final ExecutionYear executionYear) {
        return getRegime(executionYear) != null;
    }

    /**
     * Maintened for legacy code compatibility purposes only. Makes no sense to
     * check an Enrolment concept in a CurricularCourse.
     *
     * @return true if CurricularCourseType checks accordingly
     */
    @Deprecated
    final public boolean isPropaedeutic() {
        // if (isBolonhaDegree()) {
        // throw new
        // DomainException("CurricularCourse.must.check.propaedeutic.status.in.enrolment.in.bolonha.degrees");
        // }
        return getType().equals(CurricularCourseType.P_TYPE_COURSE);
    }

    public boolean isOptionalCurricularCourse() {
        return false;
    }

    @Override
    final public boolean isOptional() {
        return getType() == CurricularCourseType.OPTIONAL_COURSE;
    }

    final public boolean isTFC() {
        return getType() == CurricularCourseType.TFC_COURSE;
    }

    @Override
    public boolean isDissertation() {
        CompetenceCourse competenceCourse = getCompetenceCourse();
        return competenceCourse == null ? false : competenceCourse.isDissertation();
    }

    public boolean isAnual() {
        return getCompetenceCourse() != null && getCompetenceCourse().isAnual();
    }

    public boolean isAnual(final ExecutionYear executionYear) {
        return getCompetenceCourse() != null && getCompetenceCourse().isAnual(executionYear);
    }

    public boolean isSemestrial(final ExecutionYear executionYear) {
        return getCompetenceCourse() != null && getCompetenceCourse().isSemestrial(executionYear);
    }

    public boolean isEquivalent(CurricularCourse oldCurricularCourse) {
        return equals(oldCurricularCourse)
                || (getCompetenceCourse() != null && getCompetenceCourse().getAssociatedCurricularCoursesSet().contains(
                        oldCurricularCourse));
    }

    public boolean hasScopeForCurricularYear(final Integer curricularYear, final ExecutionSemester executionSemester) {
        for (final DegreeModuleScope degreeModuleScope : getDegreeModuleScopes()) {
            if (degreeModuleScope.isActiveForExecutionPeriod(executionSemester)
                    && degreeModuleScope.getCurricularYear().equals(curricularYear)) {
                return true;
            }
        }
        return false;
    }

    static public List<CurricularCourse> readByCurricularStage(final CurricularStage curricularStage) {
        final List<CurricularCourse> result = new ArrayList<CurricularCourse>();
        for (CurricularCourse curricularCourse : CurricularCourse.readCurricularCourses()) {
            if (curricularCourse.getCurricularStage() != null && curricularCourse.getCurricularStage().equals(curricularStage)) {
                result.add(curricularCourse);
            }
        }
        return result;
    }

    public Set<CurricularCourseScope> findCurricularCourseScopesIntersectingPeriod(final Date beginDate, final Date endDate) {
        final Set<CurricularCourseScope> curricularCourseScopes = new HashSet<CurricularCourseScope>();
        for (final CurricularCourseScope curricularCourseScope : getScopesSet()) {
            if (curricularCourseScope.intersects(beginDate, endDate)) {
                curricularCourseScopes.add(curricularCourseScope);
            }
        }
        return curricularCourseScopes;
    }

    public Set<CurricularCourseScope> findCurricularCourseScopesIntersectingExecutionCourse(ExecutionCourse executionCourse) {
        AcademicInterval academicInterval = executionCourse.getAcademicInterval();

        return findCurricularCourseScopesIntersectingPeriod(academicInterval.getStart().toDate(), academicInterval.getEnd()
                .toDate());
    }

    public Set<Enrolment> getEnrolmentsNotInAnyMarkSheet(EvaluationSeason season, ExecutionSemester executionSemester) {

        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {

            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;

                if (enrolment.isValid(executionSemester)
                        && (season.isSpecialAuthorization() || (season.equals(enrolment.getEvaluationSeason()) && !enrolment
                                .hasAssociatedMarkSheetOrFinalGrade(season)))) {
                    result.add(enrolment);
                } else if (season.isImprovement()) {
                    if (enrolment.hasImprovementFor(executionSemester) && !enrolment.hasAssociatedMarkSheet(season)) {
                        result.add(enrolment);
                    }
                }
            }
        }

        if (season.isImprovement()) {
            addImprovementEnrolmentsFromEquivalentCourses(result, executionSemester, season);
        }

        return result;
    }

    private void addImprovementEnrolmentsFromEquivalentCourses(Set<Enrolment> result, ExecutionSemester executionSemester,
            EvaluationSeason season) {

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();

        for (final CurricularCourseEquivalence equivalence : getCurricularCourseEquivalencesSet()) {
            if (equivalence.isFrom(degreeCurricularPlan)) {
                addImprovementEnrolments(equivalence, result, executionSemester, season);
            }
        }
    }

    private void addImprovementEnrolments(CurricularCourseEquivalence equivalence, Set<Enrolment> result,
            ExecutionSemester executionSemester, EvaluationSeason season) {

        for (final CurricularCourse curricularCourse : equivalence.getOldCurricularCoursesSet()) {
            if (curricularCourse.getDegreeCurricularPlan().isBolonhaDegree()) {

                for (final CurriculumModule module : curricularCourse.getCurriculumModulesSet()) {
                    if (module.isEnrolment()) {
                        final Enrolment enrolment = (Enrolment) module;

                        if (enrolment.hasImprovementFor(executionSemester) && !enrolment.hasAssociatedMarkSheet(season)) {
                            result.add(enrolment);
                        }
                    }
                }
            }
        }
    }

    public Set<Enrolment> getEnrolmentsNotInAnyMarkSheetForOldMarkSheets(EvaluationSeason season,
            ExecutionSemester executionSemester) {

        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {

            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;

                if (enrolment.isValid(executionSemester) && season.equals(enrolment.getEvaluationSeason())) {
                    if (season.isSpecialAuthorization() || enrolment.canBeSubmittedForOldMarkSheet(season)) {
                        result.add(enrolment);
                    }
                } else if (season.isImprovement()) {
                    if (enrolment.hasImprovementFor(executionSemester) && enrolment.canBeSubmittedForOldMarkSheet(season)) {
                        result.add(enrolment);
                    }
                }
            }
        }
        return result;
    }

    private boolean hasEnrolmentsNotInAnyMarkSheet(ExecutionSemester executionSemester) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {

            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;

                if (enrolment.isValid(executionSemester) && enrolment.getEvaluationSeason().isNormal()) {
                    if (!enrolment.hasAssociatedMarkSheetOrFinalGrade(EvaluationSeason.readNormalSeason())) {
                        return true;
                    }
                }
                if (enrolment.hasImprovement() && !enrolment.hasAssociatedMarkSheet(EvaluationSeason.readImprovementSeason())
                        && enrolment.hasImprovementFor(executionSemester)) {
                    return true;
                }
            }
        }
        return false;
    }

    public MarkSheet createNormalMarkSheet(ExecutionSemester executionSemester, Teacher responsibleTeacher, Date evaluationDate,
            EvaluationSeason season, Boolean submittedByTeacher, Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans,
            Person person) {

        return MarkSheet.createNormal(this, executionSemester, responsibleTeacher, evaluationDate, season, submittedByTeacher,
                evaluationBeans, person);
    }

    public MarkSheet createOldNormalMarkSheet(ExecutionSemester executionSemester, Teacher responsibleTeacher,
            Date evaluationDate, EvaluationSeason season, Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans,
            Person person) {

        return MarkSheet.createOldNormal(this, executionSemester, responsibleTeacher, evaluationDate, season, evaluationBeans,
                person);
    }

    public MarkSheet rectifyEnrolmentEvaluation(MarkSheet markSheet, EnrolmentEvaluation enrolmentEvaluation,
            Date evaluationDate, Grade grade, String reason, Person person) {

        if (markSheet == null || evaluationDate == null || grade.isEmpty()) {
            throw new DomainException("error.markSheet.invalid.arguments");
        }

        if (!markSheet.getEnrolmentEvaluationsSet().contains(enrolmentEvaluation)) {
            throw new DomainException("error.no.student.in.markSheet");
        }

        if (markSheet.isNotConfirmed()) {
            throw new DomainException("error.markSheet.must.be.confirmed");
        }

        if (enrolmentEvaluation.getRectification() != null) {
            throw new DomainException("error.markSheet.student.alreadyRectified", enrolmentEvaluation.getEnrolment()
                    .getStudentCurricularPlan().getRegistration().getNumber().toString());
        }

        enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
        // enrolmentEvaluation.setWhenDateTime(new DateTime());

        MarkSheet rectificationMarkSheet =
                createRectificationMarkSheet(markSheet.getExecutionPeriod(), evaluationDate, markSheet.getResponsibleTeacher(),
                        markSheet.getEvaluationSeason(), reason,
                        new MarkSheetEnrolmentEvaluationBean(enrolmentEvaluation.getEnrolment(), evaluationDate, grade), person);

        // Rectification MarkSheet MUST have only ONE EnrolmentEvaluation
        rectificationMarkSheet.getEnrolmentEvaluationsSet().iterator().next().setRectified(enrolmentEvaluation);
        return rectificationMarkSheet;
    }

    public MarkSheet rectifyOldEnrolmentEvaluation(EnrolmentEvaluation enrolmentEvaluation, EvaluationSeason season,
            Date evaluationDate, Grade newGrade, String reason, Person person) {

        if (enrolmentEvaluation == null || evaluationDate == null || newGrade.isEmpty()) {
            throw new DomainException("error.markSheet.invalid.arguments");
        }

        if (enrolmentEvaluation.getRectification() != null) {
            throw new DomainException("error.markSheet.student.alreadyRectified", enrolmentEvaluation.getEnrolment()
                    .getStudentCurricularPlan().getRegistration().getNumber().toString());
        }

        enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);

        MarkSheet rectificationMarkSheet =
                createRectificationOldMarkSheet(enrolmentEvaluation.getExecutionPeriod(), evaluationDate, enrolmentEvaluation
                        .getPersonResponsibleForGrade().getTeacher(), season, reason, new MarkSheetEnrolmentEvaluationBean(
                        enrolmentEvaluation.getEnrolment(), evaluationDate, newGrade), person);

        // Rectification MarkSheet MUST have only ONE EnrolmentEvaluation
        rectificationMarkSheet.getEnrolmentEvaluationsSet().iterator().next().setRectified(enrolmentEvaluation);
        return rectificationMarkSheet;

    }

    private MarkSheet createRectificationMarkSheet(ExecutionSemester executionSemester, Date evaluationDate,
            Teacher responsibleTeacher, EvaluationSeason season, String reason, MarkSheetEnrolmentEvaluationBean evaluationBean,
            Person person) {

        return MarkSheet.createRectification(this, executionSemester, responsibleTeacher, evaluationDate, season, reason,
                evaluationBean, person);
    }

    public MarkSheet createRectificationOldMarkSheet(ExecutionSemester executionSemester, Date evaluationDate,
            Teacher responsibleTeacher, EvaluationSeason season, String reason, MarkSheetEnrolmentEvaluationBean evaluationBean,
            Person person) {

        return MarkSheet.createOldRectification(this, executionSemester, responsibleTeacher, evaluationDate, season, reason,
                evaluationBean, person);
    }

    public Collection<MarkSheet> searchMarkSheets(ExecutionSemester executionSemester, Teacher teacher, Date evaluationDate,
            MarkSheetState markSheetState, EvaluationSeason season) {

        final String dateFormat = "dd/MM/yyyy";
        final Collection<MarkSheet> result = new HashSet<MarkSheet>();

        for (final MarkSheet markSheet : getMarkSheetsSet()) {
            if (executionSemester != null && markSheet.getExecutionPeriod() != executionSemester) {
                continue;
            }
            if (teacher != null && markSheet.getResponsibleTeacher() != teacher) {
                continue;
            }
            if (evaluationDate != null
                    && DateFormatUtil.compareDates(dateFormat, evaluationDate, markSheet.getEvaluationDateDateTime().toDate()) != 0) {
                continue;
            }
            if (markSheetState != null && markSheet.getMarkSheetState() != markSheetState) {
                continue;
            }
            if (season != null && !markSheet.getEvaluationSeason().equals(season)) {
                continue;
            }
            result.add(markSheet);
        }
        return result;
    }

    public boolean hasScopeInGivenSemesterAndCurricularYearInDCP(CurricularYear curricularYear,
            DegreeCurricularPlan degreeCurricularPlan, final ExecutionSemester executionSemester) {

        if (degreeCurricularPlan == null || getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
            for (DegreeModuleScope degreeModuleScope : getDegreeModuleScopes()) {
                if (degreeModuleScope.isActiveForExecutionPeriod(executionSemester)
                        && (curricularYear == null || degreeModuleScope.getCurricularYear().equals(curricularYear.getYear()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGradeSubmissionAvailableFor(ExecutionSemester executionSemester) {
        return EvaluationSeason.all().anyMatch(s -> s.isGradeSubmissionAvailable(this, executionSemester));
    }

    public ExecutionDegree getExecutionDegreeFor(AcademicInterval academicInterval) {
        return getDegreeCurricularPlan().getExecutionDegreeByAcademicInterval(academicInterval);
    }

    @Deprecated
    public ExecutionDegree getExecutionDegreeFor(ExecutionYear executionYear) {
        return getDegreeCurricularPlan().getExecutionDegreeByYear(executionYear);
    }

    public boolean hasAnyDegreeGradeToSubmit(ExecutionSemester period) {
        return hasEnrolmentsNotInAnyMarkSheet(period);
    }

    public boolean hasAnyDegreeMarkSheetToConfirm(ExecutionSemester period) {
        for (MarkSheet markSheet : getMarkSheetsSet()) {
            if (markSheet.getExecutionPeriod().equals(period) && markSheet.isNotConfirmed()) {
                return true;
            }
        }
        return false;
    }

    public List<DegreeModuleScope> getDegreeModuleScopes() {
        return DegreeModuleScope.getDegreeModuleScopes(this);
    }

    public int countAssociatedStudentsByExecutionPeriodAndEnrolmentNumber(ExecutionSemester executionSemester,
            int enrolmentNumber, int curricularCourseAndExecutionPeriodAssociatedStudents) {

        for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                Enrolment enrolmentsEntry = (Enrolment) curriculumModule;
                if (enrolmentsEntry.getExecutionPeriod() == executionSemester) {

                    StudentCurricularPlan studentCurricularPlanEntry = enrolmentsEntry.getStudentCurricularPlan();
                    int numberOfEnrolmentsForThatCurricularCourseAndExecutionPeriod = 0;

                    for (Enrolment enrolmentsFromStudentCPEntry : studentCurricularPlanEntry.getEnrolmentsSet()) {
                        if (enrolmentsFromStudentCPEntry.getCurricularCourse() == this
                                && (enrolmentsFromStudentCPEntry.getExecutionPeriod().compareTo(executionSemester) <= 0)) {
                            ++numberOfEnrolmentsForThatCurricularCourseAndExecutionPeriod;
                            if (numberOfEnrolmentsForThatCurricularCourseAndExecutionPeriod > enrolmentNumber) {
                                break;
                            }
                        }
                    }

                    if (numberOfEnrolmentsForThatCurricularCourseAndExecutionPeriod == enrolmentNumber) {
                        curricularCourseAndExecutionPeriodAssociatedStudents++;
                    }
                }
            }
        }

        return curricularCourseAndExecutionPeriodAssociatedStudents;
    }

    public Integer getTotalEnrolmentStudentNumber(ExecutionSemester executionSemester) {
        int curricularCourseAndExecutionPeriodStudentNumber = 0;

        for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                Enrolment enrolmentsEntry = (Enrolment) curriculumModule;
                if (enrolmentsEntry.getExecutionPeriod() == executionSemester) {
                    curricularCourseAndExecutionPeriodStudentNumber++;
                }
            }
        }

        return curricularCourseAndExecutionPeriodStudentNumber;
    }

    public Integer getFirstTimeEnrolmentStudentNumber(ExecutionSemester executionSemester) {
        return countAssociatedStudentsByExecutionPeriodAndEnrolmentNumber(executionSemester, 1, 0);
    }

    public Integer getSecondTimeEnrolmentStudentNumber(ExecutionSemester executionSemester) {
        return getTotalEnrolmentStudentNumber(executionSemester) - getFirstTimeEnrolmentStudentNumber(executionSemester);
    }

    public List<ExecutionCourse> getMostRecentExecutionCourses() {
        ExecutionSemester period = ExecutionSemester.readActualExecutionSemester();

        while (period != null) {
            List<ExecutionCourse> executionCourses = getExecutionCoursesByExecutionPeriod(period);
            if (executionCourses != null && !executionCourses.isEmpty()) {
                return executionCourses;
            }

            period = period.getPreviousExecutionPeriod();
        }

        return new ArrayList<ExecutionCourse>();
    }

    public boolean isActive(final ExecutionYear executionYear) {
        final ExecutionYear executionYearToCheck =
                executionYear == null ? ExecutionYear.readCurrentExecutionYear() : executionYear;
        for (final ExecutionSemester executionSemester : executionYearToCheck.getExecutionPeriodsSet()) {
            if (isActive(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    public boolean isActive(final ExecutionSemester executionSemester) {
        return getActiveScopesInExecutionPeriod(executionSemester).size() > 0
                || getActiveDegreeModuleScopesInExecutionPeriod(executionSemester).size() > 0;
    }

    public boolean hasEnrolmentForPeriod(final ExecutionSemester executionSemester) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (!enrolment.isAnnulled() && enrolment.getExecutionPeriod() == executionSemester) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void getAllDegreeModules(final Collection<DegreeModule> degreeModules) {
        degreeModules.add(this);
    }

    public List<CurricularCourseEquivalence> getOldCurricularCourseEquivalences(final DegreeCurricularPlan degreeCurricularPlan) {
        final List<CurricularCourseEquivalence> result = new ArrayList<CurricularCourseEquivalence>();
        for (final CurricularCourseEquivalence curricularCourseEquivalence : getOldCurricularCourseEquivalencesSet()) {
            if (curricularCourseEquivalence.isFrom(degreeCurricularPlan)) {
                result.add(curricularCourseEquivalence);
            }
        }
        return result;
    }

    public List<CurricularCourseEquivalence> getCurricularCourseEquivalencesFor(final CurricularCourse equivalentCurricularCourse) {
        final List<CurricularCourseEquivalence> result = new ArrayList<CurricularCourseEquivalence>();
        for (final CurricularCourseEquivalence curricularCourseEquivalence : getOldCurricularCourseEquivalencesSet()) {
            if (curricularCourseEquivalence.getEquivalentCurricularCourse() == equivalentCurricularCourse) {
                result.add(curricularCourseEquivalence);
            }
        }
        return result;
    }

    @Override
    public boolean isCurricularCourse() {
        return true;
    }

    public DegreeModuleScope getOldestDegreeModuleScope() {
        List<DegreeModuleScope> scopes = new ArrayList<DegreeModuleScope>(getDegreeModuleScopes());
        Collections.sort(scopes, DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME);
        return scopes.iterator().next();
    }

    @Override
    public Integer getMinimumValueForAcumulatedEnrollments() {
        return super.getMinimumValueForAcumulatedEnrollments() == null ? Integer.valueOf(0) : super
                .getMinimumValueForAcumulatedEnrollments();
    }

    @Override
    public Integer getMaximumValueForAcumulatedEnrollments() {
        return super.getMaximumValueForAcumulatedEnrollments() == null ? Integer.valueOf(0) : super
                .getMaximumValueForAcumulatedEnrollments();
    }

    public BigDecimal getTotalHoursByShiftType(ShiftType type, ExecutionSemester executionSemester) {
        if (type != null) {
            Double hours = null;
            switch (type) {
            case TEORICA:
                hours = getTheoreticalHours(executionSemester);
                break;
            case TEORICO_PRATICA:
                hours = getTheoPratHours();
                break;
            case PRATICA:
                hours = getPraticalHours();
                break;
            case PROBLEMS:
                hours = getProblemsHours(executionSemester);
                break;
            case LABORATORIAL:
                hours = getLabHours(executionSemester);
                break;
            case TRAINING_PERIOD:
                hours = getTrainingPeriodHours(executionSemester);
                break;
            case SEMINARY:
                hours = getSeminaryHours(executionSemester);
                break;
            case TUTORIAL_ORIENTATION:
                hours = getTutorialOrientationHours(executionSemester);
                break;
            case FIELD_WORK:
                hours = getFieldWorkHours(executionSemester);
                break;
            default:
                break;
            }
            return hours != null ? BigDecimal.valueOf(hours).multiply(BigDecimal.valueOf(CompetenceCourseLoad.NUMBER_OF_WEEKS)) : null;
        }
        return null;
    }

    public boolean hasAnyExecutionCourseIn(ExecutionSemester executionSemester) {
        for (ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
            if (executionCourse.getExecutionPeriod().equals(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<CurricularCourse> getAllCurricularCourses() {
        return Collections.singleton(this);
    }

    @Override
    public Set<CurricularCourse> getAllCurricularCourses(ExecutionSemester executionSemester) {
        return getAllCurricularCourses();
    }

    public boolean getCanCreateMarkSheet() {
        return !isDissertation() || (isDissertation() && MarkSheetPredicates.checkDissertation(getDegree()));
    }

    public Collection<MarkSheet> getMarkSheetsByPeriod(ExecutionSemester executionSemester) {
        Collection<MarkSheet> markSheets = new HashSet<MarkSheet>();
        for (MarkSheet markSheet : getMarkSheetsSet()) {
            if (markSheet.getExecutionPeriod() == executionSemester) {
                markSheets.add(markSheet);
            }
        }
        return markSheets;
    }

    @Override
    public void doForAllCurricularCourses(final CurricularCourseFunctor curricularCourseFunctor) {
        curricularCourseFunctor.doWith(this);
    }

    public CompetenceCourseLevel getCompetenceCourseLevel() {
        return getCompetenceCourse() != null ? getCompetenceCourse().getCompetenceCourseLevel() : null;
    }

    public boolean hasCompetenceCourseLevel() {
        return getCompetenceCourseLevel() != null;
    }

    public List<EnrolmentEvaluation> getEnrolmentEvaluationsForOldMarkSheet(final ExecutionSemester executionSemester,
            final EvaluationSeason season) {
        final List<EnrolmentEvaluation> res = new ArrayList<EnrolmentEvaluation>();
        for (Enrolment enrolment : getEnrolments()) {
            if (season.isImprovement()) {
                EnrolmentEvaluation latestEnrolmentEvaluationBy = enrolment.getLatestEnrolmentEvaluationBySeason(season);
                if (latestEnrolmentEvaluationBy != null
                        && latestEnrolmentEvaluationBy.getExecutionPeriod().equals(executionSemester)
                        && latestEnrolmentEvaluationBy.isFinal() && latestEnrolmentEvaluationBy.getExamDateYearMonthDay() != null) {
                    res.add(latestEnrolmentEvaluationBy);
                }
            } else {
                if (enrolment.isValid(executionSemester)) {
                    EnrolmentEvaluation latestEnrolmentEvaluationBy = enrolment.getLatestEnrolmentEvaluationBySeason(season);
                    if (latestEnrolmentEvaluationBy != null && latestEnrolmentEvaluationBy.isFinal()
                            && latestEnrolmentEvaluationBy.getExamDateYearMonthDay() != null) {
                        res.add(latestEnrolmentEvaluationBy);
                    }
                }
            }

        }
        return res;
    }

    public boolean hasExecutionDegreeByYearAndCampus(ExecutionYear executionYear, Space campus) {
        return getDegreeCurricularPlan().hasExecutionDegreeByYearAndCampus(executionYear, campus);
    }

    public boolean hasAnyExecutionDegreeFor(ExecutionYear executionYear) {
        return getDegreeCurricularPlan().hasAnyExecutionDegreeFor(executionYear);
    }

    @Override
    public void applyToCurricularCourses(final ExecutionYear executionYear, final Predicate predicate) {
        predicate.evaluate(this);
    }

    @Override
    public void addAssociatedExecutionCourses(final ExecutionCourse associatedExecutionCourses) {
        Collection<ExecutionCourse> executionCourses = getAssociatedExecutionCoursesSet();

        for (ExecutionCourse executionCourse : executionCourses) {
            if (associatedExecutionCourses != executionCourse
                    && executionCourse.getExecutionPeriod() == associatedExecutionCourses.getExecutionPeriod()) {
                throw new DomainException("error.executionCourse.curricularCourse.already.associated");
            }
        }
        super.addAssociatedExecutionCourses(associatedExecutionCourses);
    }

    public String getCodeAndName(final ExecutionInterval executionInterval) {
        final String code = getCode();
        final String name = getNameI18N(executionInterval).getContent();

        return (StringUtils.isEmpty(code) ? "" : code + " - ") + name;
    }

    @Override
    public String getCode() {
       return getCompetenceCourse() != null ? getCompetenceCourse().getCode() : null;
    }

    public void setWeight(final BigDecimal input) {
        super.setWeigth(input == null ? null : input.doubleValue());
    }

    @Deprecated
    /**
     * @deprecated Use {@link #getWeight()} instead.
     *
     */
    public Double getBaseWeight() {
        return super.getWeigth();
    }
}
