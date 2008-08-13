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

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 * 
 */
public class ViewReimbursementGuide extends Service {

    public InfoReimbursementGuide run(Integer reimbursementGuideId) throws FenixServiceException {

	ReimbursementGuide reimbursementGuide = rootDomainObject.readReimbursementGuideByOID(reimbursementGuideId);
	if (reimbursementGuide == null) {
	    throw new NonExistingServiceException();
	}

	return InfoReimbursementGuide.newInfoFromDomain(reimbursementGuide);
    }

}
