/*
 * Created on 26/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.gaugingTests.physics.GaugingTestResult;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 26/Nov/2003
 *  
 */
public interface IPersistentGaugingTestResult {
    public GaugingTestResult readByStudent(Student student) throws ExcepcaoPersistencia;
}