/*
 * Created on 16/Mai/2005
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamStudentRoom;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamStudentRoom;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Pedro Santos e Rita Carvalho
 *  
 */
public class ExamStudentRoomVO extends VersionedObjectsBase implements IPersistentExamStudentRoom {

    public List readByExamOID(Integer examOID) throws ExcepcaoPersistencia {
        IExam exam = (IExam) readByOID(Exam.class, examOID);
        
        return exam.getExamStudentRooms();
        
        /*Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExam", exam.getIdInternal());
        return queryList(ExamStudentRoom.class, criteria);*/
    }

    public List readByStudentOID(Integer studentOID) throws ExcepcaoPersistencia {
        IStudent student = (IStudent) readByOID(Student.class, studentOID);
        
        return student.getExamStudentRooms();        
        
        /*Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", student.getIdInternal());
        return queryList(ExamStudentRoom.class, criteria);*/
    }

    public IExamStudentRoom readBy(Integer examOID, Integer studentOID) throws ExcepcaoPersistencia {
        IExam exam = (IExam) readByOID(Exam.class, examOID);
        List<IExamStudentRoom> examStudentRooms = exam.getExamStudentRooms();
        for(IExamStudentRoom examStudentRoom : examStudentRooms){
            if(examStudentRoom.getStudent().getIdInternal().equals(studentOID)){
                return examStudentRoom;
            }
        }
        return null;
        /*Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExam", exam.getIdInternal());
        criteria.addEqualTo("keyStudent", student.getIdInternal());
        return (IExamStudentRoom) queryObject(ExamStudentRoom.class, criteria);*/
    }

}