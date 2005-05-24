/*
 * Created on 30/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExamStudentRoom;
import net.sourceforge.fenixedu.domain.IExamStudentRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamStudentRoom;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author João Mota
 *  
 */
public class ExamStudentRoomOJB extends PersistentObjectOJB implements IPersistentExamStudentRoom {

    public List readByExamOID(Integer examOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExam", examOID);
        return queryList(ExamStudentRoom.class, criteria);

    }

    public List readByStudentOID(Integer studentOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", studentOID);
        return queryList(ExamStudentRoom.class, criteria);
    }

    public IExamStudentRoom readBy(Integer examOID, Integer studentOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExam", examOID);
        criteria.addEqualTo("keyStudent", studentOID);
        return (IExamStudentRoom) queryObject(ExamStudentRoom.class, criteria);
    }
}