package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import pt.ist.fenixframework.FenixFramework;

public class SearchCurriculumGroupsForRegistration implements AutoCompleteProvider<CurriculumGroup> {

    @Override
    public Collection<CurriculumGroup> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return getRegistration(argsMap).getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups();
    }

    private Registration getRegistration(Map<String, String> arguments) {
        return FenixFramework.getDomainObject(arguments.get("registrationID"));
    }

}
