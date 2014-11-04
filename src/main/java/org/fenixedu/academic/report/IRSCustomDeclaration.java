/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.docs;

import java.io.Serializable;
import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
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

        private Money residenceAmount;

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
            this.residenceAmount = Money.ZERO;
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

        public void addResidenceAmount(final Money amount) {
            this.residenceAmount = this.residenceAmount.add(amount);
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

        public Money getResidenceAmount() {
            return residenceAmount;
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
            return getGratuityAmount().add(getOtherAmount()).add(getResidenceAmount());
        }

        public void addAmount(final Event event, final int civilYear) {
            if (event instanceof GratuityEventWithPaymentPlan || event instanceof StandaloneEnrolmentGratuityEvent) {
                addGratuityAmount(event.getMaxDeductableAmountForLegalTaxes(civilYear));
            } else if (event instanceof ResidenceEvent) {
                addResidenceAmount(event.getMaxDeductableAmountForLegalTaxes(civilYear));
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
        addParameter("residenceAmount", this.declaration.getResidenceAmount().toPlainString());
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
                .getDocumentIdNumber().trim().replace('/', '-').replace('\\', '-'),
                new DateTime().toString(YYYYMMMDD, getLocale()));

    }
}
