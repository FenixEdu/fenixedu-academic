/*
 * Created on 2/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.gesdis.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Section;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac2
 */
public class InsertSection implements IServico {

	private static InsertSection service = new InsertSection();

	public static InsertSection getService() {

		return service;
	}

	private InsertSection() {
	}

	public final String getNome() {

		return "InsertSection";
	}

	private void organizeExistingSectionsOrder(
		ISection superiorSection,
		ISite site,
		int insertSectionOrder)
		throws FenixServiceException {

		IPersistentSection persistentSection = null;
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSection = persistentSuport.getIPersistentSection();

			List sectionsList =
				persistentSection.readBySiteAndSection(site, superiorSection);

			if (sectionsList != null) {

				Iterator iterSections = sectionsList.iterator();
				while (iterSections.hasNext()) {

					ISection iterSection = (ISection) iterSections.next();
					int sectionOrder = iterSection.getSectionOrder().intValue();

					if (sectionOrder >= insertSectionOrder) {
						iterSection.setSectionOrder(
							new Integer(sectionOrder + 1));
						persistentSection.lockWrite(iterSection);

					}

				}

			}
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}
	}

	//infoItem with an infoSection

	public Boolean run(InfoSection infoSection) throws FenixServiceException {

		ISite site = null;
		ISection section = null;
		ISection fatherSection = null;

		IPersistentSection persistentSection = null;
		try {

			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			persistentSection = persistentSuport.getIPersistentSection();

			IExecutionYear executionYear =
				persistentSuport
					.getIPersistentExecutionYear()
					.readExecutionYearByName(
					infoSection
						.getInfoSite()
						.getInfoExecutionCourse()
						.getInfoExecutionPeriod()
						.getInfoExecutionYear()
						.getYear());
			IExecutionPeriod executionPeriod =
				persistentSuport
					.getIPersistentExecutionPeriod()
					.readByNameAndExecutionYear(
					infoSection
						.getInfoSite()
						.getInfoExecutionCourse()
						.getInfoExecutionPeriod()
						.getName(),
					executionYear);
			IDisciplinaExecucao executionCourse =
				persistentSuport
					.getIDisciplinaExecucaoPersistente()
					.readByExecutionCourseInitialsAndExecutionPeriod(
						infoSection
							.getInfoSite()
							.getInfoExecutionCourse()
							.getSigla(),
						executionPeriod);
			site =
				persistentSuport.getIPersistentSite().readByExecutionCourse(
					executionCourse);

			InfoSection fatherInfoSection =
				infoSection.getSuperiorInfoSection();

			if (fatherInfoSection.getSuperiorInfoSection() != null) {

				fatherSection =
					persistentSection.readBySiteAndSectionAndName(
						site,
						Cloner.copyInfoSection2ISection(
							fatherInfoSection.getSuperiorInfoSection()),
						fatherInfoSection.getName());
			} else {
				fatherSection =
					persistentSection.readBySiteAndSectionAndName(
						site,
						null,
						fatherInfoSection.getName());
			}
			fatherSection.setSite(site);

			section = new Section(infoSection.getName(), site, fatherSection);
			section.setSectionOrder(infoSection.getSectionOrder());

			organizeExistingSectionsOrder(
				fatherSection,
				site,
				infoSection.getSectionOrder().intValue());

			persistentSection.lockWrite(section);

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}

		return new Boolean(true);
	}

}