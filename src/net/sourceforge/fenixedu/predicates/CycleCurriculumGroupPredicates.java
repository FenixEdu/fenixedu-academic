package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class CycleCurriculumGroupPredicates {

	public static final AccessControlPredicate<CycleCurriculumGroup> MANAGE_CONCLUSION_PROCESS =
			new AccessControlPredicate<CycleCurriculumGroup>() {

				@Override
				public boolean evaluate(final CycleCurriculumGroup cycleCurriculumGroup) {
					final Person person = AccessControl.getPerson();

					if (person.hasRole(RoleType.MANAGER)) {
						return true;
					}

					return AcademicAuthorizationGroup.getProgramsForOperation(person, AcademicOperationType.MANAGE_CONCLUSION)
							.contains(cycleCurriculumGroup.getRegistration().getDegree());
				}
			};

}
