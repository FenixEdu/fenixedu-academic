/*
 * Created on 12/Mai/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author asnr and scpo
 * 
 */
public class StudentGroupOJB extends ObjectFenixOJB implements IPersistentStudentGroup {

    public IStudentGroup readStudentGroupByAttendsSetAndGroupNumber(Integer attendsSetID,
            Integer studentGroupNumber) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("attendsSet.idInternal", attendsSetID);
        criteria.addEqualTo("groupNumber", studentGroupNumber);
        return (IStudentGroup) queryObject(StudentGroup.class, criteria);
    }

    public List<IStudentGroup> readAllStudentGroupByAttendsSetAndShift(Integer attendsSetID,
            Integer shiftID) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("attendsSet.idInternal", attendsSetID);
        criteria.addEqualTo("shift.idInternal", shiftID);
        return queryList(StudentGroup.class, criteria);
    }
}
