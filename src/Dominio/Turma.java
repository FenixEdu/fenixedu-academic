/*
 * Turma.java
 *
 * Created on 17 de Outubro de 2002, 19:07
 */

package Dominio;

/**
 *
 * @author  tfc130
 */

public class Turma implements ITurma {
  protected String _nome;
  protected Integer _semestre;
  protected Integer _anoCurricular;
  protected ICurso _licenciatura;
    
  // c�digos internos da base de dados
  private Integer _codigoInterno;
  private Integer _chaveLicenciatura;

  /** Construtor sem argumentos p�blico requerido pela moldura de objectos OJB */
  public Turma() { }
    
  public Turma(String nome, Integer semestre,Integer anoCurricular, ICurso licenciatura) {
    setNome(nome);
    setSemestre(semestre);
    setAnoCurricular(anoCurricular);
    setLicenciatura(licenciatura);
  }

  public Integer getCodigoInterno() {
    return _codigoInterno;
  }
    
  public void setCodigoInterno(Integer codigoInterno) {
    _codigoInterno = codigoInterno;
  }
  
  public String getNome() {
    return _nome;
  }
    
  public void setNome(String nome) {
    _nome = nome;
  }
    
  public Integer getChaveLicenciatura() {
    return _chaveLicenciatura;
  }
    
  public void setChaveLicenciatura(Integer chaveLicenciatura) {
    _chaveLicenciatura = chaveLicenciatura;
  }

  public Integer getSemestre() {
    return _semestre;
  }
    
  public void setSemestre(Integer semestre) {
    _semestre = semestre;
  }
  
  public Integer getAnoCurricular() {
	  return _anoCurricular;
	}
    
	public void setAnoCurricular(Integer anoCurricular) {
	  _anoCurricular = anoCurricular;
	}
  
  public ICurso getLicenciatura() {
    return _licenciatura;
  }
    
  public void setLicenciatura(ICurso licenciatura) {
    _licenciatura = licenciatura;
  }

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ITurma) {
      ITurma turma = (ITurma)obj;
      resultado = getNome().equals(turma.getNome()) &&
                  getLicenciatura().getNome().equals(turma.getLicenciatura().getNome()) &&
                  getLicenciatura().getSigla().equals(turma.getLicenciatura().getSigla()) &&
                  getSemestre().equals(turma.getSemestre());
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[TURMA";
    result += ", codigoInterno=" + _codigoInterno;
    result += ", nome=" + _nome;
    result += ", semestre=" + _semestre;
    result += ", chaveLicenciatura=" + _chaveLicenciatura;
    result += "]";
    return result;
  }

}
