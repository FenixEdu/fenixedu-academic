package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.AcademicEventExemption;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public abstract class AcademicEvent extends AcademicEvent_Base {

    public AcademicEvent() {
        super();
    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person) {
        init(eventType, person);
        super.setAdministrativeOffice(administrativeOffice);
    }

    @Override
    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
        throw new DomainException("error.accounting.Event.cannot.modify.administrativeOffice");
    }

    @Override
    public boolean isPayableOnAdministrativeOffice(AdministrativeOffice administrativeOffice) {
        return (!hasAdministrativeOffice() || getAdministrativeOffice() == administrativeOffice);
    }

    @Override
    protected void disconnect() {
        super.setAdministrativeOffice(null);
        super.disconnect();
    }

    public boolean hasAcademicEventExemption() {
        return getAcademicEventExemption() != null;
    }

    public AcademicEventExemption getAcademicEventExemption() {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption instanceof AcademicEventExemption) {
                return (AcademicEventExemption) exemption;
            }
        }
        return null;
    }

    @Override
    public Unit getOwnerUnit() {
        return getAdministrativeOffice().getUnit();
    }

    @Deprecated
    public boolean hasAdministrativeOffice() {
        return getAdministrativeOffice() != null;
    }

}
