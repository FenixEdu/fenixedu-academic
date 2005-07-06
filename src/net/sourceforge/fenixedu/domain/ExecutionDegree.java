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

    public String toString() {
        String result = "[CURSO_EXECUCAO";
        result += ", codInt=" + getIdInternal();
        result += ", executionYear=" + getExecutionYear();
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
        if (obj instanceof IExecutionDegree) {
            final IExecutionDegree cursoExecucao = (IExecutionDegree) obj;
            return this.getIdInternal().equals(cursoExecucao.getIdInternal());
        }
        return false;
    }

}
