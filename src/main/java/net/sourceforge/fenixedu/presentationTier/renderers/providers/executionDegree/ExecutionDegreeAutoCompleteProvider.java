package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.ist.fenixframework.FenixFramework;

public class ExecutionDegreeAutoCompleteProvider implements AutoCompleteProvider<ExecutionDegree> {

    @Override
    public Collection<ExecutionDegree> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final String executionYearOid = argsMap.get("executionYearOid");
        final ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearOid);

        final String searchValue = StringUtils.normalize(value);

        final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final Degree degree = degreeCurricularPlan.getDegree();
            if (match(searchValue, degreeCurricularPlan.getName())
                    || match(searchValue, degree.getNameI18N(executionYear).getContent())
                    || match(searchValue, degree.getSigla())) {
                result.add(executionDegree);
                if (result.size() >= maxCount) {
                    break;
                }
            }
        }
        return result;
    }

    private boolean match(final String stringToMatch, final String content) {
        final String normalizedContent = StringUtils.normalize(content);
        return normalizedContent.indexOf(stringToMatch) >= 0;
    }
}
