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

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoStudent;
import Dominio.IAula;
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
  private ReadStudentLessons() { }

  /**
   * Returns service name */
  public final String getNome() {
    return "ReadStudentLessons";
  }

  public Object run(InfoStudent infoStudent) {
  	List infoLessons = new ArrayList();

    try {
    	ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		List lessons = sp.getITurnoAulaPersistente().readLessonsByStudent(infoStudent.getInfoPerson().getUsername());

    	for(int i = 0; i < lessons.size(); i++) {
    		IAula lesson = (IAula) lessons.get(i);

			InfoRoom infoRoom =
				new InfoRoom(
					lesson.getSala().getNome(),
					lesson.getSala().getEdificio(),
					lesson.getSala().getPiso(),
					lesson.getSala().getTipo(),
					lesson.getSala().getCapacidadeNormal(),
					lesson.getSala().getCapacidadeExame());
			InfoDegree infoDegree =
				new InfoDegree(
					lesson
						.getDisciplinaExecucao()
						.getLicenciaturaExecucao()
						.getCurso()
						.getSigla(),
					lesson
						.getDisciplinaExecucao()
						.getLicenciaturaExecucao()
						.getCurso()
						.getNome());
			InfoExecutionDegree infoED =
				new InfoExecutionDegree(
					lesson
						.getDisciplinaExecucao()
						.getLicenciaturaExecucao()
						.getAnoLectivo(),
					infoDegree);
			InfoExecutionCourse infoEC =
				new InfoExecutionCourse(
					lesson.getDisciplinaExecucao().getNome(),
					lesson.getDisciplinaExecucao().getSigla(),
					lesson.getDisciplinaExecucao().getPrograma(),
					infoED,
					lesson.getDisciplinaExecucao().getTheoreticalHours(),
					lesson.getDisciplinaExecucao().getPraticalHours(),
					lesson.getDisciplinaExecucao().getTheoPratHours(),
					lesson.getDisciplinaExecucao().getLabHours());
			InfoLesson infoLesson =
				new InfoLesson(
					lesson.getDiaSemana(),
					lesson.getInicio(),
					lesson.getFim(),
					lesson.getTipo(),
					infoRoom,
					infoEC);

			infoLessons.add(infoLesson);
    	}
	} catch (ExcepcaoPersistencia ex) {
		ex.printStackTrace();
    }

    return infoLessons;
  }

}