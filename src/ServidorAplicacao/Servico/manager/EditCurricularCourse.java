/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
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
//		IPersistentDegreeCurricularPlan persistentDegreeCP = null;

		
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
				oldCurricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(oldCurricularCourseId), false);
				
				List CurricularCourses = persistentCurricularCourse.readAll();
				CurricularCourses.remove((ICurricularCourse)oldCurricularCourse);
				
//				persistentDegreeCP = persistentSuport.getIPersistentDegreeCurricularPlan();
//				degreeCP = (IDegreeCurricularPlan) persistentDegreeCP.readByOId(new DegreeCurricularPlan(degreeCPId), false);	
			
			
				String newName = newInfoCurricularCourse.getName();
				String newCode = newInfoCurricularCourse.getCode();
			
				List errors = new ArrayList(2);
				errors.add(null);
				errors.add(null);
				
					int modified = 0; 
				
					Iterator iter = CurricularCourses.iterator();
					while(iter.hasNext()) {
						ICurricularCourse curricularCourseIter = (ICurricularCourse) iter.next();
						if(newName.compareToIgnoreCase(curricularCourseIter.getName())==0 || newCode.compareToIgnoreCase(curricularCourseIter.getCode())==0){
							modified++;
							errors.set(0, newName);
							errors.set(1, newCode);
						}
					}
//					System.out.println("ERRO ERRO ERRO ERRO"+errors);

					if(modified == 0) {
						errors = null; 
					
					oldCurricularCourse.setName(newName);
					oldCurricularCourse.setCode(newCode);
					oldCurricularCourse.setCredits(newInfoCurricularCourse.getCredits());
					oldCurricularCourse.setTheoreticalHours(newInfoCurricularCourse.getTheoreticalHours());
					oldCurricularCourse.setPraticalHours(newInfoCurricularCourse.getPraticalHours());
					oldCurricularCourse.setTheoPratHours(newInfoCurricularCourse.getTheoPratHours());
					oldCurricularCourse.setLabHours(newInfoCurricularCourse.getLabHours());
					oldCurricularCourse.setType(newInfoCurricularCourse.getType());
					oldCurricularCourse.setMandatory(newInfoCurricularCourse.getMandatory());
					oldCurricularCourse.setBasic(newInfoCurricularCourse.getBasic());
				
					persistentCurricularCourse.simpleLockWrite(oldCurricularCourse);
//					System.out.println("TA NO SERVICO INSERT DEGREE CURRICULAR PLAN!!!DEPOIS DOS SETS");

				}
							
				return errors;
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}
