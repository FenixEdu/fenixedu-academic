/*
 * Created on 6/Mai/2003
 *
 *
 */
package ServidorAplicacao.Servico.publico;

import DataBeans.ISiteComponent;
import DataBeans.RoomKey;
import DataBeans.SiteView;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.RoomSiteComponentBuilder;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

    public Object run(ISiteComponent bodyComponent, RoomKey roomKey,
            Integer infoExecutionPeriodCode) throws FenixServiceException {
        SiteView siteView = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ISalaPersistente persistentRoom = sp.getISalaPersistente();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp
                    .getIPersistentExecutionPeriod();

            ISala room = persistentRoom.readByName(roomKey.getNomeSala());
            IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod
                    .readByOID(ExecutionPeriod.class, infoExecutionPeriodCode);
            if (executionPeriod == null) {
                throw new NonExistingServiceException();
            }
            RoomSiteComponentBuilder componentBuilder = RoomSiteComponentBuilder
                    .getInstance();
            bodyComponent = componentBuilder.getComponent(bodyComponent,
                    executionPeriod, room);

            siteView = new SiteView(bodyComponent);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return siteView;
    }
}