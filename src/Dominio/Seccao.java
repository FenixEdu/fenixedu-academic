/*
 * Seccao.java
 *
 * Created on 23 de Agosto de 2002, 16:14
 */

package Dominio;

/**
 *
 * @author  ars
 */

import java.util.List;

public class Seccao implements ISeccao {
  protected String _nome;
  protected int _ordem;
  protected ISitio _sitio;
  protected ISeccao _seccaoSuperior;
  protected List _seccoesInferiores;
  protected List _itens;
    
  // códigos internos da base de dados
  private int _codigoInterno;
  private Integer _chaveSitio;
  private Integer _chaveSeccaoSuperior;
    
  /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
  public Seccao() { }
    
  public Seccao(String nome, int ordem, ISitio sitio, ISeccao seccaoSuperior) {
    setNome(nome);
    setOrdem(ordem);
    setSitio(sitio);
    setSeccaoSuperior(seccaoSuperior);
  }
    
  public int getCodigoInterno() {
    return _codigoInterno;
  }
    
  public void setCodigoInterno(int codigoInterno) {
    _codigoInterno = codigoInterno;
  }
    
  public Integer getChaveSitio() {
    return _chaveSitio;
  }
    
  public void setChaveSitio(Integer chaveSitio) {
    _chaveSitio = chaveSitio;
  }
    
  public Integer getChaveSeccaoSuperior() {
    return _chaveSeccaoSuperior;
  }
    
  public void setChaveSeccaoSuperior(Integer chaveSeccaoSuperior) {
    _chaveSeccaoSuperior = chaveSeccaoSuperior;
  }
    
  public String getNome() {
    return _nome;
  }
    
  public int getOrdem() {
    return _ordem;
  }
    
  public ISitio getSitio() {
    return _sitio;
  }
    
  public ISeccao getSeccaoSuperior() {
    return _seccaoSuperior;
  }
    
  public List getSeccoesInferiores() {
    return _seccoesInferiores;
  }
    
  public List getItens() {
    return _itens;
  }
    
  public void setNome(String nome) {
    _nome = nome;
  }
    
  public void setOrdem(int ordem) {
    _ordem = ordem;
  }
    
  public void setSitio(ISitio sitio) {
    _sitio = sitio;
  }
    
  public void setSeccaoSuperior(ISeccao seccao) {
    _seccaoSuperior = seccao;
  }
    
  public void setSeccoesInferiores(List seccoesInferiores) {
    _seccoesInferiores = seccoesInferiores;
  }
    
  public void setItens(List itens) {
    _itens = itens;
  }
    
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ISeccao) {
      ISeccao seccao = (ISeccao)obj;
      resultado = getNome().equals(seccao.getNome()) &&
	getSitio().equals(seccao.getSitio());
      if (getSeccaoSuperior() == null) {
	resultado &= (seccao.getSeccaoSuperior() == null);
      }	else {
	resultado &= getSeccaoSuperior().equals(seccao.getSeccaoSuperior());
      }
    }
    return resultado;
  }

  public String toString() {
    String result = "[SECCAO";
    result += ", codInt=" + _codigoInterno;
    result += ", nome=" + _nome;
    result += ", ordem=" + _ordem;
    result += ", sitio=" + _sitio;
    result += ", seccaosuperior=" + _seccaoSuperior;
    result += "]";
    return result;
  }
}
