/*
 * Aula.java
 *
 * Created on 18 de Outubro de 2002, 00:54
 */

package Dominio;

/**
 *
 * @author  tfc130
 */
import java.util.Calendar;

import Util.DiaSemana;
import Util.TipoAula;

public class Aula implements IAula {
  protected DiaSemana _diaSemana;
  protected Calendar _inicio;
  protected Calendar _fim;
  protected TipoAula _tipo;
  protected ISala _sala;
  protected IDisciplinaExecucao _disciplinaExecucao;
    
  // c�digos internos da base de dados
  private Integer _codigoInterno;
  private Integer _chaveSala;
  private Integer _chaveDisciplinaExecucao;
    
  /** Construtor sem argumentos p�blico requerido pela moldura de objectos OJB */
  public Aula() { }
    
  public Aula(DiaSemana diaSemana, Calendar inicio, Calendar fim, TipoAula tipo, ISala sala,
              IDisciplinaExecucao disciplinaExecucao) {
    setDiaSemana(diaSemana);
    setInicio(inicio);
    setFim(fim);
    setTipo(tipo);
    setSala(sala);
    setDisciplinaExecucao(disciplinaExecucao);
  }

  public Integer getCodigoInterno() {
    return _codigoInterno;
  }
    
  public void setCodigoInterno(Integer codigoInterno) {
    _codigoInterno = codigoInterno;
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

  public Integer getChaveSala() {
    return _chaveSala;
  }
    
  public void setChaveSala(Integer chaveSala) {
    _chaveSala = chaveSala;
  }
  
  public Integer getChaveDisciplinaExecucao() {
    return _chaveDisciplinaExecucao;
  }
    
  public void setChaveDisciplinaExecucao(Integer chaveDisciplinaExecucao) {
    _chaveDisciplinaExecucao = chaveDisciplinaExecucao;
  }

  public IDisciplinaExecucao getDisciplinaExecucao() {
    return _disciplinaExecucao;
  }
    
  public void setDisciplinaExecucao(IDisciplinaExecucao disciplinaExecucao) {
    _disciplinaExecucao = disciplinaExecucao;
  }
  
  public ISala getSala() {
    return _sala;
  }
    
  public void setSala(ISala sala) {
    _sala = sala;
  }
  
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof IAula) {
      IAula aula = (IAula)obj;

      resultado = getDiaSemana().equals(aula.getDiaSemana()) &&
                  (getInicio().get(Calendar.HOUR_OF_DAY) == aula.getInicio().get(Calendar.HOUR_OF_DAY)) &&
                  (getInicio().get(Calendar.MINUTE) == aula.getInicio().get(Calendar.MINUTE)) &&
                  (getFim().get(Calendar.HOUR_OF_DAY) == aula.getFim().get(Calendar.HOUR_OF_DAY)) &&
                  (getFim().get(Calendar.MINUTE) == aula.getFim().get(Calendar.MINUTE)) &&
                  getSala().getNome().equals(aula.getSala().getNome());
    }

    return resultado;
  }
  
  public String toString() {
    String result = "[AULA";
    result += ", codInt=" + _codigoInterno;
    result += ", diaSemana=" + _diaSemana;
    if (_inicio!=null)
    result += ", inicio=" + _inicio.get(Calendar.HOUR_OF_DAY) +":"+_inicio.get(Calendar.MINUTE);
	if (_fim!=null)
    result += ", fim=" + _fim.get(Calendar.HOUR_OF_DAY) +":"+_fim.get(Calendar.MINUTE);;
    result += ", tipo=" + _tipo;
    result += ", chaveSala=" + _chaveSala;
    result += ", chaveDisciplinaExecucao=" + _chaveDisciplinaExecucao;
    result += "]";
    return result;
  }

}
