/*
 * Created on Apr 22, 2004
 */
package net.sourceforge.fenixedu.domain.gratuity.masterDegree;


import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class SibsPaymentFileEntry extends SibsPaymentFileEntry_Base {

	private SibsPaymentType paymentType;

	private Timestamp transactionDate;

	private SibsPaymentStatus paymentStatus;

	public SibsPaymentFileEntry() {
	}

	/**
	 * @param year
	 * @param studentNumber
	 * @param paymentType
	 * @param transactionDate
	 * @param payedValue
	 * @param sibsPaymentFile
	 * @param sibsPaymentStatusType
	 */
	public SibsPaymentFileEntry(Integer year, Integer studentNumber,
			SibsPaymentType paymentType, Timestamp transactionDate,
			Double payedValue, ISibsPaymentFile sibsPaymentFile,
			SibsPaymentStatus paymentStatus) {
		setYear(year);
		setStudentNumber(studentNumber);
		this.paymentType = paymentType;
		this.transactionDate = transactionDate;
		setPayedValue(payedValue);
		this.setSibsPaymentFile(sibsPaymentFile);
		this.paymentStatus = paymentStatus;
	}

	/**
	 * @return Returns the paymentType.
	 */
	public SibsPaymentType getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType
	 *            The paymentType to set.
	 */
	public void setPaymentType(SibsPaymentType paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return Returns the transactionDate.
	 */
	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            The transactionDate to set.
	 */
	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return Returns the paymentStatus.
	 */
	public SibsPaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	/**
	 * @param paymentStatus
	 *            The paymentStatus to set.
	 */
	public void setPaymentStatus(SibsPaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if ((obj instanceof ISibsPaymentFileEntry)) {
			ISibsPaymentFileEntry sibsFileEntry = (ISibsPaymentFileEntry) obj;
			if ((this.getIdInternal() != null)
					&& (sibsFileEntry.getIdInternal() != null)
					&& (this.getIdInternal().equals(sibsFileEntry
							.getIdInternal()))) {

				result = true;
			}

		}

		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": \n";
		result += "idInternal = " + getIdInternal() + "; \n";
		result += "payedValue = " + getPayedValue() + "; \n";
		result += "paymentType = " + this.paymentType + "; \n";
		result += "paymentStatus = " + this.paymentStatus + "; \n";
		result += "sibsPaymentFile = " + getSibsPaymentFile().toString()
				+ "; \n";
		result += "studentNumber = " + getStudentNumber() + "; \n";
		result += "transactionDate = " + this.transactionDate.toString()
				+ "; \n";
		result += "year = " + getYear() + "; \n";
		result += "] \n";

		return result;
	}

}