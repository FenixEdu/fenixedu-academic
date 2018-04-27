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
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class AlumniIdentityCheckRequest extends AlumniIdentityCheckRequest_Base {

    public AlumniIdentityCheckRequest(String contactEmail, String documentIdNumber, String fullName,
            YearMonthDay dateOfBirthYearMonthDay, String districtOfBirth, String districtSubdivisionOfBirth,
            String parishOfBirth, final Country fiscalCountry, String socialSecurityNumber, String nameOfFather, 
            String nameOfMother, AlumniRequestType requestType) {
        super();

        checkParameters(contactEmail, documentIdNumber, fullName, dateOfBirthYearMonthDay, districtOfBirth,
                districtSubdivisionOfBirth, parishOfBirth, socialSecurityNumber, nameOfFather, nameOfMother, requestType);

        setContactEmail(contactEmail.trim());
        setDocumentIdNumber(documentIdNumber.trim());
        setFullName(fullName.trim());
        setDateOfBirthYearMonthDay(dateOfBirthYearMonthDay);
        setDistrictOfBirth(districtOfBirth.trim());
        setDistrictSubdivisionOfBirth(districtSubdivisionOfBirth.trim());
        setParishOfBirth(parishOfBirth.trim());
        setFiscalCountry(fiscalCountry);
        setSocialSecurityNumber(socialSecurityNumber);
        setNameOfFather(nameOfFather.trim());
        setNameOfMother(nameOfMother.trim());
        setCreationDateTime(new DateTime());
        setRequestType(requestType);
        setRequestToken(UUID.randomUUID());

        setRootDomainObject(Bennu.getInstance());
    }

    private void checkParameters(String contactEmail, String documentIdNumber, String fullName,
            YearMonthDay dateOfBirthYearMonthDay, String districtOfBirth, String districtSubdivisionOfBirth,
            String parishOfBirth, String socialSecurityNumber, String nameOfFather, String nameOfMother,
            AlumniRequestType requestType) {

        if (StringUtils.isEmpty(contactEmail)) {
            throw new DomainException("alumni.identity.request.contactEmail.null");
        }

        if (StringUtils.isEmpty(documentIdNumber)) {
            throw new DomainException("alumni.identity.request.documentIdNumber.null");
        }

        if (requestType == null) {
            throw new DomainException("alumni.identity.request.requestType.null");
        }

    }

    public static boolean hasPendingRequestsForDocumentNumber(String documentIdNumber) {
        for (AlumniIdentityCheckRequest request : Bennu.getInstance().getAlumniIdentityRequestSet()) {
            if (request.getDocumentIdNumber().equals(documentIdNumber)) {
                return true;
            }
        }
        return false;
    }

    public static Collection<AlumniIdentityCheckRequest> readPendingRequests() {
        Collection<AlumniIdentityCheckRequest> pendingRequests = new ArrayList<AlumniIdentityCheckRequest>();
        Set<AlumniIdentityCheckRequest> requests = Bennu.getInstance().getAlumniIdentityRequestSet();

        AlumniIdentityCheckRequest request;
        Iterator iter = requests.iterator();
        while (iter.hasNext()) {
            request = (AlumniIdentityCheckRequest) iter.next();
            if (request.getApproved() == null) {
                pendingRequests.add(request);
            }
        }
        return pendingRequests;
    }

    public static Object readClosedRequests() {
        Collection<AlumniIdentityCheckRequest> pendingRequests = new ArrayList<AlumniIdentityCheckRequest>();

        AlumniIdentityCheckRequest request;
        Iterator<AlumniIdentityCheckRequest> iter = Bennu.getInstance().getAlumniIdentityRequestSet().iterator();
        while (iter.hasNext()) {
            request = iter.next();
            if (request.getApproved() != null) {
                pendingRequests.add(request);
            }
        }
        return pendingRequests;
    }

    public boolean isValid() {
        // ugly: refactor
        Person person = getAlumni().getStudent().getPerson();
        return (!StringUtils.isEmpty(person.getName()) && person.getName().equals(getFullName()))
                && (person.getDateOfBirthYearMonthDay().equals(getDateOfBirthYearMonthDay()))
                && (!StringUtils.isEmpty(person.getDistrictOfBirth()) && person.getDistrictOfBirth().equals(getDistrictOfBirth()))
                && (!StringUtils.isEmpty(person.getDistrictSubdivisionOfBirth()) && person.getDistrictSubdivisionOfBirth()
                        .equals(getDistrictSubdivisionOfBirth()))
                && (!StringUtils.isEmpty(person.getParishOfBirth()) && person.getParishOfBirth().equals(getParishOfBirth()))
                && (person.getFiscalCountry() != null && getFiscalCountry() == person.getFiscalCountry())
                && (!StringUtils.isEmpty(person.getSocialSecurityNumber()) && person.getSocialSecurityNumber().equals(
                        getSocialSecurityNumber()))
                && (!StringUtils.isEmpty(person.getNameOfFather()) && person.getNameOfFather().equals(getNameOfFather()))
                && (!StringUtils.isEmpty(person.getNameOfMother()) && person.getNameOfMother().equals(getNameOfMother()));
    }

    public boolean isApproved() {
        return Boolean.TRUE.equals(getApproved());
    }

    public boolean isPending() {
        return getApproved() == null;
    }

    public void validate(Boolean approval) {
        setApproved(approval);
        setDecisionDateTime(new DateTime());
    }

    public void validate(Boolean approval, Person operator) {
        validate(approval);
        setOperator(operator);
    }

    @Deprecated
    public java.util.Date getDateOfBirth() {
        org.joda.time.YearMonthDay ymd = getDateOfBirthYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setDateOfBirth(java.util.Date date) {
        if (date == null) {
            setDateOfBirthYearMonthDay(null);
        } else {
            setDateOfBirthYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getCreation() {
        org.joda.time.DateTime dt = getCreationDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setCreation(java.util.Date date) {
        if (date == null) {
            setCreationDateTime(null);
        } else {
            setCreationDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Date getDecision() {
        org.joda.time.DateTime dt = getDecisionDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setDecision(java.util.Date date) {
        if (date == null) {
            setDecisionDateTime(null);
        } else {
            setDecisionDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

}
