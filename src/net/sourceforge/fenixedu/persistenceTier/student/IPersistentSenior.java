/*
 * Created on Dec 22, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.student;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.Senior;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 *
 */
public interface IPersistentSenior extends IPersistentObject {
    public Senior readByStudent(Student student) throws ExcepcaoPersistencia;
}
