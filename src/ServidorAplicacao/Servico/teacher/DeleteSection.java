package ServidorAplicacao.Servico.teacher;
/**
 *
 * @author  lmac1
 */
import java.util.Iterator;
import java.util.List;
import org.apache.slide.common.SlideException;
import fileSuport.FileSuport;
import fileSuport.IFileSuport;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Section;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
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
	public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode)
		throws FenixServiceException {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IPersistentSite persistentSite = sp.getIPersistentSite();
			IPersistentSection persistentSection = sp.getIPersistentSection();
			ISite site = null;
			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					new DisciplinaExecucao(infoExecutionCourseCode),
					false);
			site = persistentSite.readByExecutionCourse(executionCourse);
			ISection sectionToDelete =
				(ISection) persistentSection.readByOId(
					new Section(sectionCode),
					false);
			if (sectionToDelete == null) {
				throw new FenixServiceException("non existing section");
			}

			ISection superiorSection = sectionToDelete.getSuperiorSection();
			Integer sectionToDeleteOrder = sectionToDelete.getSectionOrder();

			persistentSection.delete(sectionToDelete);
			IFileSuport fileSuport = FileSuport.getInstance();
			try {
				fileSuport.deleteFolder( sectionToDelete.getSlideName());
			} catch (SlideException e1) {
				System.out.println("não consegui apagar os ficheiros dos items da secção");
			}
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