package ServidorAplicacao.Servico.teacher;
/**
 *
 * @author  lmac1
 */
import java.util.Iterator;
import java.util.List;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Section;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import fileSuport.FileSuport;
import fileSuport.IFileSuport;
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
	public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode)
		throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIPersistentExecutionCourse();
			IPersistentSite persistentSite = sp.getIPersistentSite();
			IPersistentSection persistentSection = sp.getIPersistentSection();
			ISite site = null;
			IExecutionCourse executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(
					new ExecutionCourse(infoExecutionCourseCode),
					false);
			site = persistentSite.readByExecutionCourse(executionCourse);
			ISection sectionToDelete =
				(ISection) persistentSection.readByOId(
					new Section(sectionCode),
					false);
			if (sectionToDelete == null) {
				throw new FenixServiceException("non existing section");
			}
			IFileSuport fileSuport = FileSuport.getInstance();			
			long size=1;
               
			size = fileSuport.getDirectorySize(sectionToDelete.getSlideName());		
            
			if (size>0) {
				throw new notAuthorizedServiceDeleteException();
				}
			ISection superiorSection = sectionToDelete.getSuperiorSection();
			Integer sectionToDeleteOrder = sectionToDelete.getSectionOrder();

			persistentSection.delete(sectionToDelete);
		
			sp.confirmarTransaccao();

			sp.iniciarTransaccao();
			List sectionsReordered =
				persistentSection.readBySiteAndSection(site, superiorSection);
			Iterator iterSections = sectionsReordered.iterator();
			while (iterSections.hasNext()) {
				ISection section = (ISection) iterSections.next();
				Integer sectionOrder = section.getSectionOrder();
				if (sectionOrder.intValue()
					> sectionToDeleteOrder.intValue()) {
					persistentSection.simpleLockWrite(section);
					section.setSectionOrder(
						new Integer(sectionOrder.intValue() - 1));
				}
			}
			return new Boolean(true);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}