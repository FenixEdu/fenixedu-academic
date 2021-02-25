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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CurricularCourseFunctor;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.degreeStructure.RootCourseGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.MarkType;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;

public class EmptyDegreeCurricularPlan extends EmptyDegreeCurricularPlan_Base {

    private static volatile EmptyDegreeCurricularPlan instance = null;

    private EmptyDegreeCurricularPlan() {
        super();

        setRootDomainObject(Bennu.getInstance());
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    public static EmptyDegreeCurricularPlan getInstance() {
        if (instance == null) {
            synchronized (EmptyDegreeCurricularPlan.class) {
                if (instance == null) {
                    for (final DegreeCurricularPlan iter : Bennu.getInstance().getDegreeCurricularPlansSet()) {
                        if (iter.isEmpty()) {
                            instance = (EmptyDegreeCurricularPlan) iter;
                        }
                    }
                }
            }
        }

        return instance;
    }

    public static synchronized void init() {
        synchronized (EmptyDegreeCurricularPlan.class) {
            final EmptyDegreeCurricularPlan existing = getInstance();
            if (existing == null) {
                final EmptyDegreeCurricularPlan newinstance = new EmptyDegreeCurricularPlan();
                newinstance.setNameOnSuper("Plano Curricular de Unidades Isoladas");
                newinstance.setDegreeOnSuper(EmptyDegree.getInstance());
                RootCourseGroup.createRoot(newinstance, newinstance.getName(), newinstance.getName());
                instance = newinstance;
            }
        }
    }

    private void setDegreeOnSuper(final Degree degree) {
        super.setDegree(degree);
    }

    public void setNameOnSuper(final String name) {
        super.setName(name);
    }

    @Override
    public void edit(String name, DegreeCurricularPlanState state, Date inicialDate, Date endDate, Integer degreeDuration,
            Integer minimalYearForOptionalCourses, Double neededCredits, MarkType markType, Integer numerusClausus,
            String annotation, GradeScale gradeScale) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public void edit(String name, CurricularStage curricularStage, DegreeCurricularPlanState state, GradeScale gradeScale,
            ExecutionYear beginExecutionYear) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public boolean isBolonhaDegree() {
        return true;
    }

    @Override
    public CurricularStage getCurricularStage() {
        return CurricularStage.APPROVED;
    }

    @Override
    public void delete() {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public Integer getMinimalYearForOptionalCourses() {
        return null;
    }

    @Override
    public GradeScale getGradeScaleChain() {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public Set<ExecutionCourse> getExecutionCoursesByExecutionPeriod(ExecutionSemester executionSemester) {
        return Collections.emptySet();
    }

    @Override
    public SortedSet<DegreeModuleScope> getDegreeModuleScopes() {
        return new TreeSet<DegreeModuleScope>();
    }

    @Override
    public Set<DegreeModuleScope> getDegreeModuleScopesFor(final Integer year, final Integer semester) {
        return getDegreeModuleScopes();
    }

    @Override
    public Set<DegreeModuleScope> getDegreeModuleScopesFor(final ExecutionYear executionYear) {
        return getDegreeModuleScopes();
    }

    @Override
    public void addExecutionCoursesForExecutionPeriod(final Set<ExecutionCourse> executionCourses,
            final ExecutionSemester executionSemester, final Set<Context> contexts) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public List<ExecutionCourse> getExecutionCoursesByExecutionPeriodAndSemesterAndYear(ExecutionSemester executionSemester,
            Integer curricularYear, Integer semester) {
        return Collections.emptyList();
    }

    @Override
    public Set<CurricularCourse> getAllCurricularCourses() {
        return Collections.emptySet();
    }

    @Override
    public List<CurricularCourse> getCurricularCoursesWithExecutionIn(ExecutionYear executionYear) {
        return Collections.emptyList();
    }

    @Override
    public List<CurricularCourse> getCurricularCoursesByBasicAttribute(final Boolean basic) {
        return Collections.emptyList();
    }

    @Override
    public EnrolmentPeriodInCurricularCourses getActualEnrolmentPeriod() {
        return null;
    }

    @Override
    public EnrolmentPeriodInCurricularCoursesSpecialSeason getActualEnrolmentPeriodInCurricularCoursesSpecialSeason() {
        return null;
    }

    @Override
    public boolean hasOpenEnrolmentPeriodInCurricularCoursesSpecialSeason(final ExecutionSemester executionSemester) {
        return false;
    }

    @Override
    public boolean hasOpenEnrolmentPeriodInCurricularCoursesFor(final ExecutionSemester executionSemester) {
        return false;
    }

    @Override
    public EnrolmentPeriodInCurricularCourses getNextEnrolmentPeriod() {
        return null;
    }

    @Override
    public EnrolmentPeriodInCurricularCoursesSpecialSeason getNextEnrolmentPeriodInCurricularCoursesSpecialSeason() {
        return null;
    }

    @Override
    public CandidacyPeriodInDegreeCurricularPlan getCurrentCandidacyPeriodInDCP() {
        return null;
    }

    @Override
    public CandidacyPeriodInDegreeCurricularPlan getCandidacyPeriod(final ExecutionYear executionYear) {
        return null;
    }

    @Override
    public boolean hasCandidacyPeriodFor(final ExecutionYear executionYear) {
        return false;
    }

    @Override
    public RegistrationPeriodInDegreeCurricularPlan getRegistrationPeriod(final ExecutionYear executionYear) {
        return null;
    }

    @Override
    public boolean hasRegistrationPeriodFor(final ExecutionYear executionYear) {
        return false;
    }

    @Override
    public Collection<ExecutionYear> getCandidacyPeriodsExecutionYears() {
        return Collections.emptySet();
    }

    @Override
    public EnrolmentPeriodInCurricularCoursesSpecialSeason getEnrolmentPeriodInCurricularCoursesSpecialSeasonByExecutionPeriod(
            ExecutionSemester executionSemester) {
        return null;
    }

    @Override
    public EnrolmentPeriodInCurricularCourses getEnrolmentPeriodInCurricularCoursesBy(final ExecutionSemester executionSemester) {
        return null;
    }

    @Override
    public CurricularCourse getCurricularCourseByCode(String code) {
        return null;
    }

    @Override
    public CurricularCourse getCurricularCourseByAcronym(String acronym) {
        return null;
    }

    @Override
    public Set<CurricularCourse> getCurricularCoursesSet() {
        return Collections.emptySet();
    }

    @Override
    public void doForAllCurricularCourses(final CurricularCourseFunctor curricularCourseFunctor) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public Set<CurricularCourse> getCurricularCourses(final ExecutionSemester executionSemester) {
        return Collections.emptySet();
    }

    @Override
    public List<CompetenceCourse> getCompetenceCourses() {
        return Collections.emptyList();
    }

    @Override
    public List<CompetenceCourse> getCompetenceCourses(ExecutionYear executionYear) {
        return getCompetenceCourses();
    }

    @Override
    public List<Branch> getCommonAreas() {
        return Collections.emptyList();
    }

    @Override
    public Set<CurricularCourse> getActiveCurricularCourses() {
        return Collections.emptySet();
    }

    @Override
    public Set<CurricularCourse> getActiveCurricularCourses(final ExecutionSemester executionSemester) {
        return Collections.emptySet();
    }

    @Override
    public List<CurricularCourseScope> getActiveCurricularCourseScopes() {
        return Collections.emptyList();
    }

    @Override
    public CurricularCourse createCurricularCourse(String name, String code, String acronym, Boolean enrolmentAllowed,
            CurricularStage curricularStage) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public CourseGroup createCourseGroup(final CourseGroup parentCourseGroup, final String name, final String nameEn,
            final ExecutionSemester begin, final ExecutionSemester end) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public CurricularCourse createCurricularCourse(Double weight, String prerequisites, String prerequisitesEn,
            CurricularStage curricularStage, CompetenceCourse competenceCourse, CourseGroup parentCourseGroup,
            CurricularPeriod curricularPeriod, Integer term, ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public CurricularCourse createOptionalCurricularCourse(CourseGroup parentCourseGroup, String name, String nameEn,
            CurricularStage curricularStage, CurricularPeriod curricularPeriod, Integer term, ExecutionSemester beginExecutionPeriod,
            ExecutionSemester endExecutionPeriod) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public Boolean getUserCanBuild() {
        return false;
    }

    @Override
    public Boolean getCanModify() {
        return false;
    }

    @Override
    public void setCurricularPlanMembersGroup(Group curricularPlanMembersGroup) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public void setCurricularStage(CurricularStage curricularStage) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public void setDegree(Degree degree) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public void setRoot(RootCourseGroup courseGroup) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public void setDegreeStructure(CurricularPeriod degreeStructure) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public void setGradeScale(GradeScale gradeScale) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public void setName(String name) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public Set<CurricularCourseScope> findCurricularCourseScopesIntersectingPeriod(final Date beginDate, final Date endDate) {
        return Collections.emptySet();
    }

    @Override
    public CurricularPeriod getCurricularPeriodFor(int year, int semester) {
        return null;
    }

    @Override
    public CurricularPeriod createCurricularPeriodFor(int year, int semester) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public DegreeCurricularPlanState getState() {
        return DegreeCurricularPlanState.ACTIVE;
    }

    @Override
    public Integer getDegreeDuration() {
        return null;
    }

    @Override
    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
        return false;
    }

    @Override
    public boolean isFirstCycle() {
        return false;
    }

    @Override
    public CycleCourseGroup getFirstCycleCourseGroup() {
        return null;
    }

    @Override
    public boolean isSecondCycle() {
        return false;
    }

    @Override
    public CycleCourseGroup getSecondCycleCourseGroup() {
        return null;
    }

    @Override
    public CycleCourseGroup getThirdCycleCourseGroup() {
        return null;
    }

    @Override
    public CycleCourseGroup getCycleCourseGroup(final CycleType cycleType) {
        return null;
    }

    @Override
    public CycleCourseGroup getLastOrderedCycleCourseGroup() {
        return null;
    }

    @Override
    public String getGraduateTitle(final ExecutionYear executionYear, final ProgramConclusion programConclusion,
            final Locale locale) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public List<CurricularCourse> getDissertationCurricularCourses(ExecutionYear year) {
        return Collections.emptyList();
    }

    @Override
    public List<CurricularCourse> getDissertationCurricularCourses() {
        return Collections.emptyList();
    }

    @Override
    public DegreeCurricularPlanEquivalencePlan createEquivalencePlan(final DegreeCurricularPlan sourceDegreeCurricularPlan) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public Set<CurricularCourse> getCurricularCoursesByExecutionYearAndCurricularYear(ExecutionYear eY, Integer cY) {
        return Collections.emptySet();
    }

    @Override
    public Set<DegreeCurricularPlanEquivalencePlan> getTargetEquivalencePlans() {
        return Collections.emptySet();
    }

    @Override
    final public LocalizedString getDescriptionI18N() {
        return new LocalizedString();
    }

    @Override
    public List<CycleCourseGroup> getDestinationAffinities(final CycleType sourceCycleType) {
        return Collections.emptyList();
    }

    /*
     * Since empty degrees do not have execution degrees, this method must
     * always return true
     */
    @Override
    public boolean hasExecutionDegreeFor(ExecutionYear executionYear) {
        return true;
    }

    @Override
    public int getDurationInYears() {
        return 0;
    }

    @Override
    public int getDurationInSemesters() {
        return 0;
    }

    @Override
    public int getDurationInYears(CycleType cycleType) {
        return 0;
    }

    @Override
    public int getDurationInSemesters(CycleType cycleType) {
        return 0;
    }
}
