package DataBeans.util;

import org.apache.commons.beanutils.BeanUtils;

import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import Dominio.Aula;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.ExecutionYear;
import Dominio.IAula;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionYear;
import Dominio.IPlanoCurricularCurso;
import Dominio.ISala;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.PlanoCurricularCurso;
import Dominio.Sala;
import Dominio.Turno;

/**
 * @author jpvl
 *
 */
public abstract class Cloner {

	public static ITurno copyInfoShift2Shift(InfoShift infoShift) {
		ITurno shift = new Turno();
		IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
		ICursoExecucao executionDegree = new CursoExecucao();
		ICurso degree = new Curso();
		InfoExecutionCourse infoExecutionCourse =
			infoShift.getInfoDisciplinaExecucao();
		try {
			BeanUtils.copyProperties(shift, infoShift);
			BeanUtils.copyProperties(executionCourse, infoExecutionCourse);
			BeanUtils.copyProperties(
				executionDegree,
				infoExecutionCourse.getInfoLicenciaturaExecucao());
			BeanUtils.copyProperties(
				degree,
				infoExecutionCourse
					.getInfoLicenciaturaExecucao()
					.getInfoLicenciatura());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}

		executionCourse.setLicenciaturaExecucao(executionDegree);
		executionDegree.setCurso(degree);

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
		ICursoExecucao executionDegree = new CursoExecucao();
		ICurso degree = new Curso();
		try {
			BeanUtils.copyProperties(executionCourse, infoExecutionCourse);
			BeanUtils.copyProperties(
				executionDegree,
				infoExecutionCourse.getInfoLicenciaturaExecucao());
			BeanUtils.copyProperties(
				degree,
				infoExecutionCourse
					.getInfoLicenciaturaExecucao()
					.getInfoLicenciatura());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}

		executionCourse.setLicenciaturaExecucao(executionDegree);
		executionDegree.setCurso(degree);

		return executionCourse;
	}

	public static InfoExecutionCourse copyIExecutionCourse2InfoinfoExecutionCourse(IDisciplinaExecucao executionCourse) {
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		InfoDegree infoDegree = new InfoDegree();
		try {
			BeanUtils.copyProperties(infoExecutionCourse, executionCourse);
			BeanUtils.copyProperties(
				infoExecutionDegree,
				executionCourse.getLicenciaturaExecucao());
			BeanUtils.copyProperties(
				infoDegree,
				executionCourse.getLicenciaturaExecucao().getCurso());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}

		infoExecutionCourse.setInfoLicenciaturaExecucao(infoExecutionDegree);
		infoExecutionDegree.setInfoLicenciatura(infoDegree);

		return infoExecutionCourse;
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
	private static ISala copyInfoRoom2Room(InfoRoom infoRoom) {
		ISala room = new Sala();

		try {
			if (infoRoom == null)
				room = null;
			else
				BeanUtils.copyProperties(room, infoRoom);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}
		return room;
	}
	/**
	 * 
	 * @param room
	 * @return IAula
	 */
	public static InfoRoom copyRoom2InfoRoom(ISala room) {
		InfoRoom infoRoom = new InfoRoom();

		try {
			if (room == null)
				infoRoom = null;
			else
				BeanUtils.copyProperties(infoRoom, room);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}
		return infoRoom;
	}

	/**
	 * Method copyInfoLesson2Lesson.
	 * @param lessonExample
	 * @return IAula
	 */
	public static InfoLesson copyLesson2InfoLesson(IAula lesson) {
		InfoLesson infoLesson = new InfoLesson();
		InfoRoom infoRoom = null;

		try {
			BeanUtils.copyProperties(infoLesson, lesson);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}

		infoRoom = Cloner.copyRoom2InfoRoom(lesson.getSala());
		infoLesson.setInfoSala(infoRoom);
		return infoLesson;
	}

	/**
	 * Method copyInfoShift2Shift.
	 * @param infoShift
	 * @return ITurno
	 */
	public static ITurno copyInfoShift2IShift(InfoShift infoShift) {
		ITurno shift = new Turno();

		try {
			BeanUtils.copyProperties(shift, infoShift);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}
		IDisciplinaExecucao executionCourse =
			copyInfoExecutionCourse2ExecutionCourse(
				infoShift.getInfoDisciplinaExecucao());

		shift.setDisciplinaExecucao(executionCourse);
		return shift;
	}
	/**
	 * Method copyInfoShift2Shift.
	 * @param infoShift
	 * @return ITurno
	 */
	public static ITurno copyShift2InfoShift(ITurno shift) {
		InfoShift infoShift = new InfoShift();

		try {
			BeanUtils.copyProperties(infoShift, shift);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}
		IDisciplinaExecucao executionCourse =
			copyInfoExecutionCourse2ExecutionCourse(
				infoShift.getInfoDisciplinaExecucao());

		shift.setDisciplinaExecucao(executionCourse);
		return shift;
	}

	/**
	 * Method copyInfoShift2Shift.
	 * @param infoShift
	 * @return ITurno
	 */
	public static InfoClass copyClass2InfoClass(ITurma classD) {
		InfoClass infoClass = new InfoClass();
		InfoDegree infoDegree = new InfoDegree();
		try {
			BeanUtils.copyProperties(infoClass, classD);
			BeanUtils.copyProperties(infoDegree, classD.getLicenciatura());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}
		infoClass.setInfoLicenciatura(infoDegree);
		return infoClass;
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
		try {

			BeanUtils.copyProperties(executionDegree, infoExecutionDegree);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}

		executionDegree.setExecutionYear(executionYear);
		executionDegree.setCurricularPlan(degreeCurricularPlan);

		return executionDegree;

	}
	/**
	 * Method copyInfoDegreeCurricularPlan2IDegreeCurricularPlan.
	 * @param infoDegreeCurricularPlan
	 * @return IPlanoCurricularCurso
	 */
	private static IPlanoCurricularCurso copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
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
		try {
			BeanUtils.copyProperties(executionYear, infoExecutionYear);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
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
	private static InfoDegree copyIDegree2InfoDegree(ICurso degree) {
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
	private static ICurso copyInfoDegree2IDegree(InfoDegree infoDegree) {
		ICurso degree = new Curso();
		try {
			BeanUtils.copyProperties(degree, infoDegree);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return degree;
		
	}

}
