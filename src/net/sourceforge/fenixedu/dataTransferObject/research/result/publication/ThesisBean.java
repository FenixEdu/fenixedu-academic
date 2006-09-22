package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;
import bibtex.dom.BibtexEntry;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis.ThesisType;

public class ThesisBean extends ResultPublicationBean implements Serializable {
    private String address;

    private Integer numberPages;

    private String language;

    private ThesisType thesisType;

    private ThesisBean() {
        this.setPublicationType(ResultPublicationType.Thesis);
        this.setActiveSchema("result.publication.create.Thesis");
        this.setParticipationSchema("resultParticipation.simple");
    }

    public ThesisBean(Thesis thesis) {
        this();
        if (thesis != null) {
            this.fillCommonFields(thesis);
            this.setThesisType(thesis.getThesisType());
            this.setAddress(thesis.getAddress());
            this.setNumberPages(thesis.getNumberPages());
            this.setLanguage(thesis.getLanguage());
        }
    }

    public ThesisBean(BibtexEntry entry) {
        this();
        if (entry.getEntryType().equalsIgnoreCase("mastersthesis"))
            setThesisType(ThesisType.Masters_Thesis);
        else
            setThesisType(ThesisType.PhD_Thesis);

        setYearFromBibtexEntry(entry);
        setMonthFromBibtexEntry(entry);
        setUnitFromBibtexEntry("organization", entry);// school

        setTitle(getStringValueFromBibtexEntry("title", entry));
        setAddress(getStringValueFromBibtexEntry("address", entry));
        setNote(getStringValueFromBibtexEntry("note", entry));
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public ThesisType getThesisType() {
        return thesisType;
    }

    public void setThesisType(ThesisType thesisType) {
        this.thesisType = thesisType;
    }
}
