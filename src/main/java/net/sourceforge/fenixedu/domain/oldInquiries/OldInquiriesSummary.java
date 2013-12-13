/*
 * Created on Nov 15, 2004
 *
 */
package net.sourceforge.fenixedu.domain.oldInquiries;

import pt.ist.bennu.core.domain.Bennu;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class OldInquiriesSummary extends OldInquiriesSummary_Base {

    public OldInquiriesSummary() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setExecutionPeriod(null);
        setDegree(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasGepCourseName() {
        return getGepCourseName() != null;
    }

    @Deprecated
    public boolean hasAverage3_3_numerical() {
        return getAverage3_3_numerical() != null;
    }

    @Deprecated
    public boolean hasAverage3_4_interval() {
        return getAverage3_4_interval() != null;
    }

    @Deprecated
    public boolean hasAverageAppreciationCourse() {
        return getAverageAppreciationCourse() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasNumberEvaluated() {
        return getNumberEvaluated() != null;
    }

    @Deprecated
    public boolean hasGepDegreeName() {
        return getGepDegreeName() != null;
    }

    @Deprecated
    public boolean hasNumberEnrollments() {
        return getNumberEnrollments() != null;
    }

    @Deprecated
    public boolean hasNumberAnswers() {
        return getNumberAnswers() != null;
    }

    @Deprecated
    public boolean hasAverage3_3_interval() {
        return getAverage3_3_interval() != null;
    }

    @Deprecated
    public boolean hasFirstEnrollment() {
        return getFirstEnrollment() != null;
    }

    @Deprecated
    public boolean hasAverage3_4_numerical() {
        return getAverage3_4_numerical() != null;
    }

    @Deprecated
    public boolean hasGepCourseId() {
        return getGepCourseId() != null;
    }

    @Deprecated
    public boolean hasAverageAppreciationTeachers() {
        return getAverageAppreciationTeachers() != null;
    }

    @Deprecated
    public boolean hasRoomAverage() {
        return getRoomAverage() != null;
    }

    @Deprecated
    public boolean hasSemester() {
        return getSemester() != null;
    }

    @Deprecated
    public boolean hasCurricularYear() {
        return getCurricularYear() != null;
    }

    @Deprecated
    public boolean hasAverage2_7_numerical() {
        return getAverage2_7_numerical() != null;
    }

    @Deprecated
    public boolean hasNumberApproved() {
        return getNumberApproved() != null;
    }

    @Deprecated
    public boolean hasAverage2_5() {
        return getAverage2_5() != null;
    }

    @Deprecated
    public boolean hasAverage2_4() {
        return getAverage2_4() != null;
    }

    @Deprecated
    public boolean hasAverage2_3() {
        return getAverage2_3() != null;
    }

    @Deprecated
    public boolean hasAverage2_2() {
        return getAverage2_2() != null;
    }

    @Deprecated
    public boolean hasInquiryId() {
        return getInquiryId() != null;
    }

    @Deprecated
    public boolean hasAverage2_8() {
        return getAverage2_8() != null;
    }

    @Deprecated
    public boolean hasAverage3_10() {
        return getAverage3_10() != null;
    }

    @Deprecated
    public boolean hasAverage3_9() {
        return getAverage3_9() != null;
    }

    @Deprecated
    public boolean hasAverage3_11() {
        return getAverage3_11() != null;
    }

    @Deprecated
    public boolean hasAverage2_6() {
        return getAverage2_6() != null;
    }

    @Deprecated
    public boolean hasAverage3_8() {
        return getAverage3_8() != null;
    }

    @Deprecated
    public boolean hasGepExecutionYear() {
        return getGepExecutionYear() != null;
    }

    @Deprecated
    public boolean hasAverage3_7() {
        return getAverage3_7() != null;
    }

    @Deprecated
    public boolean hasAverage6_1() {
        return getAverage6_1() != null;
    }

    @Deprecated
    public boolean hasAverage3_6() {
        return getAverage3_6() != null;
    }

    @Deprecated
    public boolean hasAverage3_5() {
        return getAverage3_5() != null;
    }

    @Deprecated
    public boolean hasAverage6_2() {
        return getAverage6_2() != null;
    }

    @Deprecated
    public boolean hasCourseCode() {
        return getCourseCode() != null;
    }

    @Deprecated
    public boolean hasAverage2_7_interval() {
        return getAverage2_7_interval() != null;
    }

    @Deprecated
    public boolean hasAverage6_3() {
        return getAverage6_3() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasRepresentationQuota() {
        return getRepresentationQuota() != null;
    }

}
