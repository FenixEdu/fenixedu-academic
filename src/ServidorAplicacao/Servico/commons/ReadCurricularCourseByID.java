package ServidorAplicacao.Servico.commons;

import DataBeans.InfoCurricularCourse;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCurricularCourseByID implements IServico {

	private static ReadCurricularCourseByID service = new ReadCurricularCourseByID();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadCurricularCourseByID getService() {
	  return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadCurricularCourseByID";
	}
	

	public InfoCurricularCourse run(Integer curricularCourseID) throws FenixServiceException {
                        
	  
	  ICurricularCourse curricularCourse = null;
	  try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		
		
		ICurricularCourse ccTemp = new CurricularCourse();
		ccTemp.setIdInternal(curricularCourseID);
		
		curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse().readByOId(ccTemp, false);
		
		
	  } catch (ExcepcaoPersistencia ex) {
	  	throw new FenixServiceException(ex);
	  }
    
      if (curricularCourse == null){
      	throw new NonExistingServiceException();
      }
    
	  return Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
	}
}
