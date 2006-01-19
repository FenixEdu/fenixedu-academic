package net.sourceforge.fenixedu.applicationTier.Servico.commons.degree;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * modified by:
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 */
public class ReadNumerusClausus implements IService {

    public Integer run(Integer degreeCurricularPlanID) throws NonExistingServiceException, ExcepcaoPersistencia {

        DegreeCurricularPlan degreeCurricularPlan = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = sp
                .getIPersistentDegreeCurricularPlan();
        degreeCurricularPlan = (DegreeCurricularPlan) degreeCurricularPlanDAO.readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return degreeCurricularPlan.getNumerusClausus();
    }

}