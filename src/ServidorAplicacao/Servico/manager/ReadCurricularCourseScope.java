/*
 * Created on 22/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.util.Cloner;
import Dominio.CurricularCourseScope;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadCurricularCourseScope implements IServico {

	private static ReadCurricularCourseScope service = new ReadCurricularCourseScope();

	/**
	* The singleton access method of this class.
	*/
	public static ReadCurricularCourseScope getService() {
		return service;
	}

	/**
	* The constructor of this class.
	*/
	private ReadCurricularCourseScope() { }

	/**
	* Service name
	*/
	public final String getNome() {
		return "ReadCurricularCourseScope";
	}

	/**
	* Executes the service. Returns the current InfoCurricularCourseScope.
	*/
	public InfoCurricularCourseScope run(Integer idInternal) throws FenixServiceException {
		ICurricularCourseScope curricularCourseScope;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ICurricularCourseScope curricularCourseScopeToRead = new CurricularCourseScope();
			curricularCourseScopeToRead.setIdInternal(idInternal);
			curricularCourseScope = (ICurricularCourseScope) sp.getIPersistentCurricularCourseScope().readByOId(curricularCourseScopeToRead, false);
			System.out.println("CURRICULAR COURSE Scope"+curricularCourseScope);
		} catch (ExcepcaoPersistencia excepcaoPersistencia){
			throw new FenixServiceException(excepcaoPersistencia);
		}
     
		if (curricularCourseScope == null) {
			return null;
		}

		//TODO:perguntar joao funciona no branch(so tem o getInternalId) e nao funciona no semester:NAO SEI ENTAO QUAL O ERRO(getInternalID( se eu nao usar este o ID vem a null)(e eh este que ta  a usar) e tem getInternalId)
		Integer curricularSemesterId = curricularCourseScope.getCurricularSemester().getIdInternal();
		InfoCurricularCourseScope infoCurricularCourseScope = Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
		
		infoCurricularCourseScope.getInfoCurricularSemester().setIdInternal(curricularSemesterId);
		
		System.out.println("IIIIIIIIIIIIIIIIII"+curricularCourseScope.getCurricularSemester().getIdInternal());
		
		return infoCurricularCourseScope;
	}
}