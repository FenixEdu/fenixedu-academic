package net.sourceforge.fenixedu.domain.research.result.publication;

import bibtex.dom.BibtexEntry;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * Used for relation of Inproceedings and Proceedings with Event Required
 * fields: Conference (Event)
 */
public abstract class ConferenceArticles extends ConferenceArticles_Base {

    public ConferenceArticles() {
	super();
    }

    @Checked("ResultPredicates.writePredicate")
    public void delete() {
	super.setEvent(null);
	super.delete();
    }

    @Override
    public abstract BibtexEntry exportToBibtexEntry();

    @Override
    public abstract String getResume();

    @Override
    public void removeEvent() {
	throw new DomainException("error.researcher.ConferenceArticles.call", "removeEvent");
    }

}
