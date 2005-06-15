/*
 * ExecutionDegree.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author rpfi
 */

public class ExecutionDegree extends ExecutionDegree_Base {

    /** Construtor sem argumentos publico requerido pela moldura de objectos OJB */
    public ExecutionDegree() {
    }

    public ExecutionDegree(IExecutionYear executionYear, IDegreeCurricularPlan curricularPlan) {
        setExecutionYear(executionYear);
        setDegreeCurricularPlan(curricularPlan);
    }

    public String toString() {
        String result = "[CURSO_EXECUCAO";
        result += ", codInt=" + getIdInternal();
        result += ", executionYear=" + getExecutionYear();
        //result += ", keyExecutionYear=" + academicYear;
        result += ", degreeCurricularPlan=" + getDegreeCurricularPlan();
        if (getCoordinatorsList() != null) {
            result += ", coordinatorsList=" + getCoordinatorsList().size();
        } else {
            result += ", coordinatorsList is NULL";
        }
        result += ", campus=" + getCampus();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IExecutionDegree) {
            IExecutionDegree cursoExecucao = (IExecutionDegree) obj;
            resultado = getIdInternal().equals(cursoExecucao.getIdInternal());
        }
        return resultado;
    }

}
