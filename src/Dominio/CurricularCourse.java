package Dominio;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Util.CurricularCourseExecutionScope;
import Util.CurricularCourseType;

/**
 * @author David Santos on Jul 12, 2004
 */

public class CurricularCourse extends DomainObject implements ICurricularCourse {

    private Integer departmentCourseKey;

    private Integer degreeCurricularPlanKey;

    private Integer universityKey;

    private Integer keyScientificArea;

    private Double credits;

    private Double theoreticalHours;

    private Double praticalHours;

    private Double theoPratHours;

    private Double labHours;

    private String name;

    private String code;

    private IDisciplinaDepartamento departmentCourse;

    private IDegreeCurricularPlan degreeCurricularPlan;

    private CurricularCourseType type;

    private CurricularCourseExecutionScope curricularCourseExecutionScope;

    private Boolean mandatory;

    private IUniversity university;

    private Boolean basic;

    private List associatedExecutionCourses;

    private List scopes;

    private IScientificArea scientificArea;

    private Double ectsCredits;

    private Double weigth;

    // For enrollment purposes
    private Integer maximumValueForAcumulatedEnrollments;

    private Integer minimumValueForAcumulatedEnrollments;

    private Integer enrollmentWeigth;

    private Boolean mandatoryEnrollment;

    public CurricularCourse() {
    }

//    public boolean equals(Object obj) {
//        if (getIdInternal() != null && obj != null && obj instanceof ICurricularCourse) {
//            ICurricularCourse curricularCourse = (ICurricularCourse) obj;
//            return getIdInternal().equals(curricularCourse.getIdInternal());
//        } else {
//            return false;
//        }
//    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[").append(this.getClass()).append(":").append("idInternal = ").append(
                this.getIdInternal()).append(";name = ").append(this.name).append(";code = ").append(
                this.code).append("\n degreeCurricularPlan = ").append(this.getDegreeCurricularPlan())
                .append(";type = ").append(this.type);
        return stringBuffer.toString();
    }

    public List getAssociatedExecutionCourses() {
        return associatedExecutionCourses;
    }

    public void setAssociatedExecutionCourses(List associatedExecutionCourses) {
        this.associatedExecutionCourses = associatedExecutionCourses;
    }

    public Boolean getBasic() {
        return basic;
    }

    public void setBasic(Boolean basic) {
        this.basic = basic;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public CurricularCourseExecutionScope getCurricularCourseExecutionScope() {
        return curricularCourseExecutionScope;
    }

    public void setCurricularCourseExecutionScope(
            CurricularCourseExecutionScope curricularCourseExecutionScope) {
        this.curricularCourseExecutionScope = curricularCourseExecutionScope;
    }

    public IDegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public Integer getDegreeCurricularPlanKey() {
        return degreeCurricularPlanKey;
    }

    public void setDegreeCurricularPlanKey(Integer degreeCurricularPlanKey) {
        this.degreeCurricularPlanKey = degreeCurricularPlanKey;
    }

    public IDisciplinaDepartamento getDepartmentCourse() {
        return departmentCourse;
    }

    public void setDepartmentCourse(IDisciplinaDepartamento departmentCourse) {
        this.departmentCourse = departmentCourse;
    }

    public Integer getDepartmentCourseKey() {
        return departmentCourseKey;
    }

    public void setDepartmentCourseKey(Integer departmentCourseKey) {
        this.departmentCourseKey = departmentCourseKey;
    }

    public Double getEctsCredits() {
        return ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
        this.ectsCredits = ectsCredits;
    }

    public Integer getEnrollmentWeigth() {
        return enrollmentWeigth;
    }

    public void setEnrollmentWeigth(Integer enrollmentWeigth) {
        this.enrollmentWeigth = enrollmentWeigth;
    }

    public Integer getKeyScientificArea() {
        return keyScientificArea;
    }

    public void setKeyScientificArea(Integer keyScientificArea) {
        this.keyScientificArea = keyScientificArea;
    }

    public Double getLabHours() {
        return labHours;
    }

    public void setLabHours(Double labHours) {
        this.labHours = labHours;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Boolean getMandatoryEnrollment() {
        return mandatoryEnrollment;
    }

    public void setMandatoryEnrollment(Boolean mandatoryEnrollment) {
        this.mandatoryEnrollment = mandatoryEnrollment;
    }

    public Integer getMaximumValueForAcumulatedEnrollments() {
        return maximumValueForAcumulatedEnrollments;
    }

    public void setMaximumValueForAcumulatedEnrollments(Integer maximumValueForAcumulatedEnrollments) {
        this.maximumValueForAcumulatedEnrollments = maximumValueForAcumulatedEnrollments;
    }

    public Integer getMinimumValueForAcumulatedEnrollments() {
        return minimumValueForAcumulatedEnrollments;
    }

    public void setMinimumValueForAcumulatedEnrollments(Integer minimumValueForAcumulatedEnrollments) {
        this.minimumValueForAcumulatedEnrollments = minimumValueForAcumulatedEnrollments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPraticalHours() {
        return praticalHours;
    }

    public void setPraticalHours(Double praticalHours) {
        this.praticalHours = praticalHours;
    }

    public IScientificArea getScientificArea() {
        return scientificArea;
    }

    public void setScientificArea(IScientificArea scientificArea) {
        this.scientificArea = scientificArea;
    }

    public List getScopes() {
        return scopes;
    }

    public void setScopes(List scopes) {
        this.scopes = scopes;
    }

    public Double getTheoPratHours() {
        return theoPratHours;
    }

    public void setTheoPratHours(Double theoPratHours) {
        this.theoPratHours = theoPratHours;
    }

    public Double getTheoreticalHours() {
        return theoreticalHours;
    }

    public void setTheoreticalHours(Double theoreticalHours) {
        this.theoreticalHours = theoreticalHours;
    }

    public CurricularCourseType getType() {
        return type;
    }

    public void setType(CurricularCourseType type) {
        this.type = type;
    }

    public IUniversity getUniversity() {
        return university;
    }

    public void setUniversity(IUniversity university) {
        this.university = university;
    }

    public Integer getUniversityKey() {
        return universityKey;
    }

    public void setUniversityKey(Integer universityKey) {
        this.universityKey = universityKey;
    }

    public Double getWeigth() {
        return weigth;
    }

    public void setWeigth(Double weigth) {
        this.weigth = weigth;
    }

    public boolean curricularCourseIsMandatory() {
        return getMandatory().booleanValue();
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
                    curricularYearToReturn = getCurricularYearWithLowerYear(curricularCourseScopesFound);
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
        return this.getCode() + this.getName() + this.getDegreeCurricularPlan().getDegree().getNome()
                + this.getDegreeCurricularPlan().getDegree().getTipoCurso();
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

    private ICurricularYear getCurricularYearWithLowerYear(List listOfScopes) {

        ICurricularYear maxCurricularYear = new CurricularYear();
        maxCurricularYear.setYear(new Integer(10));

        ICurricularYear actualCurricularYear = null;

        int size = listOfScopes.size();

        for (int i = 0; i < size; i++) {

            ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) listOfScopes.get(i);
            actualCurricularYear = curricularCourseScope.getCurricularSemester().getCurricularYear();

            if (maxCurricularYear.getYear().intValue() > actualCurricularYear.getYear().intValue()) {

                maxCurricularYear = actualCurricularYear;
            }
        }

        return maxCurricularYear;
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes
    // -------------------------------------------------------------
}