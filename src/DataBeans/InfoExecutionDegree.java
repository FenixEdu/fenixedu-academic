/*
 * InfoExecutionDegree.java
 *
 * Created on 24 de Novembro de 2002, 23:05
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
public class InfoExecutionDegree {
  protected String _anoLectivo;
  protected InfoDegree _infoLicenciatura;

  public InfoExecutionDegree() { }
    
  public InfoExecutionDegree(String anoLectivo, InfoDegree infoLicenciatura) {
    setAnoLectivo(anoLectivo);
    setInfoLicenciatura(infoLicenciatura);
  }

  public String getAnoLectivo() {
    return _anoLectivo;
  }

  public void setAnoLectivo(String anoLectivo) {
    _anoLectivo = anoLectivo;
  }
    
  public InfoDegree getInfoLicenciatura() {
    return _infoLicenciatura;
  }

  public void setInfoLicenciatura(InfoDegree infoLicenciatura) {
    _infoLicenciatura = infoLicenciatura;
  }

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof InfoExecutionDegree) {
      InfoExecutionDegree iLE = (InfoExecutionDegree)obj;
      resultado = getAnoLectivo().equals(iLE.getAnoLectivo()) &&
                  getInfoLicenciatura().getSigla().equals(iLE.getInfoLicenciatura().getSigla());
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[INFOLICENCIATURAEXECUCAO";
    result += ", anoLectivo=" + _anoLectivo;
    result += ", infoLicenciatura=" + _infoLicenciatura;
    result += "]";
    return result;
  }

}
