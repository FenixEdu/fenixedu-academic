package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.InfoItem;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.Section;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Fernanda Quitério
 * 
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

	private int organizeExistingItemsOrder(ISection section, int insertItemOrder) throws FenixServiceException {

		IPersistentItem persistentItem = null;
		try {

			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

			persistentItem = persistentSuport.getIPersistentItem();

			List itemsList = persistentItem.readAllItemsBySection(section);

			if (itemsList != null) {

				if (insertItemOrder == -1) {
					insertItemOrder = itemsList.size();
				}

				Iterator iterItems = itemsList.iterator();
				while (iterItems.hasNext()) {

					IItem item = (IItem) iterItems.next();
					int itemOrder = item.getItemOrder().intValue();

					if (itemOrder >= insertItemOrder) {
						persistentItem.simpleLockWrite(item);
						item.setItemOrder(new Integer(itemOrder + 1));
					}
				}
			}
		} catch (ExistingPersistentException excepcaoPersistencia) {

			throw new ExistingServiceException(excepcaoPersistencia);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}
		return insertItemOrder;
	}

	//infoItem with an infoSection

	public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, InfoItem infoItem) throws FenixServiceException {

		IItem item = null;
		ISection section = null;

		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentSection persistentSection = persistentSuport.getIPersistentSection();
			IPersistentItem persistentItem = persistentSuport.getIPersistentItem();


			section = (ISection) persistentSection.readByOId(new Section(sectionCode), false);

			infoItem.setInfoSection(Cloner.copyISection2InfoSection(section));
			item = Cloner.copyInfoItem2IItem(infoItem);

			Integer itemOrder = new Integer(organizeExistingItemsOrder(section, infoItem.getItemOrder().intValue()));
			item.setItemOrder(itemOrder);

			persistentItem.lockWrite(item);

		} catch (ExistingPersistentException e) {

			throw new ExistingServiceException(e);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}

		return new Boolean(true);
	}
}