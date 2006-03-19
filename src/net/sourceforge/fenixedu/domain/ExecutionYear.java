package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.fileSuport.INode;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota ciapl Dominio
 * 
 */
public class ExecutionYear extends ExecutionYear_Base implements INode, Comparable {

    public ExecutionYear() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public String getSlideName() {
        String result = "/EY" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        return null;
    }

    public Collection<ExecutionDegree> getExecutionDegreesByType(final DegreeType degreeType) {

        return CollectionUtils.select(getExecutionDegrees(), new Predicate() {
            public boolean evaluate(Object arg0) {
                ExecutionDegree executionDegree = (ExecutionDegree) arg0;
                return executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                        degreeType);
            }
        });

    }

    public ExecutionYear getPreviousExecutionYear() {
        ExecutionYear previousExecutionYear = null;
        ExecutionPeriod currentExecutionPeriod = this.getExecutionPeriods().get(0);

        while (currentExecutionPeriod.getPreviousExecutionPeriod() != null) {
            currentExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();

            if (!currentExecutionPeriod.getExecutionYear().equals(this)) {
                previousExecutionYear = currentExecutionPeriod.getExecutionYear();
                break;
            }
        }

        return previousExecutionYear;
    }

    public ExecutionYear getNextExecutionYear() {
        for (ExecutionPeriod executionPeriod = this.getExecutionPeriods().get(0);
        	executionPeriod != null; executionPeriod = executionPeriod.getNextExecutionPeriod()) {
        	if (executionPeriod.getExecutionYear() != this) {
        		return executionPeriod.getExecutionYear();
        	}
        }

        return null;
    }

    public int compareTo(Object object) {
        final ExecutionYear executionYear = (ExecutionYear) object;
        return getYear().compareTo(executionYear.getYear());
    }
    
    public ExecutionPeriod getExecutionPeriodForSemester(Integer semester) {
        for (final ExecutionPeriod executionPeriod : this.getExecutionPeriods()) {
            if (executionPeriod.getSemester().equals(semester)) {
                return executionPeriod;
            }
        }
        return null;
    }

    public Collection<ExecutionDegree> getExecutionDegreesSortedByDegreeName() {
        final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(getExecutionDegrees());
        Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
        return executionDegrees;
    }

}
