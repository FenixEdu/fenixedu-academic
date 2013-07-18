package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import net.sourceforge.fenixedu.domain.DegreeSpecializationArea;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateDegreeSpecializationArea {

    /**
     * Must ensure "REQUIRED" slots are filled
     * 
     * @param degree
     * @param date
     * @throws FenixServiceException
     */
    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static void run(DegreeOfficialPublication degreeOfficialPublication, String area, String name)
            throws FenixServiceException {

        if (degreeOfficialPublication == null || area == null || name == null) {
            throw new InvalidArgumentsServiceException();
        }

        DegreeSpecializationArea specializationArea =
                new DegreeSpecializationArea(degreeOfficialPublication, new MultiLanguageString(area));
        specializationArea.setName(new MultiLanguageString(name));
        degreeOfficialPublication.addSpecializationArea(specializationArea);

    }

}