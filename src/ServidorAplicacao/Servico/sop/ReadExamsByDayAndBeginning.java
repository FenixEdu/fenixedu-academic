/*
 * ReadExamsByDayAndBeginning.java
 *
 * Created on 2003/03/19
 */

package ServidorAplicacao.Servico.sop;

/**
  * @author Luis Cruz & Sara Ribeiro
  * 
 **/
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IExam;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExamsByDayAndBeginning implements IServico {

  private static ReadExamsByDayAndBeginning _servico = new ReadExamsByDayAndBeginning();
  /**
   * The singleton access method of this class.
   **/
  public static ReadExamsByDayAndBeginning getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private ReadExamsByDayAndBeginning() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "ReadExamsByDayAndBeginning";
  }

  public List run(Date day, Calendar beginning) {
    ArrayList infoExams = new ArrayList();

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      
      List exams = sp.getIPersistentExam().readBy(day, beginning);
	  IExam exam = null;
	  for (int i = 0; i < exams.size(); i++) {
		exam = (IExam)exams.get(i);
		infoExams.add(Cloner.copyIExam2InfoExam(exam));
	}

    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }
     return infoExams;
  }

}