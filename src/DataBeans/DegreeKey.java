/*
 * SemestreAndKeysSala.java
 *
 * Created on 24 de Novembro de 2002, 15:42
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
public class DegreeKey extends InfoObject {
  protected String _sigla;

  public DegreeKey() { }

  public DegreeKey(String sigla) {
    setSigla(sigla);
  }

  public String getSigla() {
    return _sigla;
  }

  public void setSigla(String sigla) {
    _sigla = sigla;
  }

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof DegreeKey) {
      DegreeKey keyLicenciatura = (DegreeKey)obj;
      resultado = (getSigla().equals(keyLicenciatura.getSigla()));
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[SEMESTREANDKEYLICENCIATURA";
    result += ", sigla=" + _sigla;
    result += "]";
    return result;
  }

}
