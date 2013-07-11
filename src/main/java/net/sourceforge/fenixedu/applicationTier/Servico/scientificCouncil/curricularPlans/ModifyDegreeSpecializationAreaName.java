package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DegreeSpecializationArea;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ModifyDegreeSpecializationAreaName {

    /**
     * Must ensure "REQUIRED" slots are filled
     * 
     * @param degree
     * @param date
     * @throws FenixServiceException
     */
    @Atomic
    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    public static void run(DegreeSpecializationArea specializationArea, Language language, String newName)
            throws FenixServiceException {

        if (specializationArea == null || language == null || newName == null) {
            throw new InvalidArgumentsServiceException();
        }

        specializationArea.setName(specializationArea.getName().with(language, newName));
    }

}