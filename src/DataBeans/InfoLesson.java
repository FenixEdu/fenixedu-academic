/*
 * InfoLesson.java
 *
 * Created on 31 de Outubro de 2002, 11:35
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
import java.util.Calendar;

import Util.DiaSemana;
import Util.TipoAula;

public class InfoLesson {
  protected DiaSemana _diaSemana;
  protected Calendar _inicio;
  protected Calendar _fim;
  protected TipoAula _tipo;
  protected InfoRoom _infoSala;
  protected InfoExecutionCourse _infoDisciplinaExecucao;
    
  public InfoLesson() { }
    
  public InfoLesson(DiaSemana diaSemana, Calendar inicio, Calendar fim, TipoAula tipo,
                  InfoRoom infoSala, InfoExecutionCourse infoDisciplinaExecucao) {
    setDiaSemana(diaSemana);
    setInicio(inicio);
    setFim(fim);
    setTipo(tipo);
    setInfoSala(infoSala);
    setInfoDisciplinaExecucao(infoDisciplinaExecucao);
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

  public TipoAula getTipo() {
    return _tipo;
  }
    
  public void setTipo(TipoAula tipo) {
    _tipo = tipo;
  }

 
  public InfoRoom getInfoSala() {
    return _infoSala;
  }
    
  public void setInfoSala(InfoRoom infoSala) {
    _infoSala = infoSala;
  }
  
  public InfoExecutionCourse getInfoDisciplinaExecucao() {
    return _infoDisciplinaExecucao;
  }
    
  public void setInfoDisciplinaExecucao(InfoExecutionCourse infoDisciplinaExecucao) {
    _infoDisciplinaExecucao = infoDisciplinaExecucao;
  }
  
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof InfoLesson) {
      InfoLesson infoAula = (InfoLesson)obj;
      resultado = (getDiaSemana().equals(infoAula.getDiaSemana())) &&
                  (getInicio().get(Calendar.HOUR_OF_DAY) == infoAula.getInicio().get(Calendar.HOUR_OF_DAY)) &&
                  (getInicio().get(Calendar.MINUTE) == infoAula.getInicio().get(Calendar.MINUTE)) &&
                  (getInicio().get(Calendar.SECOND) == infoAula.getInicio().get(Calendar.SECOND)) &&
                  (getFim().get(Calendar.HOUR_OF_DAY) == infoAula.getFim().get(Calendar.HOUR_OF_DAY)) &&
                  (getFim().get(Calendar.MINUTE) == infoAula.getFim().get(Calendar.MINUTE)) &&
                  (getFim().get(Calendar.SECOND) == infoAula.getFim().get(Calendar.SECOND)) &&
                  (getInfoSala().equals(infoAula.getInfoSala()));
    }

    return resultado;
  }
  
  public String toString() {
    String result = "[INFOAULA";
    result += ", diaSemana=" + _diaSemana;
    result += ", inicio=" + _inicio;
    result += ", fim=" + _fim;
    result += ", tipo=" + _tipo;
    result += ", sala=" + _infoSala;
    result += ", disciplinaExecucao=" + _infoDisciplinaExecucao;
    result += "]";
    return result;
  }
  
  // Para ser retirado após ter a independencia da interface.
/*  public  String getInicioHourOfDay(){
    int hora = _inicio.get(Calendar.HOUR_OF_DAY);
    String ret = ""+hora;
    if (hora < 10)
  	  ret ="0" + ret;
    return ret;
  }

  public  int getInicioMinute(){
    return _inicio.get(Calendar.MINUTE);
  }
  
  public  int getFimHourOfDay(){
    return _inicio.get(Calendar.HOUR_OF_DAY);
  }

  public  int getFimMinute(){
    return _inicio.get(Calendar.MINUTE);
  }
*/
}