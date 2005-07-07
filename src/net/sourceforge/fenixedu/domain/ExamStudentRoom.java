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

    public boolean equals(Object obj) {
        if (obj instanceof IExamStudentRoom) {
            final IExamStudentRoom examStudentRoom = (IExamStudentRoom) obj;
            return this.getIdInternal().equals(examStudentRoom.getIdInternal());
        }
        return false;
    }

}
