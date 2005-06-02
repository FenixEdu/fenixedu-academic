/*
 * Degree.java
 *
 * Created on 31 de Outubro de 2002, 15:19
 */

package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.degree.DegreeType;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */

public class Degree extends Degree_Base {

    /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
    public Degree() {
    }

    public Degree(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Degree(String sigla, String nome, DegreeType tipoCurso) {
        setSigla(sigla);
        setNome(nome);
        setTipoCurso(tipoCurso);
    }

    public String toString() {
        String result = "[CURSO";
        result += ", codInt=" + getIdInternal();
        result += ", sigla=" + getSigla();
        result += ", nome=" + getNome();
        result += ", tipoCurso=" + getTipoCurso();
        result += "]";
        return result;
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

}