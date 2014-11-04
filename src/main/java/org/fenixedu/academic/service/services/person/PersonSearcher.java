package org.fenixedu.academic.service.services.person;

import java.util.Objects;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;

import com.google.common.base.Strings;

public class PersonSearcher {
    private String name;

    private String username;

    private String query;

    private String documentIdNumber;

    private IDDocumentType idDocumentType;

    public PersonSearcher() {
    }

    public PersonSearcher name(String name) {
        this.name = name;
        return this;
    }

    public PersonSearcher username(String username) {
        this.username = username;
        return this;
    }

    public PersonSearcher bestEffortQuery(String query) {
        this.query = query;
        return this;
    }

    public PersonSearcher documentIdNumber(String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
        return this;
    }

    public PersonSearcher documentIdType(IDDocumentType idDocumentType) {
        this.idDocumentType = idDocumentType;
        return this;
    }

    public Stream<Person> search(int maxHits) {
        Stream<UserProfile> stream;
        if (!Strings.isNullOrEmpty(query)) {
            stream =
                    Stream.concat(Stream.of(User.findByUsername(query)).filter(Objects::nonNull).map(User::getProfile),
                            UserProfile.searchByName(query, Integer.MAX_VALUE)).limit(maxHits);
        } else if (!Strings.isNullOrEmpty(username)) {
            stream = Stream.of(User.findByUsername(query)).filter(Objects::nonNull).map(User::getProfile);
        } else if (!Strings.isNullOrEmpty(name)) {
            stream = UserProfile.searchByName(name, maxHits);
        } else {
            stream = Bennu.getInstance().getUserSet().stream().map(User::getProfile);
        }
        return stream.map(UserProfile::getPerson).filter(Objects::nonNull);
    }

    public Stream<Person> search() {
        return search(Integer.MAX_VALUE);
    }
}
