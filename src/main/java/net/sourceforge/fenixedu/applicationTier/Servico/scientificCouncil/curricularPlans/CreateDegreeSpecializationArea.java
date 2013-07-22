package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import net.sourceforge.fenixedu.domain.DegreeSpecializationArea;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateDegreeSpecializationArea {

    /**
     * Must ensure "REQUIRED" slots are filled
     * 
     * @param degree
     * @param date
     * @throws FenixServiceException
     */
    @Atomic
    public static void run(DegreeOfficialPublication degreeOfficialPublication, String area, String name)
            throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        if (degreeOfficialPublication == null || area == null || name == null) {
            throw new InvalidArgumentsServiceException();
        }

        DegreeSpecializationArea specializationArea =
                new DegreeSpecializationArea(degreeOfficialPublication, new MultiLanguageString(area));
        specializationArea.setName(new MultiLanguageString(name));
        degreeOfficialPublication.addSpecializationArea(specializationArea);

    }

}