/*
 * TurnoAluno.java
 *
 * Created on 21 de Outubro de 2002, 18:50
 */

package Dominio;

/**
 *
 * @author  tfc130
 */
public class TurnoAluno implements ITurnoAluno {
  protected ITurno _turno;
  protected IStudent _aluno;    
    
  // códigos internos da base de dados
  private Integer _codigoInterno;
  private Integer _chaveTurno;
  private Integer _chaveAluno;
  
  /** Construtor sem argumentos publico requerido pela moldura de objectos OJB */
  public TurnoAluno() { }
    
  public TurnoAluno(ITurno turno, IStudent aluno) {
    setTurno(turno);
    setAluno(aluno);
  }

  public Integer getCodigoInterno() {
    return _codigoInterno;
  }
    
  public void setCodigoInterno(Integer codigoInterno) {
    _codigoInterno = codigoInterno;
  }
  
  public Integer getChaveTurno() {
    return _chaveTurno;
  }
    
  public void setChaveTurno(Integer chaveTurno) {
    _chaveTurno = chaveTurno;
  }
  
  public Integer getChaveAluno() {
    return _chaveAluno;
  }

  public void setChaveAluno(Integer chaveAluno) {
    _chaveAluno = chaveAluno;
  }  
 
  public ITurno getTurno() {
    return _turno;
  }
    
  public void setTurno(ITurno turno) {
    _turno = turno;
  }
  
  public IStudent getAluno() {
    return _aluno;
  }
    
  public void setAluno(IStudent aluno) {
    _aluno = aluno;
  }

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ITurnoAluno) {
      ITurnoAluno turnoAluno = (ITurnoAluno)obj;
      resultado = getTurno().equals(turnoAluno.getTurno()) &&
                  getAluno().equals(turnoAluno.getAluno());
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[TURNO_ALUNO";
    result += ", turno=" + _turno;   
    result += ", aluno=" + _aluno;       
    result += ", chaveTurno=" + _chaveTurno;    
    result += ", chaveAluno=" + _chaveAluno;        
    result += "]";
    return result;
  }

}
