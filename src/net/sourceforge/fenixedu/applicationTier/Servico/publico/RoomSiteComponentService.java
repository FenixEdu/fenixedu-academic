/*
 * Created on 6/Mai/2003
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Calendar;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Factory.RoomSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * @author Fernanda Quitério
 * 
 *  
 */
public class RoomSiteComponentService implements IServico {

    private static RoomSiteComponentService _servico = new RoomSiteComponentService();

    /**
     * The actor of this class.
     */

    private RoomSiteComponentService() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "RoomSiteComponentService";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadRoom
     */
    public static RoomSiteComponentService getService() {
        return _servico;
    }

    public Object run(ISiteComponent bodyComponent, RoomKey roomKey, Calendar day)
            throws FenixServiceException {
        SiteView siteView = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ISalaPersistente persistentRoom = sp.getISalaPersistente();
            //			IPersistentExecutionPeriod persistentExecutionPeriod =
            // sp.getIPersistentExecutionPeriod();

            IRoom room = persistentRoom.readByName(roomKey.getNomeSala());
            //            IExecutionPeriod executionPeriod = (IExecutionPeriod)
            // persistentExecutionPeriod
            //                    .readByOID(ExecutionPeriod.class, infoExecutionPeriodCode);
            //            if (executionPeriod == null) {
            //                throw new NonExistingServiceException();
            //            }
            RoomSiteComponentBuilder componentBuilder = RoomSiteComponentBuilder.getInstance();
            bodyComponent = componentBuilder.getComponent(bodyComponent, day, room);

            siteView = new SiteView(bodyComponent);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return siteView;
    }
}