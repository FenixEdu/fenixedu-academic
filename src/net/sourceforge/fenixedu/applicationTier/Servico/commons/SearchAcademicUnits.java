package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class SearchAcademicUnits extends AbstractSearchObjects {
	
	public List run(Class type, String value, int limit, Map<String, String> arguments) {
		 
		Set<Degree> degrees = rootDomainObject.readAllDomainObjects(Degree.class);
		List<Unit> units = new ArrayList<Unit> ();
		for(Degree degree: degrees) {
			if(degree.hasUnit()) {
				units.add(degree.getUnit());
			}
		}
		
		return super.process(units, value, limit, arguments);
	}

}
