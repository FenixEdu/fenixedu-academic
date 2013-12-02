package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacy;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class SecondCycleIndividualCandidacyEvent extends SecondCycleIndividualCandidacyEvent_Base {

    private SecondCycleIndividualCandidacyEvent() {
        super();
    }

    public SecondCycleIndividualCandidacyEvent(final SecondCycleIndividualCandidacy candidacy, final Person person) {
        this();
        super.init(candidacy, EventType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY, person);

        attachAvailablePaymentCode(person);
    }

    @Override
    protected AdministrativeOffice readAdministrativeOffice() {
        return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    public boolean hasSecondCycleIndividualCandidacyExemption() {
        return getSecondCycleIndividualCandidacyExemption() != null;
    }

    public SecondCycleIndividualCandidacyExemption getSecondCycleIndividualCandidacyExemption() {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption instanceof SecondCycleIndividualCandidacyExemption) {
                return (SecondCycleIndividualCandidacyExemption) exemption;
            }
        }
        return null;
    }

    @Override
    protected EntryType getEntryType() {
        return EntryType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY_FEE;
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = super.getDescription();
        labelFormatter.appendLabel(" - ");
        for (Degree s : getIndividualCandidacy().getAllDegrees()) {
            labelFormatter.appendLabel(s.getSigla()).appendLabel(" ");
        }
        labelFormatter.appendLabel(" - ").appendLabel(getIndividualCandidacy().getCandidacyDate().toString());

        return labelFormatter;
    }
}
