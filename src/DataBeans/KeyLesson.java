/*
 * KeyLesson.java
 *
 * Created on 31 de Outubro de 2002, 12:22
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
import java.util.Calendar;

import Util.DiaSemana;

public class KeyLesson extends InfoObject {
  protected DiaSemana _diaSemana;
  protected Calendar _inicio;
  protected Calendar _fim;
  protected RoomKey _keySala;
    
  public KeyLesson() { }

  public KeyLesson(DiaSemana diaSemana, Calendar inicio, Calendar fim, RoomKey keySala) {
    setDiaSemana(diaSemana);
    setInicio(inicio);
    setFim(fim);
    setKeySala(keySala);
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
    if (obj instanceof KeyLesson) {
      KeyLesson keyAula = (KeyLesson)obj;

      resultado = (getDiaSemana().equals(keyAula.getDiaSemana())) &&
                  (getInicio().get(Calendar.HOUR_OF_DAY) == keyAula.getInicio().get(Calendar.HOUR_OF_DAY)) &&
                  (getInicio().get(Calendar.MINUTE) == keyAula.getInicio().get(Calendar.MINUTE)) &&
                  (getInicio().get(Calendar.SECOND) == keyAula.getInicio().get(Calendar.SECOND)) &&
                  (getFim().get(Calendar.HOUR_OF_DAY) == keyAula.getFim().get(Calendar.HOUR_OF_DAY)) &&
                  (getFim().get(Calendar.MINUTE) == keyAula.getFim().get(Calendar.MINUTE)) &&
                  (getFim().get(Calendar.SECOND) == keyAula.getFim().get(Calendar.SECOND)) &&
                  (getKeySala().equals(keyAula.getKeySala()));
    }

    return resultado;
  }
  
  public String toString() {
    String result = "[KEYAULA";
    result += ", diaSemana=" + _diaSemana;
    result += ", inicio=" + _inicio;
    result += ", fim=" + _fim;
    result += ", keySala=" + _keySala;
    result += "]";
    return result;
  }

}
