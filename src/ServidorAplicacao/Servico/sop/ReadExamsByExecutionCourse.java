/*
 * ReadExamsByExecutionCourse.java
 *
 * Created on 2003/03/19
 */

package ServidorAplicacao.Servico.sop;

/**
  * @author Luis Cruz & Sara Ribeiro
  * 
 **/
import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoViewExamByDayAndShift;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExamsByExecutionCourse implements IServico {

  private static ReadExamsByExecutionCourse _servico = new ReadExamsByExecutionCourse();
  /**
   * The singleton access method of this class.
   **/
  public static ReadExamsByExecutionCourse getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private ReadExamsByExecutionCourse() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "ReadExamsByExecutionCourse";
  }

  public List run(InfoExecutionCourse infoExecutionCourse) {
    ArrayList infoViewExams = new ArrayList();

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
      List exams = sp.getIPersistentExam().readBy(executionCourse);
	  IExam exam = null;
	  InfoViewExamByDayAndShift infoViewExam = new InfoViewExamByDayAndShift();
	  Integer numberStudentsAttendingCourse = null; 
	  for (int i = 0; i < exams.size(); i++) {
		exam = (IExam)exams.get(i);
		infoViewExam.setInfoExam(Cloner.copyIExam2InfoExam(exam));
		// determine number of students attending course
		numberStudentsAttendingCourse = sp.getIFrequentaPersistente().countStudentsAttendingExecutionCourse(exam.getExecutionCourse());
		infoViewExam.setNumberStudentesAttendingCourse(numberStudentsAttendingCourse);		
		infoViewExams.add(infoViewExam);
	}

    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
     return infoViewExams;
  }
}