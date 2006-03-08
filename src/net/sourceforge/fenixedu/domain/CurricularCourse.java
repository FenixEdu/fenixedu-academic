package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

public class CurricularCourse extends CurricularCourse_Base {

    protected CurricularCourse() {
        super();
        this.setOjbConcreteClass(CurricularCourse.class.getName());
    }

    protected CurricularCourse(String name, String code, String acronym, Boolean enrolmentAllowed, CurricularStage curricularStage) {
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
            ExecutionPeriod beginExecutionPeriod) {
        
        this();       
        setWeigth(weight);
        setPrerequisites(prerequisites);
        setPrerequisitesEn(prerequisitesEn);
        setCurricularStage(curricularStage);
        setCompetenceCourse(competenceCourse);
        setType(CurricularCourseType.NORMAL_COURSE);
        new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, null);
    }
    
    /**
     * - This constructor is used to create a 'special' curricular course 
     *   that will represent any curricular course accordding to a rule 
     */
    public CurricularCourse(CourseGroup parentCourseGroup, String name, String nameEn,
            CurricularStage curricularStage, CurricularPeriod curricularPeriod, ExecutionPeriod beginExecutionPeriod) {
        
        this();
        setName(name);
        setNameEn(nameEn);
        setCurricularStage(curricularStage);
        setType(CurricularCourseType.OPTIONAL_COURSE);
        new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, null);
    }
    
	public GradeScale getGradeScaleChain() {
    	return super.getGradeScale() != null ? super.getGradeScale() : getDegreeCurricularPlan().getGradeScaleChain();
    }
	
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("[").append(this.getClass()).append(":").append("idInternal = ").append(
                this.getIdInternal()).append(";name = ").append(this.getName()).append(";code = ")
                .append(this.getCode()).append("\n degreeCurricularPlan = ").append(
                        this.getDegreeCurricularPlan()).append(";type = ").append(this.getType());
        return stringBuffer.toString();
    }
    
    public void print(StringBuilder dcp, String tabs, Context previousContext) {
        String tab = tabs + "\t";
        dcp.append(tab);
        dcp.append("[CC ").append(this.getIdInternal()).append("][");
        dcp.append(previousContext.getCurricularPeriod().getOrderByType(CurricularPeriodType.YEAR)).append("Y,");
        dcp.append(previousContext.getCurricularPeriod().getOrderByType(CurricularPeriodType.SEMESTER)).append("S] ");
        dcp.append(this.getName()).append("\n");
    }

    public boolean isLeaf() {
        return true;
    }
    
    public boolean isRoot() {
        return false;
    }
    
    public DegreeCurricularPlan getParentDegreeCurricularPlan() {
        return getDegreeModuleContexts().get(0).getParentCourseGroup().getParentDegreeCurricularPlan();
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
     * - This method is used to edit a 'special' curricular course 
     *   that will represent any curricular course according to a rule 
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
    
    public List<CurricularCourseScope> getActiveScopesInExecutionPeriod(
            final ExecutionPeriod executionPeriod) {
        final List<CurricularCourseScope> activeScopesInExecutionPeriod = new ArrayList<CurricularCourseScope>();
        for (final CurricularCourseScope scope : getScopes()) {
        	final CurricularSemester curricularSemester = scope.getCurricularSemester();
            if (curricularSemester.getSemester().equals(executionPeriod.getSemester())
            		&& (scope.getBeginDate().getTime().getTime() <= executionPeriod.getBeginDate().getTime())
                    && ((scope.getEndDate() == null)
                    		|| (scope.getEndDate().getTime().getTime() >= executionPeriod.getEndDate().getTime()))) {
                activeScopesInExecutionPeriod.add(scope);
            }
        }
        return activeScopesInExecutionPeriod;
    }
    
    public List<CurricularCourseScope> getActiveScopesInExecutionPeriodAndSemester(
            final ExecutionPeriod executionPeriod) {
        List<CurricularCourseScope> activeScopesInExecutionPeriod = new ArrayList<CurricularCourseScope>();
        for (Iterator iter = getScopes().iterator(); iter.hasNext();) {
            CurricularCourseScope scope = (CurricularCourseScope) iter.next();
            if ((scope.getCurricularSemester().getSemester().equals(executionPeriod.getSemester())) && (scope.getBeginDate().getTime().getTime() <= executionPeriod.getBeginDate().getTime())
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
        	if (scope.getBeginDate().getTime().getTime() < executionPeriod.getBeginDate().getTime()){
        		if((scope.getEnd() == null) || (scope.getEnd().getTime() >= executionPeriod.getBeginDate().getTime())){
        			activeScopesInExecutionPeriod.add(scope);
        		}
        	} else {
        		if(scope.getBeginDate().getTime().getTime() <= executionPeriod.getEndDate().getTime()){
        			activeScopesInExecutionPeriod.add(scope);
        		}
        	}
        }
        return activeScopesInExecutionPeriod;
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes
    // -------------------------------------------------------------

    public CurricularYear getCurricularYearByBranchAndSemester(final Branch branch,
            final Integer semester) {
    	Date date = new Date();
    	return getCurricularYearByBranchAndSemester(branch, semester, date);
    }

    public CurricularYear getCurricularYearByBranchAndSemester(final Branch branch,
            final Integer semester, final Date date) {

        if (this.getScopes().size() == 1) {
            return this.getScopes().get(0).getCurricularSemester().getCurricularYear();
        }

        CurricularYear curricularYearToReturn = null;
        List curricularCourseScopesFound = null;
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
                    curricularYearToReturn = getCurricularYearWithLowerYear(curricularCourseScopesFound, date);
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

    public boolean hasActiveScopeInGivenSemesterForGivenBranch(
            final CurricularSemester curricularSemester, final Branch branch) {
        final List<CurricularCourseScope> scopes = this.getScopes();
        for (final CurricularCourseScope curricularCourseScope : scopes) {
            if (curricularCourseScope.getCurricularSemester().equals(curricularSemester)
                    && curricularCourseScope.isActive() && curricularCourseScope.getBranch().equals(branch)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActiveScopeInGivenSemesterForGivenBranch(
            final Integer semester, final Branch branch) {
        final List<CurricularCourseScope> scopes = this.getScopes();
        for (final CurricularCourseScope curricularCourseScope : scopes) {
            if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester)
                    && curricularCourseScope.isActive() && curricularCourseScope.getBranch().equals(branch)) {
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

   private CurricularYear getCurricularYearWithLowerYear(List listOfScopes, Date date) {
        
        if(listOfScopes.isEmpty()) {
        	return null;
        }
        
        CurricularYear minCurricularYear = ((CurricularCourseScope) listOfScopes.get(0)).getCurricularSemester().getCurricularYear();

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
	
	public List<ExecutionCourse> getExecutionCoursesByExecutionPeriod (final ExecutionPeriod executionPeriod) {

		return (List)CollectionUtils.select(getAssociatedExecutionCourses(), new Predicate() {

			public boolean evaluate(Object o) {
				ExecutionCourse executionCourse = (ExecutionCourse)o;
				return executionCourse.getExecutionPeriod().equals(executionPeriod);
			}
		});
	}

    public Curriculum findLatestCurriculum() {
        Curriculum latestCurriculum = null;
        for (final Curriculum curriculum : getAssociatedCurriculums()) {
            if (latestCurriculum == null || latestCurriculum.getLastModificationDate().before(curriculum.getLastModificationDate())) {
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
    
    public Double getTheoreticalHours(Integer order) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getTheoreticalHours(order);
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
    
    public Double getProblemsHours(Integer order) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getProblemsHours(order);
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
    
    public Double getLaboratorialHours(Integer order) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getLaboratorialHours(order);
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
    
    public Double getSeminaryHours(Integer order) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getSeminaryHours(order);
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
    
    public Double getFieldWorkHours(Integer order) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getFieldWorkHours(order);
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
    
    public Double getTrainingPeriodHours(Integer order) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getTrainingPeriodHours(order);
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
    
    public Double getTutorialOrientationHours(Integer order) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getTutorialOrientationHours(order);
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
    
    public Double getAutonomousWorkHours(Integer order) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getAutonomousWorkHours(order);
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
    
    public Double getContactLoad(Integer order) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getContactLoad(order);
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
    
    public Double getTotalLoad(Integer order) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getTotalLoad(order);
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
    
    public Double getEctsCredits(Integer order) {
        double result = 0.0;
        if (this.getCompetenceCourse() != null) {
            result = this.getCompetenceCourse().getEctsCredits(order);
        }
        return result;
    }

    public CurricularSemester getCurricularSemesterWithLowerYearBySemester(Integer semester, Date date) {
    	CurricularSemester result = null;
    	for (CurricularCourseScope curricularCourseScope : getScopes()) {
			if(curricularCourseScope.getCurricularSemester().getSemester().equals(semester) && curricularCourseScope.isActive(date)) {
				if(result == null) {
					result = curricularCourseScope.getCurricularSemester();
				} else {
					if(result.getCurricularYear().getYear() > curricularCourseScope.getCurricularSemester().getCurricularYear().getYear()) {
						result = curricularCourseScope.getCurricularSemester();
					}
				}
			}
		}
    	return result;
    }
    
    private List<EnrolmentEvaluation> getActiveEnrollments(ExecutionPeriod executionPeriod,
            Student student) {
        List<Enrolment> enrollments = getEnrolments();
        List<EnrolmentEvaluation> results = new ArrayList<EnrolmentEvaluation>();
        for (Enrolment enrollment : enrollments) {
            boolean filters = true;
            filters &= !enrollment.getEnrollmentState().equals(EnrollmentState.ANNULED);
            filters &= executionPeriod == null
                    || enrollment.getExecutionPeriod().equals(executionPeriod);
            filters &= student == null
                    || enrollment.getStudentCurricularPlan().getStudent().equals(student);

            if (filters) {
                results.addAll(enrollment.getEvaluations());
            }
        }
        return results;
    }

    public List<EnrolmentEvaluation> getActiveEnrollmentEvaluations(Student student) {
        return getActiveEnrollments(null, student);
    }

    public List<EnrolmentEvaluation> getActiveEnrollmentEvaluations() {
        return getActiveEnrollments(null, null);
    }

    public List<EnrolmentEvaluation> getActiveEnrollmentEvaluations(ExecutionPeriod executionPeriod) {
        return getActiveEnrollments(executionPeriod, null);
    }

    public List<EnrolmentEvaluation> getActiveEnrollmentEvaluations(ExecutionYear executionYear) {
        List<EnrolmentEvaluation> results = new ArrayList<EnrolmentEvaluation>();

        for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
            results.addAll(getActiveEnrollmentEvaluations(executionPeriod));
        }

        return results;
    }
    
    public Context addContext(CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
            ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
        checkContextsFor(parentCourseGroup, curricularPeriod);
        return new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);        
    }
    
    protected void checkContextsFor(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod) {
        for (final Context context : this.getDegreeModuleContexts()) {
            if (context.getParentCourseGroup() == parentCourseGroup && context.getCurricularPeriod() == curricularPeriod) {
                throw new DomainException("courseGroup.contextAlreadyExistForCourseGroup");
            }
        }
    }

    @Override
    public String getName() {
        if ((super.getName() == null || super.getName().length() == 0) && this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getName();
        }
        return super.getName();
    }        
    
        @Override
    public String getNameEn() {
        if ((super.getNameEn() == null || super.getNameEn().length() == 0) && this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getNameEn();
        }
        return super.getNameEn();
    }
    
    @Override
    public String getAcronym() {
        if ((super.getAcronym() == null || super.getAcronym().length() == 0) && this.getCompetenceCourse() != null) {
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
    
    public RegimeType getRegime() {
        if (this.getCompetenceCourse() != null) {
            return this.getCompetenceCourse().getRegime();
		}
        return null;
    }

    @Override
    protected void addOwnPartipatingCurricularRules(final List<CurricularRule> result) {
        // no rules to add
    }

    public boolean isOptional() {
        return getType().equals(CurricularCourseType.OPTIONAL_COURSE);
    }

}
