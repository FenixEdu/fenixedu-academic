package net.sourceforge.fenixedu.applicationTier.Servico.commons.degree;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 *         modified by:
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 */
public class ReadNumerusClausus {

    @Service
    public static Integer run(Integer degreeCurricularPlanID) throws NonExistingServiceException {

        DegreeCurricularPlan degreeCurricularPlan = null;

        degreeCurricularPlan = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanID);

        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return degreeCurricularPlan.getNumerusClausus();
    }

}