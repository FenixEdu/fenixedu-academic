/*
 * Created on 20/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadShiftsByExecutionCourseCode implements IServico {

	private static ReadShiftsByExecutionCourseCode service =
		new ReadShiftsByExecutionCourseCode();

	public static ReadShiftsByExecutionCourseCode getService() {

		return service;
	}

	public ReadShiftsByExecutionCourseCode() {
	}

	public String getNome() {
		return "ReadShiftsByExecutionCourseCode";
	}

	public List run(Integer executionCourseId) throws FenixServiceException {
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSuport.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(executionCourseId);
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}

			List infoShiftList =
				persistentSuport.getITurnoPersistente().readByExecutionCourse(
					executionCourse);
			Iterator itShiftList = infoShiftList.iterator();
			List result = new ArrayList();
			while (itShiftList.hasNext()) {
				ITurno shift = (ITurno) itShiftList.next();
				InfoShift infoTurno = Cloner.copyIShift2InfoShift(shift);

				List lessons = shift.getAssociatedLessons();
				Iterator itLessons = lessons.iterator();

				List infoLessons = new ArrayList();
				InfoLesson infoLesson;

				while (itLessons.hasNext()) {
					infoLesson =
						Cloner.copyILesson2InfoLesson((IAula) itLessons.next());
					infoLessons.add(infoLesson);
				}

				infoTurno.setInfoLessons(infoLessons);
				result.add(infoTurno);
			}
			return result;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
