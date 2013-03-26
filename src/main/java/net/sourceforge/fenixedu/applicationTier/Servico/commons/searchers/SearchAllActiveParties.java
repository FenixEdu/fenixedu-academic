package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

import org.joda.time.YearMonthDay;

public class SearchAllActiveParties extends SearchParties {

    @Override
    protected Collection search(String value, int size) {
        ArrayList<Party> result = new ArrayList<Party>();

        if (value.length() > 3 && value.substring(0, 3).equals("ist")) {
            Person person = Person.readPersonByIstUsername(value);
            if (person != null) {
                result.add(person);
            }
        } else {
            result.addAll(Person.findPerson(value, size));
        }

        YearMonthDay currentDate = new YearMonthDay();
        for (UnitName unitName : UnitName.find(value, size)) {
            if (unitName.getUnit().isActive(currentDate)) {
                result.add(unitName.getUnit());
            }
        }

        return result;
    }

}
