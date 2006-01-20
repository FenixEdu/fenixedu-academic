/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons.curriculumHistoric;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadActiveDegreeCurricularPlansByExecutionYear extends Service {

    public List run(Integer executionYearID) throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentExecutionDegree persistentExecutionDegree = persistentSupport.getIPersistentExecutionDegree();
        List executionDegrees = persistentExecutionDegree.readByExecutionYearOID(executionYearID);

        if (executionDegrees == null) {
            throw new FenixServiceException("nullDegree");
        }

        List infoDegreeCurricularPlans = (List) CollectionUtils.collect(executionDegrees,
                new Transformer() {
                    public Object transform(Object obj) {
                        ExecutionDegree cursoExecucao = (ExecutionDegree) obj;
                        DegreeCurricularPlan degreeCurricularPlan = cursoExecucao
                                .getDegreeCurricularPlan();
                        return InfoDegreeCurricularPlanWithDegree
                                .newInfoFromDomain(degreeCurricularPlan);
                    }
                });

        return infoDegreeCurricularPlans;
    }

}