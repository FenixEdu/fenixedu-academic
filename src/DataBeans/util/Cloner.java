package DataBeans.util;

import org.apache.commons.beanutils.BeanUtils;

import DataBeans.InfoClass;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoLesson;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoPerson;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import DataBeans.InfoStudent;
import Dominio.Aula;
import Dominio.CurricularCourse;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IAula;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import Dominio.IPlanoCurricularCurso;
import Dominio.ISala;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.MasterDegreeCandidate;
import Dominio.Pessoa;
import Dominio.PlanoCurricularCurso;
import Dominio.Sala;
import Dominio.Student;
import Dominio.Turma;
import Dominio.Turno;

/**
 * @author jpvl
 *
 */
public abstract class Cloner {

	public static ITurno copyInfoShift2Shift(InfoShift infoShift) {
		ITurno shift = new Turno();
		IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoShift.getInfoDisciplinaExecucao());

		copyObjectProperties(shift, infoShift);		

		shift.setDisciplinaExecucao(executionCourse);
		return shift;
	}
	/**
	 * Method copyInfoExecutionCourse2ExecutionCourse.
	 * @param infoExecutionCourse
	 * @return IDisciplinaExecucao
	 */
	public static IDisciplinaExecucao copyInfoExecutionCourse2ExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
		IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
		IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionCourse.getInfoExecutionPeriod());
		
		copyObjectProperties(executionCourse, infoExecutionCourse);
		
		executionCourse.setExecutionPeriod(executionPeriod);
		return executionCourse;
	}

	public static InfoExecutionCourse copyIExecutionCourse2InfoExecutionCourse(IDisciplinaExecucao executionCourse) {
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		InfoExecutionPeriod infoExecutionPeriod = Cloner.copyIExecutionPeriod2InfoExecutionPeriod(executionCourse.getExecutionPeriod());

		copyObjectProperties(infoExecutionCourse, executionCourse);

		infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
		return infoExecutionCourse;
	}

	/**
	 * Method copyInfoCurricularCourse2ICurricularCourse.
	 * @param infoCurricularCourse
	 * @return ICurricularCourse
	 */
	public static ICurricularCourse copyInfoCurricularCourse2ExecutionCourse(InfoCurricularCourse infoCurricularCourse) {
		ICurricularCourse curricularCourse = new CurricularCourse();
		IPlanoCurricularCurso planoCurricularCurso =
			copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(
				infoCurricularCourse.getInfoDegreeCurricularPlan());
		
		copyObjectProperties(curricularCourse, infoCurricularCourse);
		
		curricularCourse.setDegreeCurricularPlan(planoCurricularCurso);
		return curricularCourse;
	}

	public static InfoCurricularCourse copyICurricularCourse2InfoCurricularCourse(ICurricularCourse curricularCourse) {
		InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = 
			copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(curricularCourse.getDegreeCurricularPlan());

		copyObjectProperties(infoCurricularCourse, curricularCourse);

		infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		return infoCurricularCourse;
	}

	/**
	 * Method copyInfoLesson2Lesson.
	 * @param lessonExample
	 * @return IAula
	 */
	public static IAula copyInfoLesson2Lesson(InfoLesson infoLesson) {
		IAula lesson = new Aula();
		ISala sala = null;

		try {
			BeanUtils.copyProperties(lesson, infoLesson);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}

		sala = Cloner.copyInfoRoom2Room(infoLesson.getInfoSala());
		lesson.setSala(sala);

		return lesson;
	}
	/**
	 * Method copyInfoRoom2Room.
	 * @param infoRoom
	 */
	public static ISala copyInfoRoom2Room(InfoRoom infoRoom) {
		ISala room = new Sala();
		copyObjectProperties(room, infoRoom);
		return room;
	}
	/**
	 * 
	 * @param room
	 * @return IAula
	 */
	public static InfoRoom copyRoom2InfoRoom(ISala room) {
		InfoRoom infoRoom = new InfoRoom();
		
		copyObjectProperties(infoRoom, room);
		
		return infoRoom;
	}

	/**
	 * Method copyInfoLesson2Lesson.
	 * @param lessonExample
	 * @return IAula
	 */
	public static InfoLesson copyILesson2InfoLesson(IAula lesson) {
		InfoLesson infoLesson = new InfoLesson();
		InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(lesson.getDisciplinaExecucao());
		
		InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(lesson.getSala());

		copyObjectProperties(infoLesson, lesson);

		infoLesson.setInfoSala(infoRoom);
		infoLesson.setInfoDisciplinaExecucao(infoExecutionCourse);
		return infoLesson;
	}

	/**
	 * Method copyInfoShift2Shift.
	 * @param infoShift
	 * @return ITurno
	 */
	public static ITurno copyInfoShift2IShift(InfoShift infoShift) {
		ITurno shift = new Turno();
		IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoShift.getInfoDisciplinaExecucao());

		copyObjectProperties(shift, infoShift);

		shift.setDisciplinaExecucao(executionCourse);

		return shift;
	}
	/**
	 * Method copyInfoShift2Shift.
	 * @param infoShift
	 * @return ITurno
	 */
	public static InfoShift copyShift2InfoShift(ITurno shift) {
		InfoShift infoShift = new InfoShift();

		InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(shift.getDisciplinaExecucao());

		copyObjectProperties(infoShift, shift);

		infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

		return infoShift;
	}

	/**
	 * Method copyInfoShift2Shift.
	 * @param infoShift
	 * @return ITurno
	 */
	public static InfoClass copyClass2InfoClass(ITurma classD) {
		InfoClass infoClass = new InfoClass();
		InfoExecutionDegree infoExecutionDegree = Cloner.copyIExecutionDegree2InfoExecutionDegree(classD.getExecutionDegree());
		InfoExecutionPeriod infoExecutionPeriod = Cloner.copyIExecutionPeriod2InfoExecutionPeriod(classD.getExecutionPeriod());
		
		copyObjectProperties(infoClass, classD);
		
		infoClass.setInfoExecutionDegree(infoExecutionDegree);
		infoClass.setInfoExecutionPeriod(infoExecutionPeriod);
		return infoClass;
	}
	/**
	 * Method copyIExecutionPeriod2InfoExecutionPeriod.
	 * @param iExecutionPeriod
	 * @return InfoExecutionPeriod
	 */
	public static InfoExecutionPeriod copyIExecutionPeriod2InfoExecutionPeriod(IExecutionPeriod executionPeriod) {
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
		InfoExecutionYear infoExecutionYear = Cloner.copyIExecutionYear2InfoExecutionYear(executionPeriod.getExecutionYear());
		
		copyObjectProperties(infoExecutionPeriod, executionPeriod);
		
		infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
		return infoExecutionPeriod;
	}
	
	
	private static void copyObjectProperties(
		Object destination,
		Object source) {
		if (source != null)
			try {
				BeanUtils.copyProperties(destination, source);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	}
	
	/**
	 * 
	 * @param infoExecutionDegree
	 * @return ICursoExecucao
	 */
	public static ICursoExecucao copyInfoExecutionDegree2ExecutionDegree(InfoExecutionDegree infoExecutionDegree) {

		ICursoExecucao executionDegree = new CursoExecucao();
		IPlanoCurricularCurso degreeCurricularPlan = Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoExecutionDegree.getInfoDegreeCurricularPlan());
		
		IExecutionYear executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionDegree.getInfoExecutionYear());
		
		copyObjectProperties(executionDegree, infoExecutionDegree);
		
		executionDegree.setExecutionYear(executionYear);
		executionDegree.setCurricularPlan(degreeCurricularPlan);

		return executionDegree;

	}
	/**
	 * Method copyInfoDegreeCurricularPlan2IDegreeCurricularPlan.
	 * @param infoDegreeCurricularPlan
	 * @return IPlanoCurricularCurso
	 */
	public static IPlanoCurricularCurso copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
		IPlanoCurricularCurso degreeCurricularPlan =
			new PlanoCurricularCurso();

		ICurso degree =
			Cloner.copyInfoDegree2IDegree(infoDegreeCurricularPlan.getInfoDegree());
		try {
			BeanUtils.copyProperties(
			degreeCurricularPlan, infoDegreeCurricularPlan
				);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		degreeCurricularPlan.setCurso(degree);

		return degreeCurricularPlan;
		
		
	}

	public static InfoExecutionDegree copyIExecutionDegree2InfoExecutionDegree(ICursoExecucao executionDegree) {

		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();

		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(
				executionDegree.getCurricularPlan());

		InfoExecutionYear infoExecutionYear =
			Cloner.copyIExecutionYear2InfoExecutionYear(
				executionDegree.getExecutionYear());
		try {
			BeanUtils.copyProperties(infoExecutionDegree, executionDegree);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}

		infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
		infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		

		return infoExecutionDegree;

	}
	/**
	 * Method copyInfoExecutionYear2IExecutionYear.
	 * @param infoExecutionYear
	 * @return IExecutionYear
	 */
	public static IExecutionYear copyInfoExecutionYear2IExecutionYear(InfoExecutionYear infoExecutionYear) {
		IExecutionYear executionYear = new ExecutionYear();
		try {
			BeanUtils.copyProperties(executionYear, infoExecutionYear);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		return executionYear;
	}
	/**
	 * Method copyInfoExecutionYear2IExecutionYear.
	 * @param infoExecutionYear
	 * @return IExecutionYear
	 */
	public static InfoExecutionYear copyIExecutionYear2InfoExecutionYear(IExecutionYear executionYear) {
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		copyObjectProperties(infoExecutionYear, executionYear);
		return infoExecutionYear;
	}

	public static InfoDegreeCurricularPlan copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(IPlanoCurricularCurso degreeCurricularPlan) {
		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			new InfoDegreeCurricularPlan();

		InfoDegree infoDegree =
			Cloner.copyIDegree2InfoDegree(degreeCurricularPlan.getCurso());
		try {
			BeanUtils.copyProperties(
				infoDegreeCurricularPlan,
				degreeCurricularPlan);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		infoDegreeCurricularPlan.setInfoDegree(infoDegree);

		return infoDegreeCurricularPlan;
	}
	/**
	 * Method copyIDegree2InfoDegree.
	 * @param iCurso
	 * @return InfoDegree
	 */
	public static InfoDegree copyIDegree2InfoDegree(ICurso degree) {
		InfoDegree infoDegree = new InfoDegree();
		try {
			BeanUtils.copyProperties(infoDegree, degree);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return infoDegree;
	}
	/**
	 * Method copyInfoDegree2IDegree.
	 * @param infoDegree
	 * @return ICurso
	 */
	public static ICurso copyInfoDegree2IDegree(InfoDegree infoDegree) {
		ICurso degree = new Curso();
		try {
			BeanUtils.copyProperties(degree, infoDegree);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return degree;
		
	}
	/**
	 * Method copyInfoExecutionPeriod2IExecutionPeriod.
	 * @param infoExecutionPeriod
	 * @return IExecutionPeriod
	 */
	public static IExecutionPeriod copyInfoExecutionPeriod2IExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
		
		IExecutionPeriod executionPeriod = new ExecutionPeriod();
		
		IExecutionYear executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionPeriod.getInfoExecutionYear());
		
		copyObjectProperties(executionPeriod, infoExecutionPeriod);
		
		executionPeriod.setExecutionYear(executionYear);
		
		return executionPeriod;
	}
	/**
	 * Method copyInfoClass2Class.
	 * @param infoTurma
	 * @return ITurma
	 */
	public static ITurma copyInfoClass2Class(InfoClass infoClass) {
		ITurma domainClass = new Turma();
		
		IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoClass.getInfoExecutionPeriod());
		ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoClass.getInfoExecutionDegree());
		
		copyObjectProperties(domainClass, infoClass);
		
		domainClass.setExecutionDegree(executionDegree);
		domainClass.setExecutionPeriod(executionPeriod);
		return domainClass;
	}
	/**
	 * Method copyIShift2InfoShift.
	 * @param elem
	 * @return Object
	 */
	public static InfoShift copyIShift2InfoShift(ITurno shift) {
		InfoShift infoShift = new InfoShift();
		InfoExecutionCourse infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(shift.getDisciplinaExecucao());
		copyObjectProperties(infoShift, shift);
		infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
		return infoShift;
	}
	/**
	 * Method copyInfoStudent2IStudent.
	 * @param infoStudent
	 * @return IStudent
	 */
	public static IStudent copyInfoStudent2IStudent(InfoStudent infoStudent) {
		IStudent student = new Student();
		IPessoa person = Cloner.copyInfoPerson2IPerson(infoStudent.getInfoPerson());
		copyObjectProperties(student, infoStudent);
		student.setPerson(person);
		
		return student;
	}
	
	
	/**
	 * Method copyIStudent2InfoStudent.
	 * @param elem
	 * @return Object
	 */
	public static InfoStudent copyIStudent2InfoStudent(IStudent student) {
		InfoStudent infoStudent = new InfoStudent();
		copyObjectProperties(infoStudent, student);
		infoStudent.setInfoPerson(Cloner.copyIPerson2InfoPerson(student.getPerson()));
		return infoStudent;
	}
	
	
	
	
	/**
	 * Method copyInfoPerson2IPerson.
	 * @param infoPerson
	 * @return IPessoa
	 */
	public static IPessoa copyInfoPerson2IPerson(InfoPerson infoPerson) {
		IPessoa person = new Pessoa();
		copyObjectProperties(person, infoPerson);
		return person;
	}


	/**
	 * Method copyInfoPerson2IPerson.
	 * @param infoPerson
	 * @return IPessoa
	 */
	public static InfoPerson copyIPerson2InfoPerson(IPessoa person) {
		InfoPerson infoPerson = new InfoPerson();
		copyObjectProperties(infoPerson, person);
		return infoPerson;
	}


	/**
	 * Method copyInfoMasterDegreeCandidate2IMasterDegreCandidate
	 * @param infoMasterDegreeCandidate
	 * @return IMasterDegreeCandidate
	 */
	public static IMasterDegreeCandidate copyInfoMasterDegreeCandidate2IMasterDegreCandidate(InfoMasterDegreeCandidate infoMasterDegreeCandidate) {
		IMasterDegreeCandidate masterDegreeCandidate = new MasterDegreeCandidate();
		copyObjectProperties(masterDegreeCandidate, infoMasterDegreeCandidate);
		return masterDegreeCandidate;
	}

	/**
	 * Method copyIMasterDegreeCandidate2InfoMasterDegreCandidate
	 * @param masterDegreeCandidate
	 * @return InfoMasterDegreeCandidate 
	 */
	public static InfoMasterDegreeCandidate copyIMasterDegreeCandidate2InfoMasterDegreCandidate(IMasterDegreeCandidate masterDegreeCandidate) {
		InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();
		copyObjectProperties(infoMasterDegreeCandidate, masterDegreeCandidate);
		InfoDegree infoDegree = Cloner.copyIDegree2InfoDegree(masterDegreeCandidate.getDegree());
		infoMasterDegreeCandidate.setInfoDegree(infoDegree);
		infoMasterDegreeCandidate.setCountry(masterDegreeCandidate.getCountry().getName());
		infoMasterDegreeCandidate.setIdentificationDocumentType(masterDegreeCandidate.getIdentificationDocumentType().toString2());
		return infoMasterDegreeCandidate;
	}

}
