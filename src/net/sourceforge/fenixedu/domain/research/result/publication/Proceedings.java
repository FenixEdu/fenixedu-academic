package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;

/**
 * (collection: A collection of works. Same as Proceedings.)
 * The proceedings of a conference.
 * Required fields: title, year.
 * Optional fields: editor, publisher, organization, address, month, note.
 * 
 * Extra from previous publications: conference
 */
public class Proceedings extends Proceedings_Base {

    public Proceedings() {
        super();
    }

    //constructor with required fields, we need a participator
    public Proceedings(Person participator, String title, Integer year, Event event) {
        super();
        if ((participator == null) || (title == null) || (title.length() == 0) || (year == null)
                || (event == null))
            throw new DomainException("error.publication.missingRequiredFields");

        //by default any participation is of type Editor
        setParticipation(participator, ResultParticipationRole.Editor);
        setTitle(title);
        setYear(year);
        setEvent(event);
    }

    //edit with required fields
    public void edit(String title, Integer year, Event event) {
        if ((title == null) || (title.length() == 0) || (year == null) || (event == null))
            throw new DomainException("error.publication.missingRequiredFields");

        setTitle(title);
        setYear(year);
        setEvent(event);
    }

    public void setNonRequiredAttributes(Unit publisher, Unit organization, String address, String note,
            Month month, String url) {

        this.setPublisher(publisher);
        this.setOrganization(organization);
        this.setAddress(address);
        this.setNote(note);
        this.setMonth(month);
        this.setUrl(url);
    }

}
