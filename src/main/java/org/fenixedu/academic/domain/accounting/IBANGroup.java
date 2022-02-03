package org.fenixedu.academic.domain.accounting;

import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.search.domain.DomainIndexSystem;

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
        setDefault();
    }

    public IBAN allocateIBAN() {
        final int i = getNextInfix();
        if (i >= 100000000) {
            throw new Error("IBAN Group has allocated all possible numbers");
        }
        final String value = calculateIBAN(String.format("0%08d", i));
        setNextInfix(i + 1);
        final int j = value.length() - 2;
        return new IBAN(this, value.substring(getPrefix().length(), j), value.substring(j));
    }

    private String calculateIBAN(final String accountNumber) {
        final String s = getPrefix() + accountNumber;
        for (int i = 99; i >= 0; i--) {
            final String iban = s + String.format("%02d", i);
            if (IBANCheckDigit.IBAN_CHECK_DIGIT.isValid(iban)) {
                return iban;
            }
        }
        throw new Error("Unable to calculate correct IBAN for " + accountNumber);
    }

    public IBAN lookup(final String ibanNumber) {
        return DomainIndexSystem.getInstance().search(ibanNumber, (index) -> index.getIBANSet().stream())
            .filter(iban -> iban.getIBANGroup() == this)
            .filter(iban -> iban.getIBANNumber().equals(ibanNumber))
            .findAny().orElse(null);
    }

    public static IBAN allocate() {
        return Bennu.getInstance().getIBANGroupSet().stream()
                .filter(group -> group.getActive())
                .map(group -> group.allocateIBAN())
                .findAny().orElse(null);
    }

    public void setDefault() {
        setActive(true);
        Bennu.getInstance().getIBANGroupSet().stream()
                .filter(group -> group != this)
                .forEach(group -> group.setActive(false));
    }
}
