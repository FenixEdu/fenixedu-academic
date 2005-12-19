/*
 * Created on 21/Out/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRoomOccupation;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Ana e Ricardo
 *  
 */
public class RoomOccupationOJB extends ObjectFenixOJB implements IPersistentRoomOccupation {
    
    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(RoomOccupation.class, criteria);
    }
}