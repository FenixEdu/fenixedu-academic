package ServidorPersistente.OJB.grant.contract;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.contract.GrantResponsibleTeacher;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantResponsibleTeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.grant.IPersistentGrantResponsibleTeacher;

/**
 *
 * @author  Barbosa
 * @author  Pica
 */

public class GrantResponsibleTeacherOJB
    extends ServidorPersistente.OJB.ObjectFenixOJB
    implements IPersistentGrantResponsibleTeacher
{
    public GrantResponsibleTeacherOJB()
    {
    }

    public IGrantResponsibleTeacher readActualGrantResponsibleTeacherByContract(
        IGrantContract contract,
        Integer idInternal)
        throws ExcepcaoPersistencia
    {
        List grantResponsibleTeacher = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyContract", contract.getIdInternal());
        criteria.addOrderBy("beginDate", false);
        criteria.addNotEqualTo("idInternal",idInternal);
        grantResponsibleTeacher = queryList(GrantResponsibleTeacher.class, criteria);
        
        Iterator respIter = grantResponsibleTeacher.iterator();
        IGrantResponsibleTeacher responsibleTeacher = null;
        if (respIter.hasNext())
        	responsibleTeacher = (IGrantResponsibleTeacher) respIter.next();
        	
        return responsibleTeacher;
    }
}