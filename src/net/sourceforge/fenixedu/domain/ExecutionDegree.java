/*
 * ExecutionDegree.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package net.sourceforge.fenixedu.domain;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

/**
 * 
 * @author rpfi
 */

public class ExecutionDegree extends ExecutionDegree_Base {

    public boolean isFirstYear() {

        List<IExecutionDegree> executionDegrees = this.getDegreeCurricularPlan().getExecutionDegrees();

        IExecutionDegree firstExecutionDegree = (IExecutionDegree) Collections.min(executionDegrees,
                new BeanComparator("executionYear.year"));

        if (firstExecutionDegree.equals(this)) {
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
