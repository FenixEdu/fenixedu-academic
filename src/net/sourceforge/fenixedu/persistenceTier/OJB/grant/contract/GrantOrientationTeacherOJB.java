package net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Barbosa
 * @author Pica
 */
public class GrantOrientationTeacherOJB extends PersistentObjectOJB implements
        IPersistentGrantOrientationTeacher {

    public IGrantOrientationTeacher readActualGrantOrientationTeacherByContract(Integer contractId,
            Integer idInternal) throws ExcepcaoPersistencia {
        List grantOrientationTeacher = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyContract", contractId);
        criteria.addNotEqualTo("idInternal", idInternal);
        grantOrientationTeacher = queryList(GrantOrientationTeacher.class, criteria, "beginDate", false);

        Iterator respIter = grantOrientationTeacher.iterator();
        IGrantOrientationTeacher orientationTeacher = null;
        if (respIter.hasNext())
            orientationTeacher = (IGrantOrientationTeacher) respIter.next();
        return orientationTeacher;
    }
}