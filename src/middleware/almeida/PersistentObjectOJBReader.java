/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CurricularCourse;
import Dominio.DegreeCurricularPlan;
import Dominio.DisciplinaExecucao;
import Dominio.Enrolment;
import Dominio.ExecutionPeriod;
import Dominio.Frequenta;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import Util.PeriodState;
import Util.TipoCurso;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class PersistentObjectOJBReader extends PersistentObjectOJB {

	public IStudentCurricularPlan readStudentCurricularPlan(Integer studentNumber) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student.number", studentNumber);
		List result = query(StudentCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IStudentCurricularPlan) result.get(0);
		} else {
			return null;
		}
	}

	public IStudent readStudent(Integer studentNumber, TipoCurso degreeType) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("number", studentNumber);
		criteria.addEqualTo("degreeType", degreeType);
		List result = query(Student.class, criteria);
		if (result.size() == 1) {
			return (IStudent) result.get(0);
		} else {
			return null;
		}
	}

	public IDegreeCurricularPlan readDegreeCurricularPlan(Integer degreeID) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degree.codigoInterno", degreeID);
		List result = query(DegreeCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IDegreeCurricularPlan) result.get(0);
		} else {
			return null;
		}
	}

	private ICurricularCourse readCurricularCourse(
		String code,
		Integer degreeID) {
		// Delete blank space in the beggining of code1
		if (code.charAt(0) == ' ') {
			code = code.substring(1);
		} else if (code.equals("ALG") && degreeID.intValue() == 5) {
			code = "AP9";
		} else if (code.equals("AWX") && degreeID.intValue() == 21) {
			code = "AXK";
		}

		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		criteria.addEqualTo("degreeCurricularPlan", degreeID);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println(
				"code: "
					+ code
					+ "  degreeID: "
					+ degreeID
					+ " result.size="
					+ result.size());
		} else /* if (result.size() == 0) */ {
			System.out.println(
				"code: "
					+ code
					+ "  degreeID: "
					+ degreeID
					+ " result.size="
					+ result.size());
		}
		return null;
	}

	public ICurricularCourse readCurricularCourse(
		String name,
		Integer degreeID,
		String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeCurricularPlan", degreeID);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else {
			return readCurricularCourse(code, degreeID);
		}
	}

	public Almeida_disc readAlmeidaCurricularCourse(String code, long curso) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coddis", code);
		criteria.addEqualTo("codcur", (new Integer("" + curso)));
		List result = query(Almeida_disc.class, criteria);
		//System.out.println("result.size" + result.size());
		if (result.size() > 0) {
			return (Almeida_disc) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param curricularCourse
	 * @param executionPeriod
	 * @return
	 */
	public IDisciplinaExecucao readExecutionCourse(
		ICurricularCourse curricularCourse,
		IExecutionPeriod executionPeriod) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"associatedCurricularCourses.idInternal",
			((CurricularCourse) curricularCourse).getIdInternal());
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo(
			"executionPeriod.name.executionYear.year",
			executionPeriod.getExecutionYear().getYear());
		List result = query(DisciplinaExecucao.class, criteria);
		//System.out.println("result.size" + result.size());
		if (result.size() == 1) {
			return (IDisciplinaExecucao) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param curricularCourse
	 * @param executionPeriod
	 * @return
	 */
	public IExecutionPeriod readActiveExecutionPeriod() {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("state", PeriodState.ACTUAL_CODE);
		List result = query(ExecutionPeriod.class, criteria);
		if (result.size() == 1) {
			return (IExecutionPeriod) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param curricularCourse
	 * @param executionPeriod
	 * @return
	 */
	public IEnrolment readEnrolment(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse,
		IExecutionPeriod executionPeriod) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"studentCurricularPlan.internalCode",
			((StudentCurricularPlan) studentCurricularPlan).getInternalCode());
			criteria.addEqualTo(
				"curricularCourse.idInternal",
				((CurricularCourse) curricularCourse).getIdInternal());
			criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
			criteria.addEqualTo(
				"executionPeriod.name.executionYear.year",
				executionPeriod.getExecutionYear().getYear());
		List result = query(Enrolment.class, criteria);
		if (result.size() == 1) {
			return (IEnrolment) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param student
	 * @param disciplinaExecucao
	 * @return
	 */
	public IFrequenta readFrequenta(IStudent student, IDisciplinaExecucao disciplinaExecucao) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"aluno.internalCode",
			((Student) student).getInternalCode());
			criteria.addEqualTo(
				"disciplinaExecucao.idInternal",
				((DisciplinaExecucao) disciplinaExecucao).getIdInternal());
		List result = query(Frequenta.class, criteria);
		if (result.size() == 1) {
			return (IFrequenta) result.get(0);
		} else {
			return null;
		}
	}

}
