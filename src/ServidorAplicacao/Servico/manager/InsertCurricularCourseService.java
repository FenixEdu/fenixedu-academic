/*
 * Created on 8/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import Dominio.CurricularCourse;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;

/**
 * @author lmac1
 */

public class InsertCurricularCourseService implements IServico {

	private static InsertCurricularCourseService service = new InsertCurricularCourseService();

	public static InsertCurricularCourseService getService() {
		return service;
	}

	private InsertCurricularCourseService() {
	}

	public final String getNome() {
		return "InsertCurricularCourseService";
	}
	

	public List run(String name, String code, String credits, String theoreticalHours,
						String praticalHours, String theoPratHours, String labHours,
						String type, String mandatory, String basic,
						Integer degreeCurricularPlanId) throws FenixServiceException {

		IPersistentCurricularCourse persistentCurricularCourse = null;
	
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
//deveria ser lido tambem plo degreeCurricularPlan    o degreeCurricularPlan ta em baixo
				ICurricularCourse curricularCourse = persistentCurricularCourse.readCurricularCourseByNameAndCode(name, code);
				// if it doesn´t exist in the database yet
				if(curricularCourse == null) {
					curricularCourse = new CurricularCourse();			
					curricularCourse.setName(name);
					curricularCourse.setCode(code);
					if(credits.compareTo("") != 0) {
						curricularCourse.setCredits(new Double(credits));
					}
					if(theoreticalHours.compareTo("") != 0) {
						curricularCourse.setTheoreticalHours(new Double(theoreticalHours));
					}
					if(praticalHours.compareTo("") != 0) {
						curricularCourse.setPraticalHours(new Double(praticalHours));
					}
					if(theoPratHours.compareTo("") != 0) {
						curricularCourse.setTheoPratHours(new Double(theoPratHours));
					}
					if(labHours.compareTo("") != 0) {
						curricularCourse.setLabHours(new Double(labHours));
					}
					if(type.compareTo("") != 0) {
						curricularCourse.setType(new CurricularCourseType(new Integer(type)));
					}
					if(mandatory.compareTo("") != 0) {
						curricularCourse.setMandatory(new Boolean(mandatory));
					}
					
//					Integer universityId = new Integer(universityId);
//					IPersistentUniversity persistentUniversity = persistentSuport.getIPersistentUniversity();
//					IUniversity university = (IUniversity) persistentUniversity.readByOid(new University(universityId), false);
//					curricularCourse.setUniversity(university);

					if(basic.compareTo("") != 0) {
						curricularCourse.setBasic(new Boolean(basic));
					}
					
//					Integer departmentCourseId = new Integer(fieldsList[11]);
//					IDisciplinaDepartamentoPersistente persistentDepartmentCourse = persistentSuport.getIDisciplinaDepartamentoPersistente();
//					DisciplinaDepartamento dc = new DisciplinaDepartamento();
//					dc.setCodigoInterno(departmentCourseId);
//					IDisciplinaDepartamento departmentCourse = (IDisciplinaDepartamento) persistentDepartmentCourse.readByOId(dc, false);
//					curricularCourse.setDepartmentCourse(departmentCourse);
					
					IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
					IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(new DegreeCurricularPlan(degreeCurricularPlanId), false);
					curricularCourse.setDegreeCurricularPlan(degreeCurricularPlan);
					
//  curricularCourseExecutionScope? scopes?
				
					// associatedExecutionCourses? nao pode ser null qd se insere, pk dp n da papagar
					List emptyList = new ArrayList();
					curricularCourse.setAssociatedExecutionCourses(emptyList);

					persistentCurricularCourse.simpleLockWrite(curricularCourse);
					return null;
				}
				//if already exists
				else {
					List errors = new ArrayList(2);
					errors.add(name);
					errors.add(code);
					return errors;
				}
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}