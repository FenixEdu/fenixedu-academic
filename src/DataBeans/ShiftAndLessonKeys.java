/*
 * ShiftAndLessonKeys.java
 *
 * Created on 31 de Outubro de 2002, 10:49
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
import java.util.Calendar;

import Util.DiaSemana;

public class ShiftAndLessonKeys extends InfoObject {
  protected String _nomeTurno;
  protected DiaSemana _diaSemana;
  protected Calendar _inicio;
  protected Calendar _fim;
  protected RoomKey _keySala;
    
  public ShiftAndLessonKeys() { }

  public ShiftAndLessonKeys(String nomeTurno, DiaSemana diaSemana,
                          Calendar inicio, Calendar fim, RoomKey keySala) {
    setNomeTurno(nomeTurno);
    setDiaSemana(diaSemana);
    setInicio(inicio);
    setFim(fim);
    setKeySala(keySala);
  }

  public String getNomeTurno() {
    return _nomeTurno;
  }
    
  public void setNomeTurno(String nomeTurno) {
    _nomeTurno = nomeTurno;
  }
  
  public DiaSemana getDiaSemana() {
    return _diaSemana;
  }
    
  public void setDiaSemana(DiaSemana diaSemana) {
    _diaSemana = diaSemana;
  }
  
  public Calendar getInicio() {
    return _inicio;
  }
    
  public void setInicio(Calendar inicio) {
    _inicio = inicio;
  }

  public Calendar getFim() {
    return _fim;
  }
    
  public void setFim(Calendar fim) {
    _fim = fim;
  }

  public RoomKey getKeySala() {
    return _keySala;
  }
    
  public void setKeySala(RoomKey keySala) {
    _keySala = keySala;
  }
  
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ShiftAndLessonKeys) {
      ShiftAndLessonKeys keysTurnoAndAula = (ShiftAndLessonKeys)obj;

      resultado = (getNomeTurno().equals(keysTurnoAndAula.getNomeTurno())) &&
                  (getDiaSemana().equals(keysTurnoAndAula.getDiaSemana())) &&
                  (getInicio().get(Calendar.HOUR_OF_DAY) == keysTurnoAndAula.getInicio().get(Calendar.HOUR_OF_DAY)) &&
                  (getInicio().get(Calendar.MINUTE) == keysTurnoAndAula.getInicio().get(Calendar.MINUTE)) &&
                  (getInicio().get(Calendar.SECOND) == keysTurnoAndAula.getInicio().get(Calendar.SECOND)) &&
                  (getFim().get(Calendar.HOUR_OF_DAY) == keysTurnoAndAula.getFim().get(Calendar.HOUR_OF_DAY)) &&
                  (getFim().get(Calendar.MINUTE) == keysTurnoAndAula.getFim().get(Calendar.MINUTE)) &&
                  (getFim().get(Calendar.SECOND) == keysTurnoAndAula.getFim().get(Calendar.SECOND)) &&
                  (getKeySala().equals(keysTurnoAndAula.getKeySala()));
    }

    return resultado;
  }
  
  public String toString() {
    String result = "[KEYSTURNOANDAULA";
    result += ", turno=" + _nomeTurno;
    result += ", diaSemana=" + _diaSemana;
    result += ", inicio=" + _inicio;
    result += ", fim=" + _fim;
    result += ", keySala=" + _keySala;
    result += "]";
    return result;
  }

}
