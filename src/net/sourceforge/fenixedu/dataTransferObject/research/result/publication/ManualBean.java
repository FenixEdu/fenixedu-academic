package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import bibtex.dom.BibtexEntry;

public class ManualBean extends ResultPublicationBean implements Serializable {
    private String address;

    private String edition;

    public ManualBean() {
	this.setPublicationType(ResultPublicationType.Manual);
	this.setActiveSchema("result.publication.create.Manual");
	this.setParticipationSchema("resultParticipation.simple");
    }

    public ManualBean(Manual manual) {
	this();
	fillCommonFields(manual);
	fillSpecificFields(manual);
    }

    public ManualBean(ResultPublicationBean bean) {
	this();
	fillCommonBeanFields(bean);
    }

    public ManualBean(BibtexEntry bibtexEntry) {
	this();
	fillBibTeXFields(bibtexEntry);
	this.setActiveSchema("result.publication.import.Manual");
    }

    @Override
    protected void fillSpecificFields(ResearchResultPublication publication) {
	this.setAddress(((Manual) publication).getAddress());
	this.setEdition(((Manual) publication).getEdition());
    }

    @Override
    protected void fillBibTeXFields(BibtexEntry bibtexEntry) {
	setUnitFromBibtexEntry("organization", bibtexEntry);
	setYearFromBibtexEntry(bibtexEntry);
	setMonthFromBibtexEntry(bibtexEntry);

	setTitle(getStringValueFromBibtexEntry("title", bibtexEntry));
	setAddress(getStringValueFromBibtexEntry("address", bibtexEntry));
	setEdition(getStringValueFromBibtexEntry("edition", bibtexEntry));
	setNote(getStringValueFromBibtexEntry("note", bibtexEntry));
    }

    @Override
    public ResultPublicationBean convertTo(ResultPublicationType type) {
	return ResultPublicationBeanConversions.manualTo(this, type);
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
