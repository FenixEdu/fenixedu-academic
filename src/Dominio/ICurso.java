/*
 * ICurso.java
 * 
 * Created on 31 de Outubro de 2002, 14:46
 */

package Dominio;

import java.util.List;

import Util.TipoCurso;

/**
 * @author rpfi
 */
public interface ICurso extends IDomainObject
{
    public String getSigla();
    public String getNome();
    public TipoCurso getTipoCurso();
    public List getDegreeCurricularPlans();
    public List getDegreeInfos();
	public String getConcreteClassForDegreeCurricularPlans();
	public IDegreeCurricularPlan getNewDegreeCurricularPlan();

    public void setSigla(String sigla);
    public void setNome(String nome);
    public void setTipoCurso(TipoCurso tipoCurso);
    public void setDegreeCurricularPlans(List degreeCurricularPlans);
    public void setDegreeInfos(List degreeInfos);
	public void setConcreteClassForDegreeCurricularPlans(String concreteClassForDegreeCurricularPlans);
}
