/*
 * KeysTurma.java
 *
 * Created on 31 de Outubro de 2002, 12:32
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
public class ClassKey extends InfoObject {
  protected String _nomeTurma;

  public ClassKey() { }

  public ClassKey(String nomeTurma) {
    setNomeTurma(nomeTurma);
  }

  public String getNomeTurma() {
    return _nomeTurma;
  }

  public void setNomeTurma(String nomeTurma) {
    _nomeTurma = nomeTurma;
  }

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ClassKey) {
      ClassKey keyTurma = (ClassKey)obj;

      resultado = (getNomeTurma().equals(keyTurma.getNomeTurma()));
    }

    return resultado;
  }
  
  public String toString() {
    String result = "[KEYTURMA";
    result += ", turma=" + _nomeTurma;
    result += "]";
    return result;
  }

}
