/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons.curriculumHistoric;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadActiveDegreeCurricularPlansByExecutionYear implements IService {

    public List run(Integer execution_year_Id) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            List executionDegrees = persistentExecutionDegree.readByExecutionYearOID(execution_year_Id);

            if (executionDegrees == null) {
                throw new FenixServiceException("nullDegree");
            }

            List infoDegreeCurricularPlans = (List) CollectionUtils.collect(executionDegrees,
                    new Transformer() {
                        public Object transform(Object obj) {
                            IExecutionDegree cursoExecucao = (IExecutionDegree) obj;
                            IDegreeCurricularPlan degreeCurricularPlan = cursoExecucao
                                    .getCurricularPlan();
                            return InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(degreeCurricularPlan);
                        }
                    });

            return infoDegreeCurricularPlans;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}