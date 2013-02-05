package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.PastDiplomaRequest;
import net.sourceforge.fenixedu.util.Money;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PastDegreeDiplomaRequestEvent extends PastDegreeDiplomaRequestEvent_Base implements IPastRequestEvent {

    protected PastDegreeDiplomaRequestEvent() {
        super();
    }

    public PastDegreeDiplomaRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
            final PastDiplomaRequest pastDiplomaRequest, final Money pastAmount) {
        this();
        super.init(administrativeOffice, EventType.PAST_DEGREE_DIPLOMA_REQUEST, person, pastDiplomaRequest);
        super.setPastAmount(pastAmount);
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getEventType().getQualifiedName(), "enum");
        labelFormatter.appendLabel(" (");
        labelFormatter.appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES);
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent());
        labelFormatter.appendLabel(")");
        return labelFormatter;
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        return Collections.singleton(EntryType.DIPLOMA_REQUEST_FEE);
    }

    @Override
    public void setPastAmount(Money pastDegreeDiplomaRequestAmount) {
        throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

}
