/*
 * Created on 30/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author João Mota
 *  
 */
public class ExamStudentRoom extends ExamStudentRoom_Base {

    /**
     *  
     */
    public ExamStudentRoom() {
    }

    public ExamStudentRoom(IExam exam, IStudent student, IRoom room) {
        this.setExam(exam);
        this.setStudent(student);
        this.setRoom(room);
    }

    public boolean equals(Object obj) {
        if (obj instanceof IExamStudentRoom) {
            final IExamStudentRoom examStudentRoom = (IExamStudentRoom) obj;
            return this.getIdInternal().equals(examStudentRoom.getIdInternal());
        }
        return false;
    }

}
