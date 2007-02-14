package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import bibtex.dom.BibtexEntry;

public class OtherPublicationBean extends ResultPublicationBean implements Serializable {
    private String address;

    private Integer numberPages;

    private String language;

    private String howPublished;

    private String otherPublicationType;

    public OtherPublicationBean() {
	this.setPublicationType(ResultPublicationType.OtherPublication);
	this.setActiveSchema("result.publication.create.OtherPublication");
	this.setParticipationSchema("resultParticipation.simple");
    }

    public OtherPublicationBean(OtherPublication publication) {
	this();
	fillCommonFields(publication);
	fillSpecificFields(publication);
    }

    public OtherPublicationBean(ResultPublicationBean bean) {
	this();
	fillCommonBeanFields(bean);
    }

    public OtherPublicationBean(BibtexEntry bibtexEntry) {
	this();
	fillBibTeXFields(bibtexEntry);
	this.setActiveSchema("result.publication.import.OtherPublication");
    }

    @Override
    protected void fillSpecificFields(ResearchResultPublication publication) {
	this.setHowPublished(((OtherPublication) publication).getHowPublished());
	this.setOtherPublicationType(((OtherPublication) publication).getOtherPublicationType());
	this.setNumberPages(((OtherPublication) publication).getNumberPages());
	this.setLanguage(((OtherPublication) publication).getLanguage());
	this.setAddress(((OtherPublication) publication).getAddress());
    }

    @Override
    protected void fillBibTeXFields(BibtexEntry bibtexEntry) {
	// TODO: rearrange importation
	//setUnitFromBibtexEntry("publisher", bibtexEntry);
	setYearFromBibtexEntry(bibtexEntry);
	setMonthFromBibtexEntry(bibtexEntry);

	setTitle(getStringValueFromBibtexEntry("title", bibtexEntry));
	setHowPublished(getStringValueFromBibtexEntry("howpublished", bibtexEntry));
	setAddress(getStringValueFromBibtexEntry("address", bibtexEntry));
	setNote(getStringValueFromBibtexEntry("note", bibtexEntry));
    }

    @Override
    public ResultPublicationBean convertTo(ResultPublicationType type) {
	return ResultPublicationBeanConversions.otherPublicationTo(this, type);
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
