package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import bibtex.dom.BibtexEntry;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;

public class OtherPublicationBean extends ResultPublicationBean implements Serializable {
    private String address;

    private Integer numberPages;

    private String language;

    private String howPublished;

    private String otherPublicationType;

    private OtherPublicationBean() {
        this.setPublicationType(ResultPublicationType.OtherPublication);
        this.setActiveSchema("result.publication.create.OtherPublication");
        this.setParticipationSchema("resultParticipation.simple");
    }

    public OtherPublicationBean(OtherPublication otherPublication) {
        this();
        if (otherPublication != null) {
            this.fillCommonFields(otherPublication);
            this.setHowPublished(otherPublication.getHowPublished());
            this.setOtherPublicationType(otherPublication.getOtherPublicationType());
            this.setNumberPages(otherPublication.getNumberPages());
            this.setLanguage(otherPublication.getLanguage());
            this.setAddress(otherPublication.getAddress());
        }
    }

    //TODO: rearrange importation
    public OtherPublicationBean(BibtexEntry entry) {
        this();
        setUnitFromBibtexEntry("publisher", entry);
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getNumberPages() {
        return numberPages;
    }

    public void setNumberPages(Integer numberPages) {
        this.numberPages = numberPages;
    }

    public String getOtherPublicationType() {
        return otherPublicationType;
    }

    public void setOtherPublicationType(String otherPublicationType) {
        this.otherPublicationType = otherPublicationType;
    }
}
