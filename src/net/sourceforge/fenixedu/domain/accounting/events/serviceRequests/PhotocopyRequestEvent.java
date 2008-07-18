package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.PhotocopyRequest;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

import org.apache.commons.lang.StringUtils;

public class PhotocopyRequestEvent extends PhotocopyRequestEvent_Base {
    
    protected PhotocopyRequestEvent() {
        super();
    }

    public PhotocopyRequestEvent(final AdministrativeOffice administrativeOffice, final Person person, final PhotocopyRequest request) {
	this();
	super.init(administrativeOffice, request.getEventType(), person, request);
    }
    
    @Override
    public PhotocopyRequest getAcademicServiceRequest() {
        return (PhotocopyRequest) super.getAcademicServiceRequest();
    }
    
    public Integer getNumberOfPages() {
	return getAcademicServiceRequest().getNumberOfPages();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	
	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	if (!StringUtils.isEmpty(getAcademicServiceRequest().getPurpose())) {
	    labelFormatter.appendLabel(" (").appendLabel(getAcademicServiceRequest().getPurpose()).appendLabel(")");
	}
	if (getAcademicServiceRequest().hasExecutionYear()) {
	    labelFormatter.appendLabel(" - " + getExecutionYear().getYear());
	}
	return labelFormatter;
    }
}
