/*
 * Created on 1/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.gesdis.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac2
 */
public class EditSection implements IServico {

	private static EditSection service = new EditSection();

	/**
	 * The singleton access method of this class.
	 */
	public static EditSection getService() {
		return service;
	}

    
	public String getNome() {
		return "EditSection";
	}

	/**
	 * 
	 * @param newOrder
	 * @param oldOrder
	 * @param site
	 * @throws FenixServiceException
	 */
	
//	this method reorders some sections but not the section that we are editing
	private void organizeSectionsOrder(int newOrder, int oldOrder, ISection superiorSection, ISite site) throws FenixServiceException {

			IPersistentSection persistentSection = null;
			try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				persistentSection = persistentSuport.getIPersistentSection();

				List sectionsList = null;
				sectionsList = persistentSection.readBySiteAndSection(site,superiorSection);

				Iterator iterSections = sectionsList.iterator();

				if (newOrder - oldOrder > 0)
					while (iterSections.hasNext()) {

						ISection iterSection = (ISection) iterSections.next();
						int iterSectionOrder = iterSection.getSectionOrder().intValue();

						if (iterSectionOrder > oldOrder && iterSectionOrder <= newOrder) {
							iterSection.setSectionOrder(new Integer(iterSectionOrder - 1));
							persistentSection.lockWrite(iterSection);
						}
					} else
						while (iterSections.hasNext()) {
							ISection iterSection = (ISection) iterSections.next();

							int iterSectionOrder = iterSection.getSectionOrder().intValue();

							if (iterSectionOrder >= newOrder && iterSectionOrder < oldOrder) {

								iterSection.setSectionOrder(new Integer(iterSectionOrder + 1));
								persistentSection.lockWrite(iterSection);
							}
						} 
			} catch (ExcepcaoPersistencia excepcaoPersistencia) {
				throw new FenixServiceException(excepcaoPersistencia);
			}
		}

	/**
	 * Executes the service.
	 */
	public Boolean run (InfoSection oldInfoSection, InfoSection newInfoSection) throws FenixServiceException{
		
		ISection fatherSection = null; 
		IItem item=null;
		try {       		
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentSection persistentSection = sp.getIPersistentSection();
			
			ISite site = Cloner.copyInfoSite2ISite(oldInfoSection.getInfoSite());
			
			InfoSection fatherInfoSection = oldInfoSection.getSuperiorInfoSection();

			if(fatherInfoSection != null) 
				fatherSection = Cloner.copyInfoSection2ISection(fatherInfoSection);
			
			ISection section = persistentSection.readBySiteAndSectionAndName(site, fatherSection, oldInfoSection.getName());
			
			section.setLastModifiedDate(newInfoSection.getLastModifiedDate());
			section.setName(newInfoSection.getName());
			
			int newOrder = newInfoSection.getSectionOrder().intValue();
			int oldOrder = oldInfoSection.getSectionOrder().intValue();

			if (newOrder != oldOrder) {
				organizeSectionsOrder(newOrder, oldOrder,fatherSection,site);
				section.setSectionOrder(newInfoSection.getSectionOrder());
			}
			
			persistentSection.lockWrite(section);					
			}
			catch (ExcepcaoPersistencia e) {
				throw new FenixServiceException(e);
			}
				
		return new Boolean(true);
	}	
}