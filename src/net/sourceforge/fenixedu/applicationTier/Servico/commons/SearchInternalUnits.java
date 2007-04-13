package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitClassification;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

public class SearchInternalUnits extends AbstractSearchObjects {

    public Collection<Unit> run(Class type, String value, int limit, Map<String, String> arguments) {
	List<Unit> units = UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEPARTMENT);
	units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEGREE_UNIT));	
	units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.SECTION));
	units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.SCIENTIFIC_AREA));	
	units.addAll(UnitUtils.readAllActiveUnitsByClassification(UnitClassification.ASSOCIATED_LABORATORY));	
	units.addAll(UnitUtils.readAllActiveUnitsByClassification(UnitClassification.SCIENCE_INFRASTRUCTURE));
	units.addAll(UnitUtils.readAllActiveUnitsByClassification(UnitClassification.RESEARCH_UNIT));
	return super.process(units, value, limit, arguments);
    }

}
