/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.scientificCouncil;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseWithInfoDegree;
import DataBeans.SiteView;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 * 23/Jul/2003
 * fenix-head
 * ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class ReadCurricularCourseByOIdService implements IServico{

	private static ReadCurricularCourseByOIdService _servico =
		new ReadCurricularCourseByOIdService();

	/**
	  * The actor of this class.
	  **/

	private ReadCurricularCourseByOIdService() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ReadCurricularCourseByOIdService";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static ReadCurricularCourseByOIdService getService() {
		return _servico;
	}

	public SiteView run(Integer curricularCourseId) throws FenixServiceException {
			try {
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				IPersistentCurricularCourse persistentCurricularCourse= sp.getIPersistentCurricularCourse();                
				ICurricularCourse curricularCourse = new CurricularCourse(curricularCourseId);
				curricularCourse= (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse,false); 
				
				//CLONER
				//InfoCurricularCourse infoCurricularCourse = Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
				InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree.newInfoFromDomain(curricularCourse);
				
				SiteView siteView = new SiteView(infoCurricularCourse);
				
				return siteView;
			} catch (ExcepcaoPersistencia e) {
				throw new FenixServiceException(e);
			}
			
			


		
	}

}
