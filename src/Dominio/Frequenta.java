/*
 * Frequenta.java
 *
 * Created on 20 de Outubro de 2002, 14:42
 */

package Dominio;

/**
 *
 * @author  tfc130
 */
public class Frequenta implements IFrequenta {
  protected IStudent _aluno;
  protected IDisciplinaExecucao _disciplinaExecucao;
    
  // códigos internos da base de dados
  private Integer _codigoInterno;
  private Integer _chaveAluno;
  private Integer _chaveDisciplinaExecucao;

  /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
  public Frequenta() { }

  public Frequenta(IStudent aluno, IDisciplinaExecucao disciplinaExecucao) {
  	setAluno(aluno);
  	setDisciplinaExecucao(disciplinaExecucao);
  }

  public Integer getCodigoInterno() {
    return _codigoInterno;
  }
    
  public void setCodigoInterno(Integer codigoInterno) {
    _codigoInterno = codigoInterno;
  }

  public IStudent getAluno() {
    return _aluno;
  }

  public void setAluno(IStudent aluno) {
    _aluno = aluno;
  }


  public Integer getChaveAluno() {
    return _chaveAluno;
  }

  public void setChaveAluno(Integer chaveAluno) {
    _chaveAluno = chaveAluno;
  }

  public IDisciplinaExecucao getDisciplinaExecucao() {
    return _disciplinaExecucao;
  }

  public void setDisciplinaExecucao(IDisciplinaExecucao disciplinaExecucao) {
    _disciplinaExecucao = disciplinaExecucao;
  }

  public Integer getChaveDisciplinaExecucao() {
    return _chaveDisciplinaExecucao;
  }

  public void setChaveDisciplinaExecucao(Integer chaveDisciplinaExecucao) {
    _chaveDisciplinaExecucao = chaveDisciplinaExecucao;
  }
  
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof IFrequenta) {
      IFrequenta frequenta = (IFrequenta)obj;
      resultado = getAluno().getNumber().equals(frequenta.getAluno().getNumber()) &&
                  getDisciplinaExecucao().getNome().equals(frequenta.getDisciplinaExecucao().getNome()) &&
                  getDisciplinaExecucao().getSigla().equals(frequenta.getDisciplinaExecucao().getSigla()) &&
                  getDisciplinaExecucao().getPrograma().equals(frequenta.getDisciplinaExecucao().getPrograma()) &&
                  getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo().equals(frequenta.getDisciplinaExecucao().getLicenciaturaExecucao().getAnoLectivo()) &&
                  getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome().equals(frequenta.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getNome()) &&
                  getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla().equals(frequenta.getDisciplinaExecucao().getLicenciaturaExecucao().getCurso().getSigla());
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[ATTEND";
    result += ", codigoInterno=" + _codigoInterno;
    result += ", Student=" + _aluno;
	result += ", ExecutionCourse=" + _disciplinaExecucao;
    result += "]";
    return result;
  }

}
