/*
 * ExecutionDegree.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package net.sourceforge.fenixedu.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * @author rpfi
 */

public class ExecutionDegree extends ExecutionDegree_Base {

    public boolean isFirstYear() {

        List<IExecutionDegree> executionDegrees = this.getDegreeCurricularPlan().getExecutionDegrees();
        Collections.sort(executionDegrees, new Comparator() {

            public int compare(Object o1, Object o2) {
                IExecutionDegree executionDegree1 = (IExecutionDegree) o1;
                IExecutionDegree executionDegree2 = (IExecutionDegree) o2;

                return executionDegree1.getExecutionYear().getYear().compareTo(
                        executionDegree2.getExecutionYear().getYear());
            }

        });

        if (executionDegrees.get(executionDegrees.size() - 1).equals(this)) {
            return true;
        }

        return false;
    }

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

}
