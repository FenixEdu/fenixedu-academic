/*
 * Sitio.java
 *
 * Created on 25 de Agosto de 2002, 0:27
 */

package Dominio;

/**
 *
 * @author  ars
 */

import java.util.List;

public class Sitio implements ISitio {
  private String _nome;
  private int _anoCurricular;
  private int _semestre;
  private String _departamento;
  private String _curso;
  private List _seccoes;
  private ISeccao _seccaoInicial;
    
  // códigos internos da base de dados
  private int _codigoInterno;
  private Integer _chaveSeccaoInicial;
    
  /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
  public Sitio() { }
    
  public Sitio(String nome, int anoCurricular, int semestre,
               String curso, String departamento) {
    setNome(nome);
    setAnoCurricular(anoCurricular);
    setSemestre(semestre);
    setDepartamento(departamento);
    setCurso(curso);
    setSeccaoInicial(null);
  }

  public int getCodigoInterno() {
    return _codigoInterno;
  }
    
  public void setCodigoInterno(int codigoInterno) {
    _codigoInterno = codigoInterno;
  }
    
  public Integer getChaveSeccaoInicial() {
    return _chaveSeccaoInicial;
  }

  public void setChaveSeccaoInicial(Integer chaveSeccaoInicial) {
    _chaveSeccaoInicial = chaveSeccaoInicial;
  }

  public String getNome() {
    return _nome;
  }
    
  public int getAnoCurricular() {
    return _anoCurricular;
  }
    
  public int getSemestre() {
    return _semestre;
  }
    
  public String getCurso() {
    return _curso;
  }
    
  public String getDepartamento() {
    return _departamento;
  }
    
  public List getSeccoes() {
    return _seccoes;
  }

  public ISeccao getSeccaoInicial() {
    return _seccaoInicial;
  }
    
  public void setNome(String nome) {
    _nome = nome;
  }
    
  public void setAnoCurricular(int anoCurricular) {
    _anoCurricular = anoCurricular;
  }
  public void setSemestre(int semestre) {
    _semestre = semestre;
  }
    
  public void setCurso(String curso) {
    _curso = curso;
  }
    
  public void setDepartamento(String departamento) {
    _departamento = departamento;
  }
    
  public void setSeccoes(List seccoes) {
    _seccoes = seccoes;
  }

  public void setSeccaoInicial(ISeccao seccaoInicial) {
    _seccaoInicial = seccaoInicial;
  }
    
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ISitio) {
      ISitio sitio = (ISitio)obj;
      resultado = getNome().equals(sitio.getNome());
    }
    return resultado;
  }
}
