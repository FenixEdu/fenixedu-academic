/*
 * Created on 1/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegreeCurricularPlan;
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
     */
    public InfoDegreeCurricularPlan run(Integer idInternal) throws FenixServiceException {
        IDegreeCurricularPlan degreeCurricularPlan;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            degreeCurricularPlan = (IDegreeCurricularPlan) sp.getIPersistentDegreeCurricularPlan()
                    .readByOID(DegreeCurricularPlan.class, idInternal);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                .newInfoFromDomain(degreeCurricularPlan);

        return infoDegreeCurricularPlan;
    }
}