/*
 * Created on 20/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.gesdis.InfoSite;
import DataBeans.gesdis.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadProfessorships implements IServico {
	
	private static ReadProfessorships service = new ReadProfessorships();

		/**
		 * The singleton access method of this class.
		 **/

		public static ReadProfessorships getService() {

			return service;}
	/**
	 * 
	 */
	public ReadProfessorships() {
		
	
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		
		return "ReadProfessorships";
	}
	public List run(InfoTeacher infoTeacher)
			throws FenixServiceException {
				
				List infoSites = new ArrayList();
				
			try {
				ISuportePersistente sp;
				sp = SuportePersistenteOJB.getInstance();
				IPersistentTeacher persistentTeacher=sp.getIPersistentTeacher();
				IPersistentSite persistentSite=sp.getIPersistentSite();
				List executionCourseList=persistentTeacher.readProfessorShipsExecutionCoursesByNumber(infoTeacher.getTeacherNumber());
				Iterator iter = executionCourseList.iterator();
				while (iter.hasNext()){
					IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) iter.next();
					ISite site = persistentSite.readByExecutionCourse(executionCourse);
					InfoSite infoSite = Cloner.copyISite2InfoSite(site);
					infoSites.add(infoSite);
				}
					
			
				return infoSites;
			} catch (ExcepcaoPersistencia e) {
				throw new FenixServiceException(e);
			} 
		}
}
