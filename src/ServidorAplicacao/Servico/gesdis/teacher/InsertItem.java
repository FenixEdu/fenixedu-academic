/*
 * Created on 13/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */

package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.gesdis.InfoItem;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Item;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author asnr e scpo
 */

public class InsertItem implements IServico {

	private static InsertItem service = new InsertItem();

	public static InsertItem getService() {

		return service;
	}

	private InsertItem() {

	}

	public final String getNome() {

		return "InsertItem";
	}

	private void organizeExistingItemsOrder(
		ISection section,
		int insertItemOrder)
		throws FenixServiceException {

		IPersistentItem persistentItem = null;
		try {

			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
				
			persistentItem = persistentSuport.getIPersistentItem();

			List itemsList = persistentItem.readAllItemsBySection(section);

			if (itemsList != null) {

				Iterator iterItems = itemsList.iterator();
				while (iterItems.hasNext()) {

					IItem item = (IItem) iterItems.next();
					int itemOrder = item.getItemOrder().intValue();

					if (itemOrder >= insertItemOrder) {
						item.setItemOrder(new Integer(itemOrder + 1));
						persistentItem.lockWrite(item);
					}

				}

			}
		} catch (ExistingPersistentException excepcaoPersistencia) {

			throw new ExistingServiceException(excepcaoPersistencia);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}
	}

	//infoItem with an infoSection

	public Boolean run(InfoItem infoItem) throws FenixServiceException {

		IItem item = null;
		ISection section = null;

		IPersistentItem persistentItem = null;
		try {

			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			persistentItem = persistentSuport.getIPersistentItem();

			IPersistentSite persistentSite =
				persistentSuport.getIPersistentSite();

			IExecutionYear executionYear =
				persistentSuport
					.getIPersistentExecutionYear()
					.readExecutionYearByName(
					infoItem
						.getInfoSection()
						.getInfoSite()
						.getInfoExecutionCourse()
						.getInfoExecutionPeriod()
						.getInfoExecutionYear()
						.getYear());
			IExecutionPeriod executionPeriod =
				persistentSuport
					.getIPersistentExecutionPeriod()
					.readByNameAndExecutionYear(
					infoItem
						.getInfoSection()
						.getInfoSite()
						.getInfoExecutionCourse()
						.getInfoExecutionPeriod()
						.getName(),
					executionYear);
			IDisciplinaExecucao executionCourse =
				persistentSuport
					.getIDisciplinaExecucaoPersistente()
					.readByExecutionCourseInitialsAndExecutionPeriod(
						infoItem
							.getInfoSection()
							.getInfoSite()
							.getInfoExecutionCourse()
							.getSigla(),
						executionPeriod);												
			
			section =
				Cloner.copyInfoSection2ISection(infoItem.getInfoSection());
			
			ISite site = persistentSite.readByExecutionCourse(executionCourse);
										
			section.setSite(site);
			ISection superiorSection = section.getSuperiorSection(); 
			
			while (superiorSection != null) {			
				superiorSection.setSite(site);
				superiorSection = superiorSection.getSuperiorSection();
			}										

			item =
				new Item(
					infoItem.getName(),
					section,
					infoItem.getItemOrder(),
					infoItem.getInformation(),
					infoItem.getUrgent());

			organizeExistingItemsOrder(
				section,
				infoItem.getItemOrder().intValue());
			
			persistentItem.lockWrite(item);

		} catch (ExistingPersistentException e) {

			throw new ExistingServiceException(e);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}

		return new Boolean(true);
	}
}