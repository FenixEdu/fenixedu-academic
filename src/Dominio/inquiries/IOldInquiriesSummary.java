/*
 * Created on Nov 19, 2004
 * 
 */
package Dominio.inquiries;

import Dominio.IDegreeInfo;
import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public interface IOldInquiriesSummary extends IDomainObject {
    /**
     * @return Returns the average2_2.
     */
    public Double getAverage2_2();

    /**
     * @param average2_2 The average2_2 to set.
     */
    public void setAverage2_2(Double average2_2);

    /**
     * @return Returns the average2_3.
     */
    public Double getAverage2_3();

    /**
     * @param average2_3 The average2_3 to set.
     */
    public void setAverage2_3(Double average2_3);

    /**
     * @return Returns the average2_4.
     */
    public Double getAverage2_4();

    /**
     * @param average2_4 The average2_4 to set.
     */
    public void setAverage2_4(Double average2_4);

    /**
     * @return Returns the average2_5.
     */
    public Double getAverage2_5();

    /**
     * @param average2_5 The average2_5 to set.
     */
    public void setAverage2_5(Double average2_5);

    /**
     * @return Returns the average2_6.
     */
    public Double getAverage2_6();

    /**
     * @param average2_6 The average2_6 to set.
     */
    public void setAverage2_6(Double average2_6);

    /**
     * @return Returns the average2_7_interval.
     */
    public String getAverage2_7_interval();
    /**
     * @param average2_7_interval The average2_7_interval to set.
     */
    public void setAverage2_7_interval(String average2_7_interval);
    /**
     * @return Returns the average2_7_numerical.
     */
    public Double getAverage2_7_numerical();
    /**
     * @param average2_7_numerical The average2_7_numerical to set.
     */
    public void setAverage2_7_numerical(Double average2_7_numerical);
    /**
     * @return Returns the average2_8.
     */
    public Double getAverage2_8();

    /**
     * @param average2_8 The average2_8 to set.
     */
    public void setAverage2_8(Double average2_8);

    /**
     * @return Returns the average3_10.
     */
    public Double getAverage3_10();

    /**
     * @param average3_10 The average3_10 to set.
     */
    public void setAverage3_10(Double average3_10);

    /**
     * @return Returns the average3_11.
     */
    public Double getAverage3_11();

    /**
     * @param average3_11 The average3_11 to set.
     */
    public void setAverage3_11(Double average3_11);

    /**
     * @return Returns the average3_3_interval.
     */
    public String getAverage3_3_interval();

    /**
     * @param average3_3_interval The average3_3_interval to set.
     */
    public void setAverage3_3_interval(String average3_3_interval);

    /**
     * @return Returns the average3_3_numerical.
     */
    public Double getAverage3_3_numerical();

    /**
     * @param average3_3_numerical The average3_3_numerical to set.
     */
    public void setAverage3_3_numerical(Double average3_3_numerical);

    /**
     * @return Returns the average3_4_interval.
     */
    public String getAverage3_4_interval();

    /**
     * @param average3_4_interval The average3_4_interval to set.
     */
    public void setAverage3_4_interval(String average3_4_interval);

    /**
     * @return Returns the average3_4_numerical.
     */
    public Double getAverage3_4_numerical();

    /**
     * @param average3_4_numerical The average3_4_numerical to set.
     */
    public void setAverage3_4_numerical(Double average3_4_numerical);

    /**
     * @return Returns the average3_5.
     */
    public Double getAverage3_5();

    /**
     * @param average3_5 The average3_5 to set.
     */
    public void setAverage3_5(Double average3_5);

    /**
     * @return Returns the average3_6.
     */
    public Double getAverage3_6();

    /**
     * @param average3_6 The average3_6 to set.
     */
    public void setAverage3_6(Double average3_6);

    /**
     * @return Returns the average3_7.
     */
    public Double getAverage3_7();

    /**
     * @param average3_7 The average3_7 to set.
     */
    public void setAverage3_7(Double average3_7);

    /**
     * @return Returns the average3_8.
     */
    public Double getAverage3_8();

    /**
     * @param average3_8 The average3_8 to set.
     */
    public void setAverage3_8(Double average3_8);

    /**
     * @return Returns the average3_9.
     */
    public Double getAverage3_9();

    /**
     * @param average3_9 The average3_9 to set.
     */
    public void setAverage3_9(Double average3_9);

    /**
     * @return Returns the average6_1.
     */
    public Double getAverage6_1();

    /**
     * @param average6_1 The average6_1 to set.
     */
    public void setAverage6_1(Double average6_1);

    /**
     * @return Returns the average6_2.
     */
    public Double getAverage6_2();

    /**
     * @param average6_2 The average6_2 to set.
     */
    public void setAverage6_2(Double average6_2);

    /**
     * @return Returns the average6_3.
     */
    public Double getAverage6_3();

    /**
     * @param average6_3 The average6_3 to set.
     */
    public void setAverage6_3(Double average6_3);

    /**
     * @return Returns the averageAppreciationCourse.
     */
    public Double getAverageAppreciationCourse();

    /**
     * @param averageAppreciationCourse The averageAppreciationCourse to set.
     */
    public void setAverageAppreciationCourse(Double averageAppreciationCourse);

    /**
     * @return Returns the averageAppreciationTeachers.
     */
    public Double getAverageAppreciationTeachers();

    /**
     * @param averageAppreciationTeachers The averageAppreciationTeachers to set.
     */
    public void setAverageAppreciationTeachers(
            Double averageAppreciationTeachers);

    /**
     * @return Returns the degree.
     */
    public IDegreeInfo getDegree();

    /**
     * @param degree The degree to set.
     */
    public void setDegree(IDegreeInfo degree);

    /**
     * @return Returns the degreeKey.
     */
    public Integer getDegreeKey();

    /**
     * @param degreeKey The degreeKey to set.
     */
    public void setDegreeKey(Integer degreeKey);

    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod();

    /**
     * @param executionPeriod The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod);

    /**
     * @return Returns the firstEnrollment.
     */
    public Double getFirstEnrollment();

    /**
     * @param firstEnrollment The firstEnrollment to set.
     */
    public void setFirstEnrollment(Double firstEnrollment);

    /**
     * @return Returns the gepCourseId.
     */
    public Integer getGepCourseId();

    /**
     * @param gepCourseId The gepCourseId to set.
     */
    public void setGepCourseId(Integer gepCourseId);

    /**
     * @return Returns the gepCourseName.
     */
    public String getGepCourseName();

    /**
     * @param gepCourseName The gepCourseName to set.
     */
    public void setGepCourseName(String gepCourseName);

    /**
     * @return Returns the gepDegreeName.
     */
    public String getGepDegreeName();

    /**
     * @param gepDegreeName The gepDegreeName to set.
     */
    public void setGepDegreeName(String gepDegreeName);

    /**
     * @return Returns the gepExecutionYear.
     */
    public Integer getGepExecutionYear();

    /**
     * @param gepExecutionYear The gepExecutionYear to set.
     */
    public void setGepExecutionYear(Integer gepExecutionYear);

    /**
     * @return Returns the inquiryId.
     */
    public Integer getInquiryId();

    /**
     * @param inquiryId The inquiryId to set.
     */
    public void setInquiryId(Integer inquiryId);

    /**
     * @return Returns the keyCurricularYear.
     */
    public Integer getCurricularYear();

    /**
     * @param keyCurricularYear The keyCurricularYear to set.
     */
    public void setCurricularYear(Integer curricularYear);

    /**
     * @return Returns the keyExecutionPeriod.
     */
    public Integer getKeyExecutionPeriod();

    /**
     * @param keyExecutionPeriod The keyExecutionPeriod to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod);

    /**
     * @return Returns the numberAnswers.
     */
    public Integer getNumberAnswers();

    /**
     * @param numberAnswers The numberAnswers to set.
     */
    public void setNumberAnswers(Integer numberAnswers);

    /**
     * @return Returns the numberApproved.
     */
    public Integer getNumberApproved();

    /**
     * @param numberApproved The numberApproved to set.
     */
    public void setNumberApproved(Integer numberApproved);

    /**
     * @return Returns the numberEnrollments.
     */
    public Integer getNumberEnrollments();

    /**
     * @param numberEnrollments The numberEnrollments to set.
     */
    public void setNumberEnrollments(Integer numberEnrollments);

    /**
     * @return Returns the numberEvaluated.
     */
    public Integer getNumberEvaluated();

    /**
     * @param numberEvaluated The numberEvaluated to set.
     */
    public void setNumberEvaluated(Integer numberEvaluated);

    /**
     * @return Returns the representationQuota.
     */
    public Double getRepresentationQuota();

    /**
     * @param representationQuota The representationQuota to set.
     */
    public void setRepresentationQuota(Double representationQuota);

    /**
     * @return Returns the roomAverage.
     */
    public Double getRoomAverage();

    /**
     * @param roomAverage The roomAverage to set.
     */
    public void setRoomAverage(Double roomAverage);

    /**
     * @return Returns the semester.
     */
    public Integer getSemester();

    /**
     * @param semester The semester to set.
     */
    public void setSemester(Integer semester);
    /**
     * @return Returns the courseCode.
     */
    public String getCourseCode();
    /**
     * @param courseCode The courseCode to set.
     */
    public void setCourseCode(String courseCode);
}