/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.thor;

import java.util.ArrayList;
import java.util.List;

import middleware.PersistentObjectOJB;
import middleware.almeida.Almeida_coddisc;
import middleware.almeida.Almeida_curram;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularSemester;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.DisciplinaExecucao;
import Dominio.ExecutionPeriod;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularSemester;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class PersistentObjectOJBReader extends PersistentObjectOJB {

	public Almeida_coddisc readAlmeidaCoddisc(String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coddis", code);
		List result = query(Almeida_coddisc.class, criteria);
		//System.out.println("result.size" + result.size());
		if (result.size() > 0) {
			return (Almeida_coddisc) result.get(0);
		} else {
			System.out.println("Coddis=" + code);
			return null;
		}
	}

	public ICurricularCourse readCurricularCourse(
		Integer degreeID,
		String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degreeCurricularPlan", degreeID);
		criteria.addEqualTo("code", code);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println(
				"[readByDegreeAndCode] Degree= "
					+ degreeID
					+ "  Code= "
					+ code);
			return null;
		} else {
			return null;
		}
	}

	public ICurricularCourse correctCurricularCourseCode(
		Integer degreeID,
		String code) {
		String ourFunnyCode = code;
		if (ourFunnyCode.charAt(0) == ' ') {
			ourFunnyCode = ourFunnyCode.substring(1);
			try {
				Integer temp = new Integer(ourFunnyCode);
				ourFunnyCode = temp.toString();
			} catch (NumberFormatException ex) {
				// Leave ourFunnyCode as is.
			}
		}

		Criteria criteria = new Criteria();
		criteria.addEqualTo("degreeCurricularPlan", degreeID);
		criteria.addEqualTo("code", ourFunnyCode);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			ICurricularCourse curricularCourse =
				(ICurricularCourse) result.get(0);
			curricularCourse.setCode(code);
			lockWrite(curricularCourse);
			return curricularCourse;
		} else if (result.size() > 1) {
			System.out.println(
				"[readByDegreeAndCorrectedCode] Degree= "
					+ degreeID
					+ "  Code= "
					+ code);
			return null;
		} else {
			return null;
		}
	}

	public ICurricularCourse readCurricularCourse(
		String name,
		Integer degreeID,
		String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeCurricularPlan", degreeID);
		//criteria.addEqualTo("code", code);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			//			System.out.println("[" + code + "]");
			//			System.out.println(
			//				"[" + ((ICurricularCourse) result.get(0)).getCode() + "]");

			ICurricularCourse curricularCourse =
				(ICurricularCourse) result.get(0);
			//curricularCourse.setCode(code);
			//lockWrite(curricularCourse);

			//			System.out.println(name);
			//			System.out.println(((ICurricularCourse) result.get(0)).getName());

			return curricularCourse;
		} else if (result.size() > 1) {
			System.out.println(
				"[readByDegreeAndName] Degree= "
					+ degreeID
					+ "  Name= "
					+ name);
			return null;
		} else {
			return null;
		}
	}

	public ICurricularSemester readCurricularSemester(
		Integer year,
		Integer semester) {
		Criteria criteria = new Criteria();
		if (semester.equals(new Integer(2))) {
			criteria.addEqualTo("internalID", new Integer(year.intValue() * 2));
		} else {
			criteria.addEqualTo(
				"internalID",
				new Integer(year.intValue() * 2 - semester.intValue()));
		}
		List result = query(CurricularSemester.class, criteria);
		if (result.size() == 1) {
			return (ICurricularSemester) result.get(0);
		} else {
			System.out.println(
				"ERROR READING CURRICULAR SEMESTER: "
					+ year.intValue() * 2
					+ semester.intValue());
			return null;
		}
	}

	public List readThorAtribuicaoDisciplinas() {
		return query(Atribuicaodisciplinas.class, null);
	}

	public List readThorAulas() {
		return query(Aulas.class, null);
	}

	public List readThorDisciplinas() {
		return query(Disciplinas.class, null);
	}

	public List readThorTurmas() {
		return query(Turmas.class, null);
	}

	public List readThorRooms() {
		List salas = new ArrayList();

		List aulas = query(Aulas.class, null);

		for (int i = 0; i < aulas.size(); i++) {
			String sala = ((Aulas) aulas.get(i)).getSala();
			if (!(salas.contains(sala))) {
				salas.add(sala);
			}
		}
		return salas;
	}

	public List readThorAulas(int dia, int hora, String sala) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("dia", new Integer(dia));
		criteria.addEqualTo("hora", new Integer(hora));
		criteria.addEqualTo("sala", sala);
		return query(Aulas.class, criteria);
	}

	public ICurso readDegree(Integer code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("idInternal", code);
		List result = query(Curso.class, criteria);
		if (result.size() == 1) {
			return (ICurso) result.get(0);
		} else {
			System.out.println("ERROR READING DEGREE: " + code);
			return null;
		}
	}

	public Disciplinas readDisciplina(String sigla) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("sigla", sigla);
		List result = query(Disciplinas.class, criteria);
		if (result.size() == 1) {
			return (Disciplinas) result.get(0);
		} else {
			System.out.println("ERROR READING Disciplina: " + sigla);
			return null;
		}

	}

	public IExecutionCourse readExecutionCourse(String sigla) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("sigla", sigla);
		criteria.addEqualTo("keyExecutionPeriod", new Integer(2));
		List result = query(DisciplinaExecucao.class, criteria);
		if (result.size() == 1) {
			return (IExecutionCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println(
				"ERROR READING EXECUTION_COURSE: " + sigla);
			return null;
		} else {
			return null;
		}

	}

	/**
	 * @param idDegree
	 * @param string
	 * @return
	 */
	public IDegreeCurricularPlan readDegreeCurricularPlan(
		String idDegree,
		String yearString) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", idDegree + yearString);
		List result = query(DegreeCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IDegreeCurricularPlan) result.get(0);
		} else {
			System.out.println(
				"NON EXISTING DEGREE CURRICULAR PLAN: "
					+ idDegree
					+ yearString);
			return null;
		}
	}

	/**
	 * @param almeida_disc
	 * @return
	 */
	public Almeida_curram readAlmeida_Curram(Integer codCur, Integer codRam) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("codcur", codCur);
		criteria.addEqualTo("codram", codRam);
		List result = query(Almeida_curram.class, criteria);
		if (result.size() >= 1) {
			return (Almeida_curram) result.get(0);
		} else {
			System.out.println(
				"NON EXISTING BRANCH: codCur= "
					+ codCur
					+ " - codRam= "
					+ codRam);
			return null;
		}
	}

	/**
	 * @param degreeCurricularPlan
	 * @param string
	 * @return
	 */
	public ICurricularCourse readCurricularCourse(
		IDegreeCurricularPlan degreeCurricularPlan,
		String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		criteria.addEqualTo(
			"degreeCurricularPlanKey",
			degreeCurricularPlan.getIdInternal());
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (CurricularCourse) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	public IBranch readBranch(String name, String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		criteria.addEqualTo("name", name);
		List result = query(Branch.class, criteria);
		if (result.size() == 1) {
			return (IBranch) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @return
	 */
	public IExecutionPeriod readExecutionPeriod() {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("idInternal", new Integer(2));
		List result = query(ExecutionPeriod.class, criteria);
		if (result.size() == 1) {
			return (IExecutionPeriod) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param string
	 * @return
	 */
	public IDegreeCurricularPlan readDegreeCurricularPlanNextSemester(String idDegree) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degreeKey", new Integer(idDegree));
		criteria.addLike("name", "%2003/2004");
		List result = query(DegreeCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IDegreeCurricularPlan) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param degreeCurricularPlan
	 * @param disciplina
	 * @return
	 */
	public ICurricularCourse getCurricularCourse(
		IDegreeCurricularPlan degreeCurricularPlan,
		Disciplinas disciplina) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"degreeCurricularPlanKey",
			((DegreeCurricularPlan) degreeCurricularPlan).getIdInternal());
		//criteria.addEqualTo("code", disciplina.getSigla());
		criteria.addEqualTo("name", disciplina.getNome());
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println(
				"To many matches for course: " + disciplina.getSigla());
			return null;
		} else {
			System.out.println(
				"Could not match course: "
					+ disciplina.getSigla()
					+ " name: "
					+ disciplina.getNome()
					+ " plan: "
					+ degreeCurricularPlan.getName());
			return null;
		}
	}

	public ICurricularCourse getCurricularCourse(
		IDegreeCurricularPlan degreeCurricularPlan,
		String name) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"degreeCurricularPlanKey",
			((DegreeCurricularPlan) degreeCurricularPlan).getIdInternal());
		//criteria.addEqualTo("code", disciplina.getSigla());
		criteria.addEqualTo("name", name);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println(
				"To many matches for course: " + name);
			return null;
		} else {
			System.out.println(
				"Could not match course: "
					+ " name: "
					+ name
					+ " plan: "
					+ degreeCurricularPlan.getName());
			return null;
		}
	}


}