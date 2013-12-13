package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularCourseFunctor;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.AverageType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.MarkType;
import net.sourceforge.fenixedu.util.SituationName;
import pt.ist.bennu.core.domain.Bennu;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EmptyDegreeCurricularPlan extends EmptyDegreeCurricularPlan_Base {

    private static EmptyDegreeCurricularPlan instance = null;

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
    public boolean isBoxStructure() {
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
    public boolean isGradeValid(Grade grade) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
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
            CurricularPeriod curricularPeriod, ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public CurricularCourse createOptionalCurricularCourse(CourseGroup parentCourseGroup, String name, String nameEn,
            CurricularStage curricularStage, CurricularPeriod curricularPeriod, ExecutionSemester beginExecutionPeriod,
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
    public Set<MasterDegreeCandidate> readMasterDegreeCandidates() {
        return Collections.emptySet();
    }

    @Override
    public Set<MasterDegreeCandidate> readMasterDegreeCandidatesBySpecialization(final Specialization specialization) {
        return Collections.emptySet();
    }

    @Override
    public Set<MasterDegreeCandidate> readMasterDegreeCandidatesBySituatioName(final SituationName situationName) {
        return Collections.emptySet();
    }

    @Override
    public Set<MasterDegreeCandidate> readMasterDegreeCandidatesByCourseAssistant(boolean courseAssistant) {
        return Collections.emptySet();
    }

    @Override
    public List<MasterDegreeThesisDataVersion> readActiveMasterDegreeThesisDataVersions() {
        return Collections.emptyList();
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
    public void addExecutionCourses(final Collection<ExecutionCourseView> executionCourseViews,
            final ExecutionSemester... executionPeriods) {
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
    public String getGraduateTitle(final ExecutionYear executionYear, final CycleType cycleType, final Locale locale) {
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
    public DegreeCurricularPlan getSourceDegreeCurricularPlan() {
        return null;
    }

    @Override
    public void setSourceDegreeCurricularPlan(DegreeCurricularPlan sourceDegreeCurricularPlan) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public DegreeCurricularPlanEquivalencePlan createEquivalencePlan(final DegreeCurricularPlan sourceDegreeCurricularPlan) {
        throw new DomainException("EmptyDegreeCurricularPlan.not.available");
    }

    @Override
    public List<StudentCurricularPlan> getStudentsWithoutTutorGivenEntryYear(ExecutionYear entryYear) {
        return Collections.emptyList();
    }

    @Override
    public Set<CurricularCourse> getCurricularCoursesByExecutionYearAndCurricularYear(ExecutionYear eY, Integer cY) {
        return Collections.emptySet();
    }

    @Override
    final public AverageType getAverageType() {
        return AverageType.WEIGHTED;
    }

    @Override
    public Boolean getApplyPreviousYearsEnrolmentRule() {
        return Boolean.FALSE;
    }

    @Override
    public Set<DegreeCurricularPlanEquivalencePlan> getTargetEquivalencePlans() {
        return Collections.emptySet();
    }

    @Override
    final public MultiLanguageString getDescriptionI18N() {
        return new MultiLanguageString();
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
}
