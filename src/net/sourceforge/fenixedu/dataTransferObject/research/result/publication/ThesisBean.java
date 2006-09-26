package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis.ThesisType;
import bibtex.dom.BibtexEntry;

public class ThesisBean extends ResultPublicationBean implements Serializable {
    private String address;

    private Integer numberPages;

    private String language;

    private ThesisType thesisType;

    public ThesisBean() {
	this.setPublicationType(ResultPublicationType.Thesis);
	this.setActiveSchema("result.publication.create.Thesis");
	this.setParticipationSchema("resultParticipation.simple");
    }

    public ThesisBean(Thesis thesis) {
	this();
	fillCommonFields(thesis);
	fillSpecificFields(thesis);
    }

    public ThesisBean(ResultPublicationBean bean) {
	this();
	fillCommonBeanFields(bean);
    }

    public ThesisBean(BibtexEntry bibtexEntry) {
	this();
	fillBibTeXFields(bibtexEntry);
    }

    @Override
    protected void fillSpecificFields(ResultPublication publication) {
	this.setThesisType(((Thesis) publication).getThesisType());
	this.setAddress(((Thesis) publication).getAddress());
	this.setNumberPages(((Thesis) publication).getNumberPages());
	this.setLanguage(((Thesis) publication).getLanguage());
    }

    @Override
    protected void fillBibTeXFields(BibtexEntry bibtexEntry) {
	if (bibtexEntry.getEntryType().equalsIgnoreCase("mastersthesis"))
	    setThesisType(ThesisType.Masters_Thesis);
	else
	    setThesisType(ThesisType.PhD_Thesis);

	setYearFromBibtexEntry(bibtexEntry);
	setMonthFromBibtexEntry(bibtexEntry);
	setUnitFromBibtexEntry("organization", bibtexEntry);// school

	setTitle(getStringValueFromBibtexEntry("title", bibtexEntry));
	setAddress(getStringValueFromBibtexEntry("address", bibtexEntry));
	setNote(getStringValueFromBibtexEntry("note", bibtexEntry));
    }

    @Override
    public ResultPublicationBean convertTo(ResultPublicationType type) {
	return ResultPublicationBeanConversions.thesisTo(this, type);
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
