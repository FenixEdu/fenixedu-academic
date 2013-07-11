package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class CreateDegreeOfficialPublication {

    /**
     * Must ensure "REQUIRED" slots are filled
     * 
     * @param degree
     * @param date
     * @throws FenixServiceException
     */
    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Atomic
    public static void run(Degree degree, LocalDate date, String officialReference) throws FenixServiceException {

        if (degree == null || date == null) {
            throw new InvalidArgumentsServiceException();
        }

        DegreeOfficialPublication degreeOfficialPublication = new DegreeOfficialPublication(degree, date);
        degreeOfficialPublication.setOfficialReference(officialReference);

    }

}