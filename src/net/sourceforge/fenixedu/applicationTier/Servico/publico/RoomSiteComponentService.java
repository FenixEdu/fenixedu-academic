/*
 * Created on 6/Mai/2003
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Calendar;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author João Mota
 * @author Fernanda Quitério
 * 
 * 
 */
public class RoomSiteComponentService extends Service {

    public static Object run(ISiteComponent bodyComponent, RoomKey roomKey, Calendar day) throws Exception {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
        final ExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
        return RoomSiteComponentServiceByExecutionPeriodID.runService(bodyComponent, roomKey, day, executionPeriod);
    }

}