package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

public class SearchInternalUnits extends AbstractSearchObjects<Unit> implements AutoCompleteProvider<Unit> {

    @Override
    public Collection<Unit> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        List<Unit> units = UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEPARTMENT);
        units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEGREE_UNIT));
        // units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.SECTION));
        units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.SCIENTIFIC_AREA));
        // units.addAll(UnitUtils.readAllActiveUnitsByClassification(UnitClassification.ASSOCIATED_LABORATORY));
        // units.addAll(UnitUtils.readAllActiveUnitsByClassification(UnitClassification.SCIENCE_INFRASTRUCTURE));
        // units.addAll(UnitUtils.readAllActiveUnitsByClassification(UnitClassification.RESEARCH_UNIT));
        for (Iterator<Unit> iterator = units.iterator(); iterator.hasNext();) {
            Unit unit = iterator.next();
            if (unit.getUnitName().getIsExternalUnit()) {
                iterator.remove();
            }
        }

        return super.process(units, value, maxCount, argsMap);
    }

}
