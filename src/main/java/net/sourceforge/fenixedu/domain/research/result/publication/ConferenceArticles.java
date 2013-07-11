package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.util.Month;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import bibtex.dom.BibtexEntry;

/**
 * Used for relation of Inproceedings and Proceedings with ResearchEvent
 * Required fields: Conference (ResearchEvent)
 */
public abstract class ConferenceArticles extends ConferenceArticles_Base {

    public ConferenceArticles() {
        super();
    }

    @Override
    @Checked("ResultPredicates.writePredicate")
    public void delete() {
        if (this.hasEventConferenceArticlesAssociation()) {
            this.getEventConferenceArticlesAssociation().delete();
            this.setEventConferenceArticlesAssociation(null);
        }
        super.delete();
    }

    public Month getOldMonth() {
        return super.getMonth();
    }

    public String getOldOrganization() {
        return super.getOrganization();
    }

    public Integer getOldYear() {
        return super.getYear();
    }

    @Override
    public abstract BibtexEntry exportToBibtexEntry();

    @Override
    public abstract String getResume();

    @Deprecated
    public boolean hasEventConferenceArticlesAssociation() {
        return getEventConferenceArticlesAssociation() != null;
    }

    @Deprecated
    public boolean hasConference() {
        return getConference() != null;
    }

    @Deprecated
    public boolean hasScope() {
        return getScope() != null;
    }

}
