/*
 * CriarAula.java
 *
 * Created on 26 de Outubro de 2002, 15:09
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarAula.
 *
 * @author tfc130
 **/

import java.util.List;

import DataBeans.InfoLesson;
import DataBeans.InfoLessonServiceResult;
import DataBeans.util.Cloner;
import Dominio.Aula;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InterceptingServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidTimeIntervalServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class CriarAula implements IServico {

	private static CriarAula _servico = new CriarAula();
	/**
	 * The singleton access method of this class.
	 **/
	public static CriarAula getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CriarAula() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "CriarAula";
	}

	public InfoLessonServiceResult run(InfoLesson infoLesson)
		throws FenixServiceException {

		InfoLessonServiceResult result = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ISala sala =
				sp.getISalaPersistente().readByName(
					infoLesson.getInfoSala().getNome());

			System.out.println("infoLesson.getInfoSala().getNome()= " + infoLesson.getInfoSala().getNome());
			System.out.println("sala= " + sala);

			IPersistentExecutionCourse executionCourseDAO =
				sp.getIPersistentExecutionCourse();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoLesson
						.getInfoDisciplinaExecucao()
						.getInfoExecutionPeriod());

			IExecutionCourse executionCourse =
				executionCourseDAO
					.readByExecutionCourseInitialsAndExecutionPeriod(
					infoLesson.getInfoDisciplinaExecucao().getSigla(),
					executionPeriod);

			IAula aula =
				new Aula(
					infoLesson.getDiaSemana(),
					infoLesson.getInicio(),
					infoLesson.getFim(),
					infoLesson.getTipo(),
					sala,
					executionCourse);

			result = validTimeInterval(aula);
			if (result.getMessageType() == 1) {
				throw new InvalidTimeIntervalServiceException();
			}

			boolean resultB = validNoInterceptingLesson(aula, executionPeriod);

			if (result.isSUCESS() && resultB) {
				try {
					sp.getIAulaPersistente().lockWrite(aula);
				} catch (ExistingPersistentException ex) {

					throw new ExistingServiceException(ex);
				}
			} else {
				result.setMessageType(2);
			}

		} catch (ExcepcaoPersistencia ex) {

			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

	/**
	 * @param aula
	 * @return InfoLessonServiceResult
	 */
	private boolean validNoInterceptingLesson(
		IAula lesson,
		IExecutionPeriod executionPeriod)
		throws ExistingServiceException, InterceptingServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IAulaPersistente persistentLesson = sp.getIAulaPersistente();

			List lessonMatchList =
				persistentLesson.readLessonsInBroadPeriod(
					lesson,
					null,
					executionPeriod);

			if (lessonMatchList.size() > 0) {
				if (lessonMatchList.contains(lesson)) {
					throw new ExistingServiceException();
				} else {
					throw new InterceptingServiceException();
				}
			} else {
				return true;
			}
		} catch (ExcepcaoPersistencia e) {
			return false;
		}
	}

	private InfoLessonServiceResult validTimeInterval(IAula lesson) {
		InfoLessonServiceResult result = new InfoLessonServiceResult();

		if (lesson.getInicio().getTime().getTime()
			>= lesson.getFim().getTime().getTime()) {
			result.setMessageType(
				InfoLessonServiceResult.INVALID_TIME_INTERVAL);
		}

		return result;
	}

}