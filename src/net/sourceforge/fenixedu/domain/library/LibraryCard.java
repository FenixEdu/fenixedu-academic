package net.sourceforge.fenixedu.domain.library;

import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class LibraryCard extends LibraryCard_Base {
    
    public  LibraryCard(LibraryCardDTO libraryCardDTO) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setUserName(libraryCardDTO.getUserName());
        setUnitName(libraryCardDTO.getChosenUnitName());
        setPin(libraryCardDTO.getPin());
        setValidUntil(libraryCardDTO.getValidUntil());
        setPerson(libraryCardDTO.getPerson());
        setPartyClassification(libraryCardDTO.getPartyClassification());
        setIsCardEmited(Boolean.FALSE);
        setIsLetterGenerated(Boolean.FALSE);
    }

    public void edit(LibraryCardDTO libraryCardDTO) {
        setUserName(libraryCardDTO.getUserName());
        setUnitName(libraryCardDTO.getChosenUnitName());
    }    
}
