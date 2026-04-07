package org.fenixedu.academic.domain.person.identificationDocument;

import static org.fenixedu.academic.domain.person.identificationDocument.IdentificationDocumentTypeTest.initIdentificationDocumentType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentTest;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.FenixFrameworkRunner;

import pt.ist.fenixframework.FenixFramework;

@RunWith(FenixFrameworkRunner.class)
public class IdentificationDocumentTest {

    public static final String ID_DOCUMENT_VALUE = "123456789";
    public static final String ID_DOCUMENT_TYPE = IdentificationDocumentType.IDENTITY_CARD_CODE;
    private static Person person;

    @Before
    public void init() {
        FenixFramework.getTransactionManager().withTransaction(() -> {
            initIdentificationDocumentType();
            initIdentificationDocument();
            return null;
        });
    }

    public static void initIdentificationDocument() {
        if (person == null) {
            person = StudentTest.createStudent("Student", "student").getPerson();
        }
        IdentificationDocumentType identificationDocumentType = IdentificationDocumentType.findByCode(ID_DOCUMENT_TYPE)
                .orElseGet(IdentificationDocumentTypeTest::initIdentificationDocumentType);
        IdentificationDocument identificationDocumentDocument =
                IdentificationDocument.create(person, ID_DOCUMENT_VALUE, identificationDocumentType);
    }

    @After
    public void cleanup() {
        Bennu.getInstance().getIdentificationDocumentsSet().forEach(IdentificationDocument::delete);
        Bennu.getInstance().getIdentificationDocumentTypesSet().forEach(IdentificationDocumentType::delete);
    }

    @Test
    public void testIdentificationDocument_create() {
        String value = "123";
        IdentificationDocumentType identificationDocumentType =
                IdentificationDocumentType.findByCode(ID_DOCUMENT_TYPE).orElse(null);
        IdentificationDocument identificationDocument = IdentificationDocument.create(person, value, identificationDocumentType);

        assertNotNull(identificationDocument);
        assertEquals(value, identificationDocument.getValue());
        assertEquals(ID_DOCUMENT_TYPE, identificationDocument.getIdentificationDocumentType().getCode());
        assertEquals(person, identificationDocument.getPerson());
    }

    @Test
    public void testIdentificationDocument_delete() {
        IdentificationDocumentType identificationDocumentType =
                IdentificationDocumentType.findByCode(ID_DOCUMENT_TYPE).orElse(null);
        assertNotNull(identificationDocumentType);
        IdentificationDocument identificationDocument =
                IdentificationDocument.find(ID_DOCUMENT_VALUE, identificationDocumentType).orElse(null);
        assertNotNull(identificationDocument);

        // Verify initial state before deletion
        assertFalse(Bennu.getInstance().getIdentificationDocumentTypesSet().isEmpty());
        assertNotNull(identificationDocument.getRootDomainObject());
        assertNotNull(identificationDocument.getPerson());
        assertSame(identificationDocument.getIdentificationDocumentType(), identificationDocumentType);

        // Perform deletion
        identificationDocument.delete();

        // Verify deletion
        assertFalse(Bennu.getInstance().getIdentificationDocumentsSet().contains(identificationDocument));
        assertNull(identificationDocument.getRootDomainObject());
        assertNull(identificationDocument.getIdentificationDocumentType());
        assertNull(identificationDocument.getPerson());
        assertNotNull(identificationDocumentType);
    }

    @Test
    public void testIdentificationDocument_findFirst() {
        // Find first by IdentificationDocumentType
        IdentificationDocumentType identificationDocumentType =
                IdentificationDocumentType.findByCode(ID_DOCUMENT_TYPE).orElse(null);
        assertNotNull(identificationDocumentType);
        Optional<IdentificationDocument> identificationDocumentOptByType =
                IdentificationDocument.find(ID_DOCUMENT_VALUE, identificationDocumentType);
        assertTrue(identificationDocumentOptByType.isPresent());
        assertEquals(ID_DOCUMENT_VALUE, identificationDocumentOptByType.get().getValue());

        // Find first by IdentificationDocumentType code
        Optional<IdentificationDocument> identificationDocumentOptByCode =
                IdentificationDocument.find(ID_DOCUMENT_VALUE, identificationDocumentType);
        assertTrue(identificationDocumentOptByCode.isPresent());
        assertEquals(ID_DOCUMENT_VALUE, identificationDocumentOptByCode.get().getValue());
    }

    @Test
    public void testIdentificationDocument_findFirstNotFound() {
        IdentificationDocumentType identificationDocumentType =
                IdentificationDocumentType.findByCode(ID_DOCUMENT_TYPE).orElse(null);
        assertNotNull(identificationDocumentType);

        Optional<IdentificationDocument> identificationDocumentOpt =
                IdentificationDocument.find("NON_EXISTENT_VALUE", identificationDocumentType);
        assertFalse(identificationDocumentOpt.isPresent());
    }

    @Test
    public void testIdentificationDocument_emissionDateSync() {
        IdentificationDocumentType identificationDocumentType =
                IdentificationDocumentType.findByCode(ID_DOCUMENT_TYPE).orElse(null);
        IdentificationDocument identificationDocument =
                IdentificationDocument.find(ID_DOCUMENT_VALUE, identificationDocumentType).orElse(null);
        assertNotNull(identificationDocument);
        assertNotNull(identificationDocument.getPerson());

        YearMonthDay emissionDate = new YearMonthDay(2025, 1, 15);
        identificationDocument.setEmissionDateOfDocumentIdYearMonthDay(emissionDate);

        assertEquals(emissionDate, identificationDocument.getEmissionDateOfDocumentIdYearMonthDay());
        assertEquals(emissionDate, person.getEmissionDateOfDocumentIdYearMonthDay());
    }

    @Test
    public void testIdentificationDocument_emissionLocationSync() {
        IdentificationDocumentType identificationDocumentType =
                IdentificationDocumentType.findByCode(ID_DOCUMENT_TYPE).orElse(null);
        IdentificationDocument identificationDocument =
                IdentificationDocument.find(ID_DOCUMENT_VALUE, identificationDocumentType).orElse(null);
        assertNotNull(identificationDocument);
        assertNotNull(identificationDocument.getPerson());

        String emissionLocation = "Lisbon";
        identificationDocument.setEmissionLocationOfDocumentId(emissionLocation);

        assertEquals(emissionLocation, identificationDocument.getEmissionLocationOfDocumentId());
        assertEquals(emissionLocation, person.getEmissionLocationOfDocumentId());
    }

    @Test
    public void testIdentificationDocument_expirationDateSync() {
        IdentificationDocumentType identificationDocumentType =
                IdentificationDocumentType.findByCode(ID_DOCUMENT_TYPE).orElse(null);
        IdentificationDocument identificationDocument =
                IdentificationDocument.find(ID_DOCUMENT_VALUE, identificationDocumentType).orElse(null);
        assertNotNull(identificationDocument);
        assertNotNull(identificationDocument.getPerson());

        YearMonthDay expirationDate = new YearMonthDay(2030, 12, 31);
        identificationDocument.setExpirationDateOfDocumentIdYearMonthDay(expirationDate);

        assertEquals(expirationDate, identificationDocument.getExpirationDateOfDocumentIdYearMonthDay());
        assertEquals(expirationDate, person.getExpirationDateOfDocumentIdYearMonthDay());
    }

    @Test
    public void testIdentificationDocument_createWithPersonDates() {
        Person testPerson = StudentTest.createStudent("TestStudent", "teststudent").getPerson();

        YearMonthDay emissionDate = new YearMonthDay(2019, 6, 1);
        testPerson.setEmissionDateOfDocumentIdYearMonthDay(emissionDate);
        testPerson.setEmissionLocationOfDocumentId("Porto");
        YearMonthDay expirationDate = new YearMonthDay(2029, 6, 1);
        testPerson.setExpirationDateOfDocumentIdYearMonthDay(expirationDate);

        IdentificationDocumentType identificationDocumentType =
                IdentificationDocumentType.findByCode(ID_DOCUMENT_TYPE).orElse(null);
        IdentificationDocument identificationDocument =
                IdentificationDocument.create(testPerson, "NEW_DOC_VALUE", identificationDocumentType);

        assertNotNull(identificationDocument.getPerson());
        assertEquals(emissionDate, identificationDocument.getEmissionDateOfDocumentIdYearMonthDay());
        assertEquals("Porto", identificationDocument.getEmissionLocationOfDocumentId());
        assertEquals(expirationDate, identificationDocument.getExpirationDateOfDocumentIdYearMonthDay());
    }

    @Test
    public void testIdentificationDocument_settersWithNullPerson() {
        IdentificationDocumentType identificationDocumentType =
                IdentificationDocumentType.findByCode(ID_DOCUMENT_TYPE).orElse(null);
        IdentificationDocument identificationDocument =
                IdentificationDocument.create(null, "NO_PERSON_DOC", identificationDocumentType);

        assertNull(identificationDocument.getPerson());

        YearMonthDay emissionDate = new YearMonthDay(2020, 1, 15);
        identificationDocument.setEmissionDateOfDocumentIdYearMonthDay(emissionDate);
        assertEquals(emissionDate, identificationDocument.getEmissionDateOfDocumentIdYearMonthDay());

        String emissionLocation = "Lisbon";
        identificationDocument.setEmissionLocationOfDocumentId(emissionLocation);
        assertEquals(emissionLocation, identificationDocument.getEmissionLocationOfDocumentId());

        YearMonthDay expirationDate = new YearMonthDay(2030, 12, 31);
        identificationDocument.setExpirationDateOfDocumentIdYearMonthDay(expirationDate);
        assertEquals(expirationDate, identificationDocument.getExpirationDateOfDocumentIdYearMonthDay());
    }
}
