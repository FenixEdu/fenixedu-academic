/*
 * Degree.java
 *
 * Created on 31 de Outubro de 2002, 15:19
 */

package Dominio;

import java.util.List;

import Util.TipoCurso;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */

public class Degree extends DomainObject implements IDegree {

    protected String sigla;

    protected String nome;

    protected TipoCurso tipoCurso;

    private List degreeCurricularPlans;

    private List degreeInfos;//added by Tânia Pousão

    private String concreteClassForDegreeCurricularPlans;

    /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
    public Degree() {
    }

    public Degree(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Degree(String sigla, String nome, TipoCurso tipoCurso) {
        setSigla(sigla);
        setNome(nome);
        setTipoCurso(tipoCurso);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IDegree) {
            IDegree curso = (IDegree) obj;
            resultado = getSigla().equals(curso.getSigla());
        }
        return resultado;
    }

    public IDegreeCurricularPlan getNewDegreeCurricularPlan() {
        IDegreeCurricularPlan degreeCurricularPlan = null;

        try {
            Class classDefinition = Class.forName(getConcreteClassForDegreeCurricularPlans());
            degreeCurricularPlan = (IDegreeCurricularPlan) classDefinition.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (ClassNotFoundException e) {
        }

        return degreeCurricularPlan;
    }

    public String toString() {
        String result = "[CURSO";
        result += ", codInt=" + getIdInternal();
        result += ", sigla=" + sigla;
        result += ", nome=" + nome;
        result += ", tipoCurso=" + tipoCurso;
        result += "]";
        return result;
    }

    /**
     * Returns the nome.
     * 
     * @return String
     */
    public String getNome() {
        return nome;
    }

    /**
     * Returns the sigla.
     * 
     * @return String
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * Returns the tipoCurso.
     * 
     * @return TipoCurso
     */
    public TipoCurso getTipoCurso() {
        return tipoCurso;
    }

    /**
     * Sets the nome.
     * 
     * @param nome
     *            The nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Sets the sigla.
     * 
     * @param sigla
     *            The sigla to set
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    /**
     * Sets the tipoCurso.
     * 
     * @param tipoCurso
     *            The tipoCurso to set
     */
    public void setTipoCurso(TipoCurso tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    /**
     * @return List
     */
    public List getDegreeCurricularPlans() {
        return degreeCurricularPlans;
    }

    /**
     * Sets the degreeCurricularPlans.
     * 
     * @param degreeCurricularPlans
     *            The degreeCurricularPlans to set
     */
    public void setDegreeCurricularPlans(List degreeCurricularPlans) {
        this.degreeCurricularPlans = degreeCurricularPlans;
    }

    /**
     * @return Returns the degreeInfos.
     */
    public List getDegreeInfos() {
        return degreeInfos;
    }

    /**
     * @param degreeInfos
     *            The degreeInfos to set.
     */
    public void setDegreeInfos(List degreeInfos) {
        this.degreeInfos = degreeInfos;
    }

    public String getConcreteClassForDegreeCurricularPlans() {
        return concreteClassForDegreeCurricularPlans;
    }

    public void setConcreteClassForDegreeCurricularPlans(String concreteClassForDegreeCurricularPlans) {
        this.concreteClassForDegreeCurricularPlans = concreteClassForDegreeCurricularPlans;
    }
}