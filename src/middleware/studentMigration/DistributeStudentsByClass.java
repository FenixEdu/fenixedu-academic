package middleware.studentMigration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWStudentClass;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWStudentClass;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.Frequenta;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.ShiftStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;
import Util.TipoCurso;

/**
 * @author jpvl
 */

public class DistributeStudentsByClass
{

	/**
	 * @param student
	 * @param group
	 * @param sp
	 * @param spmw
	 */
	private static void applyStudentToClass(IStudent student, ITurma group, ISuportePersistente sp, IPersistentMiddlewareSupport spmw)
		throws ExcepcaoPersistencia
	{
		List groupShiftList = group.getAssociatedShifts();

		List groupShiftListFiltered = (List) CollectionUtils.select(groupShiftList, new Predicate()
		{
			List validTypes =
				Arrays.asList(
					new TipoAula[] { new TipoAula(TipoAula.PRATICA), new TipoAula(TipoAula.TEORICA), new TipoAula(TipoAula.TEORICO_PRATICA)});

			public boolean evaluate(Object input)
			{
				ITurno shift = (ITurno) input;
				return validTypes.contains(shift.getTipo());
			}
		});
		// extrair 1 turno de cada tipo
		Map executionCourseShiftListMap = mapExecutionCourseShiftList(groupShiftListFiltered);

		// jppara cada execução colocar o aluno num turno teorico, pratico e/ou teorico-pratico 
		enrollStudent(student, group, executionCourseShiftListMap, sp, spmw);
	}

	/**
	 * @param studentClass
	 * @param sp
	 */
	private static void distributeStudentClass(
		MWStudentClass studentClass,
		IExecutionPeriod executionPeriod,
		ISuportePersistente sp,
		IPersistentMiddlewareSupport spmw)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{
		String studentNumber = studentClass.getStudentNumber();

		IPersistentStudent studentDAO = sp.getIPersistentStudent();
		ITurmaPersistente groupDAO = sp.getITurmaPersistente();
		IStudent student = studentDAO.readByNumero(new Integer(studentNumber), TipoCurso.LICENCIATURA_OBJ);

		ICursoExecucao executionDegree = getExecutionDegree(studentClass, executionPeriod, sp, spmw);
		ITurma group = groupDAO.readByNameAndExecutionDegreeAndExecutionPeriod(studentClass.getClassName(), executionDegree, executionPeriod);

		if (student == null)
		{
			ReportDistribution.addNotFoundStudent(studentNumber);
		} else
		{
			if (group == null)
			{
				throw new RuntimeException("Cannot find class " + studentClass.getClassName());
			} else
			{
				applyStudentToClass(student, group, sp, spmw);
			}

		}

	}

	/**
	 * @param student
	 * @param shiftsToEnroll
	 * @param sp
	 * @param spmw
	 */
	private static void enrollStudent(
		IStudent student,
		ITurma group,
		Map executionCourseShiftListMap,
		ISuportePersistente sp,
		IPersistentMiddlewareSupport spmw)
		throws ExcepcaoPersistencia
	{
		IFrequentaPersistente attendDAO = sp.getIFrequentaPersistente();

		Iterator executionCourseIterator = executionCourseShiftListMap.keySet().iterator();
		while (executionCourseIterator.hasNext())
		{
			IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) executionCourseIterator.next();
			IFrequenta attend = attendDAO.readByAlunoAndDisciplinaExecucao(student, executionCourse);

			if (attend != null)
			{
				// TODO verify ShiftStudents....
			} else
			{
				attend = new Frequenta();
				attendDAO.simpleLockWrite(attend);
				attend.setAluno(student);
				attend.setDisciplinaExecucao(executionCourse);

				List shiftList = (List) executionCourseShiftListMap.get(executionCourse);

				enrollToShifts(student, group, shiftList, sp);

			}
		}
	}

	/**
	 * @param student
	 * @param shiftList
	 */
	private static void enrollToShifts(IStudent student, ITurma group, List shiftList, ISuportePersistente sp) throws ExcepcaoPersistencia
	{
		ITurnoAlunoPersistente shiftStudentDAO = sp.getITurnoAlunoPersistente();
		ITurnoPersistente shiftDAO = sp.getITurnoPersistente();
		final List availableTypes = new ArrayList();

		Iterator shiftListIterator = shiftList.iterator();
		while (shiftListIterator.hasNext())
		{
			ITurno shift = (ITurno) shiftListIterator.next();
			if (!availableTypes.contains(shift.getTipo()))
			{
				availableTypes.add(shift.getTipo());
			}
		}

		for (int i = 0; i < availableTypes.size(); i++)
		{
			TipoAula lessonType = (TipoAula) availableTypes.get(i);

			ITurno shift = null;
			ITurno shiftAux = null;

			for (int j = 0; j < shiftList.size(); j++)
			{

				shiftAux = (ITurno) shiftList.get(j);

				if (shiftAux.getAvailabilityFinal().intValue() > 0 && shiftAux.getTipo().equals(lessonType))
				{
					shift = shiftAux;
					break;
				}
			}
			if (shift == null)
			{
				shift = getShiftWithLessOcupation(shiftList, lessonType, sp);

				//				IDisciplinaExecucao executionCourse = shiftAux.getDisciplinaExecucao();
				//				ReportDistribution.addCapacityExceed(executionCourse, group.getNome(), lessonType);
				//				throw new RuntimeException("Turno capacity exceed!");
			}
			shiftDAO.simpleLockWrite(shift);
			shift.setAvailabilityFinal(new Integer(shift.getAvailabilityFinal().intValue() - 1));

			ITurnoAluno shiftStudent = new ShiftStudent(shift, student);

			shiftStudentDAO.simpleLockWrite(shiftStudent);
		}
	}

	/**
	 * @param shiftList
	 * @return
	 */
	private static ITurno getShiftWithLessOcupation(List shiftList, TipoAula lessonType, ISuportePersistente sp) throws ExcepcaoPersistencia
	{
		ITurno shift = null;
		ITurnoAlunoPersistente shiftStudentDAO = sp.getITurnoAlunoPersistente();
		int size = 0;
		for (int i = 0; i < shiftList.size(); i++)
		{
			ITurno shiftAux = (ITurno) shiftList.get(i);
			if (shiftAux.getTipo().equals(lessonType))
			{
				if (shift == null)
				{
					shift = (ITurno) shiftList.get(i);
					List studentsList = shiftStudentDAO.readByShift(shift);
					size = studentsList.size();
				} else
				{
					List studentsList = shiftStudentDAO.readByShift((ITurno) shiftList.get(i));
					if (studentsList.size() < size)
					{
						size = studentsList.size();
						shift = (ITurno) shiftList.get(i);
					}
				}
			}
		}
		return shift;
	}

	/**
	 * @param groupShiftListFiltered 
	 */
	private static HashMap mapExecutionCourseShiftList(List groupShiftListFiltered)
	{
		HashMap executionCourseShitsMap = new HashMap();

		Iterator shiftsIterator = groupShiftListFiltered.iterator();
		while (shiftsIterator.hasNext())
		{
			ITurno shift = (ITurno) shiftsIterator.next();
			List shiftList = (List) executionCourseShitsMap.get(shift.getDisciplinaExecucao());
			if (shiftList == null)
			{
				shiftList = new ArrayList();
				executionCourseShitsMap.put(shift.getDisciplinaExecucao(), shiftList);
			}
			shiftList.add(shift);
		}
		return executionCourseShitsMap;
	}

	/**
	 * @param studentClass
	 * @param sp
	 * @return
	 */
	private static ICursoExecucao getExecutionDegree(
		MWStudentClass studentClass,
		IExecutionPeriod executionPeriod,
		ISuportePersistente sp,
		IPersistentMiddlewareSupport spmw)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{
		IPersistentMWDegreeTranslation degreeTranslationDAO = spmw.getIPersistentMWDegreeTranslation();
		MWDegreeTranslation degreeTranslation = degreeTranslationDAO.readByDegreeCode(studentClass.getDegreeCode());
		ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();

		ICursoExecucao executionDegree =
			executionDegreeDAO.readByDegreeCodeAndExecutionYear(degreeTranslation.getDegree().getSigla(), executionPeriod.getExecutionYear());
		return executionDegree;
	}

	public static void main(String args[])
	{
		List studentClassList = null;
		ISuportePersistente sp = null;
		IExecutionPeriod executionPeriod = null;
		IPersistentMiddlewareSupport spmw = null;
		try
		{
			sp = SuportePersistenteOJB.getInstance();

			spmw = PersistentMiddlewareSupportOJB.getInstance();

			IPersistentMWStudentClass studentClassDAO = spmw.getIPersistentMWStudentClass();
			IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
			sp.iniciarTransaccao();
			studentClassList = studentClassDAO.readAll();
			executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
			sp.confirmarTransaccao();
		} catch (Exception e)
		{
			e.printStackTrace(System.out);
			System.exit(1);
		}

		Iterator studentClassIterator = studentClassList.iterator();
		System.out.println("Starting processing " + studentClassList.size() + " students...");
		while (studentClassIterator.hasNext())
		{
			MWStudentClass studentClass = (MWStudentClass) studentClassIterator.next();
			try
			{
				sp.iniciarTransaccao();
				distributeStudentClass(studentClass, executionPeriod, sp, spmw);
				sp.confirmarTransaccao();
			} catch (Exception e1)
			{
				System.out.println("Student:" + studentClass.getStudentNumber());
				sp.clearCache();
				try
				{
					sp.cancelarTransaccao();
				} catch (ExcepcaoPersistencia e2)
				{
					e2.printStackTrace(System.out);
				}
				e1.printStackTrace(System.out);
				return;
			}
		}
		ReportDistribution.fullReport();
	}

}
