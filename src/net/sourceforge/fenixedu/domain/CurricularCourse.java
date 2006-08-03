package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.precedences.Restriction;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class CurricularCourse extends CurricularCourse_Base {

    public static final Comparator<CurricularCourse> CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME)
                .addComparator(new BeanComparator("degreeCurricularPlan.degree.tipoCurso.name"));
        ((ComparatorChain) CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME)
                .addComparator(new BeanComparator("degreeCurricularPlan.degree.nome"));
        ((ComparatorChain) CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME)
                .addComparator(new BeanComparator("name"));
    }

    public static List<CurricularCourse> readCurricularCourses() {
        List<CurricularCourse> result = new ArrayList<CurricularCourse>();

        for (DegreeModule degreeModule : RootDomainObject.getInstance().getDegreeModules()) {
            if (degreeModule instanceof CurricularCourse) {
                result.add((CurricularCourse) degreeModule);
            }
        }

        return result;
    }

    protected CurricularCourse() {
        super();
        this.setOjbConcreteClass(CurricularCourse.class.getName());
    }

    protected CurricularCourse(String name, String code, String acronym, Boolean enrolmentAllowed,
            CurricularStage curricularStage) {
        this();
        setName(name);
        setCode(code);
        setAcronym(acronym);
        setEnrollmentAllowed(enrolmentAllowed);
        setCurricularStage(curricularStage);
    }

    public CurricularCourse(Double weight, String prerequisites, String prerequisitesEn,
            CurricularStage curricularStage, CompetenceCourse competenceCourse,
            CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
            ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {

        this();
        setWeigth(weight);
        setPrerequisites(prerequisites);
        setPrerequisitesEn(prerequisitesEn);
        setCurricularStage(curricularStage);
        setCompetenceCourse(competenceCourse);
        setType(CurricularCourseType.NORMAL_COURSE);
        new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    /**
     * - This constructor is used to create a 'special' curricular course that
     * will represent any curricular course accordding to a rule
     */
    public CurricularCourse(CourseGroup parentCourseGroup, String name, String nameEn,
            CurricularStage curricularStage, CurricularPeriod curricularPeriod,
            ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {

        this();
        setName(name);
        setNameEn(nameEn);
        setCurricularStage(curricularStage);
        setType(CurricularCourseType.OPTIONAL_COURSE);
        new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    public GradeScale getGradeScaleChain() {
        return super.getGradeScale() != null ? super.getGradeScale() : getDegreeCurricularPlan()
                .getGradeScaleChain();
    }

    public void print(StringBuilder dcp, String tabs, Context previousContext) {
        String tab = tabs + "\t";
        dcp.append(tab);
        dcp.append("[CC ").append(this.getIdInternal()).append("][");
        dcp.append(previousContext.getCurricularPeriod().getOrderByType(CurricularPeriodType.YEAR))
                .append("Y,");
        dcp.append(previousContext.getCurricularPeriod().getOrderByType(CurricularPeriodType.SEMESTER))
                .append("S] ");
        dcp.append(this.getName()).append("\n");
    }

    public boolean isLeaf() {
        return true;
    }

    public boolean isRoot() {
        return false;
    }

    public boolean isBolonha() {
        return !(getCurricularStage() == CurricularStage.OLD);
    }

    public boolean getIsBolonha() {
        return isBolonha();
    }

    public DegreeCurricularPlan getParentDegreeCurricularPlan() {
        // FIXME: in the future, a curricular course may be included in contexts
        // of diferent curricular plans
        return getParentContexts().get(0).getParentCourseGroup().getParentDegreeCurricularPlan();
    }

    @Override
    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return (isBolonha()) ? this.getParentDegreeCurricularPlan() : super.getDegreeCurricularPlan();
    }

    @Override
    public Double getWeigth() {
        if (isBolonha()) {
            if (getCompetenceCourse() != null) {
                return Double.valueOf(getCompetenceCourse().getEctsCredits());
            } else {
                return null;
            }
        } else {
            return super.getWeigth();
        }
    }
    
    public Degree getDegree() {
        return getDegreeCurricularPlan().getDegree();
    }

    public void edit(Double weight, String prerequisites, String prerequisitesEn,
            CurricularStage curricularStage, CompetenceCourse competenceCourse) {

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

    public Boolean getCanBeDeleted() {
        return true;
    }

    public void delete() {
        super.delete();
        removeCompetenceCourse();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public boolean curricularCourseIsMandatory() {
        return getMandatory().booleanValue();
    }

    public List<CurricularCourseScope> getInterminatedScopes() {
        List<CurricularCourseScope> result = new ArrayList<CurricularCourseScope>();
        for (CurricularCourseScope curricularCourseScope : this.getScopes()) {
            if (curricularCourseScope.getEndDate() == null) {
                result.add(curricularCourseScope);
            }
        }

        return result;
    }

    public List<CurricularCourseScope> getActiveScopes() {
        final List<CurricularCourseScope> activeScopes = new ArrayList<CurricularCourseScope>();
        for (CurricularCourseScope scope : this.getScopes()) {
            if (scope.isActive()) {
                activeScopes.add(scope);
            }
        }
        return activeScopes;
    }

    public List<CurricularCourseScope> getActiveScopesInExecutionPeriod(
            final ExecutionPeriod executionPeriod) {
        final List<CurricularCourseScope> activeScopesInExecutionPeriod = new ArrayList<CurricularCourseScope>();
        for (final CurricularCourseScope scope : getScopes()) {
            final CurricularSemester curricularSemester = scope.getCurricularSemester();
            if (curricularSemester.getSemester().equals(executionPeriod.getSemester())
                    && (scope.getBeginDate().getTime().getTime() <= executionPeriod.getBeginDate()
                            .getTime())
                    && ((scope.getEndDate() == null) || (scope.getEndDate().getTime().getTime() >= executionPeriod
                            .getEndDate().getTime()))) {
                activeScopesInExecutionPeriod.add(scope);
            }
        }
        return activeScopesInExecutionPeriod;
    }
    
    public boolean hasActiveScopesInExecutionPeriod(final ExecutionPeriod executionPeriod) {
    	return !getActiveScopesInExecutionPeriod(executionPeriod).isEmpty();
    }

    public Set<CurricularCourseScope> getActiveScopesInExecutionYear(final ExecutionYear executionYear) {
        final Set<CurricularCourseScope> activeScopes = new HashSet<CurricularCourseScope>();
        for (final CurricularCourseScope scope : getScopes()) {
            final CurricularSemester curricularSemester = scope.getCurricularSemester();
            for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriodsSet()) {
            	if (curricularSemester.getSemester().equals(executionPeriod.getSemester())
            			&& (scope.getBeginDate().getTime().getTime() <= executionPeriod.getBeginDate().getTime())
            			&& ((scope.getEndDate() == null) || (scope.getEndDate().getTime().getTime() >= executionPeriod.getEndDate().getTime()))) {
            		activeScopes.add(scope);
            	}
            }
        }
        return activeScopes;
    }

    public List<CurricularCourseScope> getActiveScopesInExecutionPeriodAndSemester(
            final ExecutionPeriod executionPeriod) {
        List<CurricularCourseScope> activeScopesInExecutionPeriod = new ArrayList<CurricularCourseScope>();
        for (Iterator iter = getScopes().iterator(); iter.hasNext();) {
            CurricularCourseScope scope = (CurricularCourseScope) iter.next();
            if ((scope.getCurricularSemester().getSemester().equals(executionPeriod.getSemester()))
                    && (scope.getBeginDate().getTime().getTime() <= executionPeriod.getBeginDate()
                            .getTime())
                    && ((scope.getEndDate() == null) || (scope.getEndDate().getTime().getTime() >= executionPeriod
                            .getEndDate().getTime()))) {
                activeScopesInExecutionPeriod.add(scope);
            }
        }
        return activeScopesInExecutionPeriod;
    }

    public List<CurricularCourseScope> getActiveScopesIntersectedByExecutionPeriod(
            final ExecutionPeriod executionPeriod) {
        List<CurricularCourseScope> activeScopesInExecutionPeriod = new ArrayList<CurricularCourseScope>();
        for (final CurricularCourseScope scope : getScopes()) {
            if (scope.getBeginDate().getTime().getTime() < executionPeriod.getBeginDate().getTime()) {
                if ((scope.getEnd() == null)
                        || (scope.getEnd().getTime() >= executionPeriod.getBeginDate().getTime())) {
                    activeScopesInExecutionPeriod.add(scope);
                }
            } else {
                if (scope.getBeginDate().getTime().getTime() <= executionPeriod.getEndDate().getTime()) {
                    activeScopesInExecutionPeriod.add(scope);
                }
            }
        }
        return activeScopesInExecutionPeriod;
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes
    // -------------------------------------------------------------

    public List<RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse> getRestrictionsHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse() {
        List<RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse> result = new ArrayList<RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse>();
        for (final Restriction restriction : getRestrictionsByCurricularCourse()) {
            if (restriction instanceof RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse) {
                result.add((RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse) restriction);
            }
        }
        return result;
    }

    public CurricularYear getCurricularYearByBranchAndSemester(final Branch branch, final Integer semester) {
        return getCurricularYearByBranchAndSemester(branch, semester, new Date());
    }

    public CurricularYear getCurricularYearByBranchAndSemester(final Branch branch,
            final Integer semester, final Date date) {

        if (this.getScopes().size() == 1) {
            return this.getScopes().get(0).getCurricularSemester().getCurricularYear();
        }

        CurricularYear curricularYearToReturn = null;
        List<CurricularCourseScope> curricularCourseScopesFound = null;
        CurricularCourseScope curricularCourseScopeFound = null;
        boolean foundInBranchButNotInSemester = false;
        boolean notFoundInBranch = false;
        boolean notFoundInSemester = false;

        if (branch != null) {
            curricularCourseScopesFound = (List) CollectionUtils.select(this.getScopes(),
                    new Predicate() {
                        public boolean evaluate(Object arg0) {
                            return ((CurricularCourseScope) arg0).getBranch().equals(branch);
                        }
                    });

            if (curricularCourseScopesFound != null && !curricularCourseScopesFound.isEmpty()) {

                if (semester != null) {
                    curricularCourseScopeFound = (CurricularCourseScope) CollectionUtils.find(
                            curricularCourseScopesFound, new Predicate() {
                                public boolean evaluate(Object arg0) {
                                    return ((CurricularCourseScope) arg0).getCurricularSemester()
                                            .getSemester().equals(semester);
                                }
                            });

                    if (curricularCourseScopeFound != null) {
                        curricularYearToReturn = curricularCourseScopeFound.getCurricularSemester()
                                .getCurricularYear();
                    } else {
                        foundInBranchButNotInSemester = true;
                    }
                } else {
                    foundInBranchButNotInSemester = true;
                }
            } else {
                notFoundInBranch = true;
            }
        } else {
            notFoundInBranch = true;
        }

        if (foundInBranchButNotInSemester) {
            curricularYearToReturn = getCurricularYearWithLowerYear(curricularCourseScopesFound, date);
        }

        if (notFoundInBranch) {

            if (semester != null) {
                curricularCourseScopesFound = (List) CollectionUtils.select(this.getScopes(),
                        new Predicate() {
                            public boolean evaluate(Object arg0) {
                                return ((CurricularCourseScope) arg0).getCurricularSemester()
                                        .getSemester().equals(semester);
                            }
                        });

                if (curricularCourseScopesFound != null && !curricularCourseScopesFound.isEmpty()) {
                    // if (curricularCourseScopesFound.size() == 1) {
                    // curricularYearToReturn =
                    // getCurricularYearWithLowerYear(this.getScopes());
                    // } else {
                    curricularYearToReturn = getCurricularYearWithLowerYear(curricularCourseScopesFound,
                            date);
                    // }
                } else {
                    notFoundInSemester = true;
                }
            } else {
                notFoundInSemester = true;
            }
        }

        if (notFoundInSemester) {
            curricularYearToReturn = getCurricularYearWithLowerYear(this.getScopes(), date);
        }

        return curricularYearToReturn;
    }

    public String getCurricularCourseUniqueKeyForEnrollment() {
        DegreeType degreeType = (this.getDegreeCurricularPlan() != null && this
                .getDegreeCurricularPlan().getDegree() != null) ? this.getDegreeCurricularPlan()
                .getDegree().getTipoCurso() : null;
        return constructUniqueEnrollmentKey(this.getCode(), this.getName(), degreeType);
    }

    public boolean hasActiveScopeInGivenSemester(final Integer semester) {
        List scopes = this.getScopes();

        List result = (List) CollectionUtils.select(scopes, new Predicate() {
            public boolean evaluate(Object obj) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) obj;
                return (curricularCourseScope.getCurricularSemester().getSemester().equals(semester) && curricularCourseScope
                        .isActive().booleanValue());
            }
        });

        return !result.isEmpty();
    }

    public boolean hasScopeInGivenSemester(final Integer semester) {
        List scopes = this.getScopes();

        List result = (List) CollectionUtils.select(scopes, new Predicate() {
            public boolean evaluate(Object obj) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) obj;
                return curricularCourseScope.getCurricularSemester().getSemester().equals(semester);
            }
        });

        return !result.isEmpty();
    }

    public boolean hasActiveScopeInGivenSemesterForGivenBranch(
            final CurricularSemester curricularSemester, final Branch branch) {
        final List<CurricularCourseScope> scopes = this.getScopes();
        for (final CurricularCourseScope curricularCourseScope : scopes) {
            if (curricularCourseScope.getCurricularSemester().equals(curricularSemester)
                    && curricularCourseScope.isActive()
                    && curricularCourseScope.getBranch().equals(branch)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActiveScopeInGivenSemesterForGivenBranch(final Integer semester,
            final Branch branch) {
        final List<CurricularCourseScope> scopes = this.getScopes();
        for (final CurricularCourseScope curricularCourseScope : scopes) {
            if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester)
                    && curricularCourseScope.isActive()
                    && curricularCourseScope.getBranch().equals(branch)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActiveScopeInGivenSemesterForCommonAndGivenBranch(final Integer semester,
            final Branch branch) {
        List scopes = this.getScopes();

        List result = (List) CollectionUtils.select(scopes, new Predicate() {
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

    private CurricularYear getCurricularYearWithLowerYear(List<CurricularCourseScope> listOfScopes,
            Date date) {

        if (listOfScopes.isEmpty()) {
            return null;
        }

        CurricularYear minCurricularYear = ((CurricularCourseScope) listOfScopes.get(0))
                .getCurricularSemester().getCurricularYear();

        CurricularYear actualCurricularYear = null;
        for (CurricularCourseScope curricularCourseScope : (List<CurricularCourseScope>) listOfScopes) {
            actualCurricularYear = curricularCourseScope.getCurricularSemester().getCurricularYear();

            if (minCurricularYear.getYear().intValue() > actualCurricularYear.getYear().intValue()
                    && curricularCourseScope.isActive(date).booleanValue()) {

                minCurricularYear = actualCurricularYear;
            }
        }

        return minCurricularYear;
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes
    // -------------------------------------------------------------

    private String constructUniqueEnrollmentKey(String code, String name, DegreeType tipoCurso) {
        StringBuilder stringBuffer = new StringBuilder(50);
        stringBuffer.append(code);
        stringBuffer.append(name);
        if (tipoCurso != null) {
            stringBuffer.append(tipoCurso.toString());
        }
        return StringUtils.lowerCase(stringBuffer.toString());
    }

    public Curriculum insertCurriculum(String program, String programEn, String operacionalObjectives,
            String operacionalObjectivesEn, String generalObjectives, String generalObjectivesEn) {

        Curriculum curriculum = new Curriculum();

        curriculum.setCurricularCourse(this);
        curriculum.setProgram(program);
        curriculum.setProgramEn(programEn);
        curriculum.setOperacionalObjectives(operacionalObjectives);
        curriculum.setOperacionalObjectivesEn(operacionalObjectivesEn);
        curriculum.setGeneralObjectives(generalObjectives);
        curriculum.setGeneralObjectivesEn(generalObjectivesEn);

        Calendar today = Calendar.getInstance();
        curriculum.setLastModificationDate(today.getTime());

        return curriculum;
    }

    public List<ExecutionCourse> getExecutionCoursesWithPublicSites() {
        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        
        for (final ExecutionCourse executionCourse : this.getAssociatedExecutionCourses()) {
            if (executionCourse.hasSite()) {
                result.add(executionCourse);
            }
        }
        
        return result;
    }
    
    public List<ExecutionCourse> getExecutionCoursesByExecutionPeriod(final ExecutionPeriod executionPeriod) {
        return (List) CollectionUtils.select(getAssociatedExecutionCourses(), new Predicate() {
            public boolean evaluate(Object o) {
                ExecutionCourse executionCourse = (ExecutionCourse) o;
                return executionCourse.getExecutionPeriod().equals(executionPeriod);
            }
        });
    }

    public List<ExecutionCourse> getExecutionCoursesByExecutionYear(final ExecutionYear executionYear) {
        return (List) CollectionUtils.select(getAssociatedExecutionCourses(), new Predicate() {
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
                    || latestCurriculum.getLastModificationDate().before(
                            curriculum.getLastModificationDate())) {
                latestCurriculum = curriculum;
            }
        }
        return latestCurriculum;
    }

    public Curriculum findLatestCurriculumModifiedBefore(Date date) {
        Curriculum latestCurriculum = null;

        for (Curriculum curriculum : getAssociatedCurriculums()) {
            if (curriculum.getLastModificationDate().compareTo(date) == 1) {
                // modified after date
                continue;
            }

            if (latestCurriculum == null) {
                latestCurriculum = curriculum;
                continue;
            }

            Date currentLastModificationDate = latestCurriculum.getLastModificationDate();
            if (currentLastModificationDate.before(curriculum.getLastModificationDate())) {
                latestCurriculum = curriculum;
            }
        }

        return latestCurriculum;
    }

    @Override
    public Double getTheoreticalHours() {
        Double result = 0.0;
        if (super.getTheoreticalHours() != null) {
            result = super.getTheoreticalHours();
        } else if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getTheoreticalHours();
        }
        return result;
    }

    public Double getTheoreticalHours(CurricularPeriod curricularPeriod) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getTheoreticalHours(curricularPeriod.getOrder());
        }
        return result;
    }

    public Double getProblemsHours() {
        Double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getProblemsHours();
        }
        return result;
    }

    public Double getProblemsHours(CurricularPeriod curricularPeriod) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getProblemsHours(curricularPeriod.getOrder());
        }
        return result;
    }

    public Double getLaboratorialHours() {
        Double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getLaboratorialHours();
        }
        return result;
    }

    public Double getLaboratorialHours(CurricularPeriod curricularPeriod) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getLaboratorialHours(curricularPeriod.getOrder());
        }
        return result;
    }

    public Double getSeminaryHours() {
        Double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getSeminaryHours();
        }
        return result;
    }

    public Double getSeminaryHours(CurricularPeriod curricularPeriod) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getSeminaryHours(curricularPeriod.getOrder());
        }
        return result;
    }

    public Double getFieldWorkHours() {
        Double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getFieldWorkHours();
        }
        return result;
    }

    public Double getFieldWorkHours(CurricularPeriod curricularPeriod) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getFieldWorkHours(curricularPeriod.getOrder());
        }
        return result;
    }

    public Double getTrainingPeriodHours() {
        Double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getTrainingPeriodHours();
        }
        return result;
    }

    public Double getTrainingPeriodHours(CurricularPeriod curricularPeriod) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getTrainingPeriodHours(curricularPeriod.getOrder());
        }
        return result;
    }

    public Double getTutorialOrientationHours() {
        Double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getTutorialOrientationHours();
        }
        return result;
    }

    public Double getTutorialOrientationHours(CurricularPeriod curricularPeriod) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getTutorialOrientationHours(curricularPeriod.getOrder());
        }
        return result;
    }

    public Double getAutonomousWorkHours() {
        Double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getAutonomousWorkHours();
        }
        return result;
    }

    public Double getAutonomousWorkHours(CurricularPeriod curricularPeriod) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getAutonomousWorkHours(curricularPeriod.getOrder());
        }
        return result;
    }

    public Double getContactLoad() {
        Double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getContactLoad();
        }
        return result;
    }

    public Double getContactLoad(CurricularPeriod curricularPeriod) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getContactLoad(curricularPeriod.getOrder());
        }
        return result;
    }

    public Double getTotalLoad() {
        Double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getTotalLoad();
        }
        return result;
    }

    public Double getTotalLoad(CurricularPeriod curricularPeriod) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getTotalLoad(curricularPeriod.getOrder());
        }
        return result;
    }

    @Override
    public Double getEctsCredits() {
        Double result = 0.0;
        if (super.getEctsCredits() != null) {
            result = super.getEctsCredits();
        } else if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getEctsCredits();
        }
        return result;
    }

    public Double getEctsCredits(CurricularPeriod curricularPeriod) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getEctsCredits(curricularPeriod.getOrder());
        }
        return result;
    }

    public CurricularSemester getCurricularSemesterWithLowerYearBySemester(Integer semester, Date date) {
        CurricularSemester result = null;
        for (CurricularCourseScope curricularCourseScope : getScopes()) {
            if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester)
                    && curricularCourseScope.isActive(date)) {
                if (result == null) {
                    result = curricularCourseScope.getCurricularSemester();
                } else {
                    if (result.getCurricularYear().getYear() > curricularCourseScope
                            .getCurricularSemester().getCurricularYear().getYear()) {
                        result = curricularCourseScope.getCurricularSemester();
                    }
                }
            }
        }
        return result;
    }

    private List<Enrolment> getActiveEnrollments(ExecutionPeriod executionPeriod, Student student) {
        List<Enrolment> results = new ArrayList<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            Enrolment enrollment = (Enrolment) curriculumModule;
            boolean filters = true;
            filters &= !enrollment.getEnrollmentState().equals(EnrollmentState.ANNULED);
            filters &= executionPeriod == null
                    || enrollment.getExecutionPeriod().equals(executionPeriod);
            filters &= student == null
                    || enrollment.getStudentCurricularPlan().getStudent().equals(student);

            if (filters) {
                results.add(enrollment);
            }
        }
        return results;
    }

    public List<Enrolment> getEnrolmentsByExecutionPeriod(final ExecutionPeriod executionPeriod) {
        List<Enrolment> result = new ArrayList<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            final Enrolment enrolment = (Enrolment) curriculumModule;
            if (enrolment.getExecutionPeriod().equals(executionPeriod)) {
                result.add(enrolment);
            }
        }
        return result;
    }

    public int countEnrolmentsByExecutionPeriod(final ExecutionPeriod executionPeriod) {
        return getEnrolmentsByExecutionPeriod(executionPeriod).size();
    }

    public List<Enrolment> getEnrolmentsByYear(String year) {
        ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(year);

        return getEnrolmentsByExecutionYear(executionYear);
    }

    public List<Enrolment> getEnrolmentsByExecutionYear(ExecutionYear executionYear) {
        List<Enrolment> result = new ArrayList<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            final Enrolment enrolment = (Enrolment) curriculumModule;
            if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                result.add(enrolment);
            }
        }
        return result;
    }

    public Enrolment getEnrolmentByStudentAndYear(Student student, String year) {
        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            final Enrolment enrolment = (Enrolment) curriculumModule;
            if (enrolment.getStudentCurricularPlan().getStudent().equals(student)
                    && enrolment.getExecutionPeriod().getExecutionYear().getYear().equals(year)) {
                return enrolment;
            }
        }
        return null;
    }

    public List<Enrolment> getActiveEnrollments(Student student) {
        return getActiveEnrollments(null, student);
    }

    public List<Enrolment> getActiveEnrollments() {
        return getActiveEnrollments(null, null);
    }

    public List<Enrolment> getActiveEnrollments(ExecutionPeriod executionPeriod) {
        List<Enrolment> enrollments = new ArrayList<Enrolment>();

        for (Enrolment enrollment : getEnrolmentsByExecutionPeriod(executionPeriod)) {
            if (!enrollment.getEnrollmentState().equals(EnrollmentState.ANNULED)) {
                enrollments.add(enrollment);
            }
        }

        return enrollments;
    }

    public List<Enrolment> getActiveEnrollments(ExecutionYear executionYear) {
        List<Enrolment> results = new ArrayList<Enrolment>();

        for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
            results.addAll(getActiveEnrollments(executionPeriod));
        }

        return results;
    }

    protected void checkContextsFor(final CourseGroup parentCourseGroup,
            final CurricularPeriod curricularPeriod, final Context ignoreContext) {
        for (final Context context : this.getParentContexts()) {
            if (context != ignoreContext && context.getParentCourseGroup() == parentCourseGroup
                    && context.getCurricularPeriod() == curricularPeriod) {
                throw new DomainException("courseGroup.contextAlreadyExistForCourseGroup");
            }
        }
    }

    protected void addOwnPartipatingCurricularRules(final List<CurricularRule> result) {
        // no rules to add
    }

    protected void checkOwnRestrictions(final CourseGroup parentCourseGroup,
            final CurricularPeriod curricularPeriod) {
        if (getCompetenceCourse() != null && getCompetenceCourse().getRegime() == RegimeType.ANUAL
                && (curricularPeriod.getOrder() == null || curricularPeriod.getOrder() != 1)) {
            throw new DomainException(
                    "competenceCourse.anual.but.trying.to.associate.curricular.course.not.to.first.period");
        }
    }

    @Override
    public String getName() {
        if ((super.getName() == null || super.getName().length() == 0)
                && this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getName();
        }
        return super.getName();
    }

    @Override
    public String getNameEn() {
        if ((super.getNameEn() == null || super.getNameEn().length() == 0)
                && this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getNameEn();
        }
        return super.getNameEn();
    }

    @Override
    public String getAcronym() {
        if ((super.getAcronym() == null || super.getAcronym().length() == 0)
                && this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getAcronym();
        }
        return super.getAcronym();
    }

    @Override
    public Boolean getBasic() {
        if (super.getBasic() == null && this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().isBasic();
        }
        return super.getBasic();
    }

    public String getObjectives() {
        if (this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getObjectives();
        }
        return null;
    }

    public String getObjectivesEn() {
        if (this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getObjectivesEn();
        }
        return null;
    }

    public String getProgram() {
        if (this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getProgram();
        }
        return null;
    }

    public String getProgramEn() {
        if (this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getProgramEn();
        }
        return null;
    }

    public String getEvaluationMethod() {
        if (this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getEvaluationMethod();
        }
        return null;
    }

    public String getEvaluationMethodEn() {
        if (this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getEvaluationMethodEn();
        }
        return null;
    }

    public RegimeType getRegime() {
        if (this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getRegime();
        }
        return null;
    }

    public boolean isOptional() {
        return getType().equals(CurricularCourseType.OPTIONAL_COURSE);
    }

    public boolean isAnual() {
        if (this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().isAnual();
        }
        return false;
    }

    public boolean isEquivalent(CurricularCourse oldCurricularCourse) {
        return this.equals(oldCurricularCourse)
                || this.getOldCurricularCourses().contains(oldCurricularCourse)
                || this.getCompetenceCourse().getAssociatedCurricularCourses().contains(
                        oldCurricularCourse);
    }

    public List<Context> getParentContextsByExecutionYear(ExecutionYear executionYear) {
        final List<Context> result = new ArrayList<Context>();
        for (final Context context : this.getParentContexts()) {
            if (executionYear == null || context.isValid(executionYear)) {
                result.add(context);
            }
        }
        return result;
    }

    public boolean hasScopeForCurricularYear(final Integer curricularYear, ExecutionPeriod executionPeriod) {
        for (DegreeModuleScope degreeModuleScope : getDegreeModuleScopes()) {
            if(degreeModuleScope.isActiveForExecutionPeriod(executionPeriod) &&
                    degreeModuleScope.getCurricularYear().equals(curricularYear)) {
                return true;
            }
        }
        return false;
    }

    public static List<CurricularCourse> readByCurricularStage(CurricularStage curricularStage) {
        List<CurricularCourse> result = new ArrayList<CurricularCourse>();
        for (CurricularCourse curricularCourse : CurricularCourse.readCurricularCourses()) {
            if (curricularCourse.getCurricularStage() != null
                    && curricularCourse.getCurricularStage().equals(curricularStage)) {
                result.add(curricularCourse);
            }
        }
        return result;
    }

    public Set<CurricularCourseScope> findCurricularCourseScopesIntersectingPeriod(final Date beginDate,
            final Date endDate) {
        final Set<CurricularCourseScope> curricularCourseScopes = new HashSet<CurricularCourseScope>();
        for (final CurricularCourseScope curricularCourseScope : getScopesSet()) {
            if (curricularCourseScope.intersects(beginDate, endDate)) {
                curricularCourseScopes.add(curricularCourseScope);
            }
        }
        return curricularCourseScopes;
    }

    public Set<Enrolment> getEnrolmentsNotInAnyMarkSheet(MarkSheetType markSheetType,
            ExecutionPeriod executionPeriod) {

        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {

            if (curriculumModule instanceof Enrolment) {
                final Enrolment enrolment = (Enrolment) curriculumModule;

                if (enrolment.getExecutionPeriod() == executionPeriod
                        && markSheetType.getEnrolmentEvaluationType() == enrolment
                                .getEnrolmentEvaluationType()) {
                    if (!enrolment.hasAssociatedMarkSheet(markSheetType)) {
                        result.add(enrolment);
                    }
                } else if (markSheetType == MarkSheetType.IMPROVEMENT) {
                    if (enrolment.hasImprovement() && !enrolment.hasAssociatedMarkSheet(markSheetType)
                            && enrolment.hasAttendsFor(executionPeriod)) {
                        result.add(enrolment);
                    }
                }
            }
        }
        return result;
    }
    
    private boolean hasEnrolmentsNotInAnyMarkSheet(MarkSheetType markSheetType,
            ExecutionPeriod executionPeriod) {
    	for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {

            if (curriculumModule instanceof Enrolment) {
                final Enrolment enrolment = (Enrolment) curriculumModule;

                if (enrolment.getExecutionPeriod() == executionPeriod
                        && markSheetType.getEnrolmentEvaluationType() == enrolment
                                .getEnrolmentEvaluationType()) {
                    if (!enrolment.hasAssociatedMarkSheet(markSheetType)) {
                        return true;
                    }
                } else if (markSheetType == MarkSheetType.IMPROVEMENT) {
                    if (enrolment.hasImprovement() && !enrolment.hasAssociatedMarkSheet(markSheetType)
                            && enrolment.hasAttendsFor(executionPeriod)) {
                    	return true;
                    }
                }
            }
        }
    	return false;
    }
    
    private boolean hasEnrolmentsNotInAnyMarkSheet(ExecutionPeriod executionPeriod) {
    	for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {

            if (curriculumModule instanceof Enrolment) {
                final Enrolment enrolment = (Enrolment) curriculumModule;

                if (enrolment.getExecutionPeriod() == executionPeriod
                        && enrolment.getEnrolmentEvaluationType() == EnrolmentEvaluationType.NORMAL) {
                    if (!enrolment.hasAssociatedMarkSheet(MarkSheetType.NORMAL)) {
                        return true;
                    }
                } if (enrolment.hasImprovement() && !enrolment.hasAssociatedMarkSheet(MarkSheetType.IMPROVEMENT)
                            && enrolment.hasAttendsFor(executionPeriod)) {
                    	return true;
                }
            }
        }
    	return false;
    }


    public MarkSheet createNormalMarkSheet(ExecutionPeriod executionPeriod, Teacher responsibleTeacher,
            Date evaluationDate, MarkSheetType markSheetType, Boolean submittedByTeacher,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans) {

        return MarkSheet.createNormal(this, executionPeriod, responsibleTeacher, evaluationDate,
                markSheetType, submittedByTeacher, evaluationBeans);
    }

    public MarkSheet rectifyEnrolmentEvaluation(MarkSheet markSheet,
            EnrolmentEvaluation enrolmentEvaluation, Date evaluationDate, String grade, String reason) {

        if (markSheet == null || evaluationDate == null || grade == null || grade.length() == 0) {
            throw new DomainException("error.markSheet.invalid.arguments");
        }

        if (!markSheet.hasEnrolmentEvaluations(enrolmentEvaluation)) {
            throw new DomainException("error.no.student.in.markSheet");
        }

        if (markSheet.isNotConfirmed()) {
            throw new DomainException("error.markSheet.must.be.confirmed");
        }

        if (enrolmentEvaluation.hasRectification()) {
            throw new DomainException("error.markSheet.student.alreadyRectified", enrolmentEvaluation
                    .getEnrolment().getStudentCurricularPlan().getStudent().getNumber().toString());
        }

        enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
        enrolmentEvaluation.setWhenDateTime(new DateTime());

        MarkSheet rectificationMarkSheet = createRectificationMarkSheet(markSheet.getExecutionPeriod(),
                evaluationDate, markSheet.getResponsibleTeacher(), markSheet.getMarkSheetType(), reason,
                new MarkSheetEnrolmentEvaluationBean(enrolmentEvaluation.getEnrolment(), evaluationDate,
                        grade));

        // Rectification MarkSheet MUST have only ONE EnrolmentEvaluation
        rectificationMarkSheet.getEnrolmentEvaluations().get(0).setRectified(enrolmentEvaluation);
        return rectificationMarkSheet;
    }

    public MarkSheet createRectificationMarkSheet(ExecutionPeriod executionPeriod, Date evaluationDate,
            Teacher responsibleTeacher, MarkSheetType markSheetType, String reason,
            MarkSheetEnrolmentEvaluationBean evaluationBean) {

        return MarkSheet.createRectification(this, executionPeriod, responsibleTeacher, evaluationDate,
                markSheetType, reason, evaluationBean);
    }

    public Collection<MarkSheet> searchMarkSheets(ExecutionPeriod executionPeriod, Teacher teacher,
            Date evaluationDate, MarkSheetState markSheetState, MarkSheetType markSheetType) {

        final String dateFormat = "dd/MM/yyyy";
        final Collection<MarkSheet> result = new HashSet<MarkSheet>();

        for (final MarkSheet markSheet : this.getMarkSheetsSet()) {
            if (executionPeriod != null && markSheet.getExecutionPeriod() != executionPeriod) {
                continue;
            }
            if (teacher != null && markSheet.getResponsibleTeacher() != teacher) {
                continue;
            }
            if (evaluationDate != null && DateFormatUtil.compareDates(dateFormat, evaluationDate, markSheet.getEvaluationDateDateTime().toDate()) != 0) {
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

    public boolean hasScopeInGivenSemesterAndCurricularYearInDCP(CurricularYear curricularYear, DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionPeriod executionPeriod) {

        if (degreeCurricularPlan == null || getDegreeCurricularPlan().equals(degreeCurricularPlan)) {           
            for (DegreeModuleScope degreeModuleScope : getDegreeModuleScopes()) {
                if(degreeModuleScope.isActiveForExecutionPeriod(executionPeriod) && 
                        (curricularYear == null || degreeModuleScope.getCurricularYear().equals(curricularYear.getYear()))) {
                    return true;
                }
            }
        }        
        return false;
    }
          
    public boolean isGradeSubmissionAvailableFor(ExecutionPeriod executionPeriod) {
        return isGradeSubmissionAvailableFor(executionPeriod, MarkSheetType.NORMAL) 
            || isGradeSubmissionAvailableFor(executionPeriod, MarkSheetType.IMPROVEMENT)
            || isGradeSubmissionAvailableFor(executionPeriod, MarkSheetType.SPECIAL_SEASON);
    }
    
    public boolean isGradeSubmissionAvailableFor(ExecutionPeriod executionPeriod, MarkSheetType type) {
        final ExecutionDegree executionDegree = getExecutionDegreeFor(executionPeriod.getExecutionYear());
        switch (type) {
        case NORMAL:
        case IMPROVEMENT:
            if (executionPeriod.getSemester().equals(Integer.valueOf(1))) {
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
    
    public ExecutionDegree getExecutionDegreeFor(ExecutionYear executionYear) {
        return getDegreeCurricularPlan().getExecutionDegreeByYear(executionYear);
    }

	public boolean hasAnyDegreeGradeToSubmit(ExecutionPeriod period) {
		return hasEnrolmentsNotInAnyMarkSheet(period);
	}
	
	public boolean hasAnyDegreeMarkSheetToConfirm(ExecutionPeriod period) {
		for (MarkSheet markSheet : this.getMarkSheetsSet()) {
			if(markSheet.getExecutionPeriod().equals(period) && markSheet.isNotConfirmed()) {
				return true;
			}
		}
		return false;
	}
    
    public List<DegreeModuleScope> getDegreeModuleScopes(){
        return DegreeModuleScope.getDegreeModuleScopes(this);
    }
      
}
