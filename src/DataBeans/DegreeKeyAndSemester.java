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
public class DegreeKeyAndSemester {
  protected Integer _semestre;
  protected String _sigla;

  public DegreeKeyAndSemester() { }

  public DegreeKeyAndSemester(Integer semestre, String sigla) {
    setSemestre(semestre);
    setSigla(sigla);
  }

  public Integer getSemestre() {
    return _semestre;
  }

  public void setSemestre(Integer semestre) {
    _semestre = semestre;
  }

  public String getSigla() {
    return _sigla;
  }

  public void setSigla(String sigla) {
    _sigla = sigla;
  }

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof DegreeKeyAndSemester) {
      DegreeKeyAndSemester semestreAndKeyLicenciatura = (DegreeKeyAndSemester)obj;
      resultado = (getSemestre().equals(semestreAndKeyLicenciatura.getSemestre())) && 
                  (getSigla().equals(semestreAndKeyLicenciatura.getSigla()));
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[SEMESTREANDKEYLICENCIATURA";
    result += ", semestre=" + _semestre;
    result += ", sigla=" + _sigla;
    result += "]";
    return result;
  }

}
