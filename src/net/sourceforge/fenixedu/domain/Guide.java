package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class Guide extends Guide_Base {

	protected PaymentType paymentType;

	protected Date creationDate;

	protected Date paymentDate;

	protected GuideRequester guideRequester;

	public Guide() {
	}

	public Guide(Integer number, Integer year, Double total, String remarks,
			IPerson person, IContributor contributor,
			GuideRequester guideRequester, IExecutionDegree executionDegree,
			PaymentType paymentType, Date creationDate, Integer version) {
		setContributor(contributor);
		setNumber(number);
		setPerson(person);
		setRemarks(remarks);
		setTotal(total);
		setYear(year);
		this.guideRequester = guideRequester;
		setExecutionDegree(executionDegree);
		this.paymentType = paymentType;
		this.creationDate = creationDate;
		setVersion(version);
	}

	/**
	 * @param guideId
	 */
	public Guide(Integer guideId) {
		setIdInternal(guideId);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof IGuide) {
			IGuide guide = (IGuide) obj;

			if (((getNumber() == null && guide.getNumber() == null) || (getNumber()
					.equals(guide.getNumber())))
					&& ((getYear() == null && guide.getYear() == null) || (getYear()
							.equals(guide.getYear())))) {
				resultado = true;
			}
		}

		return resultado;
	}

	public String toString() {
		String result = "[GUIDE";
		result += ", codInt=" + getIdInternal();
		result += ", number=" + getNumber();
		result += ", year=" + getYear();
		result += ", contributor=" + getContributor();
		result += ", total=" + getTotal();
		result += ", remarks=" + getRemarks();
		result += ", guide Requester=" + guideRequester;
		result += ", execution Degree=" + getExecutionDegree();
		result += ", payment Type=" + paymentType;
		result += ", creation Date=" + creationDate;
		result += ", version=" + getVersion();
		result += ", payment Date=" + paymentDate;
		result += "]";
		return result;
	}

	public IGuideSituation getActiveSituation() {
		if (this.getGuideSituations() != null) {
			Iterator iterator = this.getGuideSituations().iterator();
			while (iterator.hasNext()) {
				IGuideSituation guideSituation = (IGuideSituation) iterator
						.next();
				if (guideSituation.getState().equals(new State(State.ACTIVE))) {
					return guideSituation;
				}
			}
		}
		return null;

	}

	/**
	 * @return
	 */
	public GuideRequester getGuideRequester() {
		return guideRequester;
	}

	/**
	 * @param requester
	 */
	public void setGuideRequester(GuideRequester requester) {
		guideRequester = requester;
	}

	/**
	 * @return
	 */
	public PaymentType getPaymentType() {
		return paymentType;
	}

	/**
	 * @param type
	 */
	public void setPaymentType(PaymentType type) {
		paymentType = type;
	}

	/**
	 * @return
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param date
	 */
	public void setCreationDate(Date date) {
		creationDate = date;
	}

	/**
	 * @return
	 */
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param date
	 */
	public void setPaymentDate(Date date) {
		paymentDate = date;
	}
}