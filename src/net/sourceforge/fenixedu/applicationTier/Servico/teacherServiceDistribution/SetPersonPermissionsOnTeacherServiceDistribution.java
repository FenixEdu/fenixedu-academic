package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupDifference;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class SetPersonPermissionsOnTeacherServiceDistribution extends Service {
	public void run(
			Integer teacherServiceDistributionId, 
			Integer personId, 
			Boolean phaseManagementPermission,
			Boolean automaticValuationPermission,
			Boolean omissionConfigurationPermission,
			Boolean valuationCompetenceCoursesAndTeachersManagementPermission) {
		
		TeacherServiceDistribution teacherServiceDistribution = rootDomainObject.readTeacherServiceDistributionByOID(teacherServiceDistributionId);
		Person person = (Person) rootDomainObject.readPartyByOID(personId);
		
		if(phaseManagementPermission) {
			teacherServiceDistribution.addPhasesManagementPermission(person);
		} else {
			teacherServiceDistribution.removePhasesManagementPermission(person);
		}
		
		if(automaticValuationPermission) {
			teacherServiceDistribution.addAutomaticValuationPermission(person);
		} else {
			teacherServiceDistribution.removeAutomaticValuationPermission(person);
		}
		
		if(omissionConfigurationPermission) {
			teacherServiceDistribution.addOmissionConfigurationPermission(person);
		} else {
			teacherServiceDistribution.removeOmissionConfigurationPermission(person);
		}
		
		if(valuationCompetenceCoursesAndTeachersManagementPermission) {
			teacherServiceDistribution.addValuationCompetenceCoursesAndTeachersManagement(person);
		} else {
			teacherServiceDistribution.removeValuationCompetenceCoursesAndTeachersManagement(person);
		}	
	}
}
