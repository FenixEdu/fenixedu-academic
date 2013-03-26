package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class SearchInternalPersonsByNameHavingTeacherOrIsResearcher extends SearchParties {

    @Override
    protected Collection<PersonName> search(String value, int size) {
        final Collection<PersonName> result = new HashSet<PersonName>();
        for (final PersonName personName : PersonName.findInternalPerson(value, size)) {
            if (personName.getPerson().hasUser()
                    && (personName.getPerson().hasTeacher() || personName.getPerson().hasRole(RoleType.RESEARCHER))) {
                result.add(personName);
            }
        }
        return result;
    }

}
