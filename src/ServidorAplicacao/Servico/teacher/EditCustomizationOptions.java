package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoSite;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 *
 * 
 */
public class EditCustomizationOptions implements IServico {

	private static EditCustomizationOptions service = new EditCustomizationOptions();
	public static EditCustomizationOptions getService() {
		return service;
	}

	private EditCustomizationOptions() {
	}
	public final String getNome() {
		return "EditCustomizationOptions";
	}

	public boolean run(
		Integer infoExecutionCourseCode,
		InfoSite infoSiteNew)
		throws FenixServiceException {
			
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
			IPersistentSite persistentSite = sp.getIPersistentSite();

			ISite siteOld = null;

			IExecutionCourse executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(new ExecutionCourse(infoExecutionCourseCode), false);
			siteOld = persistentSite.readByExecutionCourse(executionCourse);

			persistentSite.lockWrite(siteOld);

			siteOld.setAlternativeSite(infoSiteNew.getAlternativeSite());
			siteOld.setMail(infoSiteNew.getMail());
			siteOld.setInitialStatement(infoSiteNew.getInitialStatement());
			siteOld.setIntroduction(infoSiteNew.getIntroduction());


		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
}