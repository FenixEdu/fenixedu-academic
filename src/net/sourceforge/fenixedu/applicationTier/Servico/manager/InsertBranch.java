/*
 * Created on 17/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author lmac1
 */

public class InsertBranch extends Service {

    public void run(InfoBranch infoBranch) throws ExcepcaoPersistencia, NonExistingServiceException {

        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
                .getIPersistentDegreeCurricularPlan();
        final Integer degreeCurricularPlanId = infoBranch.getInfoDegreeCurricularPlan().getIdInternal();
        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentDegreeCurricularPlan
                .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);

        if (degreeCurricularPlan == null)
            throw new NonExistingServiceException();

        DomainFactory.makeBranch(infoBranch.getName(), infoBranch.getNameEn(), infoBranch.getCode(),
                degreeCurricularPlan);
    }

}
