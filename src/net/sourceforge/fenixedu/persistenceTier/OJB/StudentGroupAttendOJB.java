/*
 * Created on 28/Mai/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.StudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author asnr and scpo
 *  
 */
public class StudentGroupAttendOJB extends ObjectFenixOJB implements IPersistentStudentGroupAttend
{

    public IStudentGroupAttend readBy(IStudentGroup studentGroup, IAttends attend)
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

    public List readAllByAttend(IAttends attend) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyAttend", attend.getIdInternal());

        return queryList(StudentGroupAttend.class, criteria);
    }

    
    //  by gedl AT rnl DOT ist DOT utl DOT pt at September the 12th, 2003
    public List readByStudentGroupId(Integer id) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("key_student_group", id);
        return queryList(StudentGroupAttend.class, criteria);
    }

    //by gedl AT rnl DOT ist DOT utl DOT pt at September the 10th, 2003
    public IStudentGroupAttend readBy(IAttends attend) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyAttend", attend.getIdInternal());
        return (IStudentGroupAttend) queryObject(StudentGroupAttend.class, criteria);
    }
}
