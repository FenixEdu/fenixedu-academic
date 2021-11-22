package org.fenixedu.academic.domain.accounting;

public class IBAN extends IBAN_Base {
    
    IBAN(final IBANGroup group, final String infix, final String suffix) {
        if (group == null || infix == null || suffix == null) {
            throw new NullPointerException();
        }
        setIBANGroup(group);
        setIBANGroupFromAvailable(group);
        setInfix(infix);
        setSuffix(suffix);
    }
    
}
