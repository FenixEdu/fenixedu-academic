package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.assiduousness.util.CardState;

import org.joda.time.DateTime;

public class IdentificationCard extends IdentificationCard_Base {

    public IdentificationCard(Person person, Card card, DateTime beginDate, DateTime endDate,
            CardState cardState, DateTime lastModifiedDate, Employee modifiedBy) {
        
	super();
        setUser(person.getUser());
        setCard(card);
        setCardState(cardState);
        if (cardState.equals(CardState.TAKEN)) {
            setActive(true);
        } else {
            setActive(false);
        }
        setBeginDateDateTime(beginDate);
        setEndDateDateTime(endDate);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
    }
}
