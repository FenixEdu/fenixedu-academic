/*
 * TurmaTurno.java
 *
 * Created on 19 de Outubro de 2002, 14:42
 */

package Dominio;

/**
 *
 * @author  tfc130
 */
public class TurmaTurno implements ITurmaTurno {
  protected ITurma _turma;
  protected ITurno _turno;  
    
  // c�digos internos da base de dados
  private Integer _codigoInterno;
  private Integer _chaveTurma;
  private Integer _chaveTurno;
    
  /** Construtor sem argumentos p�blico requerido pela moldura de objectos OJB */
  public TurmaTurno() { }
    
  public TurmaTurno(ITurma turma, ITurno turno) {
    setTurma(turma);
    setTurno(turno);
  }

  public Integer getCodigoInterno() {
    return _codigoInterno;
  }
    
  public void setCodigoInterno(Integer codigoInterno) {
    _codigoInterno = codigoInterno;
  }
  
  public Integer getChaveTurma() {
    return _chaveTurma;
  }
    
  public void setChaveTurma(Integer chaveTurma) {
    _chaveTurma = chaveTurma;
  }
  
  public Integer getChaveTurno() {
    return _chaveTurno;
  }
    
  public void setChaveTurno(Integer chaveTurno) {
    _chaveTurno = chaveTurno;
  }

  public ITurma getTurma() {
    return _turma;
  }
    
  public void setTurma(ITurma turma) {
    _turma = turma;
  }

  public ITurno getTurno() {
    return _turno;
  }
    
  public void setTurno(ITurno turno) {
    _turno = turno;
  }  
  
 
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ITurmaTurno) {
      ITurmaTurno turma_turno = (ITurmaTurno)obj;
      resultado = getTurma().getNome().equals(turma_turno.getTurma().getNome()) &&
                  getTurma().getSemestre().equals(turma_turno.getTurma().getSemestre()) &&
                  getTurma().getLicenciatura().getNome().equals(turma_turno.getTurma().getLicenciatura().getNome()) &&
                  getTurma().getLicenciatura().getSigla().equals(turma_turno.getTurma().getLicenciatura().getSigla()) &&
                  getTurno().getNome().equals(turma_turno.getTurno().getNome()) &&
                  getTurno().getTipo().equals(turma_turno.getTurno().getTipo()) &&
                  getTurno().getLotacao().equals(turma_turno.getTurno().getLotacao()) &&
                  getTurno().getDisciplinaExecucao().getNome().equals(turma_turno.getTurno().getDisciplinaExecucao().getNome()) &&
                  getTurno().getDisciplinaExecucao().getSigla().equals(turma_turno.getTurno().getDisciplinaExecucao().getSigla()) &&
                  getTurno().getDisciplinaExecucao().getPrograma().equals(turma_turno.getTurno().getDisciplinaExecucao().getPrograma()) &&
                  getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo().equals(turma_turno.getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo()) &&
                  getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome().equals(turma_turno.getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome()) &&
                  getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla().equals(turma_turno.getTurno().getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla());
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[TURMA_TURNO";
    result += ", turma=" + _turma;
    result += ", turno=" + _turno;   
    result += ", chaveTurma=" + _chaveTurma;
    result += ", chaveTurno=" + _chaveTurno;    
    result += "]";
    return result;
  }

}
