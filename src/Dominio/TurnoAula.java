/*
 * TurnoAula.java
 *
 * Created on 22 de Outubro de 2002, 9:13
 */

package Dominio;

/**
 *
 * @author  tfc130
 */

public class TurnoAula extends DomainObject implements ITurnoAula {
  protected ITurno _turno;
  protected IAula _aula;    
    
  // c�digos internos da base de dados
  //private Integer _codigoInterno;
  private Integer chaveTurno;
  private Integer chaveAula;
  
  /** Construtor sem argumentos p�blico requerido pela moldura de objectos OJB */
  public TurnoAula() { }
    
  public TurnoAula(ITurno turno, IAula aula) {
    setTurno(turno);
    setAula(aula);
  }

//  public Integer getCodigoInterno() {
//    return _codigoInterno;
//  }

//  public void setCodigoInterno(Integer codigoInterno) {
//    _codigoInterno = codigoInterno;
//  }
  
  public Integer getChaveTurno() {
    return chaveTurno;
  }
    
  public void setChaveTurno(Integer chaveTurno) {
    this.chaveTurno = chaveTurno;
  }

  
  public Integer getChaveAula() {
    return chaveAula;
  }

  public void setChaveAula(Integer chaveAula) {
    this.chaveAula = chaveAula;
  }  

  
  public ITurno getTurno() {
    return _turno;
  }
    
  public void setTurno(ITurno turno) {
    _turno = turno;
  }
  
  public IAula getAula() {
    return _aula;
  }
    
  public void setAula(IAula aula) {
    _aula = aula;
  }

  
 
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ITurnoAula) {
      ITurnoAula turnoAula = (ITurnoAula)obj;
      resultado = getTurno().equals(turnoAula.getTurno()) && getAula().equals(turnoAula.getAula());
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[TURNO_AULA";
    result += ", turno=" + _turno;   
    result += ", aula=" + _aula;       
    result += ", chaveTurno=" + chaveTurno;    
    result += ", chaveAula=" + chaveAula;        
    result += "]";
    return result;
  }

}
