package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import bibtex.dom.BibtexEntry;

public class ProceedingsBean extends ConferenceArticlesBean implements Serializable {
    private String address;

    public ProceedingsBean() {
	this.setPublicationType(ResultPublicationType.Proceedings);
	this.setActiveSchema("result.publication.create.Proceedings");
	this.setParticipationSchema("resultParticipation.simple");
    }

    public ProceedingsBean(Proceedings proceedings) {
	this();
	fillCommonFields(proceedings);
	fillSpecificFields(proceedings);
    }

    public ProceedingsBean(ResultPublicationBean bean) {
	this();
	fillCommonBeanFields(bean);
    }

    public ProceedingsBean(BibtexEntry bibtexEntry) {
	this();
	fillBibTeXFields(bibtexEntry);
	this.setActiveSchema("result.publication.import.Proceedings");
    }

    @Override
    protected void fillSpecificFields(ResearchResultPublication publication) {
	this.setAddress(((Proceedings) publication).getAddress());
	this.setConference(((Proceedings) publication).getConference());
    }

    @Override
    protected void fillBibTeXFields(BibtexEntry bibtexEntry) {
	setUnitFromBibtexEntry("publisher", bibtexEntry);
	setUnitFromBibtexEntry("organization", bibtexEntry);
	setYearFromBibtexEntry(bibtexEntry);
	setMonthFromBibtexEntry(bibtexEntry);

	setTitle(getStringValueFromBibtexEntry("title", bibtexEntry));
	setAddress(getStringValueFromBibtexEntry("address", bibtexEntry));
	setNote(getStringValueFromBibtexEntry("note", bibtexEntry));
    }

    @Override
    public ResultPublicationBean convertTo(ResultPublicationType type) {
	return ResultPublicationBeanConversions.proceedingsTo(this, type);
    }

    @Override
    public void setCreateEvent(Boolean createEvent) {
	this.setActiveSchema("result.publication.create.ProceedingsAndEvent");
	super.setCreateEvent(createEvent);
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }
}
