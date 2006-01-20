/*
 * Created on 1/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlan extends Service {

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

		final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentSupport.getIPersistentDegreeCurricularPlan()
                .readByOID(DegreeCurricularPlan.class, idInternal);

        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(degreeCurricularPlan);
    }
}