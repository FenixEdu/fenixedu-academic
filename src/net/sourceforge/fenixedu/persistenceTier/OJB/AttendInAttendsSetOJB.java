/*
 * Created on 17/Ago/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.AttendInAttendsSet;
import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendInAttendsSet;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author joaosa & rmalo
 */

public class AttendInAttendsSetOJB extends ObjectFenixOJB implements IPersistentAttendInAttendsSet {

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(AttendInAttendsSet.class, new Criteria());

    }

    public void delete(IAttendInAttendsSet attendInAttendsSet) throws ExcepcaoPersistencia {
        super.delete(attendInAttendsSet);
    }

}
