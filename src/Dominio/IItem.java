/*
 * IItem.java
 *
 * Created on 21 de Agosto de 2002, 23:09
 */

package Dominio;

/**
 *
 * @author  ars
 */
public interface IItem {
  public String getNome();
  public ISeccao getSeccao();
  public int getOrdem();
  public String getInformacao();
  public boolean getUrgente();

  public void setNome(String nome);
  public void setSeccao(ISeccao seccao);
  public void setOrdem(int ordem);
  public void setInformacao(String informacao);
  public void setUrgente(boolean urgente);
  
}
