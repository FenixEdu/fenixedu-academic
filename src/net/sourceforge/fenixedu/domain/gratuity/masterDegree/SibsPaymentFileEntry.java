/*
 * Created on Apr 22, 2004
 */
package net.sourceforge.fenixedu.domain.gratuity.masterDegree;

import java.util.Date;

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

    public SibsPaymentFileEntry(Integer year, Integer studentNumber, SibsPaymentType paymentType,
            Date transactionDate, Double payedValue, SibsPaymentFile sibsPaymentFile,
            SibsPaymentStatus paymentStatus) {
    	this();
        this.setYear(year);
        this.setStudentNumber(studentNumber);
        this.setPaymentType(paymentType);
        this.setTransactionDate(transactionDate);
        this.setPayedValue(payedValue);
        this.setSibsPaymentFile(sibsPaymentFile);
        this.setPaymentStatus(paymentStatus);
    }

}
