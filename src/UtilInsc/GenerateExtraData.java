package UtilInsc;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DegreeCurricularPlanState;
import Util.TipoCurso;

/**
 * @author David Santos
 * 11/Jun/2003
 */

public class GenerateExtraData {

	private static GestorServicos gestor = GestorServicos.manager();
	private static IUserView userView = null;
	private static ISuportePersistente persistentSupport =	null;

	public static void main(String[] args) {

		List curricularCoursesList = null;

		turnOnPersistentSuport();

		IPersistentCurricularCourse persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
		try {
			curricularCoursesList = persistentCurricularCourse.readAll();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
		}

		for(int i = 0; i < 10; i++) {
			ICurso degree = new Curso();
			degree.setDegreeCurricularPlans(null);
			degree.setIdInternal(null);
			degree.setNome("Curso Da Tanga " + i);
			degree.setSigla("CDT " + i);
			degree.setTipoCurso(TipoCurso.LICENCIATURA_OBJ);

			IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
			degreeCurricularPlan.setCurricularCourses(null);
			degreeCurricularPlan.setDegree(degree);
			degreeCurricularPlan.setDegreeDuration(new Integer(5));
			degreeCurricularPlan.setEndDate(new Date());
			degreeCurricularPlan.setInitialDate(new Date());
			degreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(3));
			degreeCurricularPlan.setName("Plano Curricular do Curso Da Tanga " + i);
			degreeCurricularPlan.setState(DegreeCurricularPlanState.ACTIVE_OBJ);
			
			IBranch branch = new Branch();
			branch.setCode("");
			branch.setName("");
			branch.setScopes(null);
			branch.setDegreeCurricularPlan(degreeCurricularPlan);

//			ICursoPersistente persistentDegree = persistentSupport.getICursoPersistente();
//			IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
			IPersistentBranch persistentBranch = persistentSupport.getIPersistentBranch();
			try {
				persistentBranch.lockWrite(branch);
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace(System.out);
			}
			
			generateCurricularCourses(curricularCoursesList, degreeCurricularPlan, branch, i);

		}

		turnOffPersistentSuport();
	}

	private static void turnOnPersistentSuport() {
		try {
			persistentSupport =	SuportePersistenteOJB.getInstance();
			persistentSupport.iniciarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
		}
	}

	private static void turnOffPersistentSuport() {
		try {
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
		}
	}

	private static void generateCurricularCourses(List curricularCoursesList, IDegreeCurricularPlan degreeCurricularPlan, IBranch branch, int i) {
		Iterator iterator1 = curricularCoursesList.iterator();
		while(iterator1.hasNext()) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iterator1.next();
			ICurricularCourse curricularCourseToWrite = new CurricularCourse();
			curricularCourseToWrite.setAssociatedExecutionCourses(null);
			curricularCourseToWrite.setCredits(null);
			curricularCourseToWrite.setDepartmentCourse(null);
			curricularCourseToWrite.setIdInternal(null);
			curricularCourseToWrite.setLabHours(null);
			curricularCourseToWrite.setPraticalHours(null);
			curricularCourseToWrite.setTheoPratHours(null);
			curricularCourseToWrite.setTheoreticalHours(null);
			curricularCourseToWrite.setCode(curricularCourse.getCode() + i);
			curricularCourseToWrite.setCurricularCourseExecutionScope(curricularCourse.getCurricularCourseExecutionScope());
			curricularCourseToWrite.setDegreeCurricularPlan(degreeCurricularPlan);
			curricularCourseToWrite.setMandatory(curricularCourse.getMandatory());
			curricularCourseToWrite.setName(curricularCourse.getName() + " - Plano Da Tanga " + i);
			curricularCourseToWrite.setType(curricularCourse.getType());
			curricularCourseToWrite.setUniversityCode(curricularCourse.getUniversityCode());
			curricularCourseToWrite.setScopes(null);
			Iterator iterator2 = curricularCourse.getScopes().iterator();
			while(iterator2.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator2.next();
				ICurricularCourseScope curricularCourseScopeToWrite = new CurricularCourseScope();
				curricularCourseScopeToWrite.setBranch(branch);
				curricularCourseScopeToWrite.setCurricularCourse(curricularCourseToWrite);
				curricularCourseScopeToWrite.setCurricularSemester(curricularCourseScope.getCurricularSemester());
				curricularCourseScopeToWrite.setIdInternal(null);
				curricularCourseScopeToWrite.setLabHours(null);
				curricularCourseScopeToWrite.setPraticalHours(null);
				curricularCourseScopeToWrite.setTheoPratHours(null);
				curricularCourseScopeToWrite.setTheoreticalHours(null);
			}
		}

	}








	private static void autentication() {
		String argsAutenticacao[] = {"user", "pass", Autenticacao.INTRANET};
		try {
			userView = (IUserView) executeService("Autenticacao", argsAutenticacao);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static InfoEnrolmentContext executeService(String serviceName, Object[] serviceArgs) {
		try {
			Object result = null;
			result = gestor.executar(userView, serviceName, serviceArgs);
			return (InfoEnrolmentContext) result;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}