package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class GratuityExemptionJustification extends GratuityExemptionJustification_Base {

    protected GratuityExemptionJustification() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
    }

    public GratuityExemptionJustification(final GratuityExemption gratuityExemption,
            final GratuityExemptionJustificationType gratuityExemptionJustificationType, final String reason) {
        this();
        init(gratuityExemption, gratuityExemptionJustificationType, reason);
    }

    protected void init(GratuityExemption gratuityExemption,
            GratuityExemptionJustificationType gratuityExemptionJustificationType, String reason) {
        super.init(gratuityExemption, reason);
        checkParameters(gratuityExemptionJustificationType);
        super.setGratuityExemptionJustificationType(gratuityExemptionJustificationType);
    }

    private void checkParameters(GratuityExemptionJustificationType gratuityExemptionJustificationType) {
        if (gratuityExemptionJustificationType == null) {
            throw new DomainException(
                    "error.accounting.events.gratuity.GratuityExemptionJustification.gratuityExemptionJustificationType.cannot.be.null");
        }
    }

    @Override
    public void setGratuityExemptionJustificationType(GratuityExemptionJustificationType gratuityExemptionJustificationType) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemptionJustification.cannot.modify.gratuityExemptionJustificationType");
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getGratuityExemptionJustificationType().getQualifiedName(),
                LabelFormatter.ENUMERATION_RESOURCES);

        return labelFormatter;

    }

    public boolean isSocialShareGrantOwner() {
        return getGratuityExemptionJustificationType() == GratuityExemptionJustificationType.SOCIAL_SHARE_GRANT_OWNER;
    }

    @Deprecated
    public boolean hasGratuityExemptionJustificationType() {
        return getGratuityExemptionJustificationType() != null;
    }

}
