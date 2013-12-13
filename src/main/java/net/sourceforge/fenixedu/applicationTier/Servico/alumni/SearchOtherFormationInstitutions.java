package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import pt.ist.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

public class SearchOtherFormationInstitutions implements AutoCompleteProvider<AcademicalInstitutionUnit> {

    @Override
    public Collection<AcademicalInstitutionUnit> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {

        value = value.toLowerCase();
        List<AcademicalInstitutionUnit> result = new ArrayList<AcademicalInstitutionUnit>();

        // TODO filter by foreign units only
        for (AcademicalInstitutionUnit unit : AcademicalInstitutionUnit.readOtherAcademicUnits()) {
            if (unit.getName().toLowerCase().contains(value)) {
                result.add(unit);
            }

            if (result.size() >= maxCount) {
                break;
            }
        }

        return result;
    }
}
