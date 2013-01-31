package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class DFAGratuityByNumberOfEnrolmentsPR extends DFAGratuityByNumberOfEnrolmentsPR_Base {

	private static final int SCALE_FOR_INTERMEDIATE_CALCULATIONS = 8;

	public static class DFAGratuityByNumberOfEnrolmentsPREditor extends DFAGratuityPREditor {

		private DFAGratuityByNumberOfEnrolmentsPREditor() {
			super();
		}

		@Override
		public Object execute() {
			return ((DFAGratuityByNumberOfEnrolmentsPR) getDfaGratuityPR()).edit(getBeginDate(), getDfaTotalAmount(),
					getDfaPartialAcceptedPercentage());
		}

		public static DFAGratuityByNumberOfEnrolmentsPREditor buildFrom(final DFAGratuityByNumberOfEnrolmentsPR rule) {
			final DFAGratuityByNumberOfEnrolmentsPREditor result = new DFAGratuityByNumberOfEnrolmentsPREditor();
			init(rule, result);

			return result;
		}

		static private void init(final DFAGratuityPR o1, final DFAGratuityPREditor o2) {
			o2.setDfaGratuityPR(o1);
			o2.setDfaPartialAcceptedPercentage(o1.getDfaPartialAcceptedPercentage());
			o2.setDfaTotalAmount(o1.getDfaTotalAmount());
		}

	}

	protected DFAGratuityByNumberOfEnrolmentsPR() {
		super();
	}

	public DFAGratuityByNumberOfEnrolmentsPR(DateTime startDate, DateTime endDate,
			ServiceAgreementTemplate serviceAgreementTemplate, Money dfaTotalAmount, BigDecimal partialAcceptedPercentage) {
		super();
		init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
				partialAcceptedPercentage);
	}

	@Override
	protected Money calculateDFAGratuityTotalAmountToPay(final Event event) {
		final GratuityEvent gratuityEvent = (GratuityEvent) event;
		final BigDecimal numberOfEnrolments = BigDecimal.valueOf(gratuityEvent.getEnrolmentsEctsForRegistration());
		final BigDecimal ectsCredits =
				BigDecimal.valueOf(gratuityEvent.getStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE)
						.getDefaultEcts(gratuityEvent.getExecutionYear()));

		final Money result =
				getDfaTotalAmount().multiply(
						numberOfEnrolments.divide(ectsCredits, SCALE_FOR_INTERMEDIATE_CALCULATIONS, RoundingMode.HALF_EVEN));
		return result.lessOrEqualThan(getDfaTotalAmount()) ? result : getDfaTotalAmount();
	}

	@Checked("RolePredicates.MANAGER_PREDICATE")
	public DFAGratuityByNumberOfEnrolmentsPR edit(Money dfaTotalAmount, BigDecimal partialAcceptedPercentage) {
		return edit(new DateTime(), dfaTotalAmount, partialAcceptedPercentage);
	}

	@Checked("RolePredicates.MANAGER_PREDICATE")
	public DFAGratuityByNumberOfEnrolmentsPR edit(DateTime startDate, Money dfaTotalAmount, BigDecimal partialAcceptedPercentage) {

		deactivate(startDate);

		return new DFAGratuityByNumberOfEnrolmentsPR(startDate, null, getServiceAgreementTemplate(), dfaTotalAmount,
				partialAcceptedPercentage);

	}

}
