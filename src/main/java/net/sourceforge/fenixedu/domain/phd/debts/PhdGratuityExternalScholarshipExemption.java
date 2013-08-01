package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PhdGratuityExternalScholarshipExemption extends PhdGratuityExternalScholarshipExemption_Base {

    public PhdGratuityExternalScholarshipExemption(Person responsible, Event event, Party party, Money value) {
        PhdEventExemptionJustification exemptionJustification =
                new PhdEventExemptionJustification(this,
                        PhdEventExemptionJustificationType.PHD_GRATUITY_FCT_SCHOLARSHIP_EXEMPTION, event.getWhenOccured()
                                .toLocalDate(), "Criado pela existencia de bolsa de entidade externa.");
        super.init(responsible, event, exemptionJustification);
        setParty(party);
        createExternalDebt();
        setValue(value);
        setRootDomainObject(Bennu.getInstance());
        event.recalculateState(new DateTime());
    }

    private void createExternalDebt() {
        ExternalScholarshipPhdGratuityContribuitionEvent event = new ExternalScholarshipPhdGratuityContribuitionEvent(getParty());
        this.setExternalScholarshipPhdGratuityContribuitionEvent(event);
    }

    @Atomic
    public static PhdGratuityExternalScholarshipExemption createPhdGratuityExternalScholarshipExemption(Person responsible,
            Money value, Party party, PhdGratuityEvent event) {
        if (event.hasExemptionsOfType(PhdGratuityExternalScholarshipExemption.class)) {
            throw new DomainException("error.already.has.scolarship");
        }
        PhdGratuityExternalScholarshipExemption phdGratuityExternalScholarshipExemption =
                new PhdGratuityExternalScholarshipExemption(responsible, event, party, value);
        return phdGratuityExternalScholarshipExemption;
    }

    @Override
    public LabelFormatter getDescription() {
        PhdGratuityEvent event = (PhdGratuityEvent) getEvent();
        return new LabelFormatter()
                .appendLabel(
                        "Bolsa de entidade externa (" + getParty().getName()
                                + ") aplicada à Propina do Programa de Doutoramento de ")
                .appendLabel(event.getPhdProgram().getName().getContent()).appendLabel(" referente a " + event.getYear());
    }

    public void doDelete() {
        setExternalScholarshipPhdGratuityContribuitionEvent(null);
        setParty(null);
        super.delete();
    }

    @Override
    public void delete() {
        ExternalScholarshipPhdGratuityContribuitionEvent event = getExternalScholarshipPhdGratuityContribuitionEvent();
        event.delete();
    }

    public Money getAmoutStillMissing() {
        return getExternalScholarshipPhdGratuityContribuitionEvent().calculateAmountToPay();
    }
    @Deprecated
    public boolean hasParty() {
        return getParty() != null;
    }

    @Deprecated
    public boolean hasExternalScholarshipPhdGratuityContribuitionEvent() {
        return getExternalScholarshipPhdGratuityContribuitionEvent() != null;
    }

}
