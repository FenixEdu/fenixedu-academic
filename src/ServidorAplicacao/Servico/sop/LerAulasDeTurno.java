/*
 * LerAulasDeTurno.java
 *
 * Created on 28 de Outubro de 2002, 22:23
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeTurno
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ITurno;
import ServidorAplicacao.IServico;

public class LerAulasDeTurno implements IServico {

  private static LerAulasDeTurno _servico = new LerAulasDeTurno();
  /**
   * The singleton access method of this class.
   **/
  public static LerAulasDeTurno getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private LerAulasDeTurno() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "LerAulasDeTurno";
  }

  public List run(ShiftKey shiftKey) {
    ArrayList infoAulas = null;

 //   try {
   //   ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      
      
      ITurno shift = Cloner.copyInfoShift2Shift(new InfoShift(shiftKey.getShiftName(),null, null, shiftKey.getInfoExecutionCourse()));

	System.out.println("shift= " + shift);
      
      //List aulas = sp.getITurnoAulaPersistente().readByShift(shift);
      List aulas = shift.getAssociatedLessons();

	System.out.println("aulas.size= " + aulas.size());

      Iterator iterator = aulas.iterator();
      infoAulas = new ArrayList();

      while(iterator.hasNext()) {
      	IAula elem = (IAula)iterator.next();
      	
		InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
					
		InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
		infoLesson.setInfoShift(infoShift);
      	
		infoAulas.add(infoLesson);
      }

//    } catch (ExcepcaoPersistencia ex) {
//      ex.printStackTrace();
//    }
    
    return infoAulas;
  }

}