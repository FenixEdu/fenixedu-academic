/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.organizationalStructure;

import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.bennu.core.domain.Bennu;

import com.google.common.base.Strings;

public class PartySocialSecurityNumber extends PartySocialSecurityNumber_Base {

    public PartySocialSecurityNumber() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    private PartySocialSecurityNumber(final Party party, final Country fiscalCountry, final String socialSecurityNumber) {
        this();
        super.setParty(party);
        super.setFiscalCountry(fiscalCountry);
        super.setSocialSecurityNumber(socialSecurityNumber);

        checkRules();
    }

    private void checkRules() {

        if (getParty() == null) {
            throw new DomainException("error.PartySocialSecurityNumber.invalid.party");
        }

        if (!hasSocialSecurityNumber() && !hasFiscalCountry()) {
            //allow creation of persons without social security number (for instance: external persons, candidates, etc.)
            return;
        }

        if ((hasSocialSecurityNumber() && !hasFiscalCountry()) || (hasFiscalCountry() && !hasSocialSecurityNumber())) {
            throw new DomainException("error.PartySocialSecurityNumber.fiscal.information.is.not.complete");
        }

        final String defaultSocialSecurityNumber =
                FenixEduAcademicConfiguration.getConfiguration().getDefaultSocialSecurityNumber();
        if (!Strings.isNullOrEmpty(defaultSocialSecurityNumber)
                && defaultSocialSecurityNumber.equals(getSocialSecurityNumber())) {
            if (Country.readDefault() != getFiscalCountry()) {
                throw new DomainException("error.PartySocialSecurityNumber.invalid.country.for.default.social.security.number");
            }
        }

        if (Strings.isNullOrEmpty(defaultSocialSecurityNumber)
                || !defaultSocialSecurityNumber.equals(getSocialSecurityNumber())) {
            for (final PartySocialSecurityNumber otherPartySecurityNumber : Bennu.getInstance()
                    .getPartySocialSecurityNumbersSet()) {
                if (otherPartySecurityNumber == this) {
                    continue;
                }

                if (getSocialSecurityNumber().equals(otherPartySecurityNumber.getSocialSecurityNumber())) {
                    throw new DomainException("error.PartySocialSecurityNumber.number.already.exists",
                            otherPartySecurityNumber.getParty().getName());
                }
            }
        }

        if (!TreasuryBridgeAPIFactory.implementation().isValidFiscalNumber(getFiscalCountry().getCode(),
                getSocialSecurityNumber())) {
            throw new DomainException("error.PartySocialSecurityNumber.invalid.socialSecurityNumber");
        }

        if (getParty().isPerson()) {
            TreasuryBridgeAPIFactory.implementation().updateCustomer((Person) getParty(), getFiscalCountry().getCode(),
                    getSocialSecurityNumber());
        }
    }

    public boolean hasSocialSecurityNumber(String socialSecurityNumber) {
        return hasSocialSecurityNumber() && getSocialSecurityNumber().equals(socialSecurityNumber);
    }

    public void delete() {
        setFiscalCountry(null);
        setParty(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    private void edit(final Country fiscalCountry, final String socialSecurityNumber) {

        if (hasFiscalCountry() && fiscalCountry == null) {
            throw new DomainException("error.PartySocialSecurityNumber.cannot.remove.fiscal.country");
        }

        if (hasSocialSecurityNumber() && Strings.isNullOrEmpty(socialSecurityNumber)) {
            throw new DomainException("error.PartySocialSecurityNumber.cannot.remove.social.security.number");
        }

        setFiscalCountry(fiscalCountry);
        setSocialSecurityNumber(socialSecurityNumber);

        checkRules();
    }

    protected boolean hasFiscalCountry() {
        return getFiscalCountry() != null;
    }

    protected boolean hasSocialSecurityNumber() {
        return !Strings.isNullOrEmpty(getSocialSecurityNumber());
    }

    // @formatter:off
    /* ********
     * SERVICES
     * ********
     */
    // @formatter:on

    public static PartySocialSecurityNumber editFiscalInformation(final Party party, final Country fiscalCountry,
            final String socialSecurityNumber) {
        final PartySocialSecurityNumber partySocialSecurityNumber = party.getPartySocialSecurityNumber();

        if (partySocialSecurityNumber == null) {
            return new PartySocialSecurityNumber(party, fiscalCountry, socialSecurityNumber);
        }

        partySocialSecurityNumber.edit(fiscalCountry, socialSecurityNumber);

        return partySocialSecurityNumber;
    }

    public static Party readPartyBySocialSecurityNumber(final String socialSecurityNumber) {
        if (socialSecurityNumber == null || socialSecurityNumber.length() == 0) {
            return null;
        }
        for (final PartySocialSecurityNumber securityNumber : Bennu.getInstance().getPartySocialSecurityNumbersSet()) {
            if (securityNumber.hasSocialSecurityNumber(socialSecurityNumber)) {
                return securityNumber.getParty();
            }
        }
        return null;
    }

}
