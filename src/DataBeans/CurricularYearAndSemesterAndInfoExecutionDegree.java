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
public class CurricularYearAndSemesterAndInfoExecutionDegree extends InfoObject {
  protected Integer _anoCurricular;
  protected Integer _semestre;
  protected InfoExecutionDegree _infoLicenciaturaExecucao;
  

  public CurricularYearAndSemesterAndInfoExecutionDegree() { }
    
  public CurricularYearAndSemesterAndInfoExecutionDegree(Integer anoCurricular,
                                                             Integer semestre,
                                                             InfoExecutionDegree infoLicenciaturaExecucao) {
    setAnoCurricular(anoCurricular);
    setSemestre(semestre);
    setInfoLicenciaturaExecucao(infoLicenciaturaExecucao);
  }

  public Integer getAnoCurricular() {
    return _anoCurricular;
  }

  public void setAnoCurricular(Integer anoCurricular) {
    _anoCurricular = anoCurricular;
  }
  
  public Integer getSemestre() {
    return _semestre;
  }

  public void setSemestre(Integer semestre) {
    _semestre = semestre;
  }
    
  public InfoExecutionDegree getInfoLicenciaturaExecucao() {
    return _infoLicenciaturaExecucao;
  }

  public void setInfoLicenciaturaExecucao(InfoExecutionDegree infoLicenciaturaExecucao) {
    _infoLicenciaturaExecucao = infoLicenciaturaExecucao;
  }

  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof CurricularYearAndSemesterAndInfoExecutionDegree) {
      CurricularYearAndSemesterAndInfoExecutionDegree aCSiLE = (CurricularYearAndSemesterAndInfoExecutionDegree)obj;
      resultado = (getAnoCurricular().equals(aCSiLE.getAnoCurricular())) &&
                  (getSemestre().equals(aCSiLE.getSemestre())) &&
                  (getInfoLicenciaturaExecucao().equals(aCSiLE.getInfoLicenciaturaExecucao()));
    }
    return resultado;
  }
  
  public String toString() {
    String result = "[INFOLICENCIATURAEXECUCAO";
    result += ", anoCurricular=" + _anoCurricular;
    result += ", semestre=" + _semestre;
    result += ", infoLicenciaturaExecucao=" + _infoLicenciaturaExecucao;
    result += "]";
    return result;
  }

}
