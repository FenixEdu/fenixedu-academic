package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.InfoSection;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IExecutionCourse;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Section;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Fernanda Quitério
 * 
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
	private Integer organizeSectionsOrder(
		Integer newOrder,
		Integer oldOrder,
		ISection superiorSection,
		ISite site)
		throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IPersistentSection persistentSection =
				persistentSuport.getIPersistentSection();

			List sectionsList = null;
			sectionsList =
				persistentSection.readBySiteAndSection(site, superiorSection);

			Iterator iterSections = sectionsList.iterator();

			if (newOrder.intValue() == -2) {
				newOrder = new Integer(sectionsList.size() - 1);
			}

			if (newOrder.intValue() - oldOrder.intValue() > 0) {
				while (iterSections.hasNext()) {

					ISection iterSection = (ISection) iterSections.next();
					int iterSectionOrder =
						iterSection.getSectionOrder().intValue();

					if (iterSectionOrder > oldOrder.intValue()
						&& iterSectionOrder <= newOrder.intValue()) {
						persistentSection.lockWrite(iterSection);
						iterSection.setSectionOrder(
							new Integer(iterSectionOrder - 1));
					}
				}
			} else {
				while (iterSections.hasNext()) {
					ISection iterSection = (ISection) iterSections.next();

					int iterSectionOrder =
						iterSection.getSectionOrder().intValue();

					if (iterSectionOrder >= newOrder.intValue()
						&& iterSectionOrder < oldOrder.intValue()) {

						persistentSection.lockWrite(iterSection);
						iterSection.setSectionOrder(
							new Integer(iterSectionOrder + 1));
					}
				}
			}
		} catch (ExistingPersistentException excepcaoPersistencia) {

			throw new ExistingServiceException(excepcaoPersistencia);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
		return newOrder;
	}

	/**
	 * Executes the service.
	 */
	public Boolean run(
		Integer infoExecutionCourseCode,
		Integer sectionCode,
		String newSectionName,
		Integer newOrder)
		throws FenixServiceException {

		
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IPersistentSection persistentSection = sp.getIPersistentSection();

			ISection iSection =
				(ISection) persistentSection.readByOId(
					new Section(sectionCode),
					false);

			if (iSection == null) {
				throw new NonExistingServiceException();
			}
			iSection.setName(newSectionName);
			persistentSection.lockWrite(iSection);

			IExecutionCourse executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(
					new DisciplinaExecucao(infoExecutionCourseCode),
					false);

			ISite site =
				sp.getIPersistentSite().readByExecutionCourse(executionCourse);

			InfoSection infoSection = Cloner.copyISection2InfoSection(iSection);
			Integer oldOrder = infoSection.getSectionOrder();

			if (newOrder != oldOrder) {
				newOrder =
					organizeSectionsOrder(
						newOrder,
						oldOrder,
						iSection.getSuperiorSection(),
						site);
			}

			//			persistentSection.lockWrite(iSection);

			iSection.setName(newSectionName);
			iSection.setSectionOrder(newOrder);

		} catch (ExistingPersistentException e) {
			throw new ExistingServiceException(e);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return new Boolean(true);
	}
}