/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting.paymentCodes;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PersonAccount;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.transactions.InsuranceTransaction;
import org.fenixedu.academic.domain.transactions.PaymentType;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

@Deprecated
public class MasterDegreeInsurancePaymentCode extends MasterDegreeInsurancePaymentCode_Base {

    private MasterDegreeInsurancePaymentCode() {
        super();
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
        throw new DomainException("error.accounting.paymentCodes.MasterDegreeInsurancePaymentCode.cannot.modify.executionYear");
    }

    @Override
    protected void internalProcess(Person responsiblePerson, Money amount, DateTime whenRegistered, String sibsTransactionId,
            String comments) {
        new InsuranceTransaction(amount.getAmount(), whenRegistered, PaymentType.SIBS, responsiblePerson, getPersonAccount(),
                getExecutionYear(), getRegistration());
    }

    private PersonAccount getPersonAccount() {
        return getPerson().getAssociatedPersonAccount();
    }

    private Registration getRegistration() {
        return getActiveRegistrationByDegreeType(getPerson().getStudent());
    }

    private static Registration getActiveRegistrationByDegreeType(Student student) {
        return student.getRegistrationsSet().stream()
                .filter(registration -> registration.getDegreeType().isPreBolonhaMasterDegree())
                .filter(Registration::isActive)
                .findFirst().orElse(null);
    }

    @Override
    public void delete() {
        super.setExecutionYear(null);
        super.delete();
    }
}
