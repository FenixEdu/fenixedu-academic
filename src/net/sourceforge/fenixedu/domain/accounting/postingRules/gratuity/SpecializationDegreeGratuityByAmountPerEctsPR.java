package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class SpecializationDegreeGratuityByAmountPerEctsPR extends SpecializationDegreeGratuityByAmountPerEctsPR_Base {

    public static class SpecializationDegreeGratuityByAmountPerEctsPREditor extends SpecializationDegreeGratuityPREditor {

        private Money specializationDegreeAmountPerEctsCredit;

        private SpecializationDegreeGratuityByAmountPerEctsPREditor() {
            super();
        }

        public Money getSpecializationDegreeAmountPerEctsCredit() {
            return specializationDegreeAmountPerEctsCredit;
        }

        public void setSpecializationDegreeAmountPerEctsCredit(final Money specializationDegreeAmountPerEctsCredit) {
            this.specializationDegreeAmountPerEctsCredit = specializationDegreeAmountPerEctsCredit;
        }

        @Override
        public Object execute() {
            return ((SpecializationDegreeGratuityByAmountPerEctsPR) getSpecializationDegreeGratuityPR()).edit(getBeginDate(),
                    getSpecializationDegreeTotalAmount(), getSpecializationDegreeAmountPerEctsCredit(),
                    getSpecializationDegreePartialAcceptedPercentage());
        }

        public static SpecializationDegreeGratuityByAmountPerEctsPREditor buildFrom(
                final SpecializationDegreeGratuityByAmountPerEctsPR rule) {
            final SpecializationDegreeGratuityByAmountPerEctsPREditor result =
                    new SpecializationDegreeGratuityByAmountPerEctsPREditor();
            init(rule, result);
            result.setSpecializationDegreeAmountPerEctsCredit(rule.getSpecializationDegreeAmountPerEctsCredit());

            return result;
        }

        static private void init(final SpecializationDegreeGratuityPR o1, final SpecializationDegreeGratuityPREditor o2) {
            o2.setSpecializationDegreeGratuityPR(o1);
            o2.setSpecializationDegreePartialAcceptedPercentage(o1.getSpecializationDegreePartialAcceptedPercentage());
            o2.setSpecializationDegreeTotalAmount(o1.getSpecializationDegreeTotalAmount());
        }
    }

    protected SpecializationDegreeGratuityByAmountPerEctsPR() {
        super();
    }

    public SpecializationDegreeGratuityByAmountPerEctsPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money specializationDegreeTotalAmount,
            BigDecimal partialAcceptedPercentage, Money specializationDegreeAmountPerEctsCredit) {
        super();
        init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate,
                specializationDegreeTotalAmount, partialAcceptedPercentage, specializationDegreeAmountPerEctsCredit);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money specializationDegreeTotalAmount,
            BigDecimal specializationDegreePartialAcceptedPercentage, Money specializationDegreeAmountPerEctsCredit) {

        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, specializationDegreeTotalAmount,
                specializationDegreePartialAcceptedPercentage);

        checkParameters(specializationDegreeAmountPerEctsCredit);
        super.setSpecializationDegreeAmountPerEctsCredit(specializationDegreeAmountPerEctsCredit);
    }

    private void checkParameters(Money specializationDegreeAmountPerEctsCredit) {
        if (specializationDegreeAmountPerEctsCredit == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityByAmountPerEctsPR.specializationDegreeAmountPerEctsCredit.cannot.be.null");
        }
    }

    @Override
    public void setSpecializationDegreeAmountPerEctsCredit(Money specializationDegreeAmountPerEctsCredit) {
        throw new DomainException(
                "error.accounting.postingRules.gratuity.SpecializationDegreeGratuityByAmountPerEctsPR.cannot.modify.specializationDegreeAmountPerEctsCredit");
    }

    @Override
    protected Money calculateSpecializationDegreeGratuityTotalAmountToPay(final Event event) {
        final Money result;
        final double enrolmentsEctsForRegistration = ((GratuityEvent) event).getEnrolmentsEctsForRegistration();
        result = getSpecializationDegreeAmountPerEctsCredit().multiply(new BigDecimal(enrolmentsEctsForRegistration));

        return result;
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public SpecializationDegreeGratuityByAmountPerEctsPR edit(Money specializationDegreeTotalAmount,
            Money specializationDegreeAmountPerEctsCredit, BigDecimal partialAcceptedPercentage) {
        return edit(new DateTime(), specializationDegreeTotalAmount, specializationDegreeAmountPerEctsCredit,
                partialAcceptedPercentage);
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public SpecializationDegreeGratuityByAmountPerEctsPR edit(DateTime startDate, Money specializationDegreeTotalAmount,
            Money specializationDegreeAmountPerEctsCredit, BigDecimal partialAcceptedPercentage) {
        deactivate(startDate);

        return new SpecializationDegreeGratuityByAmountPerEctsPR(startDate, null, getServiceAgreementTemplate(),
                specializationDegreeTotalAmount, partialAcceptedPercentage, specializationDegreeAmountPerEctsCredit);
    }
}
