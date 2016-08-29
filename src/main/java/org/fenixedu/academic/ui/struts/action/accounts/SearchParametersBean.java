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
package org.fenixedu.academic.ui.struts.action.accounts;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;
import org.fenixedu.commons.StringNormalizer;

import com.google.common.base.Strings;

public class SearchParametersBean implements Serializable {
    private static final long serialVersionUID = -7514731909906016794L;

    private String username;

    private String name;

    private String documentIdNumber;

    private String email;

    private String socialSecurityNumber;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentIdNumber() {
        return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public Collection<Person> search() {
        Stream<Person> matches =
                filterSocialSecurityNumber(filterEmail(filterDocumentIdNumber(filterName(filterUsername(null)))));
        return matches == null ? Collections.emptySet() : matches.collect(Collectors.toSet());
    }

    private Stream<Person> filterUsername(Stream<Person> matches) {
        if (!Strings.isNullOrEmpty(username)) {
            if (matches == null) {
                return Stream.of(User.findByUsername(username)).filter(Objects::nonNull).map(User::getPerson);
            } else {
                return matches.filter(p -> Objects.equals(p.getUsername(), username));
            }
        }
        return matches;
    }

    private Stream<Person> filterName(Stream<Person> matches) {
        if (!Strings.isNullOrEmpty(name)) {
            if (matches == null) {
                return UserProfile.searchByName(name, Integer.MAX_VALUE).map(UserProfile::getPerson).filter(Objects::nonNull);
            } else {
                return matches.filter(p -> namesMatch(name, p.getProfile().getFullName()));
            }
        }
        return matches;
    }

    private Stream<Person> filterDocumentIdNumber(Stream<Person> matches) {
        if (!Strings.isNullOrEmpty(documentIdNumber)) {
            if (matches == null) {
                return Person.findPersonByDocumentID(documentIdNumber).stream().filter(Objects::nonNull);
            } else {
                return matches.filter(p -> Objects.equals(p.getDocumentIdNumber(), documentIdNumber));
            }
        }
        return matches;
    }

    private Stream<Person> filterEmail(Stream<Person> matches) {
        if (!Strings.isNullOrEmpty(email)) {
            if (matches == null) {
                return EmailAddress.findAllActiveAndValid(email).filter(Objects::nonNull).map(EmailAddress::getParty)
                        .filter(Objects::nonNull).filter(party -> party.isPerson()).map(Person.class::cast)
                        .filter(Objects::nonNull).filter(Objects::nonNull);
            } else {
                return matches.filter(p -> p.getEmailAddresses().stream()
                        .filter(emailAddress -> emailAddress.getValue().equals(email))
                        .filter(emailAddress -> emailAddress.isActiveAndValid()).findAny().isPresent());
            }
        }
        return matches;
    }

    private Stream<Person> filterSocialSecurityNumber(Stream<Person> matches) {
        if (!Strings.isNullOrEmpty(socialSecurityNumber)) {
            if (matches == null) {
                return Stream.of(Party.readByContributorNumber(socialSecurityNumber)).filter(Objects::nonNull)
                        .filter(p -> p instanceof Person).map(p -> (Person) p).filter(Objects::nonNull);
            } else {
                return matches.filter(p -> Objects.equals(p.getSocialSecurityNumber(), socialSecurityNumber));
            }
        }
        return matches;
    }

    private static boolean namesMatch(String query, String name) {
        List<String> nameParts =
                Arrays.asList(StringNormalizer.normalizeAndRemoveAccents(name).toLowerCase().trim().split("\\s+|-"));
        List<String> queryParts =
                Arrays.asList(StringNormalizer.normalizeAndRemoveAccents(query).toLowerCase().trim().split("\\s+|-"));
        return nameParts.containsAll(queryParts);
    }
}
