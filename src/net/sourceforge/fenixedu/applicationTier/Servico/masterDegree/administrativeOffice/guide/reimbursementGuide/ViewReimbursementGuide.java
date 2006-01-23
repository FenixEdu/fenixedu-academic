/*
 * Created on 20/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 * 
 * <br>
 * <strong>Description: </strong> <br>
 * Standard reading service using the ID to identify the object
 * 
 * 
 */
public class ViewReimbursementGuide extends Service {

    public InfoReimbursementGuide run(Integer reimbursementGuideId) throws FenixServiceException,
            ExcepcaoPersistencia {

        ReimbursementGuide reimbursementGuide = (ReimbursementGuide) persistentObject.readByOID(ReimbursementGuide.class,
                        reimbursementGuideId);

        if (reimbursementGuide == null) {
            throw new NonExistingServiceException();
        }

        return InfoReimbursementGuide.newInfoFromDomain(reimbursementGuide);

    }

}