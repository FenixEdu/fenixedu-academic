package ServidorPersistente.OJB.grant.contract;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantOrientationTeacher;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantOrientationTeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;

/**
 * @author Barbosa
 * @author Pica
 */
public class GrantOrientationTeacherOJB extends ObjectFenixOJB implements
        IPersistentGrantOrientationTeacher {

    public GrantOrientationTeacherOJB() {
    }

    public IGrantOrientationTeacher readActualGrantOrientationTeacherByContract(
            IGrantContract contract, Integer idInternal)
            throws ExcepcaoPersistencia {
        List grantOrientationTeacher = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyContract", contract.getIdInternal());
        criteria.addNotEqualTo("idInternal", idInternal);
        grantOrientationTeacher = queryList(GrantOrientationTeacher.class,
                criteria, "beginDate", false);
        Iterator respIter = grantOrientationTeacher.iterator();
        IGrantOrientationTeacher orientationTeacher = null;
        if (respIter.hasNext())
                orientationTeacher = (IGrantOrientationTeacher) respIter.next();
        return orientationTeacher;
    }
}