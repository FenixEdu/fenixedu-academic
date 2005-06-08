/*
 * Created on 28/Mai/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.StudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author asnr and scpo
 * 
 */
public class StudentGroupAttendOJB extends ObjectFenixOJB implements IPersistentStudentGroupAttend {

    public IStudentGroupAttend readByStudentGroupAndAttend(Integer studentGroupID, Integer attendID)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudentGroup", studentGroupID);
        criteria.addEqualTo("keyAttend", attendID);
        return (IStudentGroupAttend) queryObject(StudentGroupAttend.class, criteria);
    }
}
