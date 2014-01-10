/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeThesisState;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveMasterDegreeThesis {

    @Atomic
    public static Collection<MasterDegreeThesis> run(MasterDegreeThesisState thesisState, Integer year, Degree degree) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        Collection<MasterDegreeThesis> result = new ArrayList<MasterDegreeThesis>();
        for (MasterDegreeThesis masterDegreeThesis : Bennu.getInstance().getMasterDegreeThesissSet()) {

            if (masterDegreeThesis.getStudentCurricularPlan().getDegree() != degree) {
                continue;
            }

            if (year != null && thesisState != MasterDegreeThesisState.NOT_DELIVERED) {
                final MasterDegreeProofVersion proofVersion = masterDegreeThesis.getActiveMasterDegreeProofVersion();

                if (proofVersion == null) {
                    continue;
                }

                if (thesisState == MasterDegreeThesisState.CONCLUDED
                        && (proofVersion.getProofDateYearMonthDay() == null || proofVersion.getProofDateYearMonthDay().getYear() != year)) {
                    continue;
                }

                if (thesisState == MasterDegreeThesisState.DELIVERED
                        && (proofVersion.getThesisDeliveryDateYearMonthDay() == null || proofVersion
                                .getThesisDeliveryDateYearMonthDay().getYear() != year)) {
                    continue;
                }

            }

            if (masterDegreeThesis.getState() == thesisState) {
                result.add(masterDegreeThesis);
            }
        }

        return result;
    }

}