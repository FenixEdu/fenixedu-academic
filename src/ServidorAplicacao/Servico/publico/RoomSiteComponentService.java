/*
 * Created on 6/Mai/2003
 *
 *
 */
package ServidorAplicacao.Servico.publico;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import DataBeans.RoomKey;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.RoomSiteComponentBuilder;
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

	private static RoomSiteComponentService _servico =
		new RoomSiteComponentService();

	/**
	  * The actor of this class.
	  **/

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
	 * @return ReadExecutionCourse
	 */
	public static RoomSiteComponentService getService() {
		return _servico;
	}

	public Object run(
		ISiteComponent commonComponent,
		ISiteComponent bodyComponent,
		RoomKey roomKey,
		Integer infoExecutionPeriodCode)
		throws FenixServiceException {
		ExecutionCourseSiteView siteView = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ISalaPersistente persistentRoom = sp.getISalaPersistente();
			IPersistentExecutionPeriod persistentExecutionPeriod =
				sp.getIPersistentExecutionPeriod();

			ISala room = persistentRoom.readByName(roomKey.getNomeSala());
			IExecutionPeriod executionPeriod =
				(IExecutionPeriod) persistentExecutionPeriod.readByOId(
					new ExecutionPeriod(infoExecutionPeriodCode));

			RoomSiteComponentBuilder componentBuilder =
				RoomSiteComponentBuilder.getInstance();
			//			commonComponent =
			//				componentBuilder.getComponent(
			//					commonComponent,
			//					site,
			//					null,
			//					null);
			bodyComponent =
				componentBuilder.getComponent(
					bodyComponent,
					executionPeriod,
					room);

			siteView =
				new ExecutionCourseSiteView(commonComponent, bodyComponent);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return siteView;
	}
}
