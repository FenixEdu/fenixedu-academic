/*
 * Created on 8/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoCurricularCourse;
import Dominio.CurricularCourse;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class InsertCurricularCourseAtDegreeCurricularPlan implements IServico {

	private static InsertCurricularCourseAtDegreeCurricularPlan service = new InsertCurricularCourseAtDegreeCurricularPlan();

	public static InsertCurricularCourseAtDegreeCurricularPlan getService() {
		return service;
	}

	private InsertCurricularCourseAtDegreeCurricularPlan() {
	}

	public final String getNome() {
		return "InsertCurricularCourseAtDegreeCurricularPlan";
	}
	

	public List run(InfoCurricularCourse infoCurricularCourse) throws FenixServiceException {

		IPersistentCurricularCourse persistentCurricularCourse = null;
	
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				
				Integer degreeCurricularPlanId = infoCurricularCourse.getInfoDegreeCurricularPlan().getIdInternal();
				
				IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
				IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(new DegreeCurricularPlan(degreeCurricularPlanId), false);
				
				String name = infoCurricularCourse.getName();
				String code = infoCurricularCourse.getCode();
				
				persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();

				ICurricularCourse curricularCourse = persistentCurricularCourse.readCurricularCourseByDegreeCurricularPlanAndNameAndCode(degreeCurricularPlanId, name, code);
//				if it doesn´t exist in the database yet
				if(curricularCourse == null) {
					curricularCourse = new CurricularCourse();
					curricularCourse.setBasic(infoCurricularCourse.getBasic());		
					curricularCourse.setCode(code);
					curricularCourse.setDegreeCurricularPlan(degreeCurricularPlan);
					curricularCourse.setMandatory(infoCurricularCourse.getMandatory());
					curricularCourse.setName(name);
					curricularCourse.setType(infoCurricularCourse.getType());
					curricularCourse.setAssociatedExecutionCourses(new ArrayList());
					IDisciplinaDepartamentoPersistente persistentDepartmentCourse = persistentSuport.getIDisciplinaDepartamentoPersistente();
//					department???
//					university???								

					persistentCurricularCourse.simpleLockWrite(curricularCourse);
					return null;
				}

//				if already exists
				List errors = new ArrayList(2);
				errors.add(name);
				errors.add(code);
				return errors;
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}