package net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.gaugingTests.physics.GaugingTestResult;
import net.sourceforge.fenixedu.domain.gaugingTests.physics.IGaugingTestResult;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;

import org.apache.ojb.broker.query.Criteria;

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