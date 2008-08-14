/*
 * Created on Apr 22, 2004
 */
package net.sourceforge.fenixedu.domain.gratuity.masterDegree;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class SibsPaymentFileEntry extends SibsPaymentFileEntry_Base {

    public SibsPaymentFileEntry() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public SibsPaymentFileEntry(Integer year, Integer studentNumber, SibsPaymentType paymentType, Date transactionDate,
	    Double payedValue, SibsPaymentFile sibsPaymentFile, SibsPaymentStatus paymentStatus) {
	this();
	this.setYear(year);
	this.setStudentNumber(studentNumber);
	this.setPaymentType(paymentType);
	this.setTransactionDate(transactionDate);
	this.setPayedValue(payedValue);
	this.setSibsPaymentFile(sibsPaymentFile);
	this.setPaymentStatus(paymentStatus);
    }

    public static List<SibsPaymentFileEntry> readNonProcessed() {
	final List<SibsPaymentFileEntry> result = new ArrayList<SibsPaymentFileEntry>();
	for (final SibsPaymentFileEntry sibsPaymentFileEntry : RootDomainObject.getInstance().getSibsPaymentFileEntrysSet()) {
	    if (sibsPaymentFileEntry.getPaymentStatus() != SibsPaymentStatus.PROCESSED_PAYMENT) {
		result.add(sibsPaymentFileEntry);
	    }
	}
	return result;
    }

    public static List<SibsPaymentFileEntry> readByYearAndStudentNumberAndPaymentType(final Integer year,
	    final Integer studentNumber, final SibsPaymentType sibsPaymentType) {
	final List<SibsPaymentFileEntry> result = new ArrayList<SibsPaymentFileEntry>();
	for (final SibsPaymentFileEntry sibsPaymentFileEntry : RootDomainObject.getInstance().getSibsPaymentFileEntrysSet()) {
	    if (sibsPaymentFileEntry.getYear().equals(year) && sibsPaymentFileEntry.getStudentNumber().equals(studentNumber)
		    && sibsPaymentFileEntry.getPaymentType() == sibsPaymentType) {
		result.add(sibsPaymentFileEntry);
	    }
	}
	return result;
    }

    public static List<SibsPaymentFileEntry> readByYearAndStudentNumberAndPaymentTypeExceptFileEntry(
	    SibsPaymentFileEntry paymentFileEntry) {
	final List<SibsPaymentFileEntry> result = new ArrayList<SibsPaymentFileEntry>();
	for (final SibsPaymentFileEntry sibsPaymentFileEntry : RootDomainObject.getInstance().getSibsPaymentFileEntrysSet()) {
	    if (sibsPaymentFileEntry != paymentFileEntry && sibsPaymentFileEntry.getYear().equals(paymentFileEntry.getYear())
		    && sibsPaymentFileEntry.getStudentNumber().equals(paymentFileEntry.getStudentNumber())
		    && sibsPaymentFileEntry.getPaymentType().equals(paymentFileEntry.getPaymentType())) {
		result.add(sibsPaymentFileEntry);
	    }
	}
	return result;
    }

}
