/*
 * Created on 17/Set/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.DistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadShiftsByDistributedTest implements IServico {

	private static ReadShiftsByDistributedTest service =
		new ReadShiftsByDistributedTest();

	public static ReadShiftsByDistributedTest getService() {
		return service;
	}

	public String getNome() {
		return "ReadShiftsByDistributedTest";
	}

	public Object run(Integer executionCourseId, Integer distributedTestId)
		throws FenixServiceException {

		ISuportePersistente persistentSuport;
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();

			List studentsList = new ArrayList();
			IDistributedTest distributedTest =
				new DistributedTest(distributedTestId);
			if (distributedTestId != null) //lista de alunos que tem teste
				studentsList =
					persistentSuport
						.getIPersistentStudentTestQuestion()
						.readStudentsByDistributedTest(distributedTest);

			IExecutionCourse executionCourse =
				new ExecutionCourse(executionCourseId);
			executionCourse =
				(IExecutionCourse) persistentSuport
					.getIPersistentExecutionCourse()
					.readByOId(executionCourse, false);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}

			List infoShiftList =
				persistentSuport.getITurnoPersistente().readByExecutionCourse(
					executionCourse);
			Iterator itShiftList = infoShiftList.iterator();

			List result = new ArrayList();
			ITurnoAlunoPersistente turnoAlunoPersistente =
				persistentSuport.getITurnoAlunoPersistente();
			while (itShiftList.hasNext()) {
				ITurno shift = (ITurno) itShiftList.next();
				List shiftStudents = turnoAlunoPersistente.readByShift(shift);
				if (!studentsList.containsAll(shiftStudents))
					result.add(Cloner.copyIShift2InfoShift(shift));
			}
			return result;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}
