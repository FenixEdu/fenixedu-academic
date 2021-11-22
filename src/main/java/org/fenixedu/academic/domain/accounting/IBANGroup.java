package org.fenixedu.academic.domain.accounting;

import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;
import org.fenixedu.bennu.core.domain.Bennu;

public class IBANGroup extends IBANGroup_Base {
    
    public IBANGroup(final String prefix) {
        if (prefix == null) {
            throw new NullPointerException("IBAN Prefix cannot be null");
        }
        if (prefix.isEmpty()) {
            throw new Error("IBAN prefix cannot be empty: " + prefix);
        }
        if (Bennu.getInstance().getIBANGroupSet().stream().anyMatch(group -> group.getPrefix().equals(prefix))) {
            throw new Error("IBAN Group for this prefix already exists: " + prefix);
        }
        setBennu(Bennu.getInstance());
        setPrefix(prefix);

        for (int i = 0; i < 1000000000; i++) {
            final String infix = String.format("%09d", i);
            final String suffix;
            try {
                suffix = IBANCheckDigit.IBAN_CHECK_DIGIT.calculate(infix);
            } catch (final CheckDigitException e) {
                throw new Error(e);
            }
            new IBAN(this, infix, suffix);
        }
    }

}
