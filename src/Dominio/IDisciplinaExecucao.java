/*
 * IDisciplinaExecucao.java
 *
 * Created on 16 de Outubro de 2002, 11:12
 */

package Dominio;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Nuno Nunes & Joana Mota
 */
public interface IDisciplinaExecucao extends Serializable{
    public java.lang.String getNome();
    public void setNome(java.lang.String nome);
    public java.lang.String getSigla();
    public void setSigla(java.lang.String sigla);
    public java.lang.String getPrograma();
    public void setPrograma(java.lang.String programa);
    public Double getTheoreticalHours();    
    public void setTheoreticalHours(Double theoreticalHours);
    public Double getPraticalHours();    
    public void setPraticalHours(Double praticalHours);
    public Double getTheoPratHours();    
    public void setTheoPratHours(Double theoPratHours);
    public Double getLabHours();    
    public void setLabHours(Double labHours);
    public Integer getSemester();
    public void setSemester (Integer semester);
    
    public ICursoExecucao getLicenciaturaExecucao();
    public void setLicenciaturaExecucao(ICursoExecucao licenciaturaExecucao);
    
    public void setAssociatedCurricularCourses(List associatedCurricularCourses);
    public List getAssociatedCurricularCourses();
	
}
