package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

public class SearchAcademicUnits extends AbstractSearchObjects {
	
	public List run(Class type, String value, int limit, Map<String, String> arguments) {
		 
		 List<DomainObject> list = new ArrayList<DomainObject> ();
		 list.addAll(UnitUtils.readAllUnitsByType(PartyTypeEnum.DEGREE_UNIT));
		 
		 return super.process(list, value, limit, arguments);
	}

}
