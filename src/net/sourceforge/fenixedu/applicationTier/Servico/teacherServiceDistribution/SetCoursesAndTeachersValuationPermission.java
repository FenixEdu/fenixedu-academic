package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class SetCoursesAndTeachersValuationPermission extends Service {
	public void run(
			Integer tsdId,
			Integer personId,
			Boolean coursesAndTeachersValuationPermission,
			Boolean coursesAndTeachersManagementPermission) {
		TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);
		Person person = (Person) rootDomainObject.readPartyByOID(personId);
				
		if(coursesAndTeachersValuationPermission) {
			tsd.addCoursesAndTeachersValuationPermission(person);
		} else {
			tsd.removeCoursesAndTeachersValuationPermission(person);
		}
				
		if(coursesAndTeachersManagementPermission) {
			tsd.addCoursesAndTeachersManagement(person);
		} else {
			tsd.removeCoursesAndTeachersManagement(person);
		}
	}
}
