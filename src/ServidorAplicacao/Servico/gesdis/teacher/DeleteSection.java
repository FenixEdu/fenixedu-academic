package ServidorAplicacao.Servico.gesdis.teacher;
/**
 *
 * @author  lmac1
 */
import java.util.Iterator;
import java.util.List;

import DataBeans.gesdis.InfoSection;
import DataBeans.util.Cloner;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSection;
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

			/* we may only delete non-initial sections */
			ISite completeSite = sp.getIPersistentSite().readByExecutionCourse(site.getExecutionCourse());
//			InfoSection initialSection = Cloner.copyISection2InfoSection(completeSite.getInitialSection());
//			if (initialSection.equals(infoSection)){
//				throw new FenixServiceException("Initial Section");
//			}
			
			ISection superiorSection = null;
			if (infoSection.getSuperiorInfoSection()!=null) {
				superiorSection = Cloner.copyInfoSection2ISection(infoSection.getSuperiorInfoSection());
			}
			String name = infoSection.getName();
			
			IPersistentSection persistentSection = sp.getIPersistentSection();
			
			ISection deletedSection = persistentSection.readBySiteAndSectionAndName(site, superiorSection, name);
			
			Integer deletedSectionOrder = deletedSection.getSectionOrder();
			
			if (deletedSection == null) throw new FenixServiceException("non existing section");
			
		    persistentSection.delete(deletedSection);
				
			List sectionsReordered = persistentSection.readBySiteAndSection(site, superiorSection);
			// persistentItem.readAllItemsBySection(section);
			
			Iterator iterSections = sectionsReordered.iterator();
			
			ISection section = null;
			Integer sectionOrder = null;
			while (iterSections.hasNext()) {
			
				section = (ISection) iterSections.next();
				sectionOrder = section.getSectionOrder();
			
				if (sectionOrder.intValue() > deletedSectionOrder.intValue()) {
			
					section.setSectionOrder(new Integer(sectionOrder.intValue() - 1));
					persistentSection.lockWrite(section);
				}
			}
			
			
				
				
				
			return new Boolean(true);	
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
   }
 }
}