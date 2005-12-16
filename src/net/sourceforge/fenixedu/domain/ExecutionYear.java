package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.fileSuport.INode;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota ciapl Dominio
 * 
 */
public class ExecutionYear extends ExecutionYear_Base {

    public String toString() {
        String result = "[EXECUTION_YEAR";
        result += ", internalCode=" + getIdInternal();
        result += ", year=" + getYear();
        result += ", begin=" + getBeginDate();
        result += ", end=" + getEndDate();
        result += "]";
        return result;
    }

    public String getSlideName() {
        String result = "/EY" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        return null;
    }

    public Collection<IExecutionDegree> getExecutionDegreesByType(final DegreeType degreeType) {

        return CollectionUtils.select(getExecutionDegrees(), new Predicate() {
            public boolean evaluate(Object arg0) {
                IExecutionDegree executionDegree = (IExecutionDegree) arg0;
                return executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                        degreeType);
            }
        });

    }

    public IExecutionYear getPreviousExecutionYear() {
        IExecutionYear previousExecutionYear = null;
        IExecutionPeriod currentExecutionPeriod = this.getExecutionPeriods().get(0);

        while (currentExecutionPeriod.getPreviousExecutionPeriod() != null) {
            currentExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();

            if (!currentExecutionPeriod.getExecutionYear().equals(this)) {
                previousExecutionYear = currentExecutionPeriod.getExecutionYear();
                break;
            }
        }

        return previousExecutionYear;
    }

    public int compareTo(Object object) {
        final IExecutionYear executionYear = (IExecutionYear) object;
        return getYear().compareTo(executionYear.getYear());
    }

}
