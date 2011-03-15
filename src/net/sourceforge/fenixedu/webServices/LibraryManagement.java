package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.library.LibraryCard;

public class LibraryManagement implements ILibraryManagement {

    @Override
    public String getLibraryCardNumberByIstUsername(String istUsername) {
	String libraryCardNumber = "";

	User user = User.readUserByUserUId(istUsername);

	if (user != null && user.getPerson() != null) {
	    LibraryCard card = user.getPerson().getLibraryCard();
	    if (card != null) {
		libraryCardNumber = card.getCardNumber();
	    }
	}

	return libraryCardNumber;
    }
}
