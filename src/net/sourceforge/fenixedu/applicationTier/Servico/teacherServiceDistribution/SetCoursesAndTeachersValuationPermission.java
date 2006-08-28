package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;

public class SetCoursesAndTeachersValuationPermission extends Service {
	public void run(
			Integer valuationGroupingId,
			Integer personId,
			Boolean coursesAndTeachersValuationPermission,
			Boolean coursesAndTeachersManagementPermission) {
		ValuationGrouping valuationGrouping = rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		Person person = (Person) rootDomainObject.readPartyByOID(personId);
				
		if(coursesAndTeachersValuationPermission) {
			valuationGrouping.addCoursesAndTeachersValuationPermission(person);
		} else {
			valuationGrouping.removeCoursesAndTeachersValuationPermission(person);
		}
				
		if(coursesAndTeachersManagementPermission) {
			valuationGrouping.addCoursesAndTeachersManagement(person);
		} else {
			valuationGrouping.removeCoursesAndTeachersManagement(person);
		}
	}
}
