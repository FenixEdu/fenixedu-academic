/*
 * Turno.java
 *
 * Created on 17 de Outubro de 2002, 19:28
 */

package Dominio;

/**
 *
 * @author  tfc130
 */
import Util.TipoAula;

public class Turno implements ITurno {
  protected String _nome;
  protected TipoAula _tipo;
  protected Integer _lotacao;
  protected IDisciplinaExecucao _disciplinaExecucao;
  
  // c�digos internos da base de dados
  private Integer codigoInterno;
  private Integer chaveDisciplinaExecucao;

  /** Construtor sem argumentos p�blico requerido pela moldura de objectos OJB */
  public Turno() { }
    
  public Turno(String nome, TipoAula tipo, Integer lotacao, IDisciplinaExecucao disciplinaExecucao) {
    setNome(nome);
    setTipo(tipo);
    setLotacao(lotacao);
    setDisciplinaExecucao(disciplinaExecucao);
  }

  public Integer getCodigoInterno() {
    return this.codigoInterno;
  }
    
  public void setCodigoInterno(Integer codigoInterno) {
    this.codigoInterno = codigoInterno;
  }
  
  public String getNome() {
    return _nome;
  }
    
  public void setNome(String nome) {
    _nome = nome;
  }
    
  public Integer getChaveDisciplinaExecucao() {
    return this.chaveDisciplinaExecucao;
  }
    
  public void setChaveDisciplinaExecucao(Integer chaveDisciplinaExecucao) {
    this.chaveDisciplinaExecucao = chaveDisciplinaExecucao;
  }

  public TipoAula getTipo() {
    return _tipo;
  }
    
  public void setTipo(TipoAula tipo) {
    _tipo = tipo;
  }
  
  public Integer getLotacao() {
    return _lotacao;
  }
    
  public void setLotacao(Integer lotacao) {
    _lotacao = lotacao;
  }

  public IDisciplinaExecucao getDisciplinaExecucao() {
    return _disciplinaExecucao;
  }
    
  public void setDisciplinaExecucao(IDisciplinaExecucao disciplinaExecucao) {
    _disciplinaExecucao = disciplinaExecucao;
  }  
  
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ITurno) {
      ITurno turno = (ITurno)obj;
      resultado = (getNome().equals(turno.getNome())) &&
                  (getTipo().equals(turno.getTipo())) &&
                  (getDisciplinaExecucao().getNome().equals(turno.getDisciplinaExecucao().getNome())) &&
                  (getDisciplinaExecucao().getPrograma().equals(turno.getDisciplinaExecucao().getPrograma())) &&
                  (getDisciplinaExecucao().getSigla().equals(turno.getDisciplinaExecucao().getSigla())) &&
                  (getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo().equals(turno.getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo())) &&
                  (getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome().equals(turno.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome())) &&
                  (getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla().equals(turno.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla())) &&
                  (getLotacao().equals(turno.getLotacao()));
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[TURNO";
    result += ", codigoInterno=" + this.codigoInterno;
    result += ", nome=" + _nome;
    result += ", tipo=" + _tipo;
    result += ", lotacao=" + _lotacao;
    result += ", chaveDisciplinaExecucao=" + this.chaveDisciplinaExecucao;
    result += "]";
    return result;
  }

}
