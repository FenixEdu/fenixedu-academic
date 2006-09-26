package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import bibtex.dom.BibtexEntry;

public class TechnicalReportBean extends ResultPublicationBean implements Serializable {
    private String address;

    private Integer numberPages;

    private String language;

    private String number;

    private String technicalReportType;

    public TechnicalReportBean() {
	this.setPublicationType(ResultPublicationType.TechnicalReport);
	this.setActiveSchema("result.publication.create.TechnicalReport");
	this.setParticipationSchema("resultParticipation.simple");
    }

    public TechnicalReportBean(TechnicalReport report) {
	this();
	fillCommonFields(report);
	fillSpecificFields(report);
    }

    public TechnicalReportBean(ResultPublicationBean bean) {
	this();
	fillCommonBeanFields(bean);
    }

    public TechnicalReportBean(BibtexEntry bibtexEntry) {
	this();
	fillBibTeXFields(bibtexEntry);
    }

    @Override
    protected void fillSpecificFields(ResultPublication publication) {
	this.setTechnicalReportType(((TechnicalReport) publication).getTechnicalReportType());
	this.setNumber(((TechnicalReport) publication).getNumber());
	this.setAddress(((TechnicalReport) publication).getAddress());
	this.setNumberPages(((TechnicalReport) publication).getNumberPages());
	this.setLanguage(((TechnicalReport) publication).getLanguage());
    }

    @Override
    protected void fillBibTeXFields(BibtexEntry bibtexEntry) {
	setUnitFromBibtexEntry("organization", bibtexEntry);
	setYearFromBibtexEntry(bibtexEntry);
	setMonthFromBibtexEntry(bibtexEntry);

	setTitle(getStringValueFromBibtexEntry("title", bibtexEntry));
	setTechnicalReportType(getStringValueFromBibtexEntry("type", bibtexEntry));
	setNumber(getStringValueFromBibtexEntry("number", bibtexEntry));
	setAddress(getStringValueFromBibtexEntry("address", bibtexEntry));
	setNote(getStringValueFromBibtexEntry("note", bibtexEntry));
    }

    @Override
    public ResultPublicationBean convertTo(ResultPublicationType type) {
	return ResultPublicationBeanConversions.technicalReportTo(this, type);
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

    public String getNumber() {
	return number;
    }

    public void setNumber(String number) {
	this.number = number;
    }

    public Integer getNumberPages() {
	return numberPages;
    }

    public void setNumberPages(Integer numberPages) {
	this.numberPages = numberPages;
    }

    public String getTechnicalReportType() {
	return technicalReportType;
    }

    public void setTechnicalReportType(String technicalReportType) {
	this.technicalReportType = technicalReportType;
    }
}
