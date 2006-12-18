package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.injectionCode.Checked;
import bibtex.dom.BibtexEntry;

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
	super.delete();
    }

    @Override
    public abstract BibtexEntry exportToBibtexEntry();

    @Override
    public abstract String getResume();



}
