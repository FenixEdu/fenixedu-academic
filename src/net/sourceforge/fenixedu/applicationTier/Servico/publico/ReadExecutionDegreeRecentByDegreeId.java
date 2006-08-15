/*
 * Created on 16/Nov/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Pedro Santos & Rita Carvalho
 */
public class ReadExecutionDegreeRecentByDegreeId extends Service {

    public InfoExecutionDegree run(final Integer degreeId) throws ExcepcaoPersistencia{
        List<ExecutionDegree> listExecutionDegrees = new ArrayList<ExecutionDegree>();
        
        Degree degree = rootDomainObject.readDegreeByOID(degreeId);
        for (DegreeCurricularPlan curricularPlan : degree.getDegreeCurricularPlans()) {
            listExecutionDegrees.addAll(curricularPlan.getExecutionDegrees());
        }
        
        ComparatorChain chain = new ComparatorChain(new BeanComparator("degreeCurricularPlan.initialDate"));
        chain.addComparator(new BeanComparator("executionYear.year"));
        ExecutionDegree executionDegree = (ExecutionDegree) Collections.max(listExecutionDegrees, chain);
        
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }
}
