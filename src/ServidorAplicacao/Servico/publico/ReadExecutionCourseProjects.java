/*
 * Created on 31/Jul/2003
 *
 */
package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IGroupProperties;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */
public class ReadExecutionCourseProjects implements IServico{
	
	private static ReadExecutionCourseProjects _servico = new ReadExecutionCourseProjects();

	/**
	 * The actor of this class.
	 **/
	private ReadExecutionCourseProjects() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ReadExecutionCourseProjects";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourseProjects
	 */
	public static ReadExecutionCourseProjects getService() {
		return _servico;
	}

	public List run(Integer executionCourseCode)throws ExcepcaoInexistente, FenixServiceException {
		
		List projectsName = null;
		
		try 
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucao executionCourse = (IDisciplinaExecucao)sp.getIDisciplinaExecucaoPersistente().readByOId(new DisciplinaExecucao(executionCourseCode),false);
			List executionCourseProjects = sp.getIPersistentGroupProperties().readAllGroupPropertiesByExecutionCourse(executionCourse);
			
			projectsName = new ArrayList();
			Iterator iterator = executionCourseProjects.iterator();
			
			while (iterator.hasNext()) {
				projectsName.add(((IGroupProperties)iterator.next()).getName());

			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException("error.impossibleReadExecutionCourseProjects");
		}
		
		return projectsName;
	}
}
