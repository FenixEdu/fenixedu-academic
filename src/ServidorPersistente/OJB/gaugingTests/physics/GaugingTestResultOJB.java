package ServidorPersistente.OJB.gaugingTests.physics;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IStudent;
import Dominio.gaugingTests.physics.GaugingTestResult;
import Dominio.gaugingTests.physics.IGaugingTestResult;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;

/**
 * Created on 2003/11/26
 * 
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 */
public class GaugingTestResultOJB extends PersistentObjectOJB implements IPersistentGaugingTestResult {

    /**
     * Constructor for ExecutionYearOJB.
     */
    public GaugingTestResultOJB() {
        super();
    }

    public IGaugingTestResult readByStudent(IStudent student) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.idInternal", student.getIdInternal());
        return (IGaugingTestResult) queryObject(GaugingTestResult.class, crit);
    }

}