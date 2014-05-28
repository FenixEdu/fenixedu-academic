/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

public abstract class IndividualCandidacyPersonalDetails extends IndividualCandidacyPersonalDetails_Base {
    static final public Comparator<IndividualCandidacyPersonalDetails> COMPARATOR_BY_NAME =
            new Comparator<IndividualCandidacyPersonalDetails>() {
                @Override
                public int compare(IndividualCandidacyPersonalDetails o1, IndividualCandidacyPersonalDetails o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            };

    static final public Comparator<IndividualCandidacyPersonalDetails> COMPARATOR_BY_NAME_AND_ID =
            new Comparator<IndividualCandidacyPersonalDetails>() {
                @Override
                public int compare(final IndividualCandidacyPersonalDetails o1, final IndividualCandidacyPersonalDetails o2) {
                    final ComparatorChain comparatorChain = new ComparatorChain();
                    comparatorChain.addComparator(COMPARATOR_BY_NAME);
                    comparatorChain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);

                    return comparatorChain.compare(o1, o2);
                }
            };

    public IndividualCandidacyPersonalDetails() {
        super();
    }

    protected Bennu getRootDomainObject() {
        return getCandidacy().getRootDomainObject();
    }

    public abstract boolean isInternal();

    public abstract void edit(PersonBean personBean);

    public abstract void editPublic(PersonBean personBean);

    public abstract void ensurePersonInternalization();

    public static void createDetails(IndividualCandidacy candidacy, IndividualCandidacyProcessBean bean) {
        if (bean.getInternalPersonCandidacy()) {
            Person person = bean.getPersonBean().getPerson();
            bean.getPersonBean().setPerson(person);
            new IndividualCandidacyInternalPersonDetails(candidacy, person);
        } else {
            new IndividualCandidacyExternalPersonDetails(candidacy, bean);
        }
    }

    public boolean hasStudent() {
        return getStudent() != null;
    }

    public abstract Student getStudent();

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Gender getGender();

    public abstract void setGender(Gender gender);

    public abstract String getProfession();

    public abstract void setProfession(String profession);

    public abstract MaritalStatus getMaritalStatus();

    public abstract void setMaritalStatus(MaritalStatus status);

    public abstract YearMonthDay getDateOfBirthYearMonthDay();

    public abstract void setDateOfBirthYearMonthDay(YearMonthDay birthday);

    /*
     * FIXME ANIL : Change to get/setNationality()
     */
    public abstract Country getCountry();

    public abstract void setCountry(Country country);

    /**
     * Return the Social Security Number
     * 
     * This method, in the context of candidacies, is used to obtain VAT which
     * is not Social Security Number
     * 
     * @see #getFiscalCode()
     */
    /*
     * 08/05/2009 - After all social security number and fiscal code are the
     * same thing.
     */
    public abstract String getSocialSecurityNumber();

    public abstract void setSocialSecurityNumber(String number);

    /**
     * Returns the VAT (fiscal code) associated to a candidate
     * 
     */
    /*
     * 08/05/2009 - Use Social Security Number instead
     */
    @Deprecated
    public abstract String getFiscalCode();

    @Deprecated
    public abstract void setFiscalCode(String value);

    /*
     * -- PERSON IDENTIFICATION DOCUMENT
     */
    public abstract String getDocumentIdNumber();

    public abstract void setDocumentIdNumber(String documentIdNumber);

    public abstract IDDocumentType getIdDocumentType();

    public abstract void setIdDocumentType(IDDocumentType type);

    public abstract YearMonthDay getEmissionDateOfDocumentIdYearMonthDay();

    public abstract void setEmissionDateOfDocumentIdYearMonthDay(YearMonthDay date);

    public abstract YearMonthDay getExpirationDateOfDocumentIdYearMonthDay();

    public abstract void setExpirationDateOfDocumentIdYearMonthDay(YearMonthDay date);

    public abstract String getEmissionLocationOfDocumentId();

    public abstract void setEmissionLocationOfDocumentId(String location);

    /*
     * -- PERSON IDENTIFICATION DOCUMENT
     */

    /*
     * -- PERSON CONTACTS --
     * 
     * FIXME: See what contacts are filled in Administrative Office candidacy
     * submission
     */

    public abstract String getTelephoneContact();

    public abstract void setTelephoneContact(String telephoneContact);

    public abstract String getEmail();

    public abstract void setEmail(String email);

    /*
     * -- END PERSON CONTACTS --
     */

    /*
     * 06/04/2009 - The next four methods will replace the method
     * getDefaultPhysicalAddress() The reason for partitioning is that
     * IndividualCandidacyExternalPersonDetails cannot be associated to a
     * PhysicalAddress because its not a Party
     */

    public abstract Country getCountryOfResidence();

    public abstract void setCountryOfResidence(Country country);

    public abstract String getAddress();

    public abstract void setAddress(String address);

    public abstract String getArea();

    public abstract void setArea(String area);

    public abstract String getAreaCode();

    public abstract void setAreaCode(String areaCode);

    public abstract String getAreaOfAreaCode();

    public abstract void setAreaOfAreaCode(String areaOfAreaCode);

    /**
     * Returns the default address associated to a candidate
     * 
     * IndividualCandidacyExternalPersonDetails is not associated to Person. The
     * calls of this method should be replaced by the following methods:
     * 
     * @see #getAddress()
     * @see #getArea()
     * @see #getAreaCode()
     * @see #getAreaOfAreaCode()
     * 
     */
    @Deprecated
    public abstract PhysicalAddress getDefaultPhysicalAddress();

    public abstract Boolean isEmployee();

    public abstract Boolean hasAnyRole();

    public abstract String getEidentifier();

    @Deprecated
    public boolean hasCandidacy() {
        return getCandidacy() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
