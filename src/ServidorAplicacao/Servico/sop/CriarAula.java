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
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.Servico.sop.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
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
		throws FenixServiceException, ExistingServiceException {

		InfoLessonServiceResult result = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ISala sala =
				sp.getISalaPersistente().readByName(
					infoLesson.getInfoSala().getNome());

			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoLesson
						.getInfoDisciplinaExecucao()
						.getInfoExecutionPeriod());

			IDisciplinaExecucao executionCourse =
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
			boolean resultB = validNoInterceptingLesson(aula);

			if (result.isSUCESS() && resultB) {
				try {
					System.out.println("Before lockWrite");
					sp.getIAulaPersistente().lockWrite(aula);
					System.out.println("After lockWrite");
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
	private boolean validNoInterceptingLesson(IAula lesson) throws ExistingServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IAulaPersistente persistentLesson = sp.getIAulaPersistente();

			List lessonMatchList =
				persistentLesson.readLessonsInBroadPeriod(lesson);

			System.out.println("Tenho aulas:" + lessonMatchList.size());
			
			if (lessonMatchList.size() > 0) {
				System.out.println("lessonMatchList is going to blow up...");
				if (lessonMatchList.contains(lesson)) {
					System.out.println("After contains");
					throw new ExistingServiceException();
				} else {
					System.out.println("Another After contains");
					// TODO: throw new exception : INTERCEPTINGMATCHEXCEPTION 
					return false;
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