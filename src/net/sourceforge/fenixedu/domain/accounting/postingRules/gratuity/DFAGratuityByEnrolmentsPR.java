package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class DFAGratuityByEnrolmentsPR extends DFAGratuityByEnrolmentsPR_Base {
	private static final int SCALE_FOR_INTERMEDIATE_CALCULATIONS = 8;

	public static class DFAGratuityByEnrolmentsPREditor extends DFAGratuityPREditor {

		private Money dfaAmountPerEnrolment;

		private DFAGratuityByEnrolmentsPREditor() {
			super();
		}

		@Override
		public Object execute() {
			return ((DFAGratuityByEnrolmentsPR) getDfaGratuityPR()).edit(getBeginDate(), getDfaTotalAmount(),
					getDfaAmountPerEnrolment(), getDfaPartialAcceptedPercentage());
		}

		public static DFAGratuityByEnrolmentsPREditor buildFrom(final DFAGratuityByEnrolmentsPR rule) {
			final DFAGratuityByEnrolmentsPREditor result = new DFAGratuityByEnrolmentsPREditor();
			init(rule, result);

			return result;
		}

		static private void init(final DFAGratuityPR o1, final DFAGratuityPREditor o2) {
			o2.setDfaGratuityPR(o1);
			o2.setDfaPartialAcceptedPercentage(o1.getDfaPartialAcceptedPercentage());
			o2.setDfaTotalAmount(o1.getDfaTotalAmount());
		}

		public Money getDfaAmountPerEnrolment() {
			return dfaAmountPerEnrolment;
		}

		public void setDfaAmountPerEnrolment(Money dfaAmountPerEnrolment) {
			this.dfaAmountPerEnrolment = dfaAmountPerEnrolment;
		}

	}

	protected DFAGratuityByEnrolmentsPR() {
		super();
	}

	public DFAGratuityByEnrolmentsPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
			Money dfaTotalAmount, BigDecimal partialAcceptedPercentage, Money dfaAmountPerEnrolment) {
		super();
		init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
				partialAcceptedPercentage, dfaAmountPerEnrolment);
	}

	protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
			ServiceAgreementTemplate serviceAgreementTemplate, Money dfaTotalAmount, BigDecimal dfaPartialAcceptedPercentage,
			Money dfaAmountPerEnrolment) {

		super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
				dfaPartialAcceptedPercentage);

		checkParameters(dfaAmountPerEnrolment);
		super.setDfaAmountPerEnrolment(dfaAmountPerEnrolment);
	}

	private void checkParameters(Money dfaAmountPerEnrolment) {
		if (dfaAmountPerEnrolment == null) {
			throw new DomainException(
					"error.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR.dfaAmountPerEnrolment.cannot.be.null");
		}
	}

	@Override
	protected Money calculateDFAGratuityTotalAmountToPay(Event event) {
		DfaGratuityEvent dfaGratuity = (DfaGratuityEvent) event;

		return getDfaAmountPerEnrolment().multiply(
				dfaGratuity.getRegistration().getEnrolments(dfaGratuity.getExecutionYear()).size());
	}

	@Override
	public void setDfaAmountPerEnrolment(Money dfaAmountPerEnrolment) {
		throw new DomainException(
				"error.accounting.postingRules.gratuity.DFAGratuityByEnrolmentsPR.cannot.modify.dfaAmountPerEnrolment");
	}

	@Checked("RolePredicates.MANAGER_PREDICATE")
	public DFAGratuityByEnrolmentsPR edit(Money dfaTotalAmount, Money dfaAmountPerEnrolment, BigDecimal partialAcceptedPercentage) {
		return edit(new DateTime(), dfaTotalAmount, dfaAmountPerEnrolment, partialAcceptedPercentage);
	}

	@Checked("RolePredicates.MANAGER_PREDICATE")
	public DFAGratuityByEnrolmentsPR edit(DateTime startDate, Money dfaTotalAmount, Money dfaAmountPerEnrolment,
			BigDecimal partialAcceptedPercentage) {

		deactivate(startDate);

		return new DFAGratuityByEnrolmentsPR(startDate, null, getServiceAgreementTemplate(), dfaTotalAmount,
				partialAcceptedPercentage, dfaAmountPerEnrolment);
	}

}
