/*
 * Created on Dec 22, 2004
 * 
 */
package Dominio.inquiries;

import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public interface IOldInquiriesTeachersRes extends IDomainObject {
    /**
     * @return Returns the active.
     */
    public String getActive();

    /**
     * @param active The active to set.
     */
    public void setActive(String active);

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
     * @return Returns the average3_3.
     */
    public String getAverage3_3();

    /**
     * @param average3_3 The average3_3 to set.
     */
    public void setAverage3_3(String average3_3);

    /**
     * @return Returns the average3_4.
     */
    public String getAverage3_4();

    /**
     * @param average3_4 The average3_4 to set.
     */
    public void setAverage3_4(String average3_4);

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
     * @return Returns the category.
     */
    public String getCategory();

    /**
     * @param category The category to set.
     */
    public void setCategory(String category);

    /**
     * @return Returns the classType.
     */
    public String getClassType();

    /**
     * @param classType The classType to set.
     */
    public void setClassType(String classType);

    /**
     * @return Returns the classTypeLong.
     */
    public String getClassTypeLong();
    
    /**
     * @param classTypeLong The classTypeLong to set.
     */
    public void setClassTypeLong(String classTypeLong);
    
    /**
     * @return Returns the courseCode.
     */
    public String getCourseCode();

    /**
     * @param courseCode The courseCode to set.
     */
    public void setCourseCode(String courseCode);

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
    public ICurso getDegree();

    /**
     * @param degree The degree to set.
     */
    public void setDegree(ICurso degree);

    /**
     * @return Returns the departmentCode.
     */
    public Integer getDepartmentCode();

    /**
     * @param departmentCode The departmentCode to set.
     */
    public void setDepartmentCode(Integer departmentCode);

    /**
     * @return Returns the deviation3_10.
     */
    public Double getDeviation3_10();

    /**
     * @param deviation3_10 The deviation3_10 to set.
     */
    public void setDeviation3_10(Double deviation3_10);

    /**
     * @return Returns the deviation3_11.
     */
    public Double getDeviation3_11();

    /**
     * @param deviation3_11 The deviation3_11 to set.
     */
    public void setDeviation3_11(Double deviation3_11);

    /**
     * @return Returns the deviation3_3.
     */
    public Double getDeviation3_3();

    /**
     * @param deviation3_3 The deviation3_3 to set.
     */
    public void setDeviation3_3(Double deviation3_3);

    /**
     * @return Returns the deviation3_4.
     */
    public Double getDeviation3_4();

    /**
     * @param deviation3_4 The deviation3_4 to set.
     */
    public void setDeviation3_4(Double deviation3_4);

    /**
     * @return Returns the deviation3_5.
     */
    public Double getDeviation3_5();

    /**
     * @param deviation3_5 The deviation3_5 to set.
     */
    public void setDeviation3_5(Double deviation3_5);

    /**
     * @return Returns the deviation3_6.
     */
    public Double getDeviation3_6();

    /**
     * @param deviation3_6 The deviation3_6 to set.
     */
    public void setDeviation3_6(Double deviation3_6);

    /**
     * @return Returns the deviation3_7.
     */
    public Double getDeviation3_7();

    /**
     * @param deviation3_7 The deviation3_7 to set.
     */
    public void setDeviation3_7(Double deviation3_7);

    /**
     * @return Returns the deviation3_8.
     */
    public Double getDeviation3_8();

    /**
     * @param deviation3_8 The deviation3_8 to set.
     */
    public void setDeviation3_8(Double deviation3_8);

    /**
     * @return Returns the deviation3_9.
     */
    public Double getDeviation3_9();

    /**
     * @param deviation3_9 The deviation3_9 to set.
     */
    public void setDeviation3_9(Double deviation3_9);

    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod();

    /**
     * @param executionPeriod The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod);

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
     * @return Returns the curricularCourse.
     */
    public ICurricularCourse getCurricularCourse();
    
    /**
     * @param curricularCourse The curricularCourse to set.
     */
    public void setCurricularCourse(ICurricularCourse curricularCourse);
    
    /**
     * @return Returns the keyCurricularCourse.
     */
    public Integer getKeyCurricularCourse();

    /**
     * @param keyCurricularCourse The keyCurricularCourse to set.
     */
    public void setKeyCurricularCourse(Integer keyCurricularCourse);

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
     * @return Returns the keyDegree.
     */
    public Integer getKeyDegree();

    /**
     * @param keyDegree The keyDegree to set.
     */
    public void setKeyDegree(Integer keyDegree);

    /**
     * @return Returns the keyExecutionPeriod.
     */
    public Integer getKeyExecutionPeriod();

    /**
     * @param keyExecutionPeriod The keyExecutionPeriod to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod);

    /**
     * @return Returns the mail.
     */
    public Integer getMail();

    /**
     * @param mail The mail to set.
     */
    public void setMail(Integer mail);

    /**
     * @return Returns the numAnswers3_10.
     */
    public Integer getNumAnswers3_10();

    /**
     * @param answers3_10 The numAnswers3_10 to set.
     */
    public void setNumAnswers3_10(Integer answers3_10);

    /**
     * @return Returns the numAnswers3_11.
     */
    public Integer getNumAnswers3_11();

    /**
     * @param answers3_11 The numAnswers3_11 to set.
     */
    public void setNumAnswers3_11(Integer answers3_11);

    /**
     * @return Returns the numAnswers3_3.
     */
    public Integer getNumAnswers3_3();

    /**
     * @param answers3_3 The numAnswers3_3 to set.
     */
    public void setNumAnswers3_3(Integer answers3_3);

    /**
     * @return Returns the numAnswers3_4.
     */
    public Integer getNumAnswers3_4();

    /**
     * @param answers3_4 The numAnswers3_4 to set.
     */
    public void setNumAnswers3_4(Integer answers3_4);

    /**
     * @return Returns the numAnswers3_5.
     */
    public Integer getNumAnswers3_5();

    /**
     * @param answers3_5 The numAnswers3_5 to set.
     */
    public void setNumAnswers3_5(Integer answers3_5);

    /**
     * @return Returns the numAnswers3_6.
     */
    public Integer getNumAnswers3_6();

    /**
     * @param answers3_6 The numAnswers3_6 to set.
     */
    public void setNumAnswers3_6(Integer answers3_6);

    /**
     * @return Returns the numAnswers3_7.
     */
    public Integer getNumAnswers3_7();

    /**
     * @param answers3_7 The numAnswers3_7 to set.
     */
    public void setNumAnswers3_7(Integer answers3_7);

    /**
     * @return Returns the numAnswers3_8.
     */
    public Integer getNumAnswers3_8();

    /**
     * @param answers3_8 The numAnswers3_8 to set.
     */
    public void setNumAnswers3_8(Integer answers3_8);

    /**
     * @return Returns the numAnswers3_9.
     */
    public Integer getNumAnswers3_9();

    /**
     * @param answers3_9 The numAnswers3_9 to set.
     */
    public void setNumAnswers3_9(Integer answers3_9);

    /**
     * @return Returns the semester.
     */
    public Integer getSemester();

    /**
     * @param semester The semester to set.
     */
    public void setSemester(Integer semester);

    /**
     * @return Returns the teacherName.
     */
    public String getTeacherName();

    /**
     * @param teacherName The teacherName to set.
     */
    public void setTeacherName(String teacherName);

    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher();
    /**
     * @param keyTeacher The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher);
    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher();
    /**
     * @param teacher The teacher to set.
     */
    public void setTeacher(ITeacher teacher);
    /**
     * @return Returns the teacherNumber.
     */
    public Integer getTeacherNumber();

    /**
     * @param teacherNumber The teacherNumber to set.
     */
    public void setTeacherNumber(Integer teacherNumber);

    /**
     * @return Returns the tolerance3_10.
     */
    public Double getTolerance3_10();

    /**
     * @param tolerance3_10 The tolerance3_10 to set.
     */
    public void setTolerance3_10(Double tolerance3_10);

    /**
     * @return Returns the tolerance3_11.
     */
    public Double getTolerance3_11();

    /**
     * @param tolerance3_11 The tolerance3_11 to set.
     */
    public void setTolerance3_11(Double tolerance3_11);

    /**
     * @return Returns the tolerance3_3.
     */
    public Double getTolerance3_3();

    /**
     * @param tolerance3_3 The tolerance3_3 to set.
     */
    public void setTolerance3_3(Double tolerance3_3);

    /**
     * @return Returns the tolerance3_4.
     */
    public Double getTolerance3_4();

    /**
     * @param tolerance3_4 The tolerance3_4 to set.
     */
    public void setTolerance3_4(Double tolerance3_4);

    /**
     * @return Returns the tolerance3_5.
     */
    public Double getTolerance3_5();

    /**
     * @param tolerance3_5 The tolerance3_5 to set.
     */
    public void setTolerance3_5(Double tolerance3_5);

    /**
     * @return Returns the tolerance3_6.
     */
    public Double getTolerance3_6();

    /**
     * @param tolerance3_6 The tolerance3_6 to set.
     */
    public void setTolerance3_6(Double tolerance3_6);

    /**
     * @return Returns the tolerance3_7.
     */
    public Double getTolerance3_7();

    /**
     * @param tolerance3_7 The tolerance3_7 to set.
     */
    public void setTolerance3_7(Double tolerance3_7);

    /**
     * @return Returns the tolerance3_8.
     */
    public Double getTolerance3_8();

    /**
     * @param tolerance3_8 The tolerance3_8 to set.
     */
    public void setTolerance3_8(Double tolerance3_8);

    /**
     * @return Returns the tolerance3_9.
     */
    public Double getTolerance3_9();

    /**
     * @param tolerance3_9 The tolerance3_9 to set.
     */
    public void setTolerance3_9(Double tolerance3_9);

    /**
     * @return Returns the totalNumberAnswers.
     */
    public Integer getTotalNumberAnswers();

    /**
     * @param totalNumberAnswers The totalNumberAnswers to set.
     */
    public void setTotalNumberAnswers(Integer totalNumberAnswers);
}