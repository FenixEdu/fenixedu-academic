/*
 * ReadShiftEnrolment.java
 *
 * Created on December 20th, 2002, 03:39
 */

package ServidorAplicacao.Servico.student;

/**
 * Service ReadShiftSignup
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadStudentLessons implements IServico {

	private static ReadStudentLessons _servico = new ReadStudentLessons();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadStudentLessons getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadStudentLessons() {
	}

	/**
	 * Returns service name */
	public final String getNome() {
		return "ReadStudentLessons";
	}

	public Object run(InfoStudent infoStudent) {
		List infoLessons = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			List lessons =
//				sp.getITurnoAulaPersistente().readLessonsByStudent(
//					infoStudent.getInfoPerson().getUsername());
				sp.getIAulaPersistente().readLessonsByStudent(
					infoStudent.getInfoPerson().getUsername());
						
			if (lessons != null)
				for (int i = 0; i < lessons.size(); i++) {
					//ITurnoAula lesson = (ITurnoAula) lessons.get(i);
					IAula lesson = (IAula) lessons.get(i);
					InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(lesson);
					ITurno shift = lesson.getShift();
					InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
					infoLesson.setInfoShift(infoShift);
					
					infoLessons.add(infoLesson);
				}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		return infoLessons;
	}

}