package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.person.PersonName;

public class SearchPresidentForThesis extends Service implements AutoCompleteSearchService {

    public Collection<PersonName> run(Class type, String value, int limit, Map<String, String> arguments) {
        if (type != PersonName.class) {
            return null;
        }

        String degreeIdString = arguments.get("degree");
        if (degreeIdString == null) {
            return null;
        }

        Integer degreeId = new Integer(degreeIdString);
        Degree degree = RootDomainObject.getInstance()
                .readDegreeByOID(degreeId);

        if (degree == null) {
            return null;
        }

        List<PersonName> result = new ArrayList<PersonName>();
        for (ScientificCommission member : degree.getCurrentScientificCommissionMembers()) {
            result.add(member.getPerson().getPersonName());
        }

        return result;
    }

}
