package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import bibtex.dom.BibtexEntry;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;

public class ManualBean extends ResultPublicationBean implements Serializable {
    private String address;

    private String edition;

    private ManualBean() {
        this.setPublicationType(ResultPublicationType.Manual);
        this.setActiveSchema("result.publication.create.Manual");
        this.setParticipationSchema("resultParticipation.simple");
    }

    public ManualBean(Manual manual) {
        this();
        if (manual != null) {
            this.fillCommonFields(manual);
            this.setAddress(manual.getAddress());
            this.setEdition(manual.getEdition());
        }
    }

    public ManualBean(BibtexEntry entry) {
        this();
        setUnitFromBibtexEntry("organization", entry);
        setYearFromBibtexEntry(entry);
        setMonthFromBibtexEntry(entry);

        setTitle(getStringValueFromBibtexEntry("title", entry));
        setAddress(getStringValueFromBibtexEntry("address", entry));
        setEdition(getStringValueFromBibtexEntry("edition", entry));
        setNote(getStringValueFromBibtexEntry("note", entry));
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }
}
