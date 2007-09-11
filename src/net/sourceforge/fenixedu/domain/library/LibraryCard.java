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
        setRole(libraryCardDTO.getRoleType());
        setIsCardEmited(Boolean.FALSE);
        setIsLetterGenerated(Boolean.FALSE);
    }
    
}
