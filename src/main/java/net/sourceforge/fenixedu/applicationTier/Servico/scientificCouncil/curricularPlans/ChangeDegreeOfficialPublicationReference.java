package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeDegreeOfficialPublicationReference {

    /**
     * Must ensure "REQUIRED" slots are filled
     * 
     * @param degree
     * @param date
     * @throws FenixServiceException
     */
    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static void run(DegreeOfficialPublication degreeOfficialPublication, String officialReference)
            throws FenixServiceException {

        if (degreeOfficialPublication == null || officialReference == null) {
            throw new InvalidArgumentsServiceException();
        }

        degreeOfficialPublication.setOfficialReference(officialReference);
    }

}