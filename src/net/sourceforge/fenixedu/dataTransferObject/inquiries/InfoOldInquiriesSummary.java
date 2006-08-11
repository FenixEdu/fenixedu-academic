/*
 * Created on Nov 26, 2004
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;

/**
 * @author João Fialho & Rita Ferreira & Gonçalo Luiz
 * 
 */
public class InfoOldInquiriesSummary extends InfoObject implements Comparable{

	private Integer keyExecutionPeriod;
	private InfoExecutionPeriod executionPeriod;
	
	private Integer inquiryId;
	
	private Integer gepExecutionYear;
	private Integer semester;
	
	private Integer keyDegree;
	private InfoDegree degree;
	
	private String gepDegreeName;
	private Integer curricularYear;
	
	private Double averageAppreciationTeachers;
	private Double averageAppreciationCourse;
	private Double representationQuota;
	
	private String courseCode;
	private Integer gepCourseId;
	private String gepCourseName;
	
	private Integer numberEnrollments;
	private Integer numberApproved;
	private Integer numberEvaluated;
	private Integer numberAnswers;
	private Double roomAverage;
	private Double firstEnrollment;
	
	private Double average2_2;
	private Double average2_3;
	private Double average2_4;
	private Double average2_5;
	private Double average2_6;
	private Double average2_7_numerical;
	private String average2_7_interval;
	private Double average2_8;
	
	private Double average3_3_numerical;
	private String average3_3_interval;
	private Double average3_4_numerical;
	private String average3_4_interval;
	private Double average3_5;
	private Double average3_6;
	private Double average3_7;
	private Double average3_8;
	private Double average3_9;
	private Double average3_10;
	private Double average3_11;

	private Double average6_1;
	private Double average6_2;
	private Double average6_3;

    
    /**
     * @return Returns the average2_2.
     */
    public Double getAverage2_2() {
        return average2_2;
    }
    /**
     * @param average2_2 The average2_2 to set.
     */
    public void setAverage2_2(Double average2_2) {
        this.average2_2 = average2_2;
    }
    /**
     * @return Returns the average2_3.
     */
    public Double getAverage2_3() {
        return average2_3;
    }
    /**
     * @param average2_3 The average2_3 to set.
     */
    public void setAverage2_3(Double average2_3) {
        this.average2_3 = average2_3;
    }
    /**
     * @return Returns the average2_4.
     */
    public Double getAverage2_4() {
        return average2_4;
    }
    /**
     * @param average2_4 The average2_4 to set.
     */
    public void setAverage2_4(Double average2_4) {
        this.average2_4 = average2_4;
    }
    /**
     * @return Returns the average2_5.
     */
    public Double getAverage2_5() {
        return average2_5;
    }
    /**
     * @param average2_5 The average2_5 to set.
     */
    public void setAverage2_5(Double average2_5) {
        this.average2_5 = average2_5;
    }
    /**
     * @return Returns the average2_6.
     */
    public Double getAverage2_6() {
        return average2_6;
    }
    /**
     * @param average2_6 The average2_6 to set.
     */
    public void setAverage2_6(Double average2_6) {
        this.average2_6 = average2_6;
    }
    /**
     * @return Returns the average2_7_interval.
     */
    public String getAverage2_7_interval() {
        return average2_7_interval;
    }
    /**
     * @param average2_7_interval The average2_7_interval to set.
     */
    public void setAverage2_7_interval(String average2_7_interval) {
        this.average2_7_interval = average2_7_interval;
    }
    /**
     * @return Returns the average2_7_numerical.
     */
    public Double getAverage2_7_numerical() {
        return average2_7_numerical;
    }
    /**
     * @param average2_7_numerical The average2_7_numerical to set.
     */
    public void setAverage2_7_numerical(Double average2_7_numerical) {
        this.average2_7_numerical = average2_7_numerical;
    }
    /**
     * @return Returns the average2_8.
     */
    public Double getAverage2_8() {
        return average2_8;
    }
    /**
     * @param average2_8 The average2_8 to set.
     */
    public void setAverage2_8(Double average2_8) {
        this.average2_8 = average2_8;
    }
    /**
     * @return Returns the average3_10.
     */
    public Double getAverage3_10() {
        return average3_10;
    }
    /**
     * @param average3_10 The average3_10 to set.
     */
    public void setAverage3_10(Double average3_10) {
        this.average3_10 = average3_10;
    }
    /**
     * @return Returns the average3_11.
     */
    public Double getAverage3_11() {
        return average3_11;
    }
    /**
     * @param average3_11 The average3_11 to set.
     */
    public void setAverage3_11(Double average3_11) {
        this.average3_11 = average3_11;
    }
    /**
     * @return Returns the average3_3_interval.
     */
    public String getAverage3_3_interval() {
        return average3_3_interval;
    }
    /**
     * @param average3_3_interval The average3_3_interval to set.
     */
    public void setAverage3_3_interval(String average3_3_interval) {
        this.average3_3_interval = average3_3_interval;
    }
    /**
     * @return Returns the average3_3_numerical.
     */
    public Double getAverage3_3_numerical() {
        return average3_3_numerical;
    }
    /**
     * @param average3_3_numerical The average3_3_numerical to set.
     */
    public void setAverage3_3_numerical(Double average3_3_numerical) {
        this.average3_3_numerical = average3_3_numerical;
    }
    /**
     * @return Returns the average3_4_interval.
     */
    public String getAverage3_4_interval() {
        return average3_4_interval;
    }
    /**
     * @param average3_4_interval The average3_4_interval to set.
     */
    public void setAverage3_4_interval(String average3_4_interval) {
        this.average3_4_interval = average3_4_interval;
    }
    /**
     * @return Returns the average3_4_numerical.
     */
    public Double getAverage3_4_numerical() {
        return average3_4_numerical;
    }
    /**
     * @param average3_4_numerical The average3_4_numerical to set.
     */
    public void setAverage3_4_numerical(Double average3_4_numerical) {
        this.average3_4_numerical = average3_4_numerical;
    }
    /**
     * @return Returns the average3_5.
     */
    public Double getAverage3_5() {
        return average3_5;
    }
    /**
     * @param average3_5 The average3_5 to set.
     */
    public void setAverage3_5(Double average3_5) {
        this.average3_5 = average3_5;
    }
    /**
     * @return Returns the average3_6.
     */
    public Double getAverage3_6() {
        return average3_6;
    }
    /**
     * @param average3_6 The average3_6 to set.
     */
    public void setAverage3_6(Double average3_6) {
        this.average3_6 = average3_6;
    }
    /**
     * @return Returns the average3_7.
     */
    public Double getAverage3_7() {
        return average3_7;
    }
    /**
     * @param average3_7 The average3_7 to set.
     */
    public void setAverage3_7(Double average3_7) {
        this.average3_7 = average3_7;
    }
    /**
     * @return Returns the average3_8.
     */
    public Double getAverage3_8() {
        return average3_8;
    }
    /**
     * @param average3_8 The average3_8 to set.
     */
    public void setAverage3_8(Double average3_8) {
        this.average3_8 = average3_8;
    }
    /**
     * @return Returns the average3_9.
     */
    public Double getAverage3_9() {
        return average3_9;
    }
    /**
     * @param average3_9 The average3_9 to set.
     */
    public void setAverage3_9(Double average3_9) {
        this.average3_9 = average3_9;
    }
    /**
     * @return Returns the average6_1.
     */
    public Double getAverage6_1() {
        return average6_1;
    }
    /**
     * @param average6_1 The average6_1 to set.
     */
    public void setAverage6_1(Double average6_1) {
        this.average6_1 = average6_1;
    }
    /**
     * @return Returns the average6_2.
     */
    public Double getAverage6_2() {
        return average6_2;
    }
    /**
     * @param average6_2 The average6_2 to set.
     */
    public void setAverage6_2(Double average6_2) {
        this.average6_2 = average6_2;
    }
    /**
     * @return Returns the average6_3.
     */
    public Double getAverage6_3() {
        return average6_3;
    }
    /**
     * @param average6_3 The average6_3 to set.
     */
    public void setAverage6_3(Double average6_3) {
        this.average6_3 = average6_3;
    }
    /**
     * @return Returns the averageAppreciationCourse.
     */
    public Double getAverageAppreciationCourse() {
        return averageAppreciationCourse;
    }
    /**
     * @param averageAppreciationCourse The averageAppreciationCourse to set.
     */
    public void setAverageAppreciationCourse(Double averageAppreciationCourse) {
        this.averageAppreciationCourse = averageAppreciationCourse;
    }
    /**
     * @return Returns the averageAppreciationTeachers.
     */
    public Double getAverageAppreciationTeachers() {
        return averageAppreciationTeachers;
    }
    /**
     * @param averageAppreciationTeachers The averageAppreciationTeachers to set.
     */
    public void setAverageAppreciationTeachers(
            Double averageAppreciationTeachers) {
        this.averageAppreciationTeachers = averageAppreciationTeachers;
    }
    /**
     * @return Returns the courseCode.
     */
    public String getCourseCode() {
        return courseCode;
    }
    /**
     * @param courseCode The courseCode to set.
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    /**
     * @return Returns the curricularYear.
     */
    public Integer getCurricularYear() {
        return curricularYear;
    }
    /**
     * @param curricularYear The curricularYear to set.
     */
    public void setCurricularYear(Integer curricularYear) {
        this.curricularYear = curricularYear;
    }
    /**
     * @return Returns the degree.
     */
    public InfoDegree getDegree() {
        return degree;
    }
    /**
     * @param degree The degree to set.
     */
    public void setDegree(InfoDegree degree) {
        this.degree = degree;
    }
    /**
     * @return Returns the executionPeriod.
     */
    public InfoExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }
    /**
     * @param executionPeriod The executionPeriod to set.
     */
    public void setExecutionPeriod(InfoExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }
    /**
     * @return Returns the firstEnrollment.
     */
    public Double getFirstEnrollment() {
        return firstEnrollment;
    }
    /**
     * @param firstEnrollment The firstEnrollment to set.
     */
    public void setFirstEnrollment(Double firstEnrollment) {
        this.firstEnrollment = firstEnrollment;
    }
    /**
     * @return Returns the gepCourseId.
     */
    public Integer getGepCourseId() {
        return gepCourseId;
    }
    /**
     * @param gepCourseId The gepCourseId to set.
     */
    public void setGepCourseId(Integer gepCourseId) {
        this.gepCourseId = gepCourseId;
    }
    /**
     * @return Returns the gepCourseName.
     */
    public String getGepCourseName() {
        return gepCourseName;
    }
    /**
     * @param gepCourseName The gepCourseName to set.
     */
    public void setGepCourseName(String gepCourseName) {
        this.gepCourseName = gepCourseName;
    }
    /**
     * @return Returns the gepDegreeName.
     */
    public String getGepDegreeName() {
        return gepDegreeName;
    }
    /**
     * @param gepDegreeName The gepDegreeName to set.
     */
    public void setGepDegreeName(String gepDegreeName) {
        this.gepDegreeName = gepDegreeName;
    }
    /**
     * @return Returns the gepExecutionYear.
     */
    public Integer getGepExecutionYear() {
        return gepExecutionYear;
    }
    /**
     * @param gepExecutionYear The gepExecutionYear to set.
     */
    public void setGepExecutionYear(Integer gepExecutionYear) {
        this.gepExecutionYear = gepExecutionYear;
    }
    /**
     * @return Returns the inquiryId.
     */
    public Integer getInquiryId() {
        return inquiryId;
    }
    /**
     * @param inquiryId The inquiryId to set.
     */
    public void setInquiryId(Integer inquiryId) {
        this.inquiryId = inquiryId;
    }
    /**
     * @return Returns the keyDegree.
     */
    public Integer getKeyDegree() {
        return keyDegree;
    }
    /**
     * @param keyDegree The keyDegree to set.
     */
    public void setKeyDegree(Integer keyDegree) {
        this.keyDegree = keyDegree;
    }
    /**
     * @return Returns the keyExecutionPeriod.
     */
    public Integer getKeyExecutionPeriod() {
        return keyExecutionPeriod;
    }
    /**
     * @param keyExecutionPeriod The keyExecutionPeriod to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod) {
        this.keyExecutionPeriod = keyExecutionPeriod;
    }
    /**
     * @return Returns the numberAnswers.
     */
    public Integer getNumberAnswers() {
        return numberAnswers;
    }
    /**
     * @param numberAnswers The numberAnswers to set.
     */
    public void setNumberAnswers(Integer numberAnswers) {
        this.numberAnswers = numberAnswers;
    }
    /**
     * @return Returns the numberApproved.
     */
    public Integer getNumberApproved() {
        return numberApproved;
    }
    /**
     * @param numberApproved The numberApproved to set.
     */
    public void setNumberApproved(Integer numberApproved) {
        this.numberApproved = numberApproved;
    }
    /**
     * @return Returns the numberEnrollments.
     */
    public Integer getNumberEnrollments() {
        return numberEnrollments;
    }
    /**
     * @param numberEnrollments The numberEnrollments to set.
     */
    public void setNumberEnrollments(Integer numberEnrollments) {
        this.numberEnrollments = numberEnrollments;
    }
    /**
     * @return Returns the numberEvaluated.
     */
    public Integer getNumberEvaluated() {
        return numberEvaluated;
    }
    /**
     * @param numberEvaluated The numberEvaluated to set.
     */
    public void setNumberEvaluated(Integer numberEvaluated) {
        this.numberEvaluated = numberEvaluated;
    }
    /**
     * @return Returns the representationQuota.
     */
    public Double getRepresentationQuota() {
        return representationQuota;
    }
    /**
     * @param representationQuota The representationQuota to set.
     */
    public void setRepresentationQuota(Double representationQuota) {
        this.representationQuota = representationQuota;
    }
    /**
     * @return Returns the roomAverage.
     */
    public Double getRoomAverage() {
        return roomAverage;
    }
    /**
     * @param roomAverage The roomAverage to set.
     */
    public void setRoomAverage(Double roomAverage) {
        this.roomAverage = roomAverage;
    }
    /**
     * @return Returns the semester.
     */
    public Integer getSemester() {
        return semester;
    }
    /**
     * @param semester The semester to set.
     */
    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    
    public static InfoOldInquiriesSummary newInfoFromDomain(OldInquiriesSummary oldInquiriesSummary) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        InfoOldInquiriesSummary newInfo = null;
        if(oldInquiriesSummary != null) {
            newInfo = new InfoOldInquiriesSummary();
            newInfo.copyFromDomain(oldInquiriesSummary);
        }
        return newInfo;
    }
       
    public void copyFromDomain(OldInquiriesSummary oldInquiresSummary) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (oldInquiresSummary != null) {
            super.copyFromDomain(oldInquiresSummary);
        }

        setAverage2_2(oldInquiresSummary.getAverage2_2());
        setAverage2_3(oldInquiresSummary.getAverage2_3());
        setAverage2_4(oldInquiresSummary.getAverage2_4());
        setAverage2_5(oldInquiresSummary.getAverage2_5());
        setAverage2_6(oldInquiresSummary.getAverage2_6());
        setAverage2_7_interval(oldInquiresSummary.getAverage2_7_interval());
        setAverage2_7_numerical(oldInquiresSummary.getAverage2_7_numerical());
        setAverage2_8(oldInquiresSummary.getAverage2_8());
        setAverage3_10(oldInquiresSummary.getAverage3_10());
        setAverage3_11(oldInquiresSummary.getAverage3_11());
        setAverage3_3_interval(oldInquiresSummary.getAverage3_3_interval());
        setAverage3_3_numerical(oldInquiresSummary.getAverage3_3_numerical());
        setAverage3_4_interval(oldInquiresSummary.getAverage3_4_interval());
        setAverage3_4_numerical(oldInquiresSummary.getAverage3_4_numerical());
        setAverage3_5(oldInquiresSummary.getAverage3_5());
        setAverage3_6(oldInquiresSummary.getAverage3_6());
        setAverage3_7(oldInquiresSummary.getAverage3_7());
        setAverage3_8(oldInquiresSummary.getAverage3_8());
        setAverage3_9(oldInquiresSummary.getAverage3_9());
        setAverage6_1(oldInquiresSummary.getAverage6_1());
        setAverage6_2(oldInquiresSummary.getAverage6_2());
        setAverage6_3(oldInquiresSummary.getAverage6_3());
        setAverageAppreciationCourse(oldInquiresSummary.getAverageAppreciationCourse());
        setAverageAppreciationTeachers(oldInquiresSummary.getAverageAppreciationTeachers());
        setCourseCode(oldInquiresSummary.getCourseCode());
        setCurricularYear(oldInquiresSummary.getCurricularYear());
        setFirstEnrollment(oldInquiresSummary.getFirstEnrollment());
        setGepCourseId(oldInquiresSummary.getGepCourseId());
        setGepCourseName(oldInquiresSummary.getGepCourseName());
        setGepDegreeName(oldInquiresSummary.getGepDegreeName());
        setGepExecutionYear(oldInquiresSummary.getGepExecutionYear());
        setIdInternal(oldInquiresSummary.getIdInternal());
        setInquiryId(oldInquiresSummary.getInquiryId());
        setNumberAnswers(oldInquiresSummary.getNumberAnswers());
        setNumberApproved(oldInquiresSummary.getNumberApproved());
        setNumberEnrollments(oldInquiresSummary.getNumberEnrollments());
        setNumberEvaluated(oldInquiresSummary.getNumberEvaluated());
        setRepresentationQuota(oldInquiresSummary.getRepresentationQuota());
        setRoomAverage(oldInquiresSummary.getRoomAverage());
        setSemester(oldInquiresSummary.getSemester());
        this.setExecutionPeriod(InfoExecutionPeriod.newInfoFromDomain(oldInquiresSummary.getExecutionPeriod()));
        this.setDegree(InfoDegree.newInfoFromDomain(oldInquiresSummary.getDegree()));
        
    }

    public int compareTo(Object arg0) {
        int result = 0;
        if (arg0 instanceof InfoOldInquiriesSummary)
        {
            InfoOldInquiriesSummary iois = (InfoOldInquiriesSummary) arg0;
            result = this.getCurricularYear().intValue() - iois.getCurricularYear().intValue();
            if(result == 0) {
                result = this.getGepCourseName().compareTo(iois.getGepCourseName());
            }
        }
        return result;

    }
       
}
