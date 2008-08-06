package net.sourceforge.fenixedu.domain.library;

import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.domain.RootDomainObject;

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
	}
    }

    public void delete() {
	removePerson();
	removeRootDomainObject();
	deleteDomainObject();
    }
}
