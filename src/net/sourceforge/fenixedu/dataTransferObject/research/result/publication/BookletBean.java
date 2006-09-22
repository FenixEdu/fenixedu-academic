package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import bibtex.dom.BibtexEntry;
import net.sourceforge.fenixedu.domain.research.result.publication.Booklet;

public class BookletBean extends ResultPublicationBean implements Serializable {
    private String address;

    private String howPublished;

    private BookletBean() {
        this.setPublicationType(ResultPublicationType.Booklet);
        this.setActiveSchema("result.publication.create.Booklet");
        this.setParticipationSchema("resultParticipation.simple");
    }

    public BookletBean(Booklet booklet) {
        this();
        if (booklet != null) {
            this.fillCommonFields(booklet);
            this.setHowPublished(booklet.getHowPublished());
            this.setAddress(booklet.getAddress());
        }
    }

    public BookletBean(BibtexEntry entry) {
        this();
        setYearFromBibtexEntry(entry);
        setMonthFromBibtexEntry(entry);

        setTitle(getStringValueFromBibtexEntry("title", entry));
        setHowPublished(getStringValueFromBibtexEntry("howpublished", entry));
        setAddress(getStringValueFromBibtexEntry("address", entry));
        setNote(getStringValueFromBibtexEntry("note", entry));
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHowPublished() {
        return howPublished;
    }

    public void setHowPublished(String howPublished) {
        this.howPublished = howPublished;
    }
}
