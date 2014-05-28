/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain;

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

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularCourseFunctor;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.predicates.MarkSheetPredicates;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CurricularCourse extends CurricularCourse_Base {

    private static final double ECTS_CREDITS_FOR_PRE_BOLONHA = 6;

    private static final double WEIGHT_FOR_PRE_BOLONHA = 6;

    static final public Comparator<CurricularCourse> CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME =
            new Comparator<CurricularCourse>() {
                @Override
                public int compare(CurricularCourse o1, CurricularCourse o2) {
                    final Degree degree1 = o1.getDegree();
                    final Degree degree2 = o2.getDegree();
                    final Collator collator = Collator.getInstance();
                    final int degreeTypeComp =
                            collator.compare(degree1.getDegreeType().getName(), degree2.getDegreeType().getName());
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
        if (curricularStage == CurricularStage.OLD) {
            setRegimeType(RegimeType.SEMESTRIAL);
        }
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
            ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {

        this();
        setWeigth(weight);
        setPrerequisites(prerequisites);
        setPrerequisitesEn(prerequisitesEn);
        setCurricularStage(curricularStage);
        setCompetenceCourse(competenceCourse);
        setType(CurricularCourseType.NORMAL_COURSE);
        new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
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
        dcp.append(" E:")
                .append(previousContext.hasEndExecutionPeriod() ? previousContext.getEndExecutionPeriod()
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

    /**
     * Temporary method, after all degrees migration this is no longer necessary
     */
    private boolean isBoxStructure() {
        return !(getCurricularStage() == CurricularStage.OLD);
    }

    @Override
    public DegreeCurricularPlan getParentDegreeCurricularPlan() {
        if (isBoxStructure()) {
            return hasAnyParentContexts() ? getParentContexts().iterator().next().getParentCourseGroup()
                    .getParentDegreeCurricularPlan() : null;
        } else {
            return super.getDegreeCurricularPlan();
        }
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
     */
    public void edit(String name, String nameEn, CurricularStage curricularStage) {
        setName(name);
        setNameEn(nameEn);
        setCurricularStage(curricularStage);
    }

    /**
     * - Edit Pre-Bolonha CurricularCourse
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
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
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
        setUniversity(null);
        setScientificArea(null);
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
        for (CurricularCourseScope curricularCourseScope : getScopes()) {
            if (curricularCourseScope.getEndDate() == null) {
                result.add(curricularCourseScope);
            }
        }

        return result;
    }

    public List<CurricularCourseScope> getActiveScopes() {
        final List<CurricularCourseScope> activeScopes = new ArrayList<CurricularCourseScope>();
        for (CurricularCourseScope scope : getScopes()) {
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
        for (final Context context : getParentContexts()) {
            if (context.isValid(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    public List<CurricularCourseScope> getActiveScopesInExecutionPeriod(final ExecutionSemester executionSemester) {
        final List<CurricularCourseScope> result = new ArrayList<CurricularCourseScope>();
        for (final CurricularCourseScope curricularCourseScope : getScopes()) {
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
        for (final CurricularCourseScope scope : getScopes()) {
            if (scope.isActiveForExecutionYear(executionYear)) {
                result.add(scope);
            }
        }
        return result;
    }

    public List<CurricularCourseScope> getActiveScopesIntersectedByExecutionPeriod(final ExecutionSemester executionSemester) {
        final List<CurricularCourseScope> activeScopesInExecutionPeriod = new ArrayList<CurricularCourseScope>();
        for (final CurricularCourseScope scope : getScopes()) {
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
        final Collection<CurricularCourseScope> scopes = getScopes();
        for (final CurricularCourseScope curricularCourseScope : scopes) {
            if (curricularCourseScope.getCurricularSemester().equals(curricularSemester) && curricularCourseScope.isActive()
                    && curricularCourseScope.getBranch().equals(branch)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActiveScopeInGivenSemesterForGivenBranch(final Integer semester, final Branch branch) {
        final Collection<CurricularCourseScope> scopes = getScopes();
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

        Collection<CurricularCourseScope> scopes = getScopes();

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

    public static class CurriculumFactoryInsertCurriculum extends CurriculumFactory implements FactoryExecutor {
        public CurriculumFactoryInsertCurriculum(CurricularCourse curricularCourse, ExecutionCourse executionCourse) {
            super(curricularCourse);
            setLastModification(executionCourse.getExecutionPeriod().getBeginDateYearMonthDay().toDateTimeAtMidnight());
        }

        @Override
        public Curriculum execute() {
            final CurricularCourse curricularCourse = getCurricularCourse();
            return curricularCourse == null ? null : curricularCourse.insertCurriculum(getProgram(), getProgramEn(),
                    getGeneralObjectives(), getGeneralObjectivesEn(), getOperacionalObjectives(), getOperacionalObjectivesEn(),
                    getLastModification());
        }

    }

    public static class CurriculumFactoryEditCurriculum extends CurriculumFactory implements FactoryExecutor {
        private Curriculum curriculum;

        public CurriculumFactoryEditCurriculum(CurricularCourse curricularCourse) {
            super(curricularCourse);
            setLastModification(new DateTime());
            curriculum = null;
        }

        public CurriculumFactoryEditCurriculum(Curriculum curriculum) {
            super(curriculum.getCurricularCourse());
            this.curriculum = curriculum;
        }

        public Curriculum getCurriculum() {
            return curriculum;
        }

        public void setCurriculum(Curriculum curriculum) {
            this.curriculum = curriculum;
            populateCurriculum(this, curriculum);
        }

        @Override
        public Curriculum execute() {
            final Curriculum curriculum = getCurriculum();
            if (curriculum == null) {
                final CurricularCourse curricularCourse = getCurricularCourse();
                return curricularCourse == null ? null : curricularCourse.editCurriculum(getProgram(), getProgramEn(),
                        getGeneralObjectives(), getGeneralObjectivesEn(), getOperacionalObjectives(),
                        getOperacionalObjectivesEn(), getLastModification());
            } else {
                final DateTime dt = curriculum.getLastModificationDateDateTime();
                curriculum.edit(getGeneralObjectives(), getOperacionalObjectives(), getProgram(), getGeneralObjectivesEn(),
                        getOperacionalObjectivesEn(), getProgramEn());
                curriculum.setLastModificationDateDateTime(dt);
                return curriculum;
            }
        }
    }

    public CurriculumFactoryEditCurriculum getCurriculumFactoryEditCurriculum() {
        final CurriculumFactoryEditCurriculum curriculumFactoryEditCurriculum = new CurriculumFactoryEditCurriculum(this);
        final Curriculum curriculum = findLatestCurriculum();
        populateCurriculum(curriculumFactoryEditCurriculum, curriculum);
        return curriculumFactoryEditCurriculum;
    }

    public CurriculumFactoryEditCurriculum getCurriculumFactoryEditCurriculum(ExecutionSemester period) {
        final CurriculumFactoryEditCurriculum curriculumFactoryEditCurriculum = new CurriculumFactoryEditCurriculum(this);
        final Curriculum curriculum = findLatestCurriculumModifiedBefore(period.getExecutionYear().getEndDate());
        populateCurriculum(curriculumFactoryEditCurriculum, curriculum);
        return curriculumFactoryEditCurriculum;
    }

    private static void populateCurriculum(final CurriculumFactoryEditCurriculum curriculumFactoryEditCurriculum,
            final Curriculum curriculum) {
        if (curriculum != null) {
            curriculumFactoryEditCurriculum.setGeneralObjectives(curriculum.getGeneralObjectives());
            curriculumFactoryEditCurriculum.setGeneralObjectivesEn(curriculum.getGeneralObjectivesEn());
            curriculumFactoryEditCurriculum.setOperacionalObjectives(curriculum.getOperacionalObjectives());
            curriculumFactoryEditCurriculum.setOperacionalObjectivesEn(curriculum.getOperacionalObjectivesEn());
            curriculumFactoryEditCurriculum.setProgram(curriculum.getProgram());
            curriculumFactoryEditCurriculum.setProgramEn(curriculum.getProgramEn());
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

    public List<ExecutionCourse> getExecutionCoursesWithPublicSites() {
        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();

        for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
            if (executionCourse.hasSite()) {
                result.add(executionCourse);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public List<ExecutionCourse> getExecutionCoursesByExecutionPeriod(final ExecutionSemester executionSemester) {
        return (List<ExecutionCourse>) CollectionUtils.select(getAssociatedExecutionCourses(), new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                ExecutionCourse executionCourse = (ExecutionCourse) o;
                return executionCourse.getExecutionPeriod().equals(executionSemester);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<ExecutionCourse> getExecutionCoursesByExecutionYear(final ExecutionYear executionYear) {
        return (List<ExecutionCourse>) CollectionUtils.select(getAssociatedExecutionCourses(), new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                ExecutionCourse executionCourse = (ExecutionCourse) o;
                return executionCourse.getExecutionPeriod().getExecutionYear().equals(executionYear);
            }
        });
    }

    public Curriculum findLatestCurriculum() {
        Curriculum latestCurriculum = null;
        for (final Curriculum curriculum : getAssociatedCurriculums()) {
            if (latestCurriculum == null
                    || latestCurriculum.getLastModificationDateDateTime().isBefore(curriculum.getLastModificationDateDateTime())) {
                latestCurriculum = curriculum;
            }
        }
        return latestCurriculum;
    }

    public Curriculum findLatestCurriculumModifiedBefore(Date date) {
        Curriculum latestCurriculum = null;

        for (Curriculum curriculum : getAssociatedCurriculums()) {
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
        return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getProblemsHours(
                curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
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
        return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getLaboratorialHours(
                curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
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
        return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getSeminaryHours(
                curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
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
        return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getFieldWorkHours(
                curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
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
        return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getTrainingPeriodHours(
                curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
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

    final public Double getTutorialOrientationHours(final CurricularPeriod curricularPeriod,
            final ExecutionSemester executionSemester) {
        return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getTutorialOrientationHours(
                curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
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
        return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getAutonomousWorkHours(
                curricularPeriod.getChildOrder(), executionYear) : 0d;
    }

    final public Double getAutonomousWorkHours(final CurricularPeriod curricularPeriod, final ExecutionSemester executionSemester) {
        return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getAutonomousWorkHours(
                curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
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
        if (isBolonhaDegree()) {
            return hasCompetenceCourse() ? getCompetenceCourse().getContactLoad(
                    curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
        } else {
            return getContactLoadForPreBolonha();
        }
    }

    private Double getContactLoadForPreBolonha() {
        final Double theoreticalHours = getTheoreticalHours() == null ? 0d : getTheoreticalHours();
        final Double theoPratHours = getTheoPratHours() == null ? 0d : getTheoPratHours();
        final Double praticalHours = getPraticalHours() == null ? 0d : getPraticalHours();
        final Double labHours = getLabHours() == null ? 0d : getLabHours();
        return CompetenceCourseLoad.NUMBER_OF_WEEKS
                * (theoreticalHours.doubleValue() + theoPratHours.doubleValue() + praticalHours.doubleValue() + labHours
                        .doubleValue());
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
        if (isBolonhaDegree()) {
            return hasCompetenceCourse() ? getCompetenceCourse().getTotalLoad(
                    curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
        } else {
            return getAutonomousWorkHours() + getContactLoadForPreBolonha();
        }
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
        if (isBolonhaDegree()) {
            return hasCompetenceCourse() ? getCompetenceCourse().getLaboratorialHours(
                    curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
        } else {
            final Double labHours = super.getLabHours();
            return labHours == null ? 0.0d : labHours;
        }
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
        if (isBolonhaDegree()) {
            return hasCompetenceCourse() ? getCompetenceCourse().getTheoreticalHours(
                    curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionSemester) : 0.0d;
        } else {
            final Double theoreticalHours = super.getTheoreticalHours();
            return theoreticalHours == null ? 0.0d : theoreticalHours;
        }
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
        return isBolonhaDegree() ? getEctsCredits() : super.getCredits();
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

    public Double getEctsCredits(final Integer order, final ExecutionSemester executionSemester) {
        if (isBolonhaDegree()) {
            if (hasCompetenceCourse()) {
                return getCompetenceCourse().getEctsCredits(order, executionSemester);
            } else if (isOptionalCurricularCourse()) {
                return Double.valueOf(0.0);
            }
        } else {
            final Double credits = getCredits();
            if (getDegreeType().isMasterDegree() && credits != null) {
                return credits;
            }

            final Double ectsCredits = super.getEctsCredits();
            return (ectsCredits == null || ectsCredits == 0.0) ? ECTS_CREDITS_FOR_PRE_BOLONHA : ectsCredits;
        }

        throw new DomainException("CurricularCourse.with.no.ects.credits");
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
        if (isBolonhaDegree()) {
            return getEctsCredits();
        }

        if (getDegreeType().isMasterDegree()) {
            return getCredits();
        }

        final Double weigth = super.getWeigth();
        return (weigth == null || weigth == 0.0) ? WEIGHT_FOR_PRE_BOLONHA : weigth;
    }

    final public Double getWeight(ExecutionSemester semester) {
        if (isBolonhaDegree()) {
            return getEctsCredits(semester);
        }

        if (getDegreeType().isMasterDegree()) {
            return getCredits();
        }

        final Double weigth = super.getWeigth();
        return (weigth == null || weigth == 0.0) ? WEIGHT_FOR_PRE_BOLONHA : weigth;
    }

    public CurricularSemester getCurricularSemesterWithLowerYearBySemester(Integer semester, Date date) {
        CurricularSemester result = null;
        for (CurricularCourseScope curricularCourseScope : getScopes()) {
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
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
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
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
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
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (!enrolment.isAnnulled() && enrolment.getExecutionPeriod() == executionSemester) {
                    enrolments.add(enrolment);
                }
            }
        }
    }

    /**
     * @use {@link #getEnrolmentsByAcademicInterval(AcademicInterval)}
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
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
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
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
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
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
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
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
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
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
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
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
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

        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            results.addAll(getActiveEnrollments(executionSemester));
        }

        return results;
    }

    @Override
    public String getName(ExecutionSemester period) {
        final String superName = super.getName();
        return ((superName == null || superName.length() == 0) && hasCompetenceCourse()) ? getCompetenceCourse().getName(period) : superName;
    }

    @Override
    public String getName() {
        return getName(null);
    }

    @Override
    public String getNameEn(ExecutionSemester period) {
        final String superNameEn = super.getNameEn();
        return ((superNameEn == null || superNameEn.length() == 0) && hasCompetenceCourse()) ? getCompetenceCourse().getNameEn(
                period) : superNameEn;
    }

    @Override
    public String getNameEn() {
        return getNameEn(null);
    }

    public String getAcronym(ExecutionSemester period) {
        return (super.getAcronym() == null || super.getAcronym().length() == 0) && hasCompetenceCourse() ? getCompetenceCourse()
                .getAcronym(period) : super.getAcronym();
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
        return ((super.getBasic() == null) && hasCompetenceCourse()) ? getCompetenceCourse().isBasic(period) : super.getBasic();
    }

    @Override
    public Boolean getBasic() {
        return getBasic(null);
    }

    public String getObjectives(ExecutionSemester period) {
        return getObjectivesI18N(period).getContent(MultiLanguageString.pt);
        /* if (isBolonhaDegree()) {
             return getCompetenceCourse().getObjectives(period);
         }
         return getCurriculumFactoryEditCurriculum(period).getObjectives();*/
    }

    public String getObjectives() {
        if (isBolonhaDegree()) {
            return getCompetenceCourse().getObjectives();
        }
        return getCurriculumFactoryEditCurriculum().getObjectives();
    }

    public String getObjectivesEn(ExecutionSemester period) {
        return getObjectivesI18N(period).getContent(MultiLanguageString.en);
        /*if (isBolonhaDegree()) {
            return getCompetenceCourse().getObjectivesEn(period);
        }
        return getCurriculumFactoryEditCurriculum(period).getObjectivesEn();*/
    }

    public String getObjectivesEn() {
        if (isBolonhaDegree()) {
            return getCompetenceCourse().getObjectivesEn();
        }
        return getCurriculumFactoryEditCurriculum().getObjectivesEn();
    }

    public MultiLanguageString getObjectivesI18N(ExecutionSemester period) {
        if (isBolonhaDegree()) {
            return new MultiLanguageString(MultiLanguageString.pt, getCompetenceCourse().getObjectives(period)).with(
                    MultiLanguageString.en, getCompetenceCourse().getObjectivesEn(period));
        }
        return new MultiLanguageString(MultiLanguageString.pt, getCurriculumFactoryEditCurriculum(period).getObjectives()).with(
                MultiLanguageString.en, getCurriculumFactoryEditCurriculum(period).getObjectivesEn());
    }

    public String getProgram(ExecutionSemester period) {
        if (isBolonhaDegree()) {
            return getCompetenceCourse().getProgram(period);
        }
        return getCurriculumFactoryEditCurriculum(period).getProgram();
    }

    public String getProgram() {
        if (isBolonhaDegree()) {
            return getCompetenceCourse().getProgram();
        }
        return getCurriculumFactoryEditCurriculum().getProgram();
    }

    public String getProgramEn(ExecutionSemester period) {
        if (isBolonhaDegree()) {
            return getCompetenceCourse().getProgramEn(period);
        }
        return getCurriculumFactoryEditCurriculum(period).getProgramEn();
    }

    public String getProgramEn() {
        if (isBolonhaDegree()) {
            return getCompetenceCourse().getProgramEn();
        }
        return getCurriculumFactoryEditCurriculum().getProgramEn();
    }

    public MultiLanguageString getProgramI18N(ExecutionSemester period) {
        if (isBolonhaDegree()) {
            return new MultiLanguageString(MultiLanguageString.pt, getCompetenceCourse().getProgram(period)).with(
                    MultiLanguageString.en, getCompetenceCourse().getProgramEn(period));
        }
        return new MultiLanguageString(MultiLanguageString.pt, getCurriculumFactoryEditCurriculum(period).getProgram()).with(
                MultiLanguageString.en, getCurriculumFactoryEditCurriculum(period).getProgramEn());
    }

    public MultiLanguageString getPrerequisitesI18N() {
        return new MultiLanguageString(MultiLanguageString.pt, getPrerequisites()).with(MultiLanguageString.en,
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

    public MultiLanguageString getEvaluationMethodI18N(ExecutionSemester period) {
        if (isBolonhaDegree()) {
            return new MultiLanguageString(MultiLanguageString.pt, getCompetenceCourse().getEvaluationMethod(period)).with(
                    MultiLanguageString.en, getCompetenceCourse().getEvaluationMethodEn(period));
        }
        return new MultiLanguageString(MultiLanguageString.pt, getExecutionCoursesByExecutionPeriod(period).iterator().next()
                .getEvaluationMethodText()).with(MultiLanguageString.en, getExecutionCoursesByExecutionPeriod(period).iterator()
                .next().getEvaluationMethodTextEn());
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
        if (hasCompetenceCourse()) {
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
        if (!isBolonhaDegree()) {
            return getRegimeType() == RegimeType.ANUAL;
        }
        return hasCompetenceCourse() && getCompetenceCourse().isAnual();
    }

    public boolean isAnual(final ExecutionYear executionYear) {
        if (!isBolonhaDegree()) {
            return getRegimeType() == RegimeType.ANUAL;
        }
        return hasCompetenceCourse() && getCompetenceCourse().isAnual(executionYear);
    }

    public boolean isSemestrial(final ExecutionYear executionYear) {
        if (!isBolonhaDegree()) {
            return getRegimeType() == RegimeType.SEMESTRIAL;
        }
        return hasCompetenceCourse() && getCompetenceCourse().isSemestrial(executionYear);
    }

    public boolean isEquivalent(CurricularCourse oldCurricularCourse) {
        return equals(oldCurricularCourse)
                || (hasCompetenceCourse() && getCompetenceCourse().getAssociatedCurricularCoursesSet().contains(
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

    public Set<Enrolment> getEnrolmentsNotInAnyMarkSheet(MarkSheetType markSheetType, ExecutionSemester executionSemester) {

        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {

            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;

                if (enrolment.isValid(executionSemester)
                        && markSheetType.getEnrolmentEvaluationType() == enrolment.getEnrolmentEvaluationType()) {
                    if (markSheetType == MarkSheetType.SPECIAL_AUTHORIZATION
                            || !enrolment.hasAssociatedMarkSheetOrFinalGrade(markSheetType)) {
                        result.add(enrolment);
                    }
                } else if (markSheetType == MarkSheetType.IMPROVEMENT) {
                    if (enrolment.hasImprovementFor(executionSemester) && !enrolment.hasAssociatedMarkSheet(markSheetType)) {
                        result.add(enrolment);
                    }
                }
            }
        }

        if (markSheetType == MarkSheetType.IMPROVEMENT) {
            addImprovementEnrolmentsFromEquivalentCourses(result, executionSemester, markSheetType);
        }

        return result;
    }

    private void addImprovementEnrolmentsFromEquivalentCourses(Set<Enrolment> result, ExecutionSemester executionSemester,
            MarkSheetType markSheetType) {

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();

        for (final CurricularCourseEquivalence equivalence : getCurricularCourseEquivalences()) {
            if (equivalence.isFrom(degreeCurricularPlan)) {
                addImprovementEnrolments(equivalence, result, executionSemester, markSheetType);
            }
        }
    }

    private void addImprovementEnrolments(CurricularCourseEquivalence equivalence, Set<Enrolment> result,
            ExecutionSemester executionSemester, MarkSheetType markSheetType) {

        for (final CurricularCourse curricularCourse : equivalence.getOldCurricularCourses()) {
            if (curricularCourse.getDegreeCurricularPlan().isBolonhaDegree()) {

                for (final CurriculumModule module : curricularCourse.getCurriculumModules()) {
                    if (module.isEnrolment()) {
                        final Enrolment enrolment = (Enrolment) module;

                        if (enrolment.hasImprovementFor(executionSemester) && !enrolment.hasAssociatedMarkSheet(markSheetType)) {
                            result.add(enrolment);
                        }
                    }
                }
            }
        }
    }

    public Set<Enrolment> getEnrolmentsNotInAnyMarkSheetForOldMarkSheets(MarkSheetType markSheetType,
            ExecutionSemester executionSemester) {

        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {

            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;

                if (enrolment.isValid(executionSemester)
                        && markSheetType.getEnrolmentEvaluationType() == enrolment.getEnrolmentEvaluationType()) {
                    if (markSheetType == MarkSheetType.SPECIAL_AUTHORIZATION
                            || enrolment.canBeSubmittedForOldMarkSheet(markSheetType.getEnrolmentEvaluationType())) {
                        result.add(enrolment);
                    }
                } else if (markSheetType == MarkSheetType.IMPROVEMENT) {
                    if (enrolment.hasImprovementFor(executionSemester)
                            && enrolment.canBeSubmittedForOldMarkSheet(markSheetType.getEnrolmentEvaluationType())) {
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

                if (enrolment.isValid(executionSemester)
                        && enrolment.getEnrolmentEvaluationType() == EnrolmentEvaluationType.NORMAL) {
                    if (!enrolment.hasAssociatedMarkSheetOrFinalGrade(MarkSheetType.NORMAL)) {
                        return true;
                    }
                }
                if (enrolment.hasImprovement() && !enrolment.hasAssociatedMarkSheet(MarkSheetType.IMPROVEMENT)
                        && enrolment.hasImprovementFor(executionSemester)) {
                    return true;
                }
            }
        }
        return false;
    }

    public MarkSheet createNormalMarkSheet(ExecutionSemester executionSemester, Teacher responsibleTeacher, Date evaluationDate,
            MarkSheetType markSheetType, Boolean submittedByTeacher,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Person person) {

        return MarkSheet.createNormal(this, executionSemester, responsibleTeacher, evaluationDate, markSheetType,
                submittedByTeacher, evaluationBeans, person);
    }

    public MarkSheet createOldNormalMarkSheet(ExecutionSemester executionSemester, Teacher responsibleTeacher,
            Date evaluationDate, MarkSheetType markSheetType, Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans,
            Person person) {

        return MarkSheet.createOldNormal(this, executionSemester, responsibleTeacher, evaluationDate, markSheetType,
                evaluationBeans, person);
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

        if (enrolmentEvaluation.hasRectification()) {
            throw new DomainException("error.markSheet.student.alreadyRectified", enrolmentEvaluation.getEnrolment()
                    .getStudentCurricularPlan().getRegistration().getNumber().toString());
        }

        enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
        // enrolmentEvaluation.setWhenDateTime(new DateTime());

        MarkSheet rectificationMarkSheet =
                createRectificationMarkSheet(markSheet.getExecutionPeriod(), evaluationDate, markSheet.getResponsibleTeacher(),
                        markSheet.getMarkSheetType(), reason,
                        new MarkSheetEnrolmentEvaluationBean(enrolmentEvaluation.getEnrolment(), evaluationDate, grade), person);

        // Rectification MarkSheet MUST have only ONE EnrolmentEvaluation
        rectificationMarkSheet.getEnrolmentEvaluations().iterator().next().setRectified(enrolmentEvaluation);
        return rectificationMarkSheet;
    }

    public MarkSheet rectifyOldEnrolmentEvaluation(EnrolmentEvaluation enrolmentEvaluation, MarkSheetType markSheetType,
            Date evaluationDate, Grade newGrade, String reason, Person person) {

        if (enrolmentEvaluation == null || evaluationDate == null || newGrade.isEmpty()) {
            throw new DomainException("error.markSheet.invalid.arguments");
        }

        if (enrolmentEvaluation.hasRectification()) {
            throw new DomainException("error.markSheet.student.alreadyRectified", enrolmentEvaluation.getEnrolment()
                    .getStudentCurricularPlan().getRegistration().getNumber().toString());
        }

        enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);

        MarkSheet rectificationMarkSheet =
                createRectificationOldMarkSheet(enrolmentEvaluation.getExecutionPeriod(), evaluationDate, enrolmentEvaluation
                        .getPersonResponsibleForGrade().getTeacher(), markSheetType, reason,
                        new MarkSheetEnrolmentEvaluationBean(enrolmentEvaluation.getEnrolment(), evaluationDate, newGrade),
                        person);

        // Rectification MarkSheet MUST have only ONE EnrolmentEvaluation
        rectificationMarkSheet.getEnrolmentEvaluations().iterator().next().setRectified(enrolmentEvaluation);
        return rectificationMarkSheet;

    }

    private MarkSheet createRectificationMarkSheet(ExecutionSemester executionSemester, Date evaluationDate,
            Teacher responsibleTeacher, MarkSheetType markSheetType, String reason,
            MarkSheetEnrolmentEvaluationBean evaluationBean, Person person) {

        return MarkSheet.createRectification(this, executionSemester, responsibleTeacher, evaluationDate, markSheetType, reason,
                evaluationBean, person);
    }

    public MarkSheet createRectificationOldMarkSheet(ExecutionSemester executionSemester, Date evaluationDate,
            Teacher responsibleTeacher, MarkSheetType markSheetType, String reason,
            MarkSheetEnrolmentEvaluationBean evaluationBean, Person person) {

        return MarkSheet.createOldRectification(this, executionSemester, responsibleTeacher, evaluationDate, markSheetType,
                reason, evaluationBean, person);
    }

    public Collection<MarkSheet> searchMarkSheets(ExecutionSemester executionSemester, Teacher teacher, Date evaluationDate,
            MarkSheetState markSheetState, MarkSheetType markSheetType) {

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
            if (markSheetType != null && markSheet.getMarkSheetType() != markSheetType) {
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
        return isGradeSubmissionAvailableFor(executionSemester, MarkSheetType.NORMAL)
                || isGradeSubmissionAvailableFor(executionSemester, MarkSheetType.IMPROVEMENT)
                || isGradeSubmissionAvailableFor(executionSemester, MarkSheetType.SPECIAL_SEASON);
    }

    public boolean isGradeSubmissionAvailableFor(ExecutionSemester executionSemester, MarkSheetType type) {
        final ExecutionDegree executionDegree = getExecutionDegreeFor(executionSemester.getExecutionYear());
        switch (type) {
        case NORMAL:
        case IMPROVEMENT:
            if (executionSemester.getSemester().equals(Integer.valueOf(1))) {
                return executionDegree.isDateInFirstSemesterNormalSeasonOfGradeSubmission(new YearMonthDay());
            } else {
                return executionDegree.isDateInSecondSemesterNormalSeasonOfGradeSubmission(new YearMonthDay());
            }

        case SPECIAL_SEASON:
            return executionDegree.isDateInSpecialSeasonOfGradeSubmission(new YearMonthDay());

        default:
            return false;
        }
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

    private int countAssociatedStudentsByExecutionPeriodAndEnrolmentNumber(ExecutionSemester executionSemester,
            int enrolmentNumber) {
        int curricularCourseAndExecutionPeriodAssociatedStudents = 0;

        for (CurriculumModule curriculumModule : getCurriculumModules()) {
            if (curriculumModule.isEnrolment()) {
                Enrolment enrolmentsEntry = (Enrolment) curriculumModule;
                if (enrolmentsEntry.getExecutionPeriod() == executionSemester) {

                    StudentCurricularPlan studentCurricularPlanEntry = enrolmentsEntry.getStudentCurricularPlan();
                    int numberOfEnrolmentsForThatCurricularCourseAndExecutionPeriod = 0;

                    for (Enrolment enrolmentsFromStudentCPEntry : studentCurricularPlanEntry.getEnrolmentsSet()) {
                        if (enrolmentsFromStudentCPEntry.getCurricularCourse() == this
                                && (enrolmentsFromStudentCPEntry.getExecutionPeriod().compareTo(executionSemester) <= 0)) {
                            ++numberOfEnrolmentsForThatCurricularCourseAndExecutionPeriod;
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

        for (CurriculumModule curriculumModule : getCurriculumModules()) {
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
        return countAssociatedStudentsByExecutionPeriodAndEnrolmentNumber(executionSemester, 1);
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
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
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
        for (final CurricularCourseEquivalence curricularCourseEquivalence : getOldCurricularCourseEquivalences()) {
            if (curricularCourseEquivalence.isFrom(degreeCurricularPlan)) {
                result.add(curricularCourseEquivalence);
            }
        }
        return result;
    }

    public List<CurricularCourseEquivalence> getCurricularCourseEquivalencesFor(final CurricularCourse equivalentCurricularCourse) {
        final List<CurricularCourseEquivalence> result = new ArrayList<CurricularCourseEquivalence>();
        for (final CurricularCourseEquivalence curricularCourseEquivalence : getOldCurricularCourseEquivalences()) {
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
        return hasCompetenceCourse() ? getCompetenceCourse().getCompetenceCourseLevel() : null;
    }

    public boolean hasCompetenceCourseLevel() {
        return getCompetenceCourseLevel() != null;
    }

    public List<EnrolmentEvaluation> getEnrolmentEvaluationsForOldMarkSheet(final ExecutionSemester executionSemester,
            final MarkSheetType markSheetType) {
        final List<EnrolmentEvaluation> res = new ArrayList<EnrolmentEvaluation>();
        for (Enrolment enrolment : getEnrolments()) {
            if (markSheetType == MarkSheetType.IMPROVEMENT) {
                EnrolmentEvaluation latestEnrolmentEvaluationBy =
                        enrolment.getLatestEnrolmentEvaluationBy(markSheetType.getEnrolmentEvaluationType());
                if (latestEnrolmentEvaluationBy != null
                        && latestEnrolmentEvaluationBy.getExecutionPeriod().equals(executionSemester)
                        && latestEnrolmentEvaluationBy.isFinal() && latestEnrolmentEvaluationBy.getExamDateYearMonthDay() != null) {
                    res.add(latestEnrolmentEvaluationBy);
                }
            } else {
                if (enrolment.isValid(executionSemester)) {
                    EnrolmentEvaluation latestEnrolmentEvaluationBy =
                            enrolment.getLatestEnrolmentEvaluationBy(markSheetType.getEnrolmentEvaluationType());
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
        Collection<ExecutionCourse> executionCourses = getAssociatedExecutionCourses();

        for (ExecutionCourse executionCourse : executionCourses) {
            if (associatedExecutionCourses != executionCourse
                    && executionCourse.getExecutionPeriod() == associatedExecutionCourses.getExecutionPeriod()) {
                throw new DomainException("error.executionCourse.curricularCourse.already.associated");
            }
        }
        super.addAssociatedExecutionCourses(associatedExecutionCourses);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MarkSheet> getMarkSheets() {
        return getMarkSheetsSet();
    }

    @Deprecated
    public boolean hasAnyMarkSheets() {
        return !getMarkSheetsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneIndividualCandidacy> getStandaloneIndividualCandidacies() {
        return getStandaloneIndividualCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyStandaloneIndividualCandidacies() {
        return !getStandaloneIndividualCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplication> getSelectedMobilityIndividualApplication() {
        return getSelectedMobilityIndividualApplicationSet();
    }

    @Deprecated
    public boolean hasAnySelectedMobilityIndividualApplication() {
        return !getSelectedMobilityIndividualApplicationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionCourse> getAssociatedExecutionCourses() {
        return getAssociatedExecutionCoursesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedExecutionCourses() {
        return !getAssociatedExecutionCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.gesdis.CourseHistoric> getAssociatedCourseHistorics() {
        return getAssociatedCourseHistoricsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedCourseHistorics() {
        return !getAssociatedCourseHistoricsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourseScope> getScopes() {
        return getScopesSet();
    }

    @Deprecated
    public boolean hasAnyScopes() {
        return !getScopesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CandidateEnrolment> getCandidateEnrolments() {
        return getCandidateEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyCandidateEnrolments() {
        return !getCandidateEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry> getStudentsInquiryRegistries() {
        return getStudentsInquiryRegistriesSet();
    }

    @Deprecated
    public boolean hasAnyStudentsInquiryRegistries() {
        return !getStudentsInquiryRegistriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourseEquivalence> getOldCurricularCourseEquivalences() {
        return getOldCurricularCourseEquivalencesSet();
    }

    @Deprecated
    public boolean hasAnyOldCurricularCourseEquivalences() {
        return !getOldCurricularCourseEquivalencesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse> getNotNeedToEnrollInCurricularCourses() {
        return getNotNeedToEnrollInCurricularCoursesSet();
    }

    @Deprecated
    public boolean hasAnyNotNeedToEnrollInCurricularCourses() {
        return !getNotNeedToEnrollInCurricularCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourseEquivalence> getCurricularCourseEquivalences() {
        return getCurricularCourseEquivalencesSet();
    }

    @Deprecated
    public boolean hasAnyCurricularCourseEquivalences() {
        return !getCurricularCourseEquivalencesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesRegistry> getAssociatedInquiriesRegistries() {
        return getAssociatedInquiriesRegistriesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedInquiriesRegistries() {
        return !getAssociatedInquiriesRegistriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Curriculum> getAssociatedCurriculums() {
        return getAssociatedCurriculumsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedCurriculums() {
        return !getAssociatedCurriculumsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.studentCurriculum.CreditsDismissal> getCreditsDismissals() {
        return getCreditsDismissalsSet();
    }

    @Deprecated
    public boolean hasAnyCreditsDismissals() {
        return !getCreditsDismissalsSet().isEmpty();
    }

    @Deprecated
    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasCredits() {
        return getCredits() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasEctsCredits() {
        return getEctsCredits() != null;
    }

    @Deprecated
    public boolean hasLabHours() {
        return getLabHours() != null;
    }

    @Deprecated
    public boolean hasMinimumValueForAcumulatedEnrollments() {
        return getMinimumValueForAcumulatedEnrollments() != null;
    }

    @Deprecated
    public boolean hasScientificArea() {
        return getScientificArea() != null;
    }

    @Deprecated
    public boolean hasUniversity() {
        return getUniversity() != null;
    }

    @Deprecated
    public boolean hasPrerequisitesEn() {
        return getPrerequisitesEn() != null;
    }

    @Deprecated
    public boolean hasCurricularCourseExecutionScope() {
        return getCurricularCourseExecutionScope() != null;
    }

    @Deprecated
    public boolean hasPraticalHours() {
        return getPraticalHours() != null;
    }

    @Deprecated
    public boolean hasPrerequisites() {
        return getPrerequisites() != null;
    }

    @Deprecated
    public boolean hasWeigth() {
        return getWeigth() != null;
    }

    @Deprecated
    public boolean hasStudentCourseReport() {
        return getStudentCourseReport() != null;
    }

    @Deprecated
    public boolean hasCompetenceCourse() {
        return getCompetenceCourse() != null;
    }

    @Deprecated
    public boolean hasMandatoryEnrollment() {
        return getMandatoryEnrollment() != null;
    }

    @Deprecated
    public boolean hasGradeScale() {
        return getGradeScale() != null;
    }

    @Deprecated
    public boolean hasTheoPratHours() {
        return getTheoPratHours() != null;
    }

    @Deprecated
    public boolean hasEnrollmentAllowed() {
        return getEnrollmentAllowed() != null;
    }

    @Deprecated
    public boolean hasBasic() {
        return getBasic() != null;
    }

    @Deprecated
    public boolean hasEnrollmentWeigth() {
        return getEnrollmentWeigth() != null;
    }

    @Deprecated
    public boolean hasMandatory() {
        return getMandatory() != null;
    }

    @Deprecated
    public boolean hasRegimeType() {
        return getRegimeType() != null;
    }

    @Deprecated
    public boolean hasTheoreticalHours() {
        return getTheoreticalHours() != null;
    }

    @Deprecated
    public boolean hasMaximumValueForAcumulatedEnrollments() {
        return getMaximumValueForAcumulatedEnrollments() != null;
    }

    @Deprecated
    public boolean hasCurricularStage() {
        return getCurricularStage() != null;
    }

}
