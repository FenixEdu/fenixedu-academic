/*
 * Curso.java
 *
 * Created on 31 de Outubro de 2002, 15:19
 */

package Dominio;

import Util.TipoCurso;

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

public class Curso implements ICurso {
  protected String sigla;
  protected String nome;
  protected TipoCurso tipoCurso;
    
  // códigos internos da base de dados
  private Integer codigoInterno;
    
  /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
  public Curso() { }
    
  public Curso(String sigla, String nome, TipoCurso tipoCurso) {
    setSigla(sigla);
    setNome(nome);
    setTipoCurso(tipoCurso);
  }
    

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ICurso) {
      ICurso curso = (ICurso)obj;
      resultado = getSigla().equals(curso.getSigla()); 
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[CURSO";
    result += ", codInt=" + codigoInterno;
    result += ", sigla=" + sigla;
    result += ", nome=" + nome;
    result += ", tipoCurso=" + tipoCurso;
    result += "]";
    return result;
  }
  
	/**
	 * Returns the codigoInterno.
	 * @return int
	 */
	public Integer getCodigoInterno() {
		return codigoInterno;
	}
	
	/**
	 * Returns the nome.
	 * @return String
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * Returns the sigla.
	 * @return String
	 */
	public String getSigla() {
		return sigla;
	}
	
	/**
	 * Returns the tipoCurso.
	 * @return TipoCurso
	 */
	public TipoCurso getTipoCurso() {
		return tipoCurso;
	}
	
	/**
	 * Sets the codigoInterno.
	 * @param codigoInterno The codigoInterno to set
	 */
	public void setCodigoInterno(Integer codigoInterno) {
		this.codigoInterno = codigoInterno;
	}
	
	/**
	 * Sets the nome.
	 * @param nome The nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Sets the sigla.
	 * @param sigla The sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	/**
	 * Sets the tipoCurso.
	 * @param tipoCurso The tipoCurso to set
	 */
	public void setTipoCurso(TipoCurso tipoCurso) {
		this.tipoCurso = tipoCurso;
	}
}
