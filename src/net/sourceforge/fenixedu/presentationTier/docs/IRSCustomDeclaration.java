package net.sourceforge.fenixedu.presentationTier.docs;

import java.text.MessageFormat;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class IRSCustomDeclaration extends FenixReport {

    public static class IRSDeclarationDTO {

	private int civilYear;

	private Money gratuityAmount;

	private Money otherAmount;

	private String studentName;

	private String studentAddress;

	private String studentAddressArea;

	private String studentAddressPostalCode;

	private Integer studentNumber;

	private String studentDocumentIdNumber;

	public IRSDeclarationDTO(final Integer studentNumber, final int civilYear) {
	    this.studentNumber = studentNumber;
	    this.civilYear = civilYear;
	    this.gratuityAmount = Money.ZERO;
	    this.otherAmount = Money.ZERO;
	}

	public void addGratuityAmount(final Money amount) {
	    this.gratuityAmount = this.gratuityAmount.add(amount);
	}

	public void addOtherAmount(final Money amount) {
	    this.otherAmount = this.otherAmount.add(amount);
	}

	public int getCivilYear() {
	    return civilYear;
	}

	public IRSDeclarationDTO setCivilYear(int civilYear) {
	    this.civilYear = civilYear;
	    return this;
	}

	public Money getGratuityAmount() {
	    return gratuityAmount;
	}

	public IRSDeclarationDTO setGratuityAmount(Money gratuityAmount) {
	    this.gratuityAmount = gratuityAmount;
	    return this;
	}

	public Money getOtherAmount() {
	    return otherAmount;
	}

	public IRSDeclarationDTO setOtherAmount(Money otherAmount) {
	    this.otherAmount = otherAmount;
	    return this;
	}

	public String getStudentName() {
	    return studentName;
	}

	public IRSDeclarationDTO setStudentName(String studentName) {
	    this.studentName = studentName;
	    return this;
	}

	public String getStudentAddress() {
	    return studentAddress;
	}

	public IRSDeclarationDTO setStudentAddress(String studentAddress) {
	    this.studentAddress = studentAddress;
	    return this;
	}

	public String getStudentAddressArea() {
	    return studentAddressArea;
	}

	public IRSDeclarationDTO setStudentAddressArea(String studentAddressArea) {
	    this.studentAddressArea = studentAddressArea;
	    return this;
	}

	public String getStudentAddressPostalCode() {
	    return studentAddressPostalCode;
	}

	public IRSDeclarationDTO setStudentAddressPostalCode(String studentAddressPostalCode) {
	    this.studentAddressPostalCode = studentAddressPostalCode;
	    return this;
	}

	public Integer getStudentNumber() {
	    return studentNumber;
	}

	public IRSDeclarationDTO setStudentNumber(Integer studentNumber) {
	    this.studentNumber = studentNumber;
	    return this;
	}

	public String getStudentDocumentIdNumber() {
	    return studentDocumentIdNumber;
	}

	public IRSDeclarationDTO setStudentDocumentIdNumber(String studentDocumentIdNumber) {
	    this.studentDocumentIdNumber = studentDocumentIdNumber;
	    return this;
	}

	public Money getTotalAmount() {
	    return this.gratuityAmount.add(this.otherAmount);
	}
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private IRSDeclarationDTO declaration;

    public IRSCustomDeclaration(final IRSDeclarationDTO declarationDTO) {
	this.declaration = declarationDTO;
	fillReport();
    }

    @Override
    protected void fillReport() {
	fillParameters();
    }

    private void fillParameters() {

	addParameter("studentName", this.declaration.getStudentName());
	addParameter("studentAddress", this.declaration.getStudentAddress());
	addParameter("studentAddressArea", this.declaration.getStudentAddressArea());
	addParameter("studentAddressPostalCode", this.declaration.getStudentAddressPostalCode());
	addParameter("studentNumber", this.declaration.getStudentNumber().toString());
	addParameter("studentDocumentIdNumber", this.declaration.getStudentDocumentIdNumber());

	addParameter("civilYear", String.valueOf(this.declaration.getCivilYear()));

	addParameter("date", new YearMonthDay().toString("yyyy/MM/dd", Language.getLocale()));
	addParameter("gratuityAmount", this.declaration.getGratuityAmount().toPlainString());
	addParameter("otherAmount", this.declaration.getOtherAmount().toPlainString());
	addParameter("totalAmount", this.declaration.getTotalAmount().toPlainString());

    }

    @Override
    public String getReportTemplateKey() {
	return getClass().getName();
    }

    @Override
    public String getReportFileName() {
	return MessageFormat.format("DECLARATION-{0}-{1}", this.declaration.getStudentNumber().toString(), new DateTime()
		.toString("yyyyMMdd"));

    }

}
