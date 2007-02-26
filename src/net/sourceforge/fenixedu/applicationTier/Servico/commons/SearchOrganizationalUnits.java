package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

public class SearchOrganizationalUnits extends AbstractSearchObjects {

	public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
		
		 List<Unit> units = new ArrayList<Unit> ();
		 units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEPARTMENT));
		 units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.SCIENTIFIC_AREA));
			 		 
		 return super.process(units, value, limit, arguments);
	}

}
