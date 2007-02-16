package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

public class SearchInternalUnits extends AbstractSearchObjects {

	public List run(Class type, String value, int limit, Map<String, String> arguments) {
		 List<Unit> units = UnitUtils.readAllUnitsByType(PartyTypeEnum.DEPARTMENT);
		 units.addAll(UnitUtils.readAllUnitsByType(PartyTypeEnum.DEGREE_UNIT));
		 units.addAll(UnitUtils.readAllUnitsByType(PartyTypeEnum.SCIENCE_INFRASTRUCTURE));
		 units.addAll(UnitUtils.readAllUnitsByType(PartyTypeEnum.RESEARCH_UNIT));
		 units.addAll(UnitUtils.readAllUnitsByType(PartyTypeEnum.SECTION));
		 units.addAll(UnitUtils.readAllUnitsByType(PartyTypeEnum.SCIENTIFIC_AREA));
		 units.addAll(UnitUtils.readAllUnitsByType(PartyTypeEnum.ASSOCIATED_LABORATORY));
		 		
		 
		 
		 return super.process(units, value, limit, arguments);
	}

}
