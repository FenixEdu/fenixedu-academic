/*
 * Created on 26/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.gaugingTests.physics.IGaugingTestResult;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 26/Nov/2003
 *  
 */
public interface IPersistentGaugingTestResult {
    public IGaugingTestResult readByStudent(IStudent student) throws ExcepcaoPersistencia;
}