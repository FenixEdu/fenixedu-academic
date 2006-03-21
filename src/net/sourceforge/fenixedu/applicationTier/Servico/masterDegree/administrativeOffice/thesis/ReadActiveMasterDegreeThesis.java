/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.GenericTrio;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.Predicate;
import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveMasterDegreeThesis extends Service {

    private static Predicate classificationPredicate = new Predicate() {
        public boolean evaluate(Object object) {
            if (object instanceof GenericTrio) {
                GenericTrio<MasterDegreeThesis, MasterDegreeClassification, Integer> trio = (GenericTrio<MasterDegreeThesis, MasterDegreeClassification, Integer>) object;
                MasterDegreeThesis masterDegreeThesis = trio.getFirst();
                MasterDegreeClassification masterDegreeClassification = trio.getSecond();
                if (masterDegreeThesis.getActiveMasterDegreeProofVersion() != null) {
                    return (masterDegreeThesis.getActiveMasterDegreeProofVersion().getFinalResult()
                            .equals(masterDegreeClassification));
                }
            }
            return false;
        }
    };

    private static Predicate yearPredicate = new Predicate() {
        public boolean evaluate(Object object) {
            if (object instanceof GenericTrio) {
                GenericTrio<MasterDegreeThesis, MasterDegreeClassification, Integer> trio = (GenericTrio<MasterDegreeThesis, MasterDegreeClassification, Integer>) object;
                MasterDegreeThesis masterDegreeThesis = trio.getFirst();
                Integer year = trio.getThird();
                if (masterDegreeThesis.getActiveMasterDegreeProofVersion() != null
                        && masterDegreeThesis.getActiveMasterDegreeProofVersion().getProofDate() != null) {
                    return (new YearMonthDay(masterDegreeThesis.getActiveMasterDegreeProofVersion()
                            .getProofDate().getTime()).getYear() == year);
                }
            }
            return false;
        }
    };

    public Collection<MasterDegreeThesis> run(MasterDegreeClassification classification, Integer year)
            throws ExcepcaoPersistencia {

        Collection<Predicate> predicates = new ArrayList<Predicate>();

        if (classification != null) {
            predicates.add(classificationPredicate);
        }

        if (year != null) {
            predicates.add(yearPredicate);
        }

        List<MasterDegreeThesis> masterDegreeThesis = rootDomainObject.getMasterDegreeThesiss();

        List<MasterDegreeThesis> result = new ArrayList<MasterDegreeThesis>();
        for (MasterDegreeThesis thesis : masterDegreeThesis) {

            boolean isToAdd = true;
            for (Predicate predicate : predicates) {
                isToAdd &= predicate
                        .evaluate(new GenericTrio<MasterDegreeThesis, MasterDegreeClassification, Integer>(
                                thesis, classification, year));
            }

            if (isToAdd) {
                result.add(thesis);
            }
        }

        return result;
    }

}
