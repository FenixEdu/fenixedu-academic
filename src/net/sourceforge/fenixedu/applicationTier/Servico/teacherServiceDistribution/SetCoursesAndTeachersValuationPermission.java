package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class SetCoursesAndTeachersValuationPermission extends Service {
	public void run(
			Integer tsdId,
			Integer personId,
			Boolean coursesValuationPermission,
			Boolean teachersValuationPermission,
			Boolean coursesManagementPermission,
			Boolean teachersManagementPermission) {
		TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);
		Person person = (Person) rootDomainObject.readPartyByOID(personId);
		
		
		if(coursesValuationPermission) {
			tsd.addCourseValuationPermission(person);
		} else {
			tsd.removeCourseValuationPermission(person);
		}
				
		if(teachersValuationPermission) {
			tsd.addTeacherValuationPermission(person);
		} else {
			tsd.removeTeacherValuationPermission(person);
		}
		
		if(coursesManagementPermission) {
			tsd.addCourseManagersPermission(person);
		} else {
			tsd.removeCourseManagersPermission(person);
		}
		
		if(teachersManagementPermission) {
			tsd.addTeachersManagersPermission(person);
		} else {
			tsd.removeTeachersManagersPermission(person);
		}
	}
}
