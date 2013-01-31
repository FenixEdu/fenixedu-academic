package net.sourceforge.fenixedu.presentationTier.docs;

import java.io.Serializable;
import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.StandaloneEnrolmentGratuityEvent;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class IRSCustomDeclaration extends FenixReport {

	public static class IRSDeclarationDTO implements Serializable {

		static private final long serialVersionUID = 1L;

		private Integer civilYear;

		private Money gratuityAmount;

		private Money otherAmount;

		private String personName;

		private String personAddress;

		private String personAddressArea;

		private String personAddressPostalCode;

		private Integer studentNumber;

		private IDDocumentType idDocumentType;

		private String documentIdNumber;

		public IRSDeclarationDTO() {
			this.gratuityAmount = Money.ZERO;
			this.otherAmount = Money.ZERO;
		}

		public IRSDeclarationDTO(Integer civilYear) {
			this();
			this.civilYear = civilYear;
		}

		public IRSDeclarationDTO(Integer year, Person person) {
			this(year);
			setPersonAddress(person.getAddress());
			setPersonAddressArea(person.getArea());
			setPersonAddressPostalCode(person.getPostalCode());
			setDocumentIdNumber(person.getDocumentIdNumber());
			setPersonName(person.getName());
			setIdDocumentType(person.getIdDocumentType());
		}

		public void addGratuityAmount(final Money amount) {
			this.gratuityAmount = this.gratuityAmount.add(amount);
		}

		public void addOtherAmount(final Money amount) {
			this.otherAmount = this.otherAmount.add(amount);
		}

		public Integer getCivilYear() {
			return civilYear;
		}

		public void setCivilYear(Integer civilYear) {
			this.civilYear = civilYear;
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

		public String getPersonName() {
			return personName;
		}

		public IRSDeclarationDTO setPersonName(String studentName) {
			this.personName = studentName;
			return this;
		}

		public String getPersonAddress() {
			return personAddress;
		}

		public IRSDeclarationDTO setPersonAddress(String studentAddress) {
			this.personAddress = studentAddress;
			return this;
		}

		public String getPersonAddressArea() {
			return personAddressArea;
		}

		public IRSDeclarationDTO setPersonAddressArea(String studentAddressArea) {
			this.personAddressArea = studentAddressArea;
			return this;
		}

		public String getPersonAddressPostalCode() {
			return personAddressPostalCode;
		}

		public IRSDeclarationDTO setPersonAddressPostalCode(String studentAddressPostalCode) {
			this.personAddressPostalCode = studentAddressPostalCode;
			return this;
		}

		public Integer getStudentNumber() {
			return studentNumber;
		}

		public IRSDeclarationDTO setStudentNumber(Integer studentNumber) {
			this.studentNumber = studentNumber;
			return this;
		}

		public String getDocumentIdNumber() {
			return documentIdNumber;
		}

		public IRSDeclarationDTO setDocumentIdNumber(String studentDocumentIdNumber) {
			this.documentIdNumber = studentDocumentIdNumber;
			return this;
		}

		public IDDocumentType getIdDocumentType() {
			return idDocumentType;
		}

		public void setIdDocumentType(IDDocumentType idDocumentType) {
			this.idDocumentType = idDocumentType;
		}

		public Money getTotalAmount() {
			return this.gratuityAmount.add(this.otherAmount);
		}

		public void addAmount(final Event event, final int civilYear) {
			if (event instanceof GratuityEventWithPaymentPlan || event instanceof StandaloneEnrolmentGratuityEvent) {
				addGratuityAmount(event.getMaxDeductableAmountForLegalTaxes(civilYear));
			} else {
				addOtherAmount(event.getMaxDeductableAmountForLegalTaxes(civilYear));
			}
		}
	}

	static private final long serialVersionUID = 1L;

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

		addParameter("personName", this.declaration.getPersonName());
		addParameter("personAddress", this.declaration.getPersonAddress());
		addParameter("personAddressArea", this.declaration.getPersonAddressArea());
		addParameter("personAddressPostalCode", this.declaration.getPersonAddressPostalCode());
		addParameter("studentNumber", this.declaration.getStudentNumber() != null ? this.declaration.getStudentNumber()
				.toString() : null);
		addParameter("idDocumentType", this.declaration.getIdDocumentType().getLocalizedName());
		addParameter("documentIdNumber", this.declaration.getDocumentIdNumber());

		addParameter("civilYear", String.valueOf(this.declaration.getCivilYear()));

		addParameter("date", new LocalDate().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
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
		return MessageFormat.format("IRS-{0}-{1}-{2}", String.valueOf(this.declaration.getCivilYear()), this.declaration
				.getDocumentIdNumber().replace('/', '-').replace('\\', '-'), new DateTime().toString(YYYYMMMDD, getLocale()));

	}
}
