package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IStudentKind;
import net.sourceforge.fenixedu.util.StudentType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentStudentKind extends IPersistentObject {

    IStudentKind readByStudentType(StudentType studentType) throws ExcepcaoPersistencia;

}