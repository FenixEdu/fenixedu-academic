/*
 * Created on Dec 22, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.student;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.student.ISenior;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 *
 */
public interface IPersistentSenior extends IPersistentObject {
    public ISenior readByStudent(IStudent student) throws ExcepcaoPersistencia;
}
