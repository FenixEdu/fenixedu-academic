/*
 * Created on 6/Mai/2003
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Factory.RoomSiteComponentBuilder;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * @author Fernanda Quitério
 * 
 * 
 */
public class RoomSiteComponentServiceByExecutionPeriodID implements IService {

    public static Object run(ISiteComponent bodyComponent, RoomKey roomKey, Calendar day, Integer executionPeriodID) throws Exception {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
        final IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(ExecutionPeriod.class, executionPeriodID);
        return (executionPeriod != null) ? runService(bodyComponent, roomKey, day, executionPeriod) : RoomSiteComponentService.run(bodyComponent, roomKey, day);
    }

    public static Object runService(ISiteComponent bodyComponent, RoomKey roomKey, Calendar day,
            IExecutionPeriod executionPeriod) throws Exception {
        SiteView siteView = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ISalaPersistente persistentRoom = sp.getISalaPersistente();
        // IPersistentExecutionPeriod persistentExecutionPeriod =
        // sp.getIPersistentExecutionPeriod();

        IRoom room = persistentRoom.readByName(roomKey.getNomeSala());
        // IExecutionPeriod executionPeriod = (IExecutionPeriod)
        // persistentExecutionPeriod
        // .readByOID(ExecutionPeriod.class, infoExecutionPeriodCode);
        // if (executionPeriod == null) {
        // throw new NonExistingServiceException();
        // }
        RoomSiteComponentBuilder componentBuilder = RoomSiteComponentBuilder.getInstance();
        bodyComponent = componentBuilder.getComponent(bodyComponent, day, room, executionPeriod);

        siteView = new SiteView(bodyComponent);

        return siteView;
    }
}