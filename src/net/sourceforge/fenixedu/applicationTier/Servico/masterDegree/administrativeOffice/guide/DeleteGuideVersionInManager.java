package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideVersionInManager extends Service {

    public void run(Integer guideID) throws InvalidChangeServiceException {
        Guide guide = rootDomainObject.readGuideByOID(guideID);

        if (!guide.canBeDeleted()) {
            throw new InvalidChangeServiceException();
        }

        guide.delete();
    }

}
