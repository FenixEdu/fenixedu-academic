package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class FixSibsEntryByID {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(Integer sibsEntryId) throws FenixServiceException {
        SibsPaymentFileEntry sibsPaymentFileEntry = AbstractDomainObject.fromExternalId(sibsEntryId);
        if (sibsPaymentFileEntry == null) {
            throw new FenixServiceException();
        }

        sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.PROCESSED_PAYMENT);
    }

}