/*
 * Created on 20/Mar/2003
 *
 * 
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoSite;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ISite;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 * 
 */
public class ReadTeacherExecutionCoursesSitesService implements IServico {

	private static ReadTeacherExecutionCoursesSitesService service =
		new ReadTeacherExecutionCoursesSitesService();

	/**
	 * The singleton access method of this class.
	 **/

	public static ReadTeacherExecutionCoursesSitesService getService() {
		return service;
	}
	/**
	 * 
	 */
	public ReadTeacherExecutionCoursesSitesService() {

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {

		return "ReadTeacherExecutionCoursesSitesService";
	}
	public List run(InfoTeacher infoTeacher) throws FenixServiceException {

		List infoSites = new ArrayList();

		try {
			ISuportePersistente persistentSuport;
			persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher =
				persistentSuport.getIPersistentTeacher();
			IPersistentSite persistentSite =
				persistentSuport.getIPersistentSite();
			IPersistentProfessorship persistentProfessorship =
				persistentSuport.getIPersistentProfessorship();
			ITeacher teacher =
				persistentTeacher.readTeacherByNumber(
					infoTeacher.getTeacherNumber());
			List professorships =
				persistentProfessorship.readByTeacher(teacher);
			Iterator iter = professorships.iterator();
			while (iter.hasNext()) {
				IProfessorship professorship = (IProfessorship) iter.next();
				IExecutionCourse executionCourse =
					professorship.getExecutionCourse();
				ISite site =
					persistentSite.readByExecutionCourse(executionCourse);
				InfoSite infoSite = Cloner.copyISite2InfoSite(site);
				infoSites.add(infoSite);
			}

			return infoSites;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}
