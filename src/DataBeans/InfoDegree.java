/*
 * InfoDegree.java
 *
 * Created on 25 de Novembro de 2002, 1:07
 */

package DataBeans;

import java.io.Serializable;

/**
 *
 * @author  tfc130
 */
public class InfoDegree implements Serializable{
  protected String _sigla;
  protected String _nome;
    

  public InfoDegree() { }
    
  public InfoDegree(String sigla, String nome) {
    setSigla(sigla);
    setNome(nome);
  }

  public String getSigla() {
    return _sigla;
  }

  public void setSigla(String sigla) {
    _sigla = sigla;
  }
    
  public String getNome() {
    return _nome;
  }
    
  public void setNome(String nome) {
    _nome = nome;
  }

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof InfoDegree) {
      InfoDegree iL = (InfoDegree)obj;
      resultado = (getSigla().equals(iL.getSigla()));
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[INFOLICENCIATURA";
    result += ", sigla=" + _sigla;
    result += ", nome=" + _nome;
    result += "]";
    return result;
  }

}
