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
	

	public void run(InfoCurricularCourse  newInfoCurricularCourse) throws FenixServiceException {
	
		IPersistentCurricularCourse persistentCurricularCourse = null;
		ICurricularCourse oldCurricularCourse = null;
		String newName = null;
		String newCode = null;
		
		try {
			
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
			oldCurricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(newInfoCurricularCourse.getIdInternal()), false);
			
			newName = newInfoCurricularCourse.getName();
			newCode = newInfoCurricularCourse.getCode();
		
			if(oldCurricularCourse == null)
				throw new NonExistingServiceException();
				
			oldCurricularCourse.setName(newName);
			oldCurricularCourse.setCode(newCode);
			oldCurricularCourse.setType(newInfoCurricularCourse.getType());
			oldCurricularCourse.setMandatory(newInfoCurricularCourse.getMandatory());
			oldCurricularCourse.setBasic(newInfoCurricularCourse.getBasic());
			oldCurricularCourse.setTheoreticalHours(newInfoCurricularCourse.getTheoreticalHours());
			oldCurricularCourse.setTheoPratHours(newInfoCurricularCourse.getTheoPratHours());
			oldCurricularCourse.setPraticalHours(newInfoCurricularCourse.getPraticalHours());
			oldCurricularCourse.setLabHours(newInfoCurricularCourse.getLabHours());
			oldCurricularCourse.setMaximumValueForAcumulatedEnrollments(newInfoCurricularCourse.getMaximumValueForAcumulatedEnrollments());
			oldCurricularCourse.setMinimumValueForAcumulatedEnrollments(newInfoCurricularCourse.getMinimumValueForAcumulatedEnrollments());
			oldCurricularCourse.setCredits(newInfoCurricularCourse.getCredits());
			oldCurricularCourse.setEctsCredits(newInfoCurricularCourse.getEctsCredits());
			oldCurricularCourse.setEnrollmentWeigth(newInfoCurricularCourse.getEnrollmentWeigth());
				
			persistentCurricularCourse.lockWrite(oldCurricularCourse);
			
		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException(ex);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}
