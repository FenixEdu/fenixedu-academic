/*
 * Created on 16/Nov/2004
 */
package ServidorAplicacao.Servico.publico;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import Dominio.IExecutionDegree;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Pedro Santos & Rita Carvalho
 */
public class ReadExecutionDegreeRecentByDegreeId implements IService {

    public InfoExecutionDegree run(final Integer degreeId) throws ExcepcaoPersistencia, FenixServiceException{
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
        List listExecutionDegrees = persistentExecutionDegree.readAll();
        CollectionUtils.filter(listExecutionDegrees, new Predicate(){

            public boolean evaluate(Object arg0) {
                IExecutionDegree executionDegree = (IExecutionDegree) arg0;
                
                return executionDegree.getCurricularPlan().getDegree().getIdInternal().equals(degreeId);
            }
        });
        ComparatorChain chain = new ComparatorChain(new BeanComparator("curricularPlan.initialDate"));
        chain.addComparator(new BeanComparator("executionYear.year"));
        Collections.sort(listExecutionDegrees, chain);
        IExecutionDegree executionDegree = (IExecutionDegree) listExecutionDegrees.get(listExecutionDegrees.size() -1);
        
        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
        
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(executionDegree.getCurricularPlan());
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        
        
        return infoExecutionDegree;
    }
}
