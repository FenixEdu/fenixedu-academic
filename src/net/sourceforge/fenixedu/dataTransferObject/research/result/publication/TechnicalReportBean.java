package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;

public class TechnicalReportBean extends ResultPublicationBean implements Serializable{
    private String address;
    private Integer numberPages;
    private String language;
    private String number;
    private String technicalReportType;
    
    private TechnicalReportBean() {
	this.setPublicationType(ResultPublicationType.TechnicalReport);
	this.setActiveSchema("result.publication.create."+this.getPublicationType());
	this.setParticipationSchema("resultParticipation.simple");
    }
    
    public TechnicalReportBean(TechnicalReport technicalReport) {
	this();
	if(technicalReport!=null) {
	    this.fillCommonFields(technicalReport);
	    this.setPublicationType(ResultPublicationType.TechnicalReport);
	    this.setTechnicalReportType(technicalReport.getTechnicalReportType());
	    this.setNumber(technicalReport.getNumber());
	    this.setAddress(technicalReport.getAddress());
	    this.setNumberPages(technicalReport.getNumberPages());
	    this.setLanguage(technicalReport.getLanguage());
	}
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
