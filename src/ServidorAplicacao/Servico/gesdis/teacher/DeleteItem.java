package ServidorAplicacao.Servico.gesdis.teacher;

/**
 *
 * @author  EP15
 */

import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.gesdis.InfoSection;
import DataBeans.gesdis.InfoSite;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteItem implements IServico {

	private static DeleteItem service = new DeleteItem();

	public static DeleteItem getService() {

		return service;

	}

	private DeleteItem() {
	}

	public final String getNome() {

		return "gesdis.teacher.DeleteItem";

	}

	public void run(
		InfoSite siteView,
		InfoSection sectionView,
		InfoExecutionCourse infoExecutionCourse,
		String itemName)
		throws FenixServiceException {

		try {
			IDisciplinaExecucao executionCourse = null;
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			IPersistentSite persistentSite = sp.getIPersistentSite();
			
			IPersistentSection persistentSection = sp.getIPersistentSection();
			
			IPersistentItem persistentItem = sp.getIPersistentItem();
			
			ISite site = persistentSite.readByExecutionCourse(executionCourse);
			
			executionCourse =
				Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
			
			List geneologicTree = (List) sectionView.getSuperiorSectionsNames();
			
			List inferiorSections = null;
			
			ISection fatherSection = null;
			
			ISection sonSection = null;
			
			String fatherSectionName = null;
			
			String sonSectionName = sectionView.getName();
			
			if (geneologicTree.isEmpty() == false) {
			
				Iterator iter = geneologicTree.iterator();
			
				while (iter.hasNext()) {
			
					fatherSectionName = (String) iter.next();
			
					fatherSection =
						persistentSection.readBySiteAndSectionAndName(
							site,
							fatherSection,
							fatherSectionName);
			
				}
			
			}
			
			sonSection =
				persistentSection.readBySiteAndSectionAndName(
					site,
					fatherSection,
					sonSectionName);
			
			Integer orderOfDeletedItem = new Integer(-1);
			
			List itemsList = null;
			
			Iterator iterItems = null;
			
			itemsList = sonSection.getItems();
			
			iterItems = itemsList.iterator();
			
			while (iterItems.hasNext()) {
			
				IItem item = (IItem) iterItems.next();
			
				if (item.getName().equals(itemName)) {
			
					orderOfDeletedItem = item.getOrder();
			
					persistentItem.delete(item);
			
					break;
			
				}
			
			}
			
			if (orderOfDeletedItem == new Integer(-1))
				throw new FenixServiceException("non existing item");
			//	throw new ItemInexistenteException("Item inexistente");
			
			itemsList = sonSection.getItems();
			
			iterItems = itemsList.iterator();
			
			while (iterItems.hasNext()) {
			
				IItem item = (IItem) iterItems.next();
			
				Integer itemOrder = item.getOrder();
			
				if (itemOrder.intValue() > orderOfDeletedItem.intValue()) {
			
					item.setOrder(new Integer(itemOrder.intValue() - 1));
			
					persistentItem.lockWrite(item);
			
				}
			
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}
