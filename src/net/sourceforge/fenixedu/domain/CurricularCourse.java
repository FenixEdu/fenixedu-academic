package net.sourceforge.fenixedu.domain;

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

    private IDepartmentCourse departmentCourse;

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

    private String acronym;

    // For enrollment purposes
    private Integer maximumValueForAcumulatedEnrollments;

    private Integer minimumValueForAcumulatedEnrollments;

    private Integer enrollmentWeigth;

    private Boolean mandatoryEnrollment;

    private Boolean enrollmentAllowed;

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
        TipoCurso tipoCurso = (this.getDegreeCurricularPlan() != null && this.getDegreeCurricularPlan()
                .getDegree() != null) ? this.getDegreeCurricularPlan().getDegree().getTipoCurso() : null;
        this.uniqueKeyForEnrollment = constructUniqueEnrollmentKey(this.getCode(), this.getName(),
                tipoCurso);
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
        TipoCurso tipoCurso = (this.getDegreeCurricularPlan() != null && this.getDegreeCurricularPlan()
                .getDegree() != null) ? this.getDegreeCurricularPlan().getDegree().getTipoCurso() : null;
        this.uniqueKeyForEnrollment = constructUniqueEnrollmentKey(this.getCode(), this.getName(),
                tipoCurso);
    }

    public Integer getDegreeCurricularPlanKey() {
        return degreeCurricularPlanKey;
    }

    public void setDegreeCurricularPlanKey(Integer degreeCurricularPlanKey) {
        this.degreeCurricularPlanKey = degreeCurricularPlanKey;
    }

    public IDepartmentCourse getDepartmentCourse() {
        return departmentCourse;
    }

    public void setDepartmentCourse(IDepartmentCourse departmentCourse) {
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
        TipoCurso tipoCurso = (this.getDegreeCurricularPlan() != null && this.getDegreeCurricularPlan()
                .getDegree() != null) ? this.getDegreeCurricularPlan().getDegree().getTipoCurso() : null;
        this.uniqueKeyForEnrollment = constructUniqueEnrollmentKey(this.getCode(), this.getName(),
                tipoCurso);
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

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
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
    /**
     * @return Returns the enrollmentAllowed.
     */
    public Boolean getEnrollmentAllowed() {
        return enrollmentAllowed;
    }

    /**
     * @param enrollmentAllowed
     *            The enrollmentAllowed to set.
     */
    public void setEnrollmentAllowed(Boolean enrollmentAllowed) {
        this.enrollmentAllowed = enrollmentAllowed;
    }

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