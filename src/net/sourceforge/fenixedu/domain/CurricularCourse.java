package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.util.BranchType;
import net.sourceforge.fenixedu.util.CurricularCourseExecutionScope;
import net.sourceforge.fenixedu.util.CurricularCourseType;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * @author David Santos on Jul 12, 2004
 */

public class CurricularCourse extends CurricularCourse_Base {
    private CurricularCourseType type;
    private CurricularCourseExecutionScope curricularCourseExecutionScope;

    // For enrollment purposes
    private String uniqueKeyForEnrollment;

    public CurricularCourse() {
    }

    public boolean equals(Object obj) {
        if (obj instanceof ICurricularCourse) {
            final ICurricularCourse curricularCourse = (ICurricularCourse) obj;
            return getIdInternal().equals(curricularCourse.getIdInternal());
        }
        return false;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[").append(this.getClass()).append(":").append("idInternal = ").append(
                this.getIdInternal()).append(";name = ").append(this.getName()).append(";code = ").append(
                this.getCode()).append("\n degreeCurricularPlan = ").append(this.getDegreeCurricularPlan())
                .append(";type = ").append(this.getType());
        return stringBuffer.toString();
    }

    public void setCode(String code) {
        super.setCode(code);
        TipoCurso tipoCurso = (this.getDegreeCurricularPlan() != null && this.getDegreeCurricularPlan()
                .getDegree() != null) ? this.getDegreeCurricularPlan().getDegree().getTipoCurso() : null;
        this.uniqueKeyForEnrollment = constructUniqueEnrollmentKey(this.getCode(), this.getName(),
                tipoCurso);
    }

    public CurricularCourseExecutionScope getCurricularCourseExecutionScope() {
        return curricularCourseExecutionScope;
    }

    public void setCurricularCourseExecutionScope(
            CurricularCourseExecutionScope curricularCourseExecutionScope) {
        this.curricularCourseExecutionScope = curricularCourseExecutionScope;
    }

    public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
        super.setDegreeCurricularPlan(degreeCurricularPlan);
        TipoCurso tipoCurso = (this.getDegreeCurricularPlan() != null && this.getDegreeCurricularPlan()
                .getDegree() != null) ? this.getDegreeCurricularPlan().getDegree().getTipoCurso() : null;
        this.uniqueKeyForEnrollment = constructUniqueEnrollmentKey(this.getCode(), this.getName(),
                tipoCurso);
    }

    public void setName(String name) {
        super.setName(name);
        TipoCurso tipoCurso = (this.getDegreeCurricularPlan() != null && this.getDegreeCurricularPlan()
                .getDegree() != null) ? this.getDegreeCurricularPlan().getDegree().getTipoCurso() : null;
        this.uniqueKeyForEnrollment = constructUniqueEnrollmentKey(this.getCode(), this.getName(),
                tipoCurso);
    }

    public CurricularCourseType getType() {
        return type;
    }

    public void setType(CurricularCourseType type) {
        this.type = type;
    }

    public boolean curricularCourseIsMandatory() {
        return getMandatory().booleanValue();
    }
    
    public List<ICurricularCourseScope> getActiveScopesInExecutionPeriod(final IExecutionPeriod executionPeriod){
        List<ICurricularCourseScope> activeScopesInExecutionPeriod = new ArrayList<ICurricularCourseScope>();
        for (Iterator iter = getScopes().iterator(); iter.hasNext();) {
            ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
            if((scope.getBeginDate().getTime().getTime() <= executionPeriod.getBeginDate().getTime()) && ((scope.getEndDate() == null) || (scope.getEndDate().getTime().getTime() >= executionPeriod.getEndDate().getTime()))) {
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
            return ((ICurricularCourseScope) this.getScopes().get(0)).getCurricularSemester()
                    .getCurricularYear();
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
                    //                    if (curricularCourseScopesFound.size() == 1) {
                    //                        curricularYearToReturn =
                    // getCurricularYearWithLowerYear(this.getScopes());
                    //                    } else {
                    curricularYearToReturn = getCurricularYearWithLowerYear(curricularCourseScopesFound);
                    //                    }
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
        return uniqueKeyForEnrollment;
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
                return ((curricularCourseScope.getBranch().getBranchType().equals(
                        BranchType.COMMON_BRANCH) || curricularCourseScope.getBranch().equals(branch))
                        && curricularCourseScope.getCurricularSemester().getSemester().equals(semester)
                        && curricularCourseScope.isActive().booleanValue());
            }
        });

        return !result.isEmpty();
    }

    private ICurricularYear getCurricularYearWithLowerYear(List listOfScopes) {

        ICurricularYear maxCurricularYear = new CurricularYear();
        maxCurricularYear.setYear(new Integer(10));

        ICurricularYear actualCurricularYear = null;

        int size = listOfScopes.size();

        for (int i = 0; i < size; i++) {

            ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) listOfScopes.get(i);
            actualCurricularYear = curricularCourseScope.getCurricularSemester().getCurricularYear();

            if (maxCurricularYear.getYear().intValue() > actualCurricularYear.getYear().intValue()
                    && curricularCourseScope.isActive().booleanValue()) {

                maxCurricularYear = actualCurricularYear;
            }
        }

        return maxCurricularYear;
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes
    // -------------------------------------------------------------

    private String constructUniqueEnrollmentKey(String code, String name, TipoCurso tipoCurso) {
        StringBuffer stringBuffer = new StringBuffer(50);
        stringBuffer.append(code);
        stringBuffer.append(name);
        if (tipoCurso != null) {
            stringBuffer.append(tipoCurso.toString());
        }
        return StringUtils.lowerCase(stringBuffer.toString());
    }
}