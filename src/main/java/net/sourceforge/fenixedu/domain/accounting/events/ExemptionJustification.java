package net.sourceforge.fenixedu.domain.accounting.events;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public abstract class ExemptionJustification extends ExemptionJustification_Base {

    protected ExemptionJustification() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
    }

    protected void init(final Exemption exemption, final String reason) {

        checkParameters(exemption);

        super.setExemption(exemption);
        super.setReason(reason);
    }

    private void checkParameters(Exemption exemption) {
        if (exemption == null) {
            throw new DomainException("error.accounting.events.ExemptionJustification.exemption.cannot.be.null");
        }
    }

    @Override
    public void setExemption(Exemption exemption) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.accounting.events.ExemptionJustification.cannot.modify.exemption");
    }

    @Override
    public void setReason(String reason) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.accounting.events.ExemptionJustification.cannot.modify.reason");
    }

    public void delete() {
        setRootDomainObject(null);
        super.setExemption(null);

        super.deleteDomainObject();
    }

    public void removeExemption() {
        super.setExemption(null);
    }

    abstract public LabelFormatter getDescription();

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExemption() {
        return getExemption() != null;
    }

    @Deprecated
    public boolean hasReason() {
        return getReason() != null;
    }

}
