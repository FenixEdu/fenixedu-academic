/*
 * SemestreAndKeysSala.java
 *
 * Created on 31 de Outubro de 2002, 13:12
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
public class RoomKeyAndSemester {
  protected Integer _semestre;
  protected String _nomeSala;

  public RoomKeyAndSemester() { }

  public RoomKeyAndSemester(Integer semestre, String nomeSala) {
    setSemestre(semestre);
    setNomeSala(nomeSala);
  }

  public Integer getSemestre() {
    return _semestre;
  }

  public void setSemestre(Integer semestre) {
    _semestre = semestre;
  }

  public String getNomeSala() {
    return _nomeSala;
  }

  public void setNomeSala(String nomeSala) {
    _nomeSala = nomeSala;
  }

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof RoomKeyAndSemester) {
      RoomKeyAndSemester semestreAndKeySala = (RoomKeyAndSemester)obj;
      resultado = (getSemestre().equals(semestreAndKeySala.getSemestre())) && 
                  (getNomeSala().equals(semestreAndKeySala.getNomeSala()));
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[SEMESTREANDKEYSALA";
    result += ", semestre=" + _semestre;    
    result += ", sala=" + _nomeSala;
    result += "]";
    return result;
  }

}
