/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoCurricularCourse;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
	

	public List run(Integer oldCurricularCourseId,InfoCurricularCourse  newInfoCurricularCourse,Integer degreeCPId) throws FenixServiceException {
	
		IPersistentCurricularCourse persistentCurricularCourse = null;
		ICurricularCourse oldCurricularCourse = null;
		
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
//			IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
//							IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(new DegreeCurricularPlan(degreeCPId), false);
			persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
			oldCurricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(oldCurricularCourseId), false);
			
			String newName = newInfoCurricularCourse.getName();
			String newCode = newInfoCurricularCourse.getCode();
	
			ICurricularCourse newCurricularCourse = persistentCurricularCourse.readCurricularCourseByDegreeCurricularPlanAndNameAndCode(degreeCPId, newName, newCode);
		
			if(newCurricularCourse == null) {
				newCurricularCourse = new CurricularCourse();
				oldCurricularCourse.setName(newName);
				oldCurricularCourse.setCode(newCode);
//				oldCurricularCourse.setCredits(newInfoCurricularCourse.getCredits());
//				oldCurricularCourse.setTheoreticalHours(newInfoCurricularCourse.getTheoreticalHours());
//				oldCurricularCourse.setPraticalHours(newInfoCurricularCourse.getPraticalHours());
//				oldCurricularCourse.setTheoPratHours(newInfoCurricularCourse.getTheoPratHours());
//				oldCurricularCourse.setLabHours(newInfoCurricularCourse.getLabHours());
				oldCurricularCourse.setType(newInfoCurricularCourse.getType());
				oldCurricularCourse.setMandatory(newInfoCurricularCourse.getMandatory());
				oldCurricularCourse.setBasic(newInfoCurricularCourse.getBasic());
				
				persistentCurricularCourse.simpleLockWrite(oldCurricularCourse);
				return null;
			}
		
			List errors = new ArrayList(2);
			
			errors.add(0, newName);
			errors.add(1, newCode);	
			
			return errors;
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}
