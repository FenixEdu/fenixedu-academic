package net.sourceforge.fenixedu.domain.library;

import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.documents.LibraryMissingCardsDocument;
import net.sourceforge.fenixedu.domain.documents.LibraryMissingLettersDocument;

public class LibraryCard extends LibraryCard_Base {

    public LibraryCard(LibraryCardDTO libraryCardDTO) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setUserName(libraryCardDTO.getUserName());
        setUnitName(libraryCardDTO.getChosenUnitName());
        setPin(libraryCardDTO.getPin());
        setValidUntil(libraryCardDTO.getValidUntil());
        setPerson(libraryCardDTO.getPerson());
        setPartyClassification(libraryCardDTO.getPartyClassification());
        setCardEmitionDate(null);
        setLetterGenerationDate(null);
    }

    public void edit(LibraryCardDTO libraryCardDTO) {
        setUserName(libraryCardDTO.getUserName());
        setUnitName(libraryCardDTO.getChosenUnitName());
        setPartyClassification(libraryCardDTO.getPartyClassification());
        if (getValidUntil() == null || libraryCardDTO.getValidUntil() == null
                || !getValidUntil().equals(libraryCardDTO.getValidUntil())) {
            setValidUntil(libraryCardDTO.getValidUntil());
            setCardEmitionDate(null);
            setLetterGenerationDate(null);
        }
    }

    public void delete() {
        setPerson(null);
        for (LibraryMissingCardsDocument document : getDocumentSet()) {
            removeDocument(document);
        }
        for (LibraryMissingLettersDocument letter : getLettersSet()) {
            removeLetters(letter);
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public static final LibraryCard readByCode(String code) {
        for (LibraryCard card : RootDomainObject.getInstance().getLibraryCardsSet()) {
            if (card.getCardNumber() != null && card.getCardNumber().equals(code)) {
                return card;
            }
        }
        return null;
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.documents.LibraryMissingCardsDocument> getDocument() {
        return getDocumentSet();
    }

    @Deprecated
    public boolean hasAnyDocument() {
        return !getDocumentSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.documents.LibraryMissingLettersDocument> getLetters() {
        return getLettersSet();
    }

    @Deprecated
    public boolean hasAnyLetters() {
        return !getLettersSet().isEmpty();
    }

    @Deprecated
    public boolean hasCardNumber() {
        return getCardNumber() != null;
    }

    @Deprecated
    public boolean hasUnitName() {
        return getUnitName() != null;
    }

    @Deprecated
    public boolean hasPin() {
        return getPin() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasValidUntil() {
        return getValidUntil() != null;
    }

    @Deprecated
    public boolean hasLetterGenerationDate() {
        return getLetterGenerationDate() != null;
    }

    @Deprecated
    public boolean hasUserName() {
        return getUserName() != null;
    }

    @Deprecated
    public boolean hasPartyClassification() {
        return getPartyClassification() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasCardEmitionDate() {
        return getCardEmitionDate() != null;
    }

}
