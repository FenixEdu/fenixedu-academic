package Dominio;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public class Regime {
  private int _codigoInterno;
  private String _designacao;
  
  /* Construtores */
  public Regime() {
    _codigoInterno = 0;
    _designacao = null;
  }
  
  public Regime(String designacao) {
    _codigoInterno = 0;
    _designacao = designacao;
  }
  
  public Regime(int codigoInterno, String designacao) {
    _codigoInterno = codigoInterno;
    _designacao = designacao;
  }
  
  /* Selectores */
  public int getCodigoInterno() {
    return _codigoInterno;
  }
  
  public String getDesignacao() {
    return _designacao;
  }
  
  /* Modificadores */
  public void setCodigoInterno(int codigoInterno) {
    _codigoInterno = codigoInterno;
  }
  
  public void setDesignacao(String designacao) {
    _designacao = designacao;
  }
  
  /* teste da igualdade */
  public boolean equals(Object obj){
    boolean resultado = false;
    
    if(obj instanceof Regime){
      Regime regime = (Regime) obj;
      
      resultado = (this.getCodigoInterno() == regime.getCodigoInterno() &&
      this.getDesignacao() == regime.getDesignacao());
    }
    return resultado;
  }
}