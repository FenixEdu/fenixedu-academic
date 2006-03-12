/*
 * ExecutionDegree.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package net.sourceforge.fenixedu.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * 
 * @author rpfi
 */

public class ExecutionDegree extends ExecutionDegree_Base {

    public static final Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME;
    static {
    	EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME = new ComparatorChain();
    	((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME).addComparator(new BeanComparator("degreeCurricularPlan.degree.tipoCurso"));
    	((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME).addComparator(new BeanComparator("degreeCurricularPlan.degree.nome"));
    }

    public boolean isFirstYear() {

        List<ExecutionDegree> executionDegrees = this.getDegreeCurricularPlan().getExecutionDegrees();

        ExecutionDegree firstExecutionDegree = (ExecutionDegree) Collections.min(executionDegrees,
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
