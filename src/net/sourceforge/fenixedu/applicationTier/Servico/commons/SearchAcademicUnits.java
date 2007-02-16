package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

public class SearchAcademicUnits extends AbstractSearchObjects {
	
	public List run(Class type, String value, int limit, Map<String, String> arguments) {
		 
		
		 return super.process(UnitUtils.readAllUnitsByType(PartyTypeEnum.DEGREE_UNIT), value, limit, arguments);
	}

}
