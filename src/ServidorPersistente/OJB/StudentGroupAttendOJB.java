/*
 * Created on 28/Mai/2003
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IFrequenta;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.StudentGroupAttend;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroupAttend;

/**
 * @author asnr and scpo
 *  
 */
public class StudentGroupAttendOJB extends PersistentObjectOJB implements IPersistentStudentGroupAttend {

    public IStudentGroupAttend readBy(IStudentGroup studentGroup, IFrequenta attend)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyStudentGroup", studentGroup.getIdInternal());
        criteria.addEqualTo("keyAttend", attend.getIdInternal());

        return (IStudentGroupAttend) queryObject(StudentGroupAttend.class, criteria);

    }

    public List readAllByStudentGroup(IStudentGroup studentGroup) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyStudentGroup", studentGroup.getIdInternal());

        return queryList(StudentGroupAttend.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(StudentGroupAttend.class, new Criteria());

    }

    public void delete(IStudentGroupAttend studentGroupAttend) throws ExcepcaoPersistencia {
        try {
            super.delete(studentGroupAttend);
        } catch (ExcepcaoPersistencia ex) {
            throw ex;
        }
    }

    //by gedl AT rnl DOT ist DOT utl DOT pt at September the 10th, 2003
    public IStudentGroupAttend readBy(IFrequenta attend) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyAttend", attend.getIdInternal());
        return (IStudentGroupAttend) queryObject(StudentGroupAttend.class, criteria);
    }

    //  by gedl AT rnl DOT ist DOT utl DOT pt at September the 12th, 2003
    public List readByStudentGroupId(Integer id) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("key_student_group", id);
        return queryList(StudentGroupAttend.class, criteria);
    }
}