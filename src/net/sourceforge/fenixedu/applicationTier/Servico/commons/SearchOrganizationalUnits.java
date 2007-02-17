package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class SearchOrganizationalUnits extends AbstractSearchObjects {

	public List run(Class type, String value, int limit, Map<String, String> arguments) {
		 Set<Department> departments = rootDomainObject.readAllDomainObjects(Department.class);
		 List<Unit> units = new ArrayList<Unit> ();
		 for(Department department : departments) {
			 units.add(department.getDepartmentUnit());
			 units.addAll(department.getDepartmentUnit().getSubUnits(PartyTypeEnum.SCIENTIFIC_AREA));
		 }
			 		 
		 return super.process(units, value, limit, arguments);
	}

}
