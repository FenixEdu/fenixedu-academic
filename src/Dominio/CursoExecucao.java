/*
 * CursoExecucao.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package Dominio;

/**
 *
 * @author  rpfi
 */

public class CursoExecucao implements ICursoExecucao {
  protected String anoLectivo;
  protected ICurso curso;
    
  // codigos internos da base de dados
  private Integer codigoInterno;
  private Integer chaveCurso;
    
  /** Construtor sem argumentos publico requerido pela moldura de objectos OJB */
  public CursoExecucao() { }
    
  public CursoExecucao(String anoLectivo, ICurso curso) {
    setAnoLectivo(anoLectivo);
    setCurso(curso);
  }
    
  public Integer getCodigoInterno() {
    return codigoInterno;
  }
  public void setCodigoInterno(Integer codInt) {
    this.codigoInterno = codInt;
  }
    
  public Integer getChaveCurso() {
    return chaveCurso;
  }
  public void setChaveCurso(Integer chaveCurso) {
    this.chaveCurso = chaveCurso;
  }
    
  public String getAnoLectivo() {
    return anoLectivo;
  }
  public void setAnoLectivo(String anoLec) {
    this.anoLectivo = anoLec;
  }
    
  public ICurso getCurso() {
    return curso;
  }
  public void setCurso(ICurso curso) {
    this.curso = curso;
  }
    
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ICursoExecucao) {
      ICursoExecucao cursoExecucao = (ICursoExecucao)obj;
      resultado = getAnoLectivo().equals(cursoExecucao.getAnoLectivo()) &&
                  getCurso().getNome().equals(cursoExecucao.getCurso().getNome()) &&
                  getCurso().getSigla().equals(cursoExecucao.getCurso().getSigla());
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[CURSO_EXECUCAO";
    result += ", codInt=" + codigoInterno;
    result += ", anoLectivo=" + anoLectivo;
    result += ", curso=" + curso;
    result += ", chaveCurso=" + chaveCurso;
    result += "]";
    return result;
  }

}
