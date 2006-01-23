package net.sourceforge.fenixedu.applicationTier.Servico.commons.degree;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * modified by:
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 */
public class ReadNumerusClausus extends Service {

    public Integer run(Integer degreeCurricularPlanID) throws NonExistingServiceException, ExcepcaoPersistencia {

        DegreeCurricularPlan degreeCurricularPlan = null;

        degreeCurricularPlan = (DegreeCurricularPlan) persistentObject.readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return degreeCurricularPlan.getNumerusClausus();
    }

}