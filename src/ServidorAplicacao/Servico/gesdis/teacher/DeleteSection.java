package ServidorAplicacao.Servico.gesdis.teacher;
/**
 *
 * @author  lmac1
 */
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoSection;
import DataBeans.util.Cloner;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
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
		return "DeleteSection";
	}
	public Boolean run(InfoSection infoSection) throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ISite site = Cloner.copyInfoSite2ISite(infoSection.getInfoSite());
			IPersistentSite persistentSite = sp.getIPersistentSite();
			site =
				persistentSite.readByExecutionCourse(site.getExecutionCourse());
		

			ISection superiorSection = null;
			if (infoSection.getSuperiorInfoSection() != null) {
				superiorSection =
					Cloner.copyInfoSection2ISection(
						infoSection.getSuperiorInfoSection());
				superiorSection.setSite(site);
			}

			IPersistentSection persistentSection = sp.getIPersistentSection();

			ISection deletedSection =
				persistentSection.readBySiteAndSectionAndName(
					site,
					superiorSection,
					infoSection.getName());
			
				if (deletedSection == null) throw new FenixServiceException("non existing section");

			Integer deletedSectionOrder = deletedSection.getSectionOrder();

			persistentSection.delete(deletedSection);
			sp.confirmarTransaccao();
			sp.iniciarTransaccao();

			List sectionsReordered =
				persistentSection.readBySiteAndSection(site, superiorSection);
			// persistentItem.readAllItemsBySection(section);
			//only necessary if we reomve the open/close transaction from the service
			//sectionsReordered.remove(deletedSection);

			Iterator iterSections = sectionsReordered.iterator();

			ISection section = null;
			Integer sectionOrder = null;
			while (iterSections.hasNext()) {

				section = (ISection) iterSections.next();
				sectionOrder = section.getSectionOrder();

				if (sectionOrder.intValue() > deletedSectionOrder.intValue()) {

					section.setSectionOrder(
						new Integer(sectionOrder.intValue() - 1));
					persistentSection.lockWrite(section);
				}
			}

			return new Boolean(true);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}