package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * @author David Santos on Jul 12, 2004
 */

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
            CourseGroup courseGroup, CurricularSemester curricularSemester,
            ExecutionPeriod beginExecutionPeriod) {
        
        this();       
        setWeigth(weight);
        setPrerequisites(prerequisites);
        setPrerequisitesEn(prerequisitesEn);
        setCurricularStage(curricularStage);
        setCompetenceCourse(competenceCourse);
        new Context(courseGroup, this, curricularSemester, beginExecutionPeriod, null);
    }
    
	public GradeScale getGradeScaleChain() {
    	return super.getGradeScale() != null ? super.getGradeScale() : getDegreeCurricularPlan().getGradeScaleChain();
    }
	
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[").append(this.getClass()).append(":").append("idInternal = ").append(
                this.getIdInternal()).append(";name = ").append(this.getName()).append(";code = ")
                .append(this.getCode()).append("\n degreeCurricularPlan = ").append(
                        this.getDegreeCurricularPlan()).append(";type = ").append(this.getType());
        return stringBuffer.toString();
    }
    
    public void print(StringBuffer dcp, String tabs, Context previousContext) {
        String tab = tabs + "\t";
        dcp.append(tab);
        dcp.append("[CC ").append(this.getIdInternal()).append("][");
        dcp.append(previousContext.getCurricularSemester().getCurricularYear().getYear()).append("Y,");
        dcp.append(previousContext.getCurricularSemester().getSemester()).append("S] ");
        dcp.append(this.getName()).append("\n");
    }

    public boolean isLeaf() {
        return true;
    }
    
    public void edit(Double weight, String prerequisites, String prerequisitesEn,
            CurricularStage curricularStage, CompetenceCourse competenceCourse) {
        
        setWeigth(weight);
        setPrerequisites(prerequisites);
        setPrerequisitesEn(prerequisitesEn);
        setCurricularStage(curricularStage);
        setCompetenceCourse(competenceCourse);
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
        StringBuffer stringBuffer = new StringBuffer(50);
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
	
	public List getExecutionCoursesByExecutionPeriod (final ExecutionPeriod executionPeriod) {

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
    
    public Double computeEctsCredits() {
        Double result = 0.0;        
        if (this.getEctsCredits() != null) {
            result = this.getEctsCredits();
        } /*else if (this.getCompetenceCourse() != null && this.getCompetenceCourse().getEctsCredits() != null) {
            result = this.getCompetenceCourse().getEctsCredits();
        }   */     
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
    
    protected void checkContextsFor(final CourseGroup parentCourseGroup, final CurricularSemester curricularSemester) {
        for (final Context context : this.getDegreeModuleContexts()) {
            if (context.getCourseGroup() == parentCourseGroup && context.getCurricularSemester() == curricularSemester) {
                throw new DomainException("error.contextAlreadyExistForCourseGroup");
            }
        }
    }

    @Override
    public String getName() {
        if (super.getName() == null || super.getName().length() == 0) {
            return this.getCompetenceCourse().getName();
        }
        return super.getName();
    }

    @Override
    public String getNameEn() {
        if (super.getNameEn() == null || super.getNameEn().length() == 0) {
            return this.getCompetenceCourse().getNameEn();
        }
        return super.getNameEn();
    }
    
    @Override
    public String getAcronym() {
        if (super.getAcronym() == null || super.getAcronym().length() == 0) {
            return this.getCompetenceCourse().getAcronym();
        }
        return super.getAcronym();
    }
    
    @Override
    public Boolean getBasic() {
        if (super.getBasic() == null) {
            return this.getCompetenceCourse().isBasic();
        }
        return super.getBasic();
    }    
}
