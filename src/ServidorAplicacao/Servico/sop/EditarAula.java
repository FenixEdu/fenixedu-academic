/*
 * EditarAula.java
 *
 * Created on 27 de Outubro de 2002, 19:05
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o EditarAula.
 *
 * @author tfc130
 **/
import java.util.List;

import DataBeans.InfoLesson;
import DataBeans.InfoLessonServiceResult;
import DataBeans.KeyLesson;
import Dominio.Aula;
import Dominio.IAula;
import Dominio.ISala;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.sop.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.sop.exceptions.InterceptingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditarAula implements IServico {

	private static EditarAula _servico = new EditarAula();
	/**
	 * The singleton access method of this class.
	 **/
	public static EditarAula getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private EditarAula() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "EditarAula";
	}

	public Object run(KeyLesson aulaAntiga, InfoLesson aulaNova)
		throws FenixServiceException {

		IAula aula = null;
		InfoLessonServiceResult result = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ISala salaAntiga =
				sp.getISalaPersistente().readByName(
					aulaAntiga.getKeySala().getNomeSala());
			ISala salaNova =
				sp.getISalaPersistente().readByName(
					aulaNova.getInfoSala().getNome());
			aula =
				sp.getIAulaPersistente().readByDiaSemanaAndInicioAndFimAndSala(
					aulaAntiga.getDiaSemana(),
					aulaAntiga.getInicio(),
					aulaAntiga.getFim(),
					salaAntiga);

			IAula newLesson =
				new Aula(
					aulaNova.getDiaSemana(),
					aulaNova.getInicio(),
					aulaNova.getFim(),
					aulaNova.getTipo(),
					salaNova,
					null);

			if (aula != null) {
				result = valid(newLesson);
				boolean resultB = validNoInterceptingLesson(newLesson, aula);

				if (result.isSUCESS() && resultB) {
					aula.setDiaSemana(aulaNova.getDiaSemana());
					aula.setInicio(aulaNova.getInicio());
					aula.setFim(aulaNova.getFim());
					aula.setTipo(aulaNova.getTipo());
					aula.setSala(salaNova);
				} 
			}

		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

	private InfoLessonServiceResult valid(IAula lesson) {
		InfoLessonServiceResult result = new InfoLessonServiceResult();

		if (lesson.getInicio().getTime().getTime()
			>= lesson.getFim().getTime().getTime()) {
			result.setMessageType(
				InfoLessonServiceResult.INVALID_TIME_INTERVAL);
		}

		return result;
	}

	/**
		   * @param aula
		   * @return InfoLessonServiceResult
		   */
	/*	private boolean validNoInterceptingLesson(IAula lesson) {
	
			try {
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
	
				IAulaPersistente persistentLesson = sp.getIAulaPersistente();
	
				List lessonMatchList =
					persistentLesson.readLessonsInBroadPeriod(lesson);
	
				System.out.println("Tenho aulas:" + lessonMatchList.size());
				
				if ((lessonMatchList.size() >0 && !lessonMatchList.contains(lesson)) || (lessonMatchList.size() >1 && lessonMatchList.contains(lesson))) {
					
					return false;
				} else {
					return true;
				}
			} catch (ExcepcaoPersistencia e) {
				return false;
	
			}
		}
	*/

	private boolean validNoInterceptingLesson(IAula newLesson, IAula oldLesson)
		throws ExistingServiceException, InterceptingServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IAulaPersistente persistentLesson = sp.getIAulaPersistente();

			List lessonMatchList =
				persistentLesson.readLessonsInBroadPeriod(newLesson, oldLesson);

			if (lessonMatchList.size() > 0) {
				if (lessonMatchList.contains(newLesson)) {
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

}