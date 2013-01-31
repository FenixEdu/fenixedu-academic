package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class SearchCurriculumGroupsForRegistration extends FenixService implements AutoCompleteSearchService {

	@Override
	public Collection<CurriculumGroup> run(Class type, String value, int limit, Map<String, String> arguments) {
		return getRegistration(arguments).getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups();
	}

	private Registration getRegistration(Map<String, String> arguments) {
		return Registration.fromExternalId(arguments.get("registrationID"));
	}

}
