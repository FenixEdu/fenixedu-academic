package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import bibtex.dom.BibtexEntry;

/**
 * This publication is used to maintain old Unstructured Publications Is used
 * only to migrate to new publications Field used: title and year
 */
public class Unstructured extends Unstructured_Base {

    public Unstructured() {
	super();
    }
    
    public Unstructured(Person participator, String title, Integer year) {
	this();
	if (title == null || title.length() == 0)
	    throw new DomainException("error.researcher.Unstructured.title.null");
	setCreatorParticipation(participator, ResultParticipationRole.Author);
	setTitle(title);
	setYear(year);
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
	return null;
    }

    @Override
    public String getResume() {
	String resume = getParticipationsAndTitleString();
	if ((getYear() != null) && (getYear() > 0))
	    resume = resume + getYear();

	resume = finishResume(resume);
	return resume;
    }

}
