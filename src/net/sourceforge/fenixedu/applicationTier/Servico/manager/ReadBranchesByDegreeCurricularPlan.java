/*
 * Created on 22/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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