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
package net.sourceforge.fenixedu.domain.accounting.util;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.util.Verhoeff;

import org.apache.commons.lang.StringUtils;

/**
 * Code Format: <numericPartOfIstId{6}><typeDigit{2}><checkDigit{1}>
 */
public class PersonPaymentCodeGenerator extends PaymentCodeGenerator {
    private static final String CODE_FILLER = "0";

    private static final int PERSON_CODE_LENGTH = 6;

    private static final int TYPE_CODE_LENGTH = 2;

    private static final int CODE_LENGTH = 9;

    public PersonPaymentCodeGenerator() {
    }

    @Override
    public boolean canGenerateNewCode(final PaymentCodeType paymentCodeType, final Person person) {
        for (PaymentCode code : person.getPaymentCodesBy(paymentCodeType)) {
            if (isCodeMadeByThisFactory(code)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String generateNewCodeFor(final PaymentCodeType paymentCodeType, final Person person) {
        String baseCode =
                getPersonCodeDigits(person)
                        + StringUtils.leftPad(Integer.toString(paymentCodeType.getTypeDigit()), TYPE_CODE_LENGTH, CODE_FILLER);
        baseCode = baseCode + Verhoeff.generateVerhoeff(baseCode);
        if (baseCode.length() != CODE_LENGTH) {
            throw new RuntimeException("Unexpected code length for generated code");
        }
        return baseCode;
    }

    private String getPersonCodeDigits(Person person) {
        if (person.getIstUsername().length() > 9) {
            throw new RuntimeException("SIBS Payment Code: " + person.getIstUsername() + " exceeded maximun size accepted");
        }
        return StringUtils.leftPad(person.getIstUsername().replace("ist", ""), PERSON_CODE_LENGTH, CODE_FILLER);
    }

    @Override
    public boolean isCodeMadeByThisFactory(PaymentCode paymentCode) {
        return paymentCode.getCode().startsWith(getPersonCodeDigits(paymentCode.getPerson()));
    }
}
