package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;

public class PercentageGratuityExemption extends PercentageGratuityExemption_Base {

    public PercentageGratuityExemption(final Person responsible, final GratuityEvent gratuityEvent,
            final GratuityExemptionJustificationType gratuityExemptionJustificationType, final String reason,
            final YearMonthDay dispatchDate, final BigDecimal percentage) {
        super();
        init(responsible, gratuityExemptionJustificationType, reason, dispatchDate, gratuityEvent, percentage);
    }

    public PercentageGratuityExemption(final GratuityEvent gratuityEvent,
            final GratuityExemptionJustificationType gratuityExemptionJustificationType, final String reason,
            final YearMonthDay dispatchDate, final BigDecimal percentage) {
        this(null, gratuityEvent, gratuityExemptionJustificationType, reason, dispatchDate, percentage);
    }

    protected void init(Person responsible, GratuityExemptionJustificationType exemptionType, String reason,
            YearMonthDay dispatchDate, GratuityEvent gratuityEvent, BigDecimal percentage) {

        checkParameters(percentage);
        super.setPercentage(percentage);

        super.init(responsible, gratuityEvent, exemptionType, reason, dispatchDate);
    }

    private void checkParameters(BigDecimal percentage) {
        if (percentage == null) {
            throw new DomainException("error.accounting.events.gratuity.PercentageGratuityExemption.percentage.cannot.be.null");
        }
    }

    @Override
    public void setPercentage(BigDecimal percentage) {
        check(this, RolePredicates.MANAGER_PREDICATE);
        super.setPercentage(percentage);
        final DateTime now = new DateTime();
        getGratuityEvent().forceChangeState(EventState.OPEN, now);
        getGratuityEvent().recalculateState(now);
    }

    @Override
    public BigDecimal calculateDiscountPercentage(Money amount) {
        return getPercentage();
    }

    public String getFormattedPercentage() {
        return getPercentage().multiply(BigDecimal.valueOf(100)).toPlainString();
    }

    @Override
    public boolean isPercentageExemption() {
        return true;
    }

    @Deprecated
    public boolean hasPercentage() {
        return getPercentage() != null;
    }

}
