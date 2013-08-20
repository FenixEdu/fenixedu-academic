/*
 * Created on 20/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 * 
 */
public class ViewReimbursementGuide {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static InfoReimbursementGuide run(String reimbursementGuideId) throws FenixServiceException {

        ReimbursementGuide reimbursementGuide = AbstractDomainObject.fromExternalId(reimbursementGuideId);
        if (reimbursementGuide == null) {
            throw new NonExistingServiceException();
        }

        return InfoReimbursementGuide.newInfoFromDomain(reimbursementGuide);
    }

}