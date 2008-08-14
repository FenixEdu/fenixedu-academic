package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.CardType;

public class Card extends Card_Base {

    public Card() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Card(Integer cardNumber, CardType cardType) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCardId(cardNumber);
	setType(cardType);
    }

}
