/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract;

import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantProject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantProject;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author pica
 * @author barbosa
 */
public class GrantProjectOJB extends PersistentObjectOJB implements IPersistentGrantProject {

    public IGrantProject readGrantProjectByNumber(String number) throws ExcepcaoPersistencia {
        IGrantProject grantProject = null;
        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", number);
        criteria.addEqualTo("class_name", GrantPaymentEntity.getGrantProjectOjbConcreteClass());
        grantProject = (IGrantProject) queryObject(GrantProject.class, criteria);
        return grantProject;
    }
}