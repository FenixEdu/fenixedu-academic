package ServidorAplicacao.Servico.gesdis;

/**
 * Created on 19/03/2003
 *
 * @author lmac1
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoSection;
import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadSectionsBySiteAndSuperiorSection implements IServico {

	private static ReadSectionsBySiteAndSuperiorSection service =
		new ReadSectionsBySiteAndSuperiorSection();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadSectionsBySiteAndSuperiorSection getService() {
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private ReadSectionsBySiteAndSuperiorSection() {
	}

	/**
	 * Service name
	 */
	public final String getNome() {
		return "ReadSectionsBySiteAndSuperiorSection";
	}

	/**
	 * Executes the service. Returns the current collection of all infosections that belong to a site.
	 */
	public List run(InfoSite infoSite, InfoSection infoSuperiorSection)
		throws FenixServiceException {

		ISite site = Cloner.copyInfoSite2ISite(infoSite);
		ISuportePersistente sp;
		List allSections = null;

		ISection superiorSection = null;
		if (infoSuperiorSection != null) {		
			superiorSection = Cloner.copyInfoSection2ISection(infoSuperiorSection);
			superiorSection.setSite(site);
		}
		
		try {
			sp = SuportePersistenteOJB.getInstance();
			allSections =
				sp.getIPersistentSection().readBySiteAndSection(
					site,
					superiorSection);

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}

		List result = new ArrayList();
		if (allSections != null) {
			// build the result of this service
			Iterator iterator = allSections.iterator();

			while (iterator.hasNext())
				result.add(
					Cloner.copyISection2InfoSection(
						(ISection) iterator.next()));
		}

		return result;
	}
}
