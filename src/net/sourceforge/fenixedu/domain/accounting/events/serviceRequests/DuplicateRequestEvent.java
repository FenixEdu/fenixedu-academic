package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class DuplicateRequestEvent extends DuplicateRequestEvent_Base {

    protected DuplicateRequestEvent() {
        super();
    }

    public DuplicateRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
            final RegistrationAcademicServiceRequest academicServiceRequest) {
        this();

        super.init(administrativeOffice, EventType.DUPLICATE_REQUEST, person, academicServiceRequest);
    }

    @Override
    final public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);

        return labelFormatter;
    }

}
