/*
 * Created on 22/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoBranch;
import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadBranchesByDegreeCurricularPlan implements IService {

    public ReadBranchesByDegreeCurricularPlan() {
    }

    /**
     * Executes the service. Returns the current collection of infoBranches.
     */
    public List run(Integer idDegreeCurricularPlan) throws FenixServiceException {
        ISuportePersistente sp;
        List allBranches = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp
                    .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                            idDegreeCurricularPlan);
            if (degreeCurricularPlan == null) {
                throw new NonExistingServiceException();
            }
            allBranches = degreeCurricularPlan.getAreas();
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allBranches == null || allBranches.isEmpty()) {
            return null;
        }

        // build the result of this service
        Iterator iterator = allBranches.iterator();
        List result = new ArrayList(allBranches.size());

        while (iterator.hasNext()) {
            result.add(InfoBranch.newInfoFromDomain((IBranch) iterator.next()));
        }
        return result;
    }
}