package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class CreateDegreeOfficialPublication {

    /**
     * Must ensure "REQUIRED" slots are filled
     * 
     * @param degree
     * @param date
     * @throws FenixServiceException
     */
    @Atomic
    public static void run(Degree degree, LocalDate date, String officialReference) throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        if (degree == null || date == null) {
            throw new InvalidArgumentsServiceException();
        }

        DegreeOfficialPublication degreeOfficialPublication = new DegreeOfficialPublication(degree, date);
        degreeOfficialPublication.setOfficialReference(officialReference);

    }

}