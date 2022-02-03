package org.fenixedu.academic.domain.accounting;

import org.fenixedu.bennu.search.domain.DomainIndexSystem;

public class IBAN extends IBAN_Base {
    
    IBAN(final IBANGroup group, final String infix, final String suffix) {
        if (group == null || infix == null || suffix == null) {
            throw new NullPointerException();
        }
        setIBANGroup(group);
        setInfix(infix);
        setSuffix(suffix);
        DomainIndexSystem.getInstance().index(getIBANNumber(), (index) -> index.getIBANSet(), this);
    }

    public String getIBANNumber() {
        return getIBANGroup().getPrefix() + getInfix() + getSuffix();
    }

}
