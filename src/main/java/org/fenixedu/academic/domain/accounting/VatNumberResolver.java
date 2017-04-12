package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.Person;

public class VatNumberResolver {

	public static VatNumberResolver RESOLVER = new VatNumberResolver(){};

    public String uVATNumberFor(final Person person) {
        final Country country = person.getCountry();
        final String ssn = person.getSocialSecurityNumber();
        return country.getCode() + ssn;
    }

}
