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
import java.util.Calendar;

public class TurnoAula implements ITurnoAula {
  protected ITurno _turno;
  protected IAula _aula;    
    
  // c�digos internos da base de dados
  private Integer _codigoInterno;
  private Integer chaveTurno;
  private Integer chaveAula;
  
  /** Construtor sem argumentos p�blico requerido pela moldura de objectos OJB */
  public TurnoAula() { }
    
  public TurnoAula(ITurno turno, IAula aula) {
    setTurno(turno);
    setAula(aula);
  }

  public Integer getCodigoInterno() {
    return _codigoInterno;
  }
    
  public void setCodigoInterno(Integer codigoInterno) {
    _codigoInterno = codigoInterno;
  }
  
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
      resultado = getTurno().getNome().equals(turnoAula.getTurno().getNome()) &&
                  getTurno().getTipo().equals(turnoAula.getTurno().getTipo()) &&
                  getTurno().getLotacao().equals(turnoAula.getTurno().getLotacao()) &&
                  getTurno().getDisciplinaExecucao().getNome().equals(turnoAula.getTurno().getDisciplinaExecucao().getNome()) &&
                  getTurno().getDisciplinaExecucao().getSigla().equals(turnoAula.getTurno().getDisciplinaExecucao().getSigla()) &&
                  getTurno().getDisciplinaExecucao().getPrograma().equals(turnoAula.getTurno().getDisciplinaExecucao().getPrograma()) &&
                  getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo().equals(turnoAula.getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo()) &&
                  getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome().equals(turnoAula.getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome()) &&
                  getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla().equals(turnoAula.getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla()) &&
                  getAula().getDiaSemana().equals(turnoAula.getAula().getDiaSemana()) &&
                  (getAula().getInicio().get(Calendar.HOUR_OF_DAY) == turnoAula.getAula().getInicio().get(Calendar.HOUR_OF_DAY)) &&
                  (getAula().getInicio().get(Calendar.MINUTE) == turnoAula.getAula().getInicio().get(Calendar.MINUTE)) &&
                  (getAula().getInicio().get(Calendar.SECOND) == turnoAula.getAula().getInicio().get(Calendar.SECOND)) &&
                  (getAula().getFim().get(Calendar.HOUR_OF_DAY) == turnoAula.getAula().getFim().get(Calendar.HOUR_OF_DAY)) &&
                  (getAula().getFim().get(Calendar.MINUTE) == turnoAula.getAula().getFim().get(Calendar.MINUTE)) &&
                  (getAula().getFim().get(Calendar.SECOND) == turnoAula.getAula().getFim().get(Calendar.SECOND)) &&
                  getAula().getTipo().equals(turnoAula.getAula().getTipo()) &&
                  getAula().getSala().getNome().equals(turnoAula.getAula().getSala().getNome()) &&
                  getAula().getSala().getEdificio().equals(turnoAula.getAula().getSala().getEdificio()) &&
                  getAula().getSala().getPiso().equals(turnoAula.getAula().getSala().getPiso()) &&
                  getAula().getSala().getTipo().equals(turnoAula.getAula().getSala().getTipo()) &&
                  getAula().getSala().getCapacidadeNormal().equals(turnoAula.getAula().getSala().getCapacidadeNormal()) &&
                  getAula().getSala().getCapacidadeExame().equals(turnoAula.getAula().getSala().getCapacidadeExame()) &&
                  getAula().getDisciplinaExecucao().getNome().equals(turnoAula.getAula().getDisciplinaExecucao().getNome()) &&
                  getAula().getDisciplinaExecucao().getSigla().equals(turnoAula.getAula().getDisciplinaExecucao().getSigla()) &&
                  getAula().getDisciplinaExecucao().getPrograma().equals(turnoAula.getAula().getDisciplinaExecucao().getPrograma()) &&
                  getAula().getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo().equals(turnoAula.getAula().getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo()) &&
                  getAula().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome().equals(turnoAula.getAula().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome()) &&
                  getAula().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla().equals(turnoAula.getAula().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla());
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
