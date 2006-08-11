/*
 * Created on Dec 22, 2004
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InfoOldInquiriesTeachersRes extends InfoObject implements
        Comparable {
    
	private Integer inquiryId;

	private Integer gepExecutionYear;

	private Integer keyExecutionPeriod;
	private InfoExecutionPeriodWithInfoExecutionYear executionPeriod;
	private Integer semester;

	private Integer keyDegree;
	private InfoDegree degree;

	private Integer curricularYear;

	private Integer gepCourseId;
	private String courseCode;
	private String gepCourseName;

	private String classType;
	private String classTypeLong;

	private Integer keyTeacher;
	private InfoTeacher teacher;
	private Integer teacherNumber;
	private String active;
	private String teacherName;
	
	private String category;
	
	private Integer departmentCode;
	private Integer mail;
	
	private Integer numAnswers3_3;
	private String average3_3;
	private Double deviation3_3;
	private Double tolerance3_3;

	private Integer numAnswers3_4;
	private String average3_4;
	private Double deviation3_4;
	private Double tolerance3_4;

	private Integer numAnswers3_5;
	private Double average3_5;
	private Double deviation3_5;
	private Double tolerance3_5;

	private Integer numAnswers3_6;
	private Double average3_6;
	private Double deviation3_6;
	private Double tolerance3_6;

	private Integer numAnswers3_7;
	private Double average3_7;
	private Double deviation3_7;
	private Double tolerance3_7;

	private Integer numAnswers3_8;
	private Double average3_8;
	private Double deviation3_8;
	private Double tolerance3_8;

	private Integer numAnswers3_9;
	private Double average3_9;
	private Double deviation3_9;
	private Double tolerance3_9;

	private Integer numAnswers3_10;
	private Double average3_10;
	private Double deviation3_10;
	private Double tolerance3_10;

	private Integer numAnswers3_11;
	private Double average3_11;
	private Double deviation3_11;
	private Double tolerance3_11;
	
	private Integer totalNumberAnswers;

    /**
     * @return Returns the active.
     */
    public String getActive() {
        return active;
    }
    /**
     * @param active The active to set.
     */
    public void setActive(String active) {
        this.active = active;
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
     * @return Returns the average3_3.
     */
    public String getAverage3_3() {
        return average3_3;
    }
    /**
     * @param average3_3 The average3_3 to set.
     */
    public void setAverage3_3(String average3_3) {
        this.average3_3 = average3_3;
    }
    /**
     * @return Returns the average3_4.
     */
    public String getAverage3_4() {
        return average3_4;
    }
    /**
     * @param average3_4 The average3_4 to set.
     */
    public void setAverage3_4(String average3_4) {
        this.average3_4 = average3_4;
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
     * @return Returns the category.
     */
    public String getCategory() {
        return category;
    }
    /**
     * @param category The category to set.
     */
    public void setCategory(String category) {
        this.category = category;
    }
    /**
     * @return Returns the classType.
     */
    public String getClassType() {
        return classType;
    }
    /**
     * @param classType The classType to set.
     */
    public void setClassType(String classType) {
        this.classType = classType;
    }
    /**
     * @return Returns the classTypeLong.
     */
    public String getClassTypeLong() {
        return classTypeLong;
    }
    /**
     * @param classTypeLong The classTypeLong to set.
     */
    public void setClassTypeLong(String classTypeLong) {
        this.classTypeLong = classTypeLong;
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
     * @return Returns the departmentCode.
     */
    public Integer getDepartmentCode() {
        return departmentCode;
    }
    /**
     * @param departmentCode The departmentCode to set.
     */
    public void setDepartmentCode(Integer departmentCode) {
        this.departmentCode = departmentCode;
    }
    /**
     * @return Returns the deviation3_10.
     */
    public Double getDeviation3_10() {
        return deviation3_10;
    }
    /**
     * @param deviation3_10 The deviation3_10 to set.
     */
    public void setDeviation3_10(Double deviation3_10) {
        this.deviation3_10 = deviation3_10;
    }
    /**
     * @return Returns the deviation3_11.
     */
    public Double getDeviation3_11() {
        return deviation3_11;
    }
    /**
     * @param deviation3_11 The deviation3_11 to set.
     */
    public void setDeviation3_11(Double deviation3_11) {
        this.deviation3_11 = deviation3_11;
    }
    /**
     * @return Returns the deviation3_3.
     */
    public Double getDeviation3_3() {
        return deviation3_3;
    }
    /**
     * @param deviation3_3 The deviation3_3 to set.
     */
    public void setDeviation3_3(Double deviation3_3) {
        this.deviation3_3 = deviation3_3;
    }
    /**
     * @return Returns the deviation3_4.
     */
    public Double getDeviation3_4() {
        return deviation3_4;
    }
    /**
     * @param deviation3_4 The deviation3_4 to set.
     */
    public void setDeviation3_4(Double deviation3_4) {
        this.deviation3_4 = deviation3_4;
    }
    /**
     * @return Returns the deviation3_5.
     */
    public Double getDeviation3_5() {
        return deviation3_5;
    }
    /**
     * @param deviation3_5 The deviation3_5 to set.
     */
    public void setDeviation3_5(Double deviation3_5) {
        this.deviation3_5 = deviation3_5;
    }
    /**
     * @return Returns the deviation3_6.
     */
    public Double getDeviation3_6() {
        return deviation3_6;
    }
    /**
     * @param deviation3_6 The deviation3_6 to set.
     */
    public void setDeviation3_6(Double deviation3_6) {
        this.deviation3_6 = deviation3_6;
    }
    /**
     * @return Returns the deviation3_7.
     */
    public Double getDeviation3_7() {
        return deviation3_7;
    }
    /**
     * @param deviation3_7 The deviation3_7 to set.
     */
    public void setDeviation3_7(Double deviation3_7) {
        this.deviation3_7 = deviation3_7;
    }
    /**
     * @return Returns the deviation3_8.
     */
    public Double getDeviation3_8() {
        return deviation3_8;
    }
    /**
     * @param deviation3_8 The deviation3_8 to set.
     */
    public void setDeviation3_8(Double deviation3_8) {
        this.deviation3_8 = deviation3_8;
    }
    /**
     * @return Returns the deviation3_9.
     */
    public Double getDeviation3_9() {
        return deviation3_9;
    }
    /**
     * @param deviation3_9 The deviation3_9 to set.
     */
    public void setDeviation3_9(Double deviation3_9) {
        this.deviation3_9 = deviation3_9;
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
    public void setExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear executionPeriod) {
        this.executionPeriod = executionPeriod;
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
     * @return Returns the mail.
     */
    public Integer getMail() {
        return mail;
    }
    /**
     * @param mail The mail to set.
     */
    public void setMail(Integer mail) {
        this.mail = mail;
    }
    /**
     * @return Returns the numAnswers3_10.
     */
    public Integer getNumAnswers3_10() {
        return numAnswers3_10;
    }
    /**
     * @param answers3_10 The numAnswers3_10 to set.
     */
    public void setNumAnswers3_10(Integer answers3_10) {
        numAnswers3_10 = answers3_10;
    }
    /**
     * @return Returns the numAnswers3_11.
     */
    public Integer getNumAnswers3_11() {
        return numAnswers3_11;
    }
    /**
     * @param answers3_11 The numAnswers3_11 to set.
     */
    public void setNumAnswers3_11(Integer answers3_11) {
        numAnswers3_11 = answers3_11;
    }
    /**
     * @return Returns the numAnswers3_3.
     */
    public Integer getNumAnswers3_3() {
        return numAnswers3_3;
    }
    /**
     * @param answers3_3 The numAnswers3_3 to set.
     */
    public void setNumAnswers3_3(Integer answers3_3) {
        numAnswers3_3 = answers3_3;
    }
    /**
     * @return Returns the numAnswers3_4.
     */
    public Integer getNumAnswers3_4() {
        return numAnswers3_4;
    }
    /**
     * @param answers3_4 The numAnswers3_4 to set.
     */
    public void setNumAnswers3_4(Integer answers3_4) {
        numAnswers3_4 = answers3_4;
    }
    /**
     * @return Returns the numAnswers3_5.
     */
    public Integer getNumAnswers3_5() {
        return numAnswers3_5;
    }
    /**
     * @param answers3_5 The numAnswers3_5 to set.
     */
    public void setNumAnswers3_5(Integer answers3_5) {
        numAnswers3_5 = answers3_5;
    }
    /**
     * @return Returns the numAnswers3_6.
     */
    public Integer getNumAnswers3_6() {
        return numAnswers3_6;
    }
    /**
     * @param answers3_6 The numAnswers3_6 to set.
     */
    public void setNumAnswers3_6(Integer answers3_6) {
        numAnswers3_6 = answers3_6;
    }
    /**
     * @return Returns the numAnswers3_7.
     */
    public Integer getNumAnswers3_7() {
        return numAnswers3_7;
    }
    /**
     * @param answers3_7 The numAnswers3_7 to set.
     */
    public void setNumAnswers3_7(Integer answers3_7) {
        numAnswers3_7 = answers3_7;
    }
    /**
     * @return Returns the numAnswers3_8.
     */
    public Integer getNumAnswers3_8() {
        return numAnswers3_8;
    }
    /**
     * @param answers3_8 The numAnswers3_8 to set.
     */
    public void setNumAnswers3_8(Integer answers3_8) {
        numAnswers3_8 = answers3_8;
    }
    /**
     * @return Returns the numAnswers3_9.
     */
    public Integer getNumAnswers3_9() {
        return numAnswers3_9;
    }
    /**
     * @param answers3_9 The numAnswers3_9 to set.
     */
    public void setNumAnswers3_9(Integer answers3_9) {
        numAnswers3_9 = answers3_9;
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
    /**
     * @return Returns the teacherName.
     */
    public String getTeacherName() {
        return teacherName;
    }
    /**
     * @param teacherName The teacherName to set.
     */
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher() {
        return keyTeacher;
    }
    /**
     * @param keyTeacher The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher) {
        this.keyTeacher = keyTeacher;
    }
    /**
     * @return Returns the teacher.
     */
    public InfoTeacher getTeacher() {
        return teacher;
    }
    /**
     * @param teacher The teacher to set.
     */
    public void setTeacher(InfoTeacher teacher) {
        this.teacher = teacher;
    }
    /**
     * @return Returns the teacherNumber.
     */
    public Integer getTeacherNumber() {
        return teacherNumber;
    }
    /**
     * @param teacherNumber The teacherNumber to set.
     */
    public void setTeacherNumber(Integer teacherNumber) {
        this.teacherNumber = teacherNumber;
    }
    /**
     * @return Returns the tolerance3_10.
     */
    public Double getTolerance3_10() {
        return tolerance3_10;
    }
    /**
     * @param tolerance3_10 The tolerance3_10 to set.
     */
    public void setTolerance3_10(Double tolerance3_10) {
        this.tolerance3_10 = tolerance3_10;
    }
    /**
     * @return Returns the tolerance3_11.
     */
    public Double getTolerance3_11() {
        return tolerance3_11;
    }
    /**
     * @param tolerance3_11 The tolerance3_11 to set.
     */
    public void setTolerance3_11(Double tolerance3_11) {
        this.tolerance3_11 = tolerance3_11;
    }
    /**
     * @return Returns the tolerance3_3.
     */
    public Double getTolerance3_3() {
        return tolerance3_3;
    }
    /**
     * @param tolerance3_3 The tolerance3_3 to set.
     */
    public void setTolerance3_3(Double tolerance3_3) {
        this.tolerance3_3 = tolerance3_3;
    }
    /**
     * @return Returns the tolerance3_4.
     */
    public Double getTolerance3_4() {
        return tolerance3_4;
    }
    /**
     * @param tolerance3_4 The tolerance3_4 to set.
     */
    public void setTolerance3_4(Double tolerance3_4) {
        this.tolerance3_4 = tolerance3_4;
    }
    /**
     * @return Returns the tolerance3_5.
     */
    public Double getTolerance3_5() {
        return tolerance3_5;
    }
    /**
     * @param tolerance3_5 The tolerance3_5 to set.
     */
    public void setTolerance3_5(Double tolerance3_5) {
        this.tolerance3_5 = tolerance3_5;
    }
    /**
     * @return Returns the tolerance3_6.
     */
    public Double getTolerance3_6() {
        return tolerance3_6;
    }
    /**
     * @param tolerance3_6 The tolerance3_6 to set.
     */
    public void setTolerance3_6(Double tolerance3_6) {
        this.tolerance3_6 = tolerance3_6;
    }
    /**
     * @return Returns the tolerance3_7.
     */
    public Double getTolerance3_7() {
        return tolerance3_7;
    }
    /**
     * @param tolerance3_7 The tolerance3_7 to set.
     */
    public void setTolerance3_7(Double tolerance3_7) {
        this.tolerance3_7 = tolerance3_7;
    }
    /**
     * @return Returns the tolerance3_8.
     */
    public Double getTolerance3_8() {
        return tolerance3_8;
    }
    /**
     * @param tolerance3_8 The tolerance3_8 to set.
     */
    public void setTolerance3_8(Double tolerance3_8) {
        this.tolerance3_8 = tolerance3_8;
    }
    /**
     * @return Returns the tolerance3_9.
     */
    public Double getTolerance3_9() {
        return tolerance3_9;
    }
    /**
     * @param tolerance3_9 The tolerance3_9 to set.
     */
    public void setTolerance3_9(Double tolerance3_9) {
        this.tolerance3_9 = tolerance3_9;
    }
    /**
     * @return Returns the totalNumberAnswers.
     */
    public Integer getTotalNumberAnswers() {
        return totalNumberAnswers;
    }
    /**
     * @param totalNumberAnswers The totalNumberAnswers to set.
     */
    public void setTotalNumberAnswers(Integer totalNumberAnswers) {
        this.totalNumberAnswers = totalNumberAnswers;
    }
    
    public void copyFromDomain(OldInquiriesTeachersRes oldInquiresTeachersRes) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (oldInquiresTeachersRes != null) {
            super.copyFromDomain(oldInquiresTeachersRes);
            setActive(oldInquiresTeachersRes.getActive());
            setAverage3_10((oldInquiresTeachersRes.getAverage3_10()));
            setAverage3_11((oldInquiresTeachersRes.getAverage3_11()));
            setAverage3_3((oldInquiresTeachersRes.getAverage3_3()));
            setAverage3_4((oldInquiresTeachersRes.getAverage3_4()));
            setAverage3_5((oldInquiresTeachersRes.getAverage3_5()));
            setAverage3_6((oldInquiresTeachersRes.getAverage3_6()));
            setAverage3_7((oldInquiresTeachersRes.getAverage3_7()));
            setAverage3_8((oldInquiresTeachersRes.getAverage3_8()));
            setAverage3_9((oldInquiresTeachersRes.getAverage3_9()));
            setCategory(oldInquiresTeachersRes.getCategory());
            setClassType(oldInquiresTeachersRes.getClassType());
            setClassTypeLong(oldInquiresTeachersRes.getClassTypeLong());
            setCourseCode(oldInquiresTeachersRes.getCourseCode());
            setCurricularYear(oldInquiresTeachersRes.getCurricularYear());
            setDepartmentCode(oldInquiresTeachersRes.getDepartmentCode());
            setDeviation3_10(oldInquiresTeachersRes.getDeviation3_10());
            setDeviation3_11(oldInquiresTeachersRes.getDeviation3_11());
            setDeviation3_3(oldInquiresTeachersRes.getDeviation3_3());
            setDeviation3_4(oldInquiresTeachersRes.getDeviation3_4());
            setDeviation3_5(oldInquiresTeachersRes.getDeviation3_5());
            setDeviation3_6(oldInquiresTeachersRes.getDeviation3_6());
            setDeviation3_7(oldInquiresTeachersRes.getDeviation3_7());
            setDeviation3_8(oldInquiresTeachersRes.getDeviation3_8());
            setDeviation3_9(oldInquiresTeachersRes.getDeviation3_9());
            setGepCourseId(oldInquiresTeachersRes.getGepCourseId());
            setGepCourseName(oldInquiresTeachersRes.getGepCourseName());
            setGepExecutionYear(oldInquiresTeachersRes.getGepExecutionYear());
            setIdInternal(oldInquiresTeachersRes.getIdInternal());
            setInquiryId(oldInquiresTeachersRes.getInquiryId());
            setMail(oldInquiresTeachersRes.getMail());
            setNumAnswers3_10(oldInquiresTeachersRes.getNumAnswers3_10());
            setNumAnswers3_11(oldInquiresTeachersRes.getNumAnswers3_11());
            setNumAnswers3_3(oldInquiresTeachersRes.getNumAnswers3_3());
            setNumAnswers3_4(oldInquiresTeachersRes.getNumAnswers3_4());
            setNumAnswers3_5(oldInquiresTeachersRes.getNumAnswers3_5());
            setNumAnswers3_6(oldInquiresTeachersRes.getNumAnswers3_6());
            setNumAnswers3_7(oldInquiresTeachersRes.getNumAnswers3_7());
            setNumAnswers3_8(oldInquiresTeachersRes.getNumAnswers3_8());
            setNumAnswers3_9(oldInquiresTeachersRes.getNumAnswers3_9());
            setSemester(oldInquiresTeachersRes.getSemester());
            setTeacherName(oldInquiresTeachersRes.getTeacherName());
            setTeacherNumber(oldInquiresTeachersRes.getTeacherNumber());
            setTolerance3_10(oldInquiresTeachersRes.getTolerance3_10());
            setTolerance3_11(oldInquiresTeachersRes.getTolerance3_11());
            setTolerance3_3(oldInquiresTeachersRes.getTolerance3_3());
            setTolerance3_4(oldInquiresTeachersRes.getTolerance3_4());
            setTolerance3_5(oldInquiresTeachersRes.getTolerance3_5());
            setTolerance3_6(oldInquiresTeachersRes.getTolerance3_6());
            setTolerance3_7(oldInquiresTeachersRes.getTolerance3_7());
            setTolerance3_8(oldInquiresTeachersRes.getTolerance3_8());
            setTolerance3_9(oldInquiresTeachersRes.getTolerance3_9());
            setTotalNumberAnswers(oldInquiresTeachersRes.getTotalNumberAnswers());
            this.setExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(oldInquiresTeachersRes.getExecutionPeriod()));
            this.setDegree(InfoDegree.newInfoFromDomain(oldInquiresTeachersRes.getDegree()));
            this.setTeacher(InfoTeacher.newInfoFromDomain(oldInquiresTeachersRes.getTeacher()));
        }
    }
    
    public static InfoOldInquiriesTeachersRes newInfoFromDomain(OldInquiriesTeachersRes oldInquiriesTeachersRes) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        InfoOldInquiriesTeachersRes newInfo = null;
        if(oldInquiriesTeachersRes != null) {
            newInfo = new InfoOldInquiriesTeachersRes();
            newInfo.copyFromDomain(oldInquiriesTeachersRes);
        }
        return newInfo;
    }
    
    public int compareTo(Object arg0) {
        int result = 0;
        if (arg0 instanceof InfoOldInquiriesTeachersRes) {
//            InfoOldInquiriesSummary iois = (InfoOldInquiriesSummary) arg0;
//            result = this.getCurricularYear().intValue() - iois.getCurricularYear().intValue();
//            if(result == 0) {
//                result = this.getGepCourseName().compareTo(iois.getGepCourseName());
//            }
        }
        return result;

    }
    
    

}
