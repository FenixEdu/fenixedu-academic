/*
 * Item.java
 *
 * Created on 19 de Agosto de 2002, 13:21
 */

package Dominio;

/**
 *
 * @author  ars
 */

public class Item implements IItem {
  protected String _nome;
  protected ISeccao _seccao;
  protected int _ordem;
  protected String _informacao;
  protected boolean _urgente;
    
  // códigos internos da base de dados
  private int _codigoInterno;
  private Integer _chaveSeccao;
  //     private int _chaveSeccao;
    
  /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
  public Item() { }
    
  public Item(String nome, ISeccao seccao, int ordem, String informacao,
              boolean urgente) {
    setNome(nome);
    setSeccao(seccao);
    setOrdem(ordem);
    setInformacao(informacao);
    setUrgente(urgente);
  }
    
  public int getCodigoInterno() {
    return _codigoInterno;
  }
    
  public void setCodigoInterno(int codigoInterno) {
    _codigoInterno = codigoInterno;
  }
    
  public Integer getChaveSeccao() {
    return _chaveSeccao;
  }
    
  public void setChaveSeccao(Integer chaveSeccao) {
    _chaveSeccao = chaveSeccao;
  }
    
  //     public int getChaveSeccao() {
  //         return _chaveSeccao;
  //     }
    
  //     public void setChaveSeccao(int chaveSeccao) {
  //         _chaveSeccao = chaveSeccao;
  //     }
    
  public String getNome() {
    return _nome;
  }
    
  public ISeccao getSeccao() {
    return _seccao;
  }
    
  public int getOrdem() {
    return _ordem;
  }
    
  public String getInformacao() {
    return _informacao;
  }
    
  public boolean getUrgente() {
    return _urgente;
  }
    
  public void setNome(String nome) {
    _nome = nome;
  }
    
  public void setSeccao(ISeccao seccao) {
    _seccao = seccao;
  }
    
  public void setOrdem(int ordem) {
    _ordem = ordem;
  }
    
  public void setInformacao(String informacao) {
    _informacao = informacao;
  }
    
  public void setUrgente(boolean urgente) {
    _urgente = urgente;
  }
    
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof Item) {
      Item item = (Item)obj;
      resultado = (getNome().equals(item.getNome())) &&
	getSeccao().equals(item.getSeccao());
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[ITEM";
    result += ", codInt=" + _codigoInterno;
    result += ", nome=" + _nome;
    result += ", seccao=" + _seccao;
    result += ", ordem=" + _ordem;
    result += ", informacao=" + _informacao;
    result += ", urgente=" + _urgente;
    result += ", chaveSeccao=" + _chaveSeccao;
    result += "]";
    return result;
  }

}
