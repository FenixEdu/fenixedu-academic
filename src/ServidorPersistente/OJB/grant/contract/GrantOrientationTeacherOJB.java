package ServidorPersistente.OJB.grant.contract;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantOrientationTeacher;
import Dominio.grant.contract.IGrantContract;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;

/**
 *
 * @author  Barbosa
 * @author  Pica
 */

public class GrantOrientationTeacherOJB
    extends ServidorPersistente.OJB.ObjectFenixOJB
    implements IPersistentGrantOrientationTeacher
{
    public GrantOrientationTeacherOJB()
    {
    }

    public List readActualGrantOrientationTeacherByContract(IGrantContract contract,Integer idInternal)
        throws ExcepcaoPersistencia
    {
        List grantOrientationTeacher = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyContract", contract.getIdInternal());
        criteria.addOrderBy("beginDate", false);
        criteria.addNotEqualTo("idInternal",idInternal);
        grantOrientationTeacher = queryList(GrantOrientationTeacher.class, criteria);
        return grantOrientationTeacher;
    }
}