package ServidorPersistente.OJB.util;

import org.apache.commons.beanutils.BeanUtils;

import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import Dominio.Aula;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.IAula;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ISala;
import Dominio.ITurma;
import Dominio.ITurno;
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

	public static InfoExecutionCourse copyinfoExecutionCourse2InfoinfoExecutionCourse(IDisciplinaExecucao executionCourse) {
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
				executionCourse
					.getLicenciaturaExecucao().getCurso());
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

}
