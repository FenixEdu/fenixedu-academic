/*
 * InfoViewExamByDayAndShift.java
 *
 * Created on 2003/03/29
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

public class InfoExecutionCourseAndExams extends InfoObject {

    protected InfoExecutionCourse infoExecutionCourse;

    protected InfoExam infoExam1;

    protected InfoExam infoExam2;

    protected Integer numberStudentesAttendingCourse;

    public InfoExecutionCourseAndExams() {
    }

    public InfoExecutionCourseAndExams(InfoExecutionCourse infoExecutionCourse, InfoExam infoExam1,
            InfoExam infoExam2, Integer numberStudentesAttendingCourse) {
        this.setInfoExam1(infoExam1);
        this.setInfoExam2(infoExam2);
        this.setInfoExecutionCourse(infoExecutionCourse);
        this.setNumberStudentesAttendingCourse(numberStudentesAttendingCourse);
    }

    public boolean equals(Object obj) {
        if (obj instanceof InfoExecutionCourseAndExams) {
            InfoExecutionCourseAndExams examObj = (InfoExecutionCourseAndExams) obj;
            return this.getInfoExecutionCourse().equals(examObj.getInfoExecutionCourse());
        }

        return false;
    }

    /**
     * @return
     */
    public InfoExam getInfoExam1() {
        return infoExam1;
    }

    /**
     * @return
     */
    public InfoExam getInfoExam2() {
        return infoExam2;
    }

    /**
     * @return
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    /**
     * @return
     */
    public Integer getNumberStudentesAttendingCourse() {
        return numberStudentesAttendingCourse;
    }

    /**
     * @param exam
     */
    public void setInfoExam1(InfoExam exam) {
        infoExam1 = exam;
    }

    /**
     * @param exam
     */
    public void setInfoExam2(InfoExam exam) {
        infoExam2 = exam;
    }

    /**
     * @param course
     */
    public void setInfoExecutionCourse(InfoExecutionCourse course) {
        infoExecutionCourse = course;
    }

    /**
     * @param integer
     */
    public void setNumberStudentesAttendingCourse(Integer integer) {
        numberStudentesAttendingCourse = integer;
    }

}