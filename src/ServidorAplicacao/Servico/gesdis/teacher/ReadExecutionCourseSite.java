package ServidorAplicacao.Servico.gesdis.teacher;

/**
 *
 * @author  EP 15
 */

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExecutionCourseSite implements IServico {

	private static ReadExecutionCourseSite service = new ReadExecutionCourseSite();

	/**
	 * The singleton access method of this class.
	 **/

	public static ReadExecutionCourseSite getService() {

		return service;

	}

	/**
	 * The ctor of this class.
	 **/

	private ReadExecutionCourseSite() {
	}

	/**
	 * Devolve o nome do servico
	 **/

	public final String getNome() {
		return "ReadExecutionCourseSite";
	}

	/**
	 * Executes the service. Returns the current collection of
	 * sitios names.
	 *
	 * @throws ExcepcaoInexistente is there is none sitio.
	 **/

	public InfoSite run(InfoExecutionCourse infoExecutionCourse)
		throws FenixServiceException {

	

		try {
			ISite site = null;
			
			ISuportePersistente sp;
			
			sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse = new ExecutionCourse();
			executionCourse.setIdInternal(infoExecutionCourse.getIdInternal());
			executionCourse =  (IExecutionCourse) executionCourseDAO.readByOId(executionCourse, false);
			
			
			site = sp.getIPersistentSite().readByExecutionCourse(executionCourse);
			
			InfoSite  infoSite=null;
			
			if (site != null){
				infoSite = Cloner.copyISite2InfoSite(site);
			}
					
			
			return infoSite;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		} 
	}

}
