/*
 * Created on Sep 19, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.IUnit;
import net.sourceforge.fenixedu.domain.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentUnit;

import org.apache.ojb.broker.query.Criteria;

public class UnitOJB extends PersistentObjectOJB implements IPersistentUnit{
    
    public IUnit readByName(String unitName) throws ExcepcaoPersistencia{
        Criteria crit = new Criteria();
        crit.addEqualTo("name", unitName);       
        return (IUnit) queryObject(Unit.class, crit);
    }
}
