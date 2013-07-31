package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.ist.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import pt.ist.fenixframework.FenixFramework;

public class SearchPresidentForThesis implements AutoCompleteProvider<PersonName> {

    @Override
    public Collection<PersonName> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {

        final String thesisIdString = argsMap.get("thesis");
        if (thesisIdString == null) {
            return null;
        }

        final Thesis thesis = FenixFramework.getDomainObject(thesisIdString);
        if (thesis == null) {
            return null;
        }

        final List<PersonName> result = new ArrayList<PersonName>();
        final Enrolment enrolment = thesis.getEnrolment();
        final DegreeCurricularPlan degreeCurricularPlan = enrolment.getDegreeCurricularPlanOfDegreeModule();
        final ExecutionYear executionYear = enrolment.getExecutionYear();
        final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
        if (executionDegree != null) {
            for (ScientificCommission member : executionDegree.getScientificCommissionMembersSet()) {
                result.add(member.getPerson().getPersonName());
            }
        }

        return result;
    }

}
