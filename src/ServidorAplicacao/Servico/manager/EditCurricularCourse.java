/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoCurricularCourse;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class EditCurricularCourse implements IServico {

	private static EditCurricularCourse service = new EditCurricularCourse();

	public static EditCurricularCourse getService() {
		return service;
	}

	private EditCurricularCourse() {
	}

	public final String getNome() {
		return "EditCurricularCourse";
	}
	

	public void run(Integer oldCurricularCourseId,InfoCurricularCourse  newInfoCurricularCourse) throws FenixServiceException {
	
		IPersistentCurricularCourse persistentCurricularCourse = null;
		ICurricularCourse oldCurricularCourse = null;
		
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
			oldCurricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(oldCurricularCourseId), false);
			
			String newName = newInfoCurricularCourse.getName();
			String newCode = newInfoCurricularCourse.getCode();
		
			if(oldCurricularCourse != null) {
				
				oldCurricularCourse.setName(newName);
				oldCurricularCourse.setCode(newCode);
				oldCurricularCourse.setType(newInfoCurricularCourse.getType());
				oldCurricularCourse.setMandatory(newInfoCurricularCourse.getMandatory());
				oldCurricularCourse.setBasic(newInfoCurricularCourse.getBasic());
				
				try {
					persistentCurricularCourse.lockWrite(oldCurricularCourse);
				} catch (ExistingPersistentException ex) {
					throw new ExistingServiceException("A disciplina curricular de nome "+newName+" e sigla "+newCode, ex);
				}
			}else
			throw new NonExistingServiceException();
		
		
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}
