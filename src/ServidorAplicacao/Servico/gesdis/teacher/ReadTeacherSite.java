package ServidorAplicacao.Servico.gesdis.teacher;

/**
 *
 * @author  EP 15
 */

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadTeacherSite implements IServico {

	private static ReadTeacherSite service = new ReadTeacherSite();

	/**
	 * The singleton access method of this class.
	 **/

	public static ReadTeacherSite getService() {

		return service;

	}

	/**
	 * The ctor of this class.
	 **/

	private ReadTeacherSite() {
	}

	/**
	 * Devolve o nome do servico
	 **/

	public final String getNome() {

		return "ReadTeacherSite";

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
			IDisciplinaExecucao executionCourse =
				Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
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
