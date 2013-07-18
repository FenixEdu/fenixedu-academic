package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AutoCompleteProvider;

public class SearchCurriculumGroupsForRegistration implements AutoCompleteProvider<CurriculumGroup> {

    @Override
    public Collection<CurriculumGroup> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return getRegistration(argsMap).getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups();
    }

    private Registration getRegistration(Map<String, String> arguments) {
        return Registration.fromExternalId(arguments.get("registrationID"));
    }

}
