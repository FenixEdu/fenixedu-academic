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
package net.sourceforge.fenixedu.domain.accounting;

import java.util.Locale;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum PaymentCodeType {

    TOTAL_GRATUITY(0),

    GRATUITY_FIRST_INSTALLMENT(1),

    GRATUITY_SECOND_INSTALLMENT(2),

    ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE(3),

    INSURANCE(4),

    PRE_BOLONHA_MASTER_DEGREE_TOTAL_GRATUITY(5),

    PRE_BOLONHA_MASTER_DEGREE_INSURANCE(6),

    RESIDENCE_FEE(7),

    INSTITUTION_ACCOUNT_CREDIT(8, true),

    INTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS(15),

    EXTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS(16),

    INTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS(17),

    EXTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS(18),

    SECOND_CYCLE_INDIVIDUAL_CANDIDACY_PROCESS(19),

    INTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS(20),

    EXTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS(21),

    OVER_23_INDIVIDUAL_CANDIDACY_PROCESS(22),

    PHD_PROGRAM_CANDIDACY_PROCESS(23),

    RECTORATE(99);

    private int typeDigit;

    private boolean reusable;

    private PaymentCodeType(int typeDigit) {
        this(typeDigit, false);
    }

    private PaymentCodeType(int typeDigit, boolean reusable) {
        this.typeDigit = typeDigit;
        this.reusable = reusable;
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return PaymentCodeType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return PaymentCodeType.class.getName() + "." + name();
    }

    public int getTypeDigit() {
        return typeDigit;
    }

    public boolean isReusable() {
        return reusable;
    }

    public String localizedName(Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }

    protected String localizedName() {
        return localizedName(I18N.getLocale());
    }

    public String getLocalizedName() {
        return localizedName();
    }

}
