/*
 * Created on Mar 8, 2005
 * 
 */
package Dominio.inquiries;

import Dominio.IDegree;
import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public interface IOldInquiriesCoursesRes extends IDomainObject {
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
     * @return Returns the average2_7.
     */
    public String getAverage2_7();

    /**
     * @param average2_7 The average2_7 to set.
     */
    public void setAverage2_7(String average2_7);

    /**
     * @return Returns the average2_8.
     */
    public Double getAverage2_8();

    /**
     * @param average2_8 The average2_8 to set.
     */
    public void setAverage2_8(Double average2_8);

    /**
     * @return Returns the courseCode.
     */
    public String getCourseCode();

    /**
     * @param courseCode The courseCode to set.
     */
    public void setCourseCode(String courseCode);

    /**
     * @return Returns the gepCourseName.
     */
    public String getGepCourseName();
    
    /**
     * @param gepCourseName The gepCourseName to set.
     */
    public void setGepCourseName(String gepCourseName);

    /**
     * @return Returns the curricularYear.
     */
    public Integer getCurricularYear();

    /**
     * @param curricularYear The curricularYear to set.
     */
    public void setCurricularYear(Integer curricularYear);

    /**
     * @return Returns the degree.
     */
    public IDegree getDegree();

    /**
     * @param degree The degree to set.
     */
    public void setDegree(IDegree degree);

    /**
     * @return Returns the keyDegree.
     */
    public Integer getKeyDegree();

    /**
     * @param keyDegree The keyDegree to set.
     */
    public void setKeyDegree(Integer keyDegree);

    /**
     * @return Returns the deviation2_2.
     */
    public Double getDeviation2_2();

    /**
     * @param deviation2_2 The deviation2_2 to set.
     */
    public void setDeviation2_2(Double deviation2_2);

    /**
     * @return Returns the deviation2_3.
     */
    public Double getDeviation2_3();

    /**
     * @param deviation2_3 The deviation2_3 to set.
     */
    public void setDeviation2_3(Double deviation2_3);

    /**
     * @return Returns the deviation2_4.
     */
    public Double getDeviation2_4();

    /**
     * @param deviation2_4 The deviation2_4 to set.
     */
    public void setDeviation2_4(Double deviation2_4);

    /**
     * @return Returns the deviation2_5.
     */
    public Double getDeviation2_5();

    /**
     * @param deviation2_5 The deviation2_5 to set.
     */
    public void setDeviation2_5(Double deviation2_5);

    /**
     * @return Returns the deviation2_6.
     */
    public Double getDeviation2_6();

    /**
     * @param deviation2_6 The deviation2_6 to set.
     */
    public void setDeviation2_6(Double deviation2_6);

    /**
     * @return Returns the deviation2_8.
     */
    public Double getDeviation2_8();

    /**
     * @param deviation2_8 The deviation2_8 to set.
     */
    public void setDeviation2_8(Double deviation2_8);

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
    public Integer getFirstEnrollment();

    /**
     * @param firstEnrollment The firstEnrollment to set.
     */
    public void setFirstEnrollment(Integer firstEnrollment);

    /**
     * @return Returns the gepCourseId.
     */
    public Integer getGepCourseId();

    /**
     * @param gepCourseId The gepCourseId to set.
     */
    public void setGepCourseId(Integer gepCourseId);

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
     * @return Returns the keyExecutionPeriod.
     */
    public Integer getKeyExecutionPeriod();

    /**
     * @param keyExecutionPeriod The keyExecutionPeriod to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod);

    /**
     * @return Returns the numAnswers2_2.
     */
    public Integer getNumAnswers2_2();

    /**
     * @param numAnswers2_2 The numAnswers2_2 to set.
     */
    public void setNumAnswers2_2(Integer numAnswers2_2);

    /**
     * @return Returns the numAnswers2_3.
     */
    public Integer getNumAnswers2_3();

    /**
     * @param numAnswers2_3 The numAnswers2_3 to set.
     */
    public void setNumAnswers2_3(Integer numAnswers2_3);

    /**
     * @return Returns the numAnswers2_4.
     */
    public Integer getNumAnswers2_4();

    /**
     * @param numAnswers2_4 The numAnswers2_4 to set.
     */
    public void setNumAnswers2_4(Integer numAnswers2_4);

    /**
     * @return Returns the numAnswers2_5_number.
     */
    public Integer getNumAnswers2_5_number();

    /**
     * @param numAnswers2_5_number The numAnswers2_5_number to set.
     */
    public void setNumAnswers2_5_number(Integer numAnswers2_5_number);

    /**
     * @return Returns the numAnswers2_5_text.
     */
    public Integer getNumAnswers2_5_text();

    /**
     * @param numAnswers2_5_text The numAnswers2_5_text to set.
     */
    public void setNumAnswers2_5_text(Integer numAnswers2_5_text);

    /**
     * @return Returns the numAnswers2_6.
     */
    public Integer getNumAnswers2_6();

    /**
     * @param numAnswers2_6 The numAnswers2_6 to set.
     */
    public void setNumAnswers2_6(Integer numAnswers2_6);

    /**
     * @return Returns the numAnswers2_7.
     */
    public Integer getNumAnswers2_7();

    /**
     * @param numAnswers2_7 The numAnswers2_7 to set.
     */
    public void setNumAnswers2_7(Integer numAnswers2_7);

    /**
     * @return Returns the numAnswers2_8.
     */
    public Integer getNumAnswers2_8();

    /**
     * @param numAnswers2_8 The numAnswers2_8 to set.
     */
    public void setNumAnswers2_8(Integer numAnswers2_8);

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
    public Double getNumberApproved();

    /**
     * @param numberApproved The numberApproved to set.
     */
    public void setNumberApproved(Double numberApproved);

    /**
     * @return Returns the numberEnrollments.
     */
    public Double getNumberEnrollments();

    /**
     * @param numberEnrollments The numberEnrollments to set.
     */
    public void setNumberEnrollments(Double numberEnrollments);

    /**
     * @return Returns the numberEvaluated.
     */
    public Double getNumberEvaluated();

    /**
     * @param numberEvaluated The numberEvaluated to set.
     */
    public void setNumberEvaluated(Double numberEvaluated);

    /**
     * @return Returns the representationQuota.
     */
    public Double getRepresentationQuota();

    /**
     * @param representationQuota The representationQuota to set.
     */
    public void setRepresentationQuota(Double representationQuota);

    /**
     * @return Returns the semester.
     */
    public Integer getSemester();

    /**
     * @param semester The semester to set.
     */
    public void setSemester(Integer semester);

    /**
     * @return Returns the tolerance2_2.
     */
    public Double getTolerance2_2();

    /**
     * @param tolerance2_2 The tolerance2_2 to set.
     */
    public void setTolerance2_2(Double tolerance2_2);

    /**
     * @return Returns the tolerance2_3.
     */
    public Double getTolerance2_3();

    /**
     * @param tolerance2_3 The tolerance2_3 to set.
     */
    public void setTolerance2_3(Double tolerance2_3);

    /**
     * @return Returns the tolerance2_4.
     */
    public Double getTolerance2_4();

    /**
     * @param tolerance2_4 The tolerance2_4 to set.
     */
    public void setTolerance2_4(Double tolerance2_4);

    /**
     * @return Returns the tolerance2_5.
     */
    public Double getTolerance2_5();

    /**
     * @param tolerance2_5 The tolerance2_5 to set.
     */
    public void setTolerance2_5(Double tolerance2_5);

    /**
     * @return Returns the tolerance2_6.
     */
    public Double getTolerance2_6();

    /**
     * @param tolerance2_6 The tolerance2_6 to set.
     */
    public void setTolerance2_6(Double tolerance2_6);

    /**
     * @return Returns the tolerance2_8.
     */
    public Double getTolerance2_8();

    /**
     * @param tolerance2_8 The tolerance2_8 to set.
     */
    public void setTolerance2_8(Double tolerance2_8);
}