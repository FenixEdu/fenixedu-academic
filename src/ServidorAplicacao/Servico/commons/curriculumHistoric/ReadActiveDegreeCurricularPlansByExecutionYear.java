/*
 * Created on Oct 7, 2004
 */
package ServidorAplicacao.Servico.commons.curriculumHistoric;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegreeCurricularPlan;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadActiveDegreeCurricularPlansByExecutionYear implements IService {

    public List run(Integer execution_year_Id) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            List executionDegrees = persistentExecutionDegree.readByExecutionYearOID(execution_year_Id);

            if (executionDegrees == null) {
                throw new FenixServiceException("nullDegree");
            }

            List infoDegreeCurricularPlans = (List) CollectionUtils.collect(executionDegrees,
                    new Transformer() {
                        public Object transform(Object obj) {
                            ICursoExecucao cursoExecucao = (ICursoExecucao) obj;
                            IDegreeCurricularPlan degreeCurricularPlan = cursoExecucao
                                    .getCurricularPlan();
                            return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
                        }
                    });

            return infoDegreeCurricularPlans;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}