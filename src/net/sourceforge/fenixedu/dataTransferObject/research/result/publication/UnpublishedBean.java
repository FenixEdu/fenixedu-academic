package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import bibtex.dom.BibtexEntry;
import net.sourceforge.fenixedu.domain.research.result.publication.Unpublished;

public class UnpublishedBean extends ResultPublicationBean implements Serializable {

    private UnpublishedBean() {
        this.setPublicationType(ResultPublicationType.Unpublished);
        this.setActiveSchema("result.publication.create.Unpublished");
        this.setParticipationSchema("resultParticipation.simple");
    }

    public UnpublishedBean(Unpublished unpublished) {
        this();
        if (unpublished != null) {
            this.fillCommonFields(unpublished);
        }
    }

    public UnpublishedBean(BibtexEntry entry) {
        this();
        setYearFromBibtexEntry(entry);
        setMonthFromBibtexEntry(entry);

        setTitle(getStringValueFromBibtexEntry("title", entry));
        setNote(getStringValueFromBibtexEntry("note", entry));
    }
}
