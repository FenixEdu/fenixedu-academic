/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida;

import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseEquivalence;
import Dominio.CurricularCourseEquivalenceRestriction;
import Dominio.CurricularCourseScope;
import Dominio.CurricularSemester;
import Dominio.CurricularYear;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.DisciplinaExecucao;
import Dominio.Employee;
import Dominio.Enrolment;
import Dominio.EnrolmentEquivalence;
import Dominio.EquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.EnrolmentEvaluation;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.Frequenta;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseEquivalence;
import Dominio.ICurricularCourseEquivalenceRestriction;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.IUniversity;
import Dominio.Pessoa;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import Dominio.Teacher;
import Dominio.University;
import Util.DegreeCurricularPlanState;
import Util.EnrolmentEvaluationType;
import Util.PeriodState;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class PersistentObjectOJBReader extends PersistentObjectOJB {

	public IStudentCurricularPlan readStudentCurricularPlanByStudentNumber(Integer studentNumber) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student.number", studentNumber);
		List result = query(StudentCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IStudentCurricularPlan) result.get(0);
		} else {
			return null;
		}
	}

	public IStudent readStudentByNumberAndDegreeType(Integer studentNumber, TipoCurso degreeType) {
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

	public IDegreeCurricularPlan readDegreeCurricularPlanByDegreeID(Integer degreeID) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degree.idInternal", degreeID);
		List result = query(DegreeCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IDegreeCurricularPlan) result.get(0);
		} else {
			System.out.println("readDegreeCurricularPlanByDegreeID: degree =  " + degreeID + " result.size = " + result.size());
			return null;
		}
	}

	private ICurricularCourse readCurricularCourseByCodeAndDegreeID(String code, Integer degreeID) {
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
			System.out.println("readCurricularCourse: code = " + code + "  degreeID = " + degreeID + " result.size =" + result.size());
		} else /* if (result.size() == 0) */ {
			System.out.println("readCurricularCourse: code = " + code + "  degreeID = " + degreeID + " result.size =" + result.size());
		}
		return null;
	}

	public ICurricularCourse readCurricularCourseByNameAndDegreeIDAndCode(String name, Integer degreeID, String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeID);
		criteria.addEqualTo("code", code);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else {
			//			return readCurricularCourse(code, degreeID);
			return null;
		}
	}

	public List readCurricularCourseByCodeAndNameInDegreeActive(String name, String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeCurricularPlan.state", new Integer(DegreeCurricularPlanState.ACTIVE));
		criteria.addEqualTo("code", code);
		List result = query(CurricularCourse.class, criteria);

		return result;
	}

	public Almeida_disc readAlmeidaCurricularCourseByCodeAndDegree(String code, long curso) {
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

	public Almeida_disc readAlmeidaCurricularCourseByCodeAndDegreeAndYear(String code, long curso, long year) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coddis", code);
		criteria.addEqualTo("codcur", new Integer("" + curso));
		criteria.addEqualTo("anoLectivo", new Integer("" + year));
		List result = query(Almeida_disc.class, criteria);
		if (result.size() > 0) {
			return (Almeida_disc) result.get(0);
		} else {
			return null;
		}
	}

	public Almeida_disc readAlmeidaCurricularCourseByCodeAndYear(String code, long year) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coddis", code);
		criteria.addEqualTo("anoLectivo", new Integer("" + year));
		List result = query(Almeida_disc.class, criteria);
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
	public IExecutionCourse readExecutionCourse(ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("associatedCurricularCourses.idInternal", ((CurricularCourse) curricularCourse).getIdInternal());
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo("executionPeriod.name.executionYear.year", executionPeriod.getExecutionYear().getYear());
		List result = query(DisciplinaExecucao.class, criteria);
		//System.out.println("result.size" + result.size());
		if (result.size() == 1) {
			return (IExecutionCourse) result.get(0);
		} else {
			return null;
		}
	}

	/** 
	 * @return IExecutionPeriod
	 */
	public IExecutionPeriod readActiveExecutionPeriod() {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("state", PeriodState.CURRENT_CODE);
		List result = query(ExecutionPeriod.class, criteria);
		if (result.size() == 1) {
			return (IExecutionPeriod) result.get(0);
		} else {
			return null;
		}
	}

	/** 
		 * @param IExecutionYear
	 * @param Integer
	 * @return IExecutionPeriod
	 */
	public IExecutionPeriod readExecutionPeriodByYearAndSemester(IExecutionYear executionYear, Integer semester) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionYear.year", executionYear.getYear());
		criteria.addEqualTo("semester", semester);
		List result = query(ExecutionPeriod.class, criteria);
		if (result.size() == 1) {
			return (IExecutionPeriod) result.get(0);
		} else {
			return null;
		}
	}

	/** 
	 * @param Integer
	 * @return IExecutionYear
	 */
	public IExecutionYear readExecutionYearByYear(String year) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("year", year);
		List result = query(ExecutionYear.class, criteria);
		if (result.size() == 1) {
			return (IExecutionYear) result.get(0);
		} else {
			return null;
		}
	}
	/**
	 * @param curricularCourse
	 * @param executionPeriod
	 * @return
	 */
	public IEnrolment readEnrolmentByOldUnique(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse,
		IExecutionPeriod executionPeriod) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", ((StudentCurricularPlan) studentCurricularPlan).getIdInternal());
		criteria.addEqualTo("curricularCourse.idInternal", ((CurricularCourse) curricularCourse).getIdInternal());
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo("executionPeriod.name.executionYear.year", executionPeriod.getExecutionYear().getYear());
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
	public IFrequenta readFrequenta(IStudent student, IExecutionCourse disciplinaExecucao) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("aluno.idInternal", ((Student) student).getIdInternal());
		criteria.addEqualTo("disciplinaExecucao.idInternal", ((DisciplinaExecucao) disciplinaExecucao).getIdInternal());
		criteria.addEqualTo("aluno.idInternal", ((Student) student).getIdInternal());
		criteria.addEqualTo("disciplinaExecucao.idInternal", ((DisciplinaExecucao) disciplinaExecucao).getIdInternal());
		List result = query(Frequenta.class, criteria);
		if (result.size() == 1) {
			return (IFrequenta) result.get(0);
		} else {
			return null;
		}
	}

	public List readAllAlmeidaCurricularCourses() {
		List result = query(Almeida_curricular_course.class, null);
		return result;
	}

	public List readAllAlmeidaDisc() {
		List result = query(Almeida_disc.class, null);
		return result;
	}

	public ICurricularCourse readCurricularCourseByCodeAndNameAndDegreeCurricularPlan(String code, String name, IDegreeCurricularPlan degreeCurricularPlan) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeCurricularPlan.idInternal", ((DegreeCurricularPlan) degreeCurricularPlan).getIdInternal());

		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourseByCodeAndNameAndDegreeCurricularPlan: name = " + name + " result.size = " + result.size());
		}
		return null;
	}

	public IDegreeCurricularPlan readDegreeCurricularPlanByName(String name) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		List result = query(DegreeCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IDegreeCurricularPlan) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readDegreeCurricularPlanByName: name = " + name + " result.size = " + result.size());
		}
		return null;
	}

	public ICurso readDegreeByCode(String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("sigla", code);
		List result = query(Curso.class, criteria);
		if (result.size() == 1) {
			return (ICurso) result.get(0);
		} else {
			return null;
		}
	}

	public ICurso readDegreeByID(Integer id) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("idInternal", id);
		List result = query(Curso.class, criteria);
		if (result.size() == 1) {
			return (ICurso) result.get(0);
		} else {
			return null;
		}
	}

	public ICurricularCourse readCurricularCourseByNameAndDegreeCurricularPlan(String name, IDegreeCurricularPlan degreeCurricularPlan) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeCurricularPlan.idInternal", ((DegreeCurricularPlan) degreeCurricularPlan).getIdInternal());

		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourseByNameAndDegreeCurricularPlan: name = " + name + " result.size = " + result.size());
		}
		return null;
	}

	public ICurricularYear readCurricularYearByYear(Integer year) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("year", year);

		List result = query(CurricularYear.class, criteria);
		if (result.size() == 1) {
			return (ICurricularYear) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularYear: year = " + year + " result.size = " + result.size());
		}
		return null;
	}

	public ICurricularSemester readCurricularSemesterBySemesterAndCurricularYear(Integer semester, ICurricularYear curricularYear) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("semester", semester);
		criteria.addEqualTo("curricularYear.internalID", curricularYear.getIdInternal());

		List result = query(CurricularSemester.class, criteria);
		if (result.size() == 1) {
			return (ICurricularSemester) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularSemester: semester = " + semester + " result.size = " + result.size());
		}
		return null;
	}

	public IBranch readBranchByKey(Integer key) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("internalID", key);

		List result = query(Branch.class, criteria);
		if (result.size() == 1) {
			return (IBranch) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readBranch: key = " + key + " result.size = " + result.size());
		}
		return null;
	}

	public IStudentCurricularPlan readStudentCurricularPlanByUnique(
		IStudent student,
		IDegreeCurricularPlan degreeCurricularPlan,
		IBranch branch,
		StudentCurricularPlanState studentCurricularPlanState) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student.idInternal", student.getIdInternal());
		criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
		criteria.addEqualTo("branch.internalID", branch.getIdInternal());
		criteria.addEqualTo("currentState", studentCurricularPlanState);
		List result = query(StudentCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IStudentCurricularPlan) result.get(0);

		} else if (result.size() > 1) {
			System.out.println("readStudentCurricularPlanByUnique mais que um");
		}

		return null;
	}

	public ICurricularCourse readCurricularCourseByCodeAndDegreeCurricularPlan(String code, IDegreeCurricularPlan degreeCurricularPlan) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		criteria.addEqualTo("degreeCurricularPlan.idInternal", ((DegreeCurricularPlan) degreeCurricularPlan).getIdInternal());

		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourseByCodeAndDegreeCurricularPlan: code = " + code + " result.size = " + result.size());
		}
		return null;
	}

	public ICurricularCourseScope readCurricularCourseScopeBySemester(ICurricularCourse curricularCourse, Integer semester) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
		criteria.addEqualTo("curricularSemester.semester", semester);

		List result = query(CurricularCourseScope.class, criteria);
		if (result.size() >= 1) {
			return (ICurricularCourseScope) result.get(0);
		} else if (result.size() < 1) {
			System.out.println("CurricularCourseScope: " + result.size());
		}
		return null;
	}

	public IPessoa readPersonByEmployeeNumber(Integer number) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("numberEmployee", number);

		List result = query(Employee.class, criteria);
		if (result.size() == 1) {
			int personKey = ((Employee) result.get(0)).getKeyPerson().intValue();
			Criteria criteria2 = new Criteria();
			criteria2.addEqualTo("idInternal", new Integer(personKey));
			List result2 = query(Pessoa.class, criteria2);
			if (result2.size() == 1) {
				return (IPessoa) result2.get(0);
			} else if (result2.size() > 1) {
				System.out.println("Pessoa: " + result2.size());
			}
		} else if (result.size() > 1) {
			System.out.println("Funcionario: " + result.size());
		}
		return null;
	}

	public List readAllStudentCurricularPlansFromDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
		return query(StudentCurricularPlan.class, criteria);
	}

	public List readAllEnrolmentsFromStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", ((StudentCurricularPlan) studentCurricularPlan).getIdInternal());
		return query(Enrolment.class, criteria);
	}

	public List readAllAlmeidaCurrams() {
		return query(Almeida_curram.class, null);
	}

	public IDegreeCurricularPlan readDegreeCurricularPlanByDegreeKeyAndState(Integer degreeKey, DegreeCurricularPlanState degreeCurricularPlanState) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degree.idInternal", degreeKey);
		criteria.addEqualTo("state", degreeCurricularPlanState);
		List result = query(DegreeCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IDegreeCurricularPlan) result.get(0);
		} else {
			System.out.println("readDegreeCurricularPlanByDegreeKeyAndState: Degree = " + degreeKey + " result.size = " + result.size());
			return null;
		}
	}

	public List readAllAlmeidaEnrolments() {
		return query(Almeida_enrolment.class, null);
	}

	public Almeida_curricular_course readAlmeidaCurricularCourseByCode(String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		List result = query(Almeida_curricular_course.class, criteria);
		if (result.size() == 1) {
			return (Almeida_curricular_course) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readAlmeidaCurricularCourse: code = " + code + " result.size = " + result.size());
		}
		return null;
	}

	public Almeida_disc readAlmeidaDiscByCodeAndDegree(String code, long curso) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coddis", code);
		criteria.addEqualTo("codcur", (new Integer("" + curso)));
		List result = query(Almeida_disc.class, criteria);
		if (result.size() == 1) {
			return (Almeida_disc) result.get(0);
		} else {
			System.out.println("readAlmeidaDiscByCodeAndName: coddis = " + code + " codcur =  " + curso + " result.size = " + result.size());
			return null;
		}
	}

	public IBranch readBranchByUnique(String code, IDegreeCurricularPlan degreeCurricularPlan) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
		List result = query(Branch.class, criteria);
		if (result.size() == 1) {
			return (IBranch) result.get(0);
		} else if (result.size() > 1) {
			System.out.println(
				"readBranchByUnique: code [" + code + "] e plano curricular [" + degreeCurricularPlan.getIdInternal() + "] result.size = " + result.size());
		}
		return null;
	}

	public Almeida_curram readAlmeidaCurramByUnique(long curso, long ramo, long orientacao) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("codcur", (new Integer("" + curso)));
		criteria.addEqualTo("codram", (new Integer("" + ramo)));
		criteria.addEqualTo("codorien", (new Integer("" + orientacao)));
		List result = query(Almeida_curram.class, criteria);
		if (result.size() == 1) {
			return (Almeida_curram) result.get(0);
		} else if (result.size() > 1) {
			System.out.println(
				"readAlmeidaCurramByUnique:" + " codcur = " + curso + " codRam =  " + ramo + " codorien =  " + orientacao + " result.size = " + result.size());
		}
		return null;
	}

	public Almeida_disc readAlmeidaDiscByUnique(
		String codigoDisciplina,
		long longCodigoCurso,
		long longCodigoRamo,
		long longSemestre,
		long executionYear,
		long longAnoCurricular,
		long longOrientacao) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coddis", codigoDisciplina);
		criteria.addEqualTo("codcur", (new Integer("" + longCodigoCurso)));
		criteria.addEqualTo("codRam", (new Integer("" + longCodigoRamo)));
		criteria.addEqualTo("semDis", (new Integer("" + longSemestre)));
		criteria.addEqualTo("anoLectivo", (new Integer("" + executionYear)));
		criteria.addEqualTo("anodis", (new Integer("" + longAnoCurricular)));
		criteria.addEqualTo("orientation", (new Integer("" + longOrientacao)));

		List result = query(Almeida_disc.class, criteria);
		if (result.size() == 1) {
			return (Almeida_disc) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readAlmeidaDiscByUnique: coddis = " + codigoDisciplina + "result.size = " + result.size());
		}
		return null;
	}

	public IEnrolment readEnrolmentByUnique(IStudentCurricularPlan plan, ICurricularCourseScope scope, IExecutionPeriod period) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", ((StudentCurricularPlan) plan).getIdInternal());
		criteria.addEqualTo("curricularCourseScope.idInternal", scope.getIdInternal());
		criteria.addEqualTo("executionPeriod.idInternal", period.getIdInternal());
		List result = query(Enrolment.class, criteria);
		if (result.size() == 1) {
			return (IEnrolment) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readEnrolmentByUnique mais que um\n");
		}
		return null;
	}

	public IEnrolment readEnrolmentByStudentCurricularPlanAndScope(IStudentCurricularPlan plan, ICurricularCourseScope scope) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", ((StudentCurricularPlan) plan).getIdInternal());
		criteria.addEqualTo("curricularCourseScope.idInternal", scope.getIdInternal());
		List result = query(Enrolment.class, criteria);
		if (result.size() == 1) {
			return (IEnrolment) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readEnrolmentByStudentCurricularPlanAndScope mais que um\n");
		}
		return null;
	}

	public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndTypeAndGrade(IEnrolment enrolment, EnrolmentEvaluationType type, String grade) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
		criteria.addEqualTo("enrolmentEvaluationType", type);
		criteria.addEqualTo("grade", grade);
		List result = query(EnrolmentEvaluation.class, criteria);
		if (result.size() == 1) {
			return (IEnrolmentEvaluation) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readEnrolmentByUnique mais que um\n");
		}
		return null;
	}

	public List readAllCurricularCourses() {
		return query(CurricularCourse.class, null);
	}

	public List readAllEnrolments() {
		return query(Enrolment.class, null);
	}

	public ICurricularCourseEquivalence readCurricularCourseEquivalenceByCurricularCourse(ICurricularCourse curricularCourse) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
		List result = query(CurricularCourseEquivalence.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourseEquivalence) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourseEquivalenceByCurricularCourse mais que um\n");
		}
		return null;
	}

	//	public IEnrolmentEquivalence readEnrolmentEquivalenceByUnique(IEnrolment enrolment) {
	//		Criteria criteria = new Criteria();
	//		criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
	//		List result = query(EnrolmentEquivalence.class, criteria);
	//		if (result.size() == 1) {
	//			return (IEnrolmentEquivalence) result.get(0);
	//		} else if (result.size() > 1) {
	//			System.out.println("readEnrolmentEquivalenceByUnique mais que um\n");
	//		}
	//		return null;
	//	}

	public IEnrolment readEnrolmentByID(int id) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("idInternal", new Integer(id));
		List result = query(Enrolment.class, criteria);
		if (result.size() == 1) {
			return (IEnrolment) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readEnrolmentByID mais que um\n");
		}
		return null;
	}

	public IUniversity readUniversityByCode(String universityCodeRead) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", universityCodeRead);
		List result = query(University.class, criteria);
		if (result.size() == 1) {
			return (IUniversity) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readUniversityByCode mais que um\n");
		}
		return null;
	}

	public ICurricularCourseEquivalenceRestriction readCurricularCourseEquivalenceRestrictionByUnique(
		ICurricularCourse equivalentCurricularCourse,
		ICurricularCourseEquivalence curricularCourseEquivalence,
		String yearOfEquivalence) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("equivalentCurricularCourse.idInternal", equivalentCurricularCourse.getIdInternal());
		criteria.addEqualTo("curricularCourseEquivalence.idInternal", curricularCourseEquivalence.getIdInternal());
		criteria.addEqualTo("yearOfEquivalence", yearOfEquivalence);
		
		List result = query(CurricularCourseEquivalenceRestriction.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourseEquivalenceRestriction) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourseEquivalenceRestrictionByUnique mais que um\n");
		}
		return null;
	}

	public List readAllCurricularCourseEquivalences() {
		Query query = new QueryByCriteria(CurricularCourseEquivalence.class, null);
		return (List) broker.getCollectionByQuery(query);
	}

	public List readEnrolmentsByCurricularCourse(ICurricularCourse curricularCourseEnroled) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourseScope.curricularCourse.idInternal", curricularCourseEnroled.getIdInternal());
		List result = query(Enrolment.class, criteria);
		if (result.size() < 0) {
			return null;
		}
		return result;
	}

	public IEquivalentEnrolmentForEnrolmentEquivalence readEnrolmentEquivalenceRestrictionByUnique(IEnrolment enrolment) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("equivalentEnrolment.idInternal", enrolment.getIdInternal());
		List result = query(EquivalentEnrolmentForEnrolmentEquivalence.class, criteria);
		if (result.size() == 1) {
			return (IEquivalentEnrolmentForEnrolmentEquivalence) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("IEquivalentEnrolmentForEnrolmentEquivalence mais que um\n");
		}
		return null;
	}

	public Almeida_escola readAlmeidaEscolaByUnique(String codigo, String nome) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", codigo);
		criteria.addEqualTo("name", nome);
		List result = query(Almeida_escola.class, criteria);
		if (result.size() == 1) {
			return (Almeida_escola) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readAlmeidaEscolaByUnique mais que um\n");
		}
		return null;
	}

	public List readAllAlmeidaEscolas() {
		Query query = new QueryByCriteria(Almeida_escola.class, null);
		return (List) broker.getCollectionByQuery(query);
	}

	public IUniversity readUniversityByUnique(String code, String name) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		criteria.addEqualTo("name", name);
		List result = query(University.class, criteria);
		if (result.size() == 1) {
			return (IUniversity) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readUniversityByCode mais que um\n");
			return (IUniversity) result.get(0);
		}
		return null;
	}

	public List readAllLEQCurricularCourses(IDegreeCurricularPlan plan) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degreeCurricularPlan.idInternal", plan.getIdInternal());
		List result = query(CurricularCourse.class, criteria);
		if (result == null) {
			return null;
		} else if (result.isEmpty()) {
			return null;
		}

		return result;
	}

	public IExecutionCourse readExecutionCourseByUnique(String code, IExecutionPeriod executionPeriod) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("sigla", code);
		criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
		criteria.addEqualTo("executionPeriod.name.executionYear.year", executionPeriod.getExecutionYear().getYear());
		List result = query(DisciplinaExecucao.class, criteria);
		if (result.size() == 1) {
			return (IExecutionCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readExecutionCourse mais que um\n");
		}
		return null;
	}

	public ICurricularCourseScope readCurricularCourseScopeByUnique(
		ICurricularCourse curricularCourse,
		IBranch branch,
		ICurricularSemester curricularSemester) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
		criteria.addEqualTo("branch.internalID", branch.getIdInternal());
		criteria.addEqualTo("curricularSemester.internalID", curricularSemester.getIdInternal());

		List result = query(CurricularCourseScope.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourseScope) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("CurricularCourseScope: " + result.size());
		}
		return null;
	}

	public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndBranch(ICurricularCourse course, IBranch branch) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourse.idInternal", course.getIdInternal());
		criteria.addEqualTo("branch.internalID", branch.getIdInternal());

		List result = query(CurricularCourseScope.class, criteria);
		if (result.size() < 1) {
			return null;
		}
		// neste caso retorna-se um qualquer mesmo que haja mais que um
		return (ICurricularCourseScope) result.get(0);
	}

	public ICurricularCourseScope readCurricularCourseScopeByCurricularCourse(ICurricularCourse course) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourse.idInternal", course.getIdInternal());

		List result = query(CurricularCourseScope.class, criteria);
		if (result.size() < 1) {
			return null;
		}
		// neste caso retorna-se um qualquer mesmo que haja mais que um
		return (ICurricularCourseScope) result.get(0);
	}

	public ITeacher readTeacher(Integer teacherNumber) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("teacherNumber", teacherNumber);

		List result = query(Teacher.class, criteria);
		if (result.size() == 1) {
			return (ITeacher) result.get(0);
		} else {
			return null;
		}
	}

	public ICurricularCourse readCurricularCourseByCode(String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourseByCode mais que um\n");
		}
		return null;
	}

	public List readAllDegreeCurricularPlans() {
		Query query = new QueryByCriteria(DegreeCurricularPlan.class, null);
		return (List) broker.getCollectionByQuery(query);
	}

	public List readListOfCurricularCoursesByNameAndDegreCurricularPlan(String name, IDegreeCurricularPlan plan) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeCurricularPlan.idInternal", ((DegreeCurricularPlan) plan).getIdInternal());

		List result = query(CurricularCourse.class, criteria);
		if (result.size() < 1) {
			return null;
		} else {
			return result;
		}
	}

	public IStudentCurricularPlan readActiveStudentCurricularPlanByStudentAndDegreeCurricularPlan(
		IStudent student,
		IDegreeCurricularPlan degreeCurricularPlan) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student.idInternal", student.getIdInternal());
		criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
		criteria.addEqualTo("currentState", StudentCurricularPlanState.ACTIVE_OBJ);
		List result = query(StudentCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IStudentCurricularPlan) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readActiveStudentCurricularPlan mais que um");
		}
		return null;

	}
	public ICurricularCourseScope readCurricularCourseScopeByUniqueWithoutBranch(
		ICurricularCourse curricularCourse,
		ICurricularSemester curricularSemester,
		Integer executionYear) {

		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
		criteria.addEqualTo("curricularSemester.internalID", curricularSemester.getIdInternal());
		//criteria.addEqualTo("executionYear", executionYear);

		List result = query(CurricularCourseScope.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourseScope) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("CurricularCourseScope: " + result.size());
		}
		return null;
	}

	public IEnrolmentEvaluation readEnrolmentEvaluationByEvaluationDate(
		IEnrolment enrolment,
		EnrolmentEvaluationType type,
		String grade,
		Date evaluationDate) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
		criteria.addEqualTo("enrolmentEvaluationType", type);
		criteria.addEqualTo("grade", grade);
		if (evaluationDate != null) {
			criteria.addEqualTo("examDate", evaluationDate);
		}

		List result = query(EnrolmentEvaluation.class, criteria);
		if (result.size() == 1) {
			return (IEnrolmentEvaluation) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readEnrolmentByUnique mais que um\n");
		}
		return null;
	}

	public IStudentCurricularPlan readStudentCurricularPlanByStudentAndState(IStudent student, StudentCurricularPlanState studentCurricularPlanState) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student.idInternal", student.getIdInternal());
		criteria.addEqualTo("currentState", studentCurricularPlanState);
		List result = query(StudentCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IStudentCurricularPlan) result.get(0);

		} else if (result.size() > 1) {
			System.out.println("readStudentCurricularPlanByUnique mais que um");
		}

		return null;
	}

	public ICurricularCourse readFirstCurricularCourseByCode(String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() > 0) {
			return (ICurricularCourse) result.get(0);
		} else {
			System.out.println("readFirstCurricularCourseByCode menos que um\n");
		}
		return null;
	}

	public IBranch readBranchByCodeAndDegree(
		String branchCode,
		Integer degreeCode) {
		Criteria criteria = new Criteria();
		if (branchCode != null && branchCode.length() > 0) {
			criteria.addEqualTo("code", branchCode);
		} else {
			criteria.addEqualTo("code", new String());
			//criteria.addLike("code", null);
		}
		criteria.addEqualTo(
			"degreeCurricularPlan.degree.idInternal",
			degreeCode);
		criteria.addLessOrEqualThan(
			"degreeCurricularPlan.idInternal",
			new Integer(23));

		List result = query(Branch.class, criteria);

		if (result.size() == 1) {
			return (IBranch) result.get(0);
		} else {
			System.out.println(
				"Found: ["
					+ result.size()
					+ "] branches for degree ["
					+ degreeCode
					+ "] code ["
					+ branchCode
					+ "]");
			if (result.size() == 2) {
				System.out.println(result.get(0));
				System.out.println(result.get(1));
			}
			return null;
		}
	}

	public List readAllExecutionPeriods() {
		List result = query(ExecutionPeriod.class, null);
		if (result.size() > 0) {
			return result;
		} else {
			return null;
		}
	}

	public Integer readFirstEnrolmentYearOfStudentCurricularPlan(IStudentCurricularPlan oldStudentCurricularPlan) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlanKey", oldStudentCurricularPlan.getIdInternal());
		criteria.addOrderBy("executionPeriod.executionYear.year", true);
		List result = query(Enrolment.class, criteria);
		String yearStr = ((IEnrolment)result.get(0)).getExecutionPeriod().getExecutionYear().getYear(); 
		Integer year = new Integer(yearStr.substring(0,4));
		return year;
	}

	public ICurricularCourseEquivalenceRestriction readCurricularCourseEquivalenceRestrictionByEquivalentCurricularCourse(ICurricularCourse course) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("equivalentCurricularCourse.idInternal", course.getIdInternal());
		List result = query(CurricularCourseEquivalenceRestriction.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourseEquivalenceRestriction) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readCurricularCourseEquivalenceRestrictionByEquivalentCurricularCourse mais que um");
		}

		return null;
	}

	public List readEnrolmentsByCurricularCourseAndStudentCurricularPlan(ICurricularCourse curricularCourse, IStudentCurricularPlan studentCurricularPlan) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourseScope.curricularCourse.idInternal", curricularCourse.getIdInternal());
		criteria.addEqualTo("studentCurricularPlanKey", studentCurricularPlan.getIdInternal());
		List result = query(Enrolment.class, criteria);
		if (result.size() < 0) {
			return null;
		}
		return result;
	}

	public IEnrolmentEquivalence readEnrolmentEquivalenceByUnique(IEnrolment newEnrolment) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("enrolmentKey", newEnrolment.getIdInternal());
		List result = query(EnrolmentEquivalence.class, criteria);
		if (result.size() == 1) {
			return (IEnrolmentEquivalence) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("readEnrolmentEquivalenceByUnique mais que um");
		}
		return null;
	}

	public List readEnrolmentsByCurricularCourseAndStudentCurricularPlanAndAcademicYear(ICurricularCourse curricularCourse, IStudentCurricularPlan studentCurricularPlan, String year) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularCourseScope.curricularCourse.idInternal", curricularCourse.getIdInternal());
		criteria.addEqualTo("studentCurricularPlanKey", studentCurricularPlan.getIdInternal());
		criteria.addEqualTo("executionPeriod.executionYear.year", year);
		List result = query(Enrolment.class, criteria);
		if (result.size() < 0) {
			return null;
		}
		return result;
	}
	/**
	 * @param persistentObject
	 */
	public void delete(Object persistentObject) {
		super.broker.delete(persistentObject);
	}
}