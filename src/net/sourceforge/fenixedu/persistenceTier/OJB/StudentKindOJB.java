package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.IStudentKind;
import net.sourceforge.fenixedu.domain.StudentKind;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentKind;
import net.sourceforge.fenixedu.util.StudentType;

import org.apache.ojb.broker.query.Criteria;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class StudentKindOJB extends PersistentObjectOJB implements IPersistentStudentKind {

    public StudentKindOJB() {
    }

    public IStudentKind readByStudentType(StudentType studentType) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("studentType", studentType);

        return (IStudentKind) queryObject(StudentKind.class, criteria);

    }
}