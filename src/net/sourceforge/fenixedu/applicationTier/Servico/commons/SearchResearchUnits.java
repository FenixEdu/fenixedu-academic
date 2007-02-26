package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

public class SearchResearchUnits extends AbstractSearchObjects {

	public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
	
		 List<Unit> units = UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.SCIENCE_INFRASTRUCTURE);
		 units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.RESEARCH_UNIT));
		 units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.ASSOCIATED_LABORATORY));
		 	
		 return super.process(units, value, limit, arguments);
	}
}
