package ServidorAplicacao.Servico.gesdis.teacher;
/**
 *
 * @author  EP15
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import DataBeans.gesdis.SectionView;
import DataBeans.gesdis.SiteView;
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
public class DeleteSection implements IServico {
	private static DeleteSection service = new DeleteSection();
	public static DeleteSection getService() {
		return service;
	}
	private DeleteSection() {
	}
	public final String getNome() {
		return "gesdis.teacher.ApagarSeccao";
	}
	public String run(SiteView siteView, SectionView sectionView)
		throws FenixServiceException {

		IPersistentSite persistentSite;
		ISite site;
		IPersistentSection persistentSection;
		ISection sonSection;
		IPersistentItem persistentItem;
		String fatherSectionName;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			persistentSite = sp.getIPersistentSite();
			persistentSection = sp.getIPersistentSection();
			persistentItem = sp.getIPersistentItem();
			IDisciplinaExecucao executionCourse = null;
			if (siteView != null) {
				executionCourse =
					Cloner.copyInfoExecutionCourse2ExecutionCourse(
						siteView.getInfoExecutionCourse());
			}
			site = persistentSite.readByExecutionCourse(executionCourse);
			List geneologicTree = sectionView.getSuperiorSectionsNames();
			List inferiorSections = null;
			ISection fatherSection = null;
			sonSection = null;
			fatherSectionName = null;
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
				inferiorSections = fatherSection.getInferiorSections();
			} else {
				inferiorSections = site.getSections();
			}
			sonSection =
				persistentSection.readBySiteAndSectionAndName(
					site,
					fatherSection,
					sonSectionName);
			ISection testSection;
			Integer testSectionOrder;
			Iterator iter = inferiorSections.iterator();
			while (iter.hasNext()) {
				testSection = (ISection) iter.next();
				testSectionOrder = testSection.getOrder();
				if (testSectionOrder.intValue()
					> sonSection.getOrder().intValue()) {
					testSection.setOrder(
						new Integer(testSectionOrder.intValue() - 1));
					persistentSection.lockWrite(testSection);
				}
			}
			ApagarSeccaoAux(
				persistentSite,
				site,
				persistentSection,
				sonSection,
				persistentItem);
			return fatherSectionName;

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}
	
	
	private void ApagarSeccaoAux(
		IPersistentSite persistentSite,
		ISite site,
		IPersistentSection persistentSection,
		ISection section,
		IPersistentItem persistentItem)
		throws FenixServiceException {

		try {
			List fatherSectionsList = new ArrayList();
			List sonSectionsList = null;
			fatherSectionsList.add(section);
			do {
				List itemsList = new ArrayList();
				sonSectionsList = new ArrayList();
				Iterator iter = fatherSectionsList.iterator();
				while (iter.hasNext()) {
					ISection fatherSection = (ISection) iter.next();
					sonSectionsList.addAll(fatherSection.getInferiorSections());
					itemsList = fatherSection.getItems();
					Iterator iterItens = itemsList.iterator();
					while (iterItens.hasNext()) {
						IItem item = (IItem) iterItens.next();
						persistentItem.delete(item);
					}
					if (site.getInitialSection() != null
						&& site.getInitialSection().equals(fatherSection)) {
						site.setInitialSection(null);
						persistentSite.lockWrite(site);
					}
					persistentSection.delete(fatherSection);
				}
				fatherSectionsList = sonSectionsList;
			}
			while (sonSectionsList.isEmpty() == false);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}
