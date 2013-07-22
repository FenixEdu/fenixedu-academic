package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.sibs.InfoSibsPaymentFileEntry;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadNonProcessedSibsEntries {

    @Atomic
    public static List run() throws FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        final List<InfoSibsPaymentFileEntry> result = new ArrayList<InfoSibsPaymentFileEntry>();
        for (final SibsPaymentFileEntry sibsPaymentFileEntry : SibsPaymentFileEntry.readNonProcessed()) {
            result.add(InfoSibsPaymentFileEntry.newInfoFromDomain(sibsPaymentFileEntry));
        }
        return result;
    }
}