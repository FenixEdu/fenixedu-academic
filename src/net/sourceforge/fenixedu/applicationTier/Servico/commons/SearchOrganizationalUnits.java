package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

public class SearchOrganizationalUnits extends AbstractSearchObjects {

	public List run(Class type, String value, int limit, Map<String, String> arguments) {
		 List<Unit> units = UnitUtils.readAllUnitsByType(PartyTypeEnum.DEPARTMENT);
		 units.addAll(UnitUtils.readAllUnitsByType(PartyTypeEnum.SCIENTIFIC_AREA));
		 
		 List<DomainObject> list = new ArrayList<DomainObject> ();
		 list.addAll(units);
		 
		 return super.process(list, value, limit, arguments);
	}

}
