package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Locale;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DegreeSpecializationArea;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ModifyDegreeSpecializationAreaName {

    /**
     * Must ensure "REQUIRED" slots are filled
     * 
     * @param degree
     * @param date
     * @throws FenixServiceException
     */
    @Atomic
    public static void run(DegreeSpecializationArea specializationArea, Locale language, String newName)
            throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        if (specializationArea == null || language == null || newName == null) {
            throw new InvalidArgumentsServiceException();
        }

        specializationArea.setName(specializationArea.getName().with(language, newName));
    }

}