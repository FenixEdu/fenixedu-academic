/*
 * ClassAndShiftKeys.java
 *
 * Created on 31 de Outubro de 2002, 12:17
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
public class ClassAndShiftKeys  extends InfoObject {
  protected String _nomeTurma;
  protected String _nomeTurno;

  public ClassAndShiftKeys() { }

  public ClassAndShiftKeys(String nomeTurma, String nomeTurno) {
    setNomeTurma(nomeTurma);
    setNomeTurno(nomeTurno);
  }

  public String getNomeTurma() {
    return _nomeTurma;
  }

  public void setNomeTurma(String nomeTurma) {
    _nomeTurma = nomeTurma;
  }

  public String getNomeTurno() {
    return _nomeTurno;
  }
    
  public void setNomeTurno(String nomeTurno) {
    _nomeTurno = nomeTurno;
  }

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof ClassAndShiftKeys) {
      ClassAndShiftKeys keysTurmaAndTurno = (ClassAndShiftKeys)obj;

      resultado = (getNomeTurno().equals(keysTurmaAndTurno.getNomeTurno())) &&
                  (getNomeTurma().equals(keysTurmaAndTurno.getNomeTurma()));
    }

    return resultado;
  }
  
  public String toString() {
    String result = "[KEYSTURMAANDTURNO";
    result += ", turma=" + _nomeTurma;
    result += ", turno=" + _nomeTurno;
    result += "]";
    return result;
  }

}
