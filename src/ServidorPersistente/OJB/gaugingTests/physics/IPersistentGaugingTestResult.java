/*
 * Created on 26/Nov/2003
 *
 */
package ServidorPersistente.OJB.gaugingTests.physics;

import Dominio.IStudent;
import Dominio.gaugingTests.physics.IGaugingTestResult;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 26/Nov/2003
 *  
 */
public interface IPersistentGaugingTestResult {
    public IGaugingTestResult readByStudent(IStudent student) throws ExcepcaoPersistencia;
}