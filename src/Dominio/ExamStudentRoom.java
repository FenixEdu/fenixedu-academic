/*
 * Created on 30/Mai/2003
 *
 * 
 */
package Dominio;

/**
 * @author João Mota
 *  
 */
public class ExamStudentRoom extends DomainObject implements IExamStudentRoom {
    private IExam exam;

    private IStudent student;

    private ISala room;

    private Integer keyExam;

    private Integer keyStudent;

    private Integer keyRoom;

    /**
     *  
     */
    public ExamStudentRoom() {
    }

    public ExamStudentRoom(IExam exam, IStudent student, ISala room) {
        setExam(exam);
        setStudent(student);
        setRoom(room);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof IExamStudentRoom) {
            IExamStudentRoom examStudentRoom = (IExamStudentRoom) obj;
            ISala room = examStudentRoom.getRoom();
            ISala thisRoom = this.getRoom();
            resultado = this.getExam().equals(examStudentRoom.getExam())
                    && (((thisRoom == null) && (room == null)) || ((thisRoom != null) && (thisRoom
                            .equals(room)))) && this.getStudent().equals(examStudentRoom.getStudent());
        }
        return resultado;
    }

    /**
     * @return
     */
    public IExam getExam() {
        return exam;
    }

    /**
     * @return
     */
    public Integer getKeyExam() {
        return keyExam;
    }

    /**
     * @return
     */
    public Integer getKeyRoom() {
        return keyRoom;
    }

    /**
     * @return
     */
    public Integer getKeyStudent() {
        return keyStudent;
    }

    /**
     * @return
     */
    public ISala getRoom() {
        return room;
    }

    /**
     * @return
     */
    public IStudent getStudent() {
        return student;
    }

    /**
     * @param exam
     */
    public void setExam(IExam exam) {
        this.exam = exam;
    }

    /**
     * @param integer
     */
    public void setKeyExam(Integer integer) {
        keyExam = integer;
    }

    /**
     * @param integer
     */
    public void setKeyRoom(Integer integer) {
        keyRoom = integer;
    }

    /**
     * @param integer
     */
    public void setKeyStudent(Integer integer) {
        keyStudent = integer;
    }

    /**
     * @param sala
     */
    public void setRoom(ISala sala) {
        room = sala;
    }

    /**
     * @param student
     */
    public void setStudent(IStudent student) {
        this.student = student;
    }

}