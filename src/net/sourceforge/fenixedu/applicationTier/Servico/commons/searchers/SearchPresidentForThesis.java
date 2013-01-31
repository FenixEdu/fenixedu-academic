package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class SearchPresidentForThesis extends FenixService implements AutoCompleteSearchService {

	@Override
	public Collection<PersonName> run(final Class type, final String value, final int limit, final Map<String, String> arguments) {
		if (type != PersonName.class) {
			return null;
		}

		final String thesisIdString = arguments.get("thesis");
		if (thesisIdString == null) {
			return null;
		}

		final Integer thesisId = new Integer(thesisIdString);
		final Thesis thesis = rootDomainObject.readThesisByOID(thesisId);
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
