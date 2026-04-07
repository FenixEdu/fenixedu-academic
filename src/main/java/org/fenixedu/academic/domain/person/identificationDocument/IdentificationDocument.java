package org.fenixedu.academic.domain.person.identificationDocument;

import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

public class IdentificationDocument extends IdentificationDocument_Base {

    protected IdentificationDocument() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public static IdentificationDocument create(final Person person, final String value,
            final IdentificationDocumentType identificationDocumentType) {
        final IdentificationDocument identificationDocument = new IdentificationDocument();
        identificationDocument.setPerson(person);
        identificationDocument.setValue(value);
        identificationDocument.setIdentificationDocumentType(identificationDocumentType);

        if (person != null) {
            identificationDocument.setEmissionDateOfDocumentIdYearMonthDay(person.getEmissionDateOfDocumentIdYearMonthDay());
            identificationDocument.setEmissionLocationOfDocumentId(person.getEmissionLocationOfDocumentId());
            identificationDocument.setExpirationDateOfDocumentIdYearMonthDay(person.getExpirationDateOfDocumentIdYearMonthDay());
        }

        return identificationDocument;
    }

    public void delete() {
        setPerson(null);
        setIdentificationDocumentType(null);

        setRootDomainObject(null);
        this.deleteDomainObject();
    }

    @Override
    public void setEmissionLocationOfDocumentId(final String value) {
        super.setEmissionLocationOfDocumentId(value);

        final Person person = getPerson();
        if (person != null) {
            person.syncEmissionLocationOfDocumentIdFromIdentificationDocument(value);
        }
    }

    @Override
    public void setEmissionDateOfDocumentIdYearMonthDay(final YearMonthDay value) {
        super.setEmissionDateOfDocumentIdYearMonthDay(value);

        final Person person = getPerson();
        if (person != null) {
            person.syncEmissionDateOfDocumentIdYearMonthDayFromIdentificationDocument(value);
        }
    }

    @Override
    public void setExpirationDateOfDocumentIdYearMonthDay(final YearMonthDay value) {
        super.setExpirationDateOfDocumentIdYearMonthDay(value);

        final Person person = getPerson();
        if (person != null) {
            person.syncExpirationDateOfDocumentIdYearMonthDayFromIdentificationDocument(value);
        }
    }

    public void syncEmissionLocationOfDocumentIdFromPerson(final String value) {
        super.setEmissionLocationOfDocumentId(value);
    }

    public void syncEmissionDateOfDocumentIdYearMonthDayFromPerson(final YearMonthDay value) {
        super.setEmissionDateOfDocumentIdYearMonthDay(value);
    }

    public void syncExpirationDateOfDocumentIdYearMonthDayFromPerson(final YearMonthDay value) {
        super.setExpirationDateOfDocumentIdYearMonthDay(value);
    }

    public static Optional<IdentificationDocument> find(final String idDocumentValue,
            final IdentificationDocumentType identificationDocumentType) {
        if (identificationDocumentType == null) {
            return Optional.empty();
        }

        return identificationDocumentType.getIdentificationDocumentsSet().stream()
                .filter(idDoc -> idDoc.getValue().equalsIgnoreCase(idDocumentValue))
                .findAny();
    }

    public static Stream<IdentificationDocument> find(final String idDocumentValue) {
        if (StringUtils.isBlank(idDocumentValue)) {
            return Stream.empty();
        }

        return Bennu.getInstance().getIdentificationDocumentsSet().stream()
                .filter(idDoc -> idDocumentValue.equalsIgnoreCase(idDoc.getValue()));
    }

}
