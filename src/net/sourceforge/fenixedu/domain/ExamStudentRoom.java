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
        setExam(exam);
        setStudent(student);
        setRoom(room);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof IExamStudentRoom) {
            IExamStudentRoom examStudentRoom = (IExamStudentRoom) obj;
            IRoom room = examStudentRoom.getRoom();
            IRoom thisRoom = this.getRoom();
            resultado = this.getExam().equals(examStudentRoom.getExam())
                    && (((thisRoom == null) && (room == null)) || ((thisRoom != null) && (thisRoom
                            .equals(room)))) && this.getStudent().equals(examStudentRoom.getStudent());
        }
        return resultado;
    }
}