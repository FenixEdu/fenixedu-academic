/*
 * Created on 1/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoDegreeCurricularPlanWithDegree;
import Dominio.DegreeCurricularPlan;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlan implements IService {

    /**
     * The constructor of this class.
     */
    public ReadDegreeCurricularPlan() {
    }

    /**
     * Executes the service. Returns the current InfoDegreeCurricularPlan.
     * 
     * @throws ExcepcaoPersistencia
     */
    public InfoDegreeCurricularPlan run(final Integer idInternal) throws FenixServiceException,
            ExcepcaoPersistencia {

        final IDegreeCurricularPlan degreeCurricularPlan;

        final ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        degreeCurricularPlan = (IDegreeCurricularPlan) sp.getIPersistentDegreeCurricularPlan()
                .readByOID(DegreeCurricularPlan.class, idInternal);

        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlanWithDegree
                .newInfoFromDomain(degreeCurricularPlan);

        return infoDegreeCurricularPlan;
    }
}