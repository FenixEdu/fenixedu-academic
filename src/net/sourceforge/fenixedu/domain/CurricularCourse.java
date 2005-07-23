package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * @author David Santos on Jul 12, 2004
 */

public class CurricularCourse extends CurricularCourse_Base {

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[").append(this.getClass()).append(":").append("idInternal = ").append(
                this.getIdInternal()).append(";name = ").append(this.getName()).append(";code = ")
                .append(this.getCode()).append("\n degreeCurricularPlan = ").append(
                        this.getDegreeCurricularPlan()).append(";type = ").append(this.getType());
        return stringBuffer.toString();
    }

    public boolean curricularCourseIsMandatory() {
        return getMandatory().booleanValue();
    }

    public List<ICurricularCourseScope> getActiveScopesInExecutionPeriod(
            final IExecutionPeriod executionPeriod) {
        List<ICurricularCourseScope> activeScopesInExecutionPeriod = new ArrayList<ICurricularCourseScope>();
        for (Iterator iter = getScopes().iterator(); iter.hasNext();) {
            ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
            if ((scope.getBeginDate().getTime().getTime() <= executionPeriod.getBeginDate().getTime())
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

    public ICurricularYear getCurricularYearByBranchAndSemester(final IBranch branch,
            final Integer semester) {

        if (this.getScopes().size() == 1) {
            return this.getScopes().get(0).getCurricularSemester().getCurricularYear();
        }

        ICurricularYear curricularYearToReturn = null;
        List curricularCourseScopesFound = null;
        ICurricularCourseScope curricularCourseScopeFound = null;
        boolean foundInBranchButNotInSemester = false;
        boolean notFoundInBranch = false;
        boolean notFoundInSemester = false;

        if (branch != null) {
            curricularCourseScopesFound = (List) CollectionUtils.select(this.getScopes(),
                    new Predicate() {
                        public boolean evaluate(Object arg0) {
                            return ((ICurricularCourseScope) arg0).getBranch().equals(branch);
                        }
                    });

            if (curricularCourseScopesFound != null && !curricularCourseScopesFound.isEmpty()) {

                if (semester != null) {
                    curricularCourseScopeFound = (ICurricularCourseScope) CollectionUtils.find(
                            curricularCourseScopesFound, new Predicate() {
                                public boolean evaluate(Object arg0) {
                                    return ((ICurricularCourseScope) arg0).getCurricularSemester()
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
            curricularYearToReturn = getCurricularYearWithLowerYear(curricularCourseScopesFound);
        }

        if (notFoundInBranch) {

            if (semester != null) {
                curricularCourseScopesFound = (List) CollectionUtils.select(this.getScopes(),
                        new Predicate() {
                            public boolean evaluate(Object arg0) {
                                return ((ICurricularCourseScope) arg0).getCurricularSemester()
                                        .getSemester().equals(semester);
                            }
                        });

                if (curricularCourseScopesFound != null && !curricularCourseScopesFound.isEmpty()) {
                    // if (curricularCourseScopesFound.size() == 1) {
                    // curricularYearToReturn =
                    // getCurricularYearWithLowerYear(this.getScopes());
                    // } else {
                    curricularYearToReturn = getCurricularYearWithLowerYear(curricularCourseScopesFound);
                    // }
                } else {
                    notFoundInSemester = true;
                }
            } else {
                notFoundInSemester = true;
            }
        }

        if (notFoundInSemester) {
            curricularYearToReturn = getCurricularYearWithLowerYear(this.getScopes());
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
                ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) obj;
                return (curricularCourseScope.getCurricularSemester().getSemester().equals(semester) && curricularCourseScope
                        .isActive().booleanValue());
            }
        });

        return !result.isEmpty();
    }

    public boolean hasActiveScopeInGivenSemesterForGivenBranch(final Integer semester,
            final IBranch branch) {
        List scopes = this.getScopes();

        List result = (List) CollectionUtils.select(scopes, new Predicate() {
            public boolean evaluate(Object obj) {
                ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) obj;
                return (curricularCourseScope.getCurricularSemester().getSemester().equals(semester)
                        && curricularCourseScope.isActive().booleanValue() && curricularCourseScope
                        .getBranch().equals(branch));
            }
        });

        return !result.isEmpty();
    }

    public boolean hasActiveScopeInGivenSemesterForCommonAndGivenBranch(final Integer semester,
            final IBranch branch) {
        List scopes = this.getScopes();

        List result = (List) CollectionUtils.select(scopes, new Predicate() {
            public boolean evaluate(Object obj) {
                ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) obj;
                return ((curricularCourseScope.getBranch().getBranchType().equals(BranchType.COMNBR) || curricularCourseScope
                        .getBranch().equals(branch))
                        && curricularCourseScope.getCurricularSemester().getSemester().equals(semester) && curricularCourseScope
                        .isActive().booleanValue());
            }
        });

        return !result.isEmpty();
    }

    private ICurricularYear getCurricularYearWithLowerYear(List listOfScopes) {

		ICurricularCourseScope minYearScope = (ICurricularCourseScope)Collections.min(listOfScopes, 
								new BeanComparator("curricularSemester.curricularYear.year"));
		
		return minYearScope.getCurricularSemester().getCurricularYear();        
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

    public ICurriculum insertCurriculum(String program, String programEn, String operacionalObjectives,
            String operacionalObjectivesEn, String generalObjectives, String generalObjectivesEn) {

        ICurriculum curriculum = new Curriculum();
        
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
	
	public List getExecutionCoursesByExecutionPeriod (final IExecutionPeriod executionPeriod) {

		return (List)CollectionUtils.select(getAssociatedExecutionCourses(), new Predicate() {

			public boolean evaluate(Object o) {
				IExecutionCourse executionCourse = (IExecutionCourse)o;
				return executionCourse.getExecutionPeriod().equals(executionPeriod);
			}
		});
	}
}
