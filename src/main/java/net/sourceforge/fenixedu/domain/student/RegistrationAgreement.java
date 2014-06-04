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
package net.sourceforge.fenixedu.domain.student;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum RegistrationAgreement {

    NORMAL(true, true, true, false),

    AFA(false, false, false, false),

    MA(false, false, false, false),

    NC(false, false, false, false),

    ERASMUS(false, false, true, false),

    SOCRATES(false, false, true, false),

    SOCRATES_ERASMUS(false, false, true, false),

    TEMPUS(false, false, true, false),

    BILATERAL_AGREEMENT(false, false, true, false),

    ALFA2(false, false, true, false),

    UNIFOR(false, false, true, false),

    TIME(false, false, true, false),

    TOTAL(false, true, true, false),

    OTHER_EXTERNAL(false, false, true, false),

    MITP(false, true, true, false),

    SMILE(false, false, true, false),

    ANGOLA_TELECOM(false, true, true, false),

    ERASMUS_MUNDUS(false, false, true, false),

    ALMEIDA_GARRETT(false, false, true, false),

    INOV_IST(false, false, true, false),

    TECMIC(false, false, true, false),

    IST_UCP(false, false, true, false),

    IST_USP(false, false, true, false),

    CLUSTER(false, false, true, false),

    EUSYSBIO(false, false, true, false),

    IST_ISA(false, false, true, false),

    IST_PHARMACY_FACULTY(false, false, true, false),

    IBERO_SANTANDER(false, false, true, false),

    BRAZIL_SANTANDER(false, false, true, false),

    CHINA_AGREEMENTS(false, false, true, false),

    RUSSIA_AGREEMENTS(false, false, true, false),

    AFRICA_AGREEMENTS(false, false, true, false),

    BRAZIL_AGREEMENTS(false, false, true, false),

    INDIA_AGREEMENTS(false, false, true, false),

    JAPAN_AGREEMENTS(false, false, true, false),

    THAILAND_AGREEMENTS(false, false, true, false),

    SCIENCE_WITHOUT_BORDERS(false, false, true, false),

    USA_AGREEMENTS(false, true, true, false),

    KIC_INNOENERGY(false, false, true, true),

    KIC_RENE(false, false, true, true),

    KIC_SELECT(false, false, true, true),

    KIC_ENTECH(false, false, true, true),

    KIC_CLEAN_COAL(false, false, true, true),

    DOUBLE_DEGREE_CLUSTER(false, false, true, false),

    DOUBLE_DEGREE_BRAZIL(false, false, true, false),

    DOUBLE_DEGREE_FRANCE(false, false, true, false),

    DOUBLE_DEGREE_ITALY(false, false, true, false),

    DOUBLE_DEGREE_CHINA(false, false, true, false),

    DOUBLE_DEGREE_NETHERLANDS(false, false, true, false),

    IS_LINK(false, false, true, false);

    private boolean enrolmentByStudentAllowed;

    private boolean payGratuity;

    private boolean allowsIDCard;

    private boolean onlyAllowedDegreeEnrolment;

    private RegistrationAgreement(final boolean enrolmentByStudentAllowed, final boolean payGratuity, final boolean allowsIDCard,
            final boolean onlyAllowedDegreeEnrolment) {
        this.enrolmentByStudentAllowed = enrolmentByStudentAllowed;
        this.payGratuity = payGratuity;
        this.allowsIDCard = allowsIDCard;
        this.onlyAllowedDegreeEnrolment = onlyAllowedDegreeEnrolment;
    }

    public boolean isNormal() {
        return this.equals(RegistrationAgreement.NORMAL);
    }

    public boolean isMilitaryAgreement() {
        return this.equals(RegistrationAgreement.AFA) || this.equals(RegistrationAgreement.MA);
    }

    public String getName() {
        return name();
    }

    public boolean isEnrolmentByStudentAllowed() {
        return this.enrolmentByStudentAllowed;
    }

    public boolean isToPayGratuity() {
        return payGratuity;
    }

    public boolean allowsIDCard() {
        return this.allowsIDCard;
    }

    public boolean isOnlyAllowedDegreeEnrolment() {
        return onlyAllowedDegreeEnrolment;
    }

    public static RegistrationAgreement getByLegacyCode(int code) {

        switch (code) {
        case 1:
            return AFA;
        case 2:
            return MA;
        case 3:
            return NC;
        case 4:
            return ERASMUS;
        case 5:
            return SOCRATES;
        case 6:
            return SOCRATES_ERASMUS;
        case 7:
            return TEMPUS;
        case 8:
            return BILATERAL_AGREEMENT;
        case 9:
            return ALFA2;
        case 10:
            return UNIFOR;
        }

        return null;
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

    public String getDescription() {
        return getDescription(I18N.getLocale());
    }

    public String getDescription(final Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, locale, getQualifiedName());
    }

    final public static List<RegistrationAgreement> EXEMPTED_AGREEMENTS = Arrays.asList(IST_UCP);

    final public static List<RegistrationAgreement> MOBILITY_AGREEMENTS = Arrays.asList(ERASMUS, SMILE, CLUSTER, TIME,
            BILATERAL_AGREEMENT, ERASMUS_MUNDUS, IBERO_SANTANDER, SCIENCE_WITHOUT_BORDERS, CHINA_AGREEMENTS, RUSSIA_AGREEMENTS,
            AFRICA_AGREEMENTS, BRAZIL_AGREEMENTS, ALMEIDA_GARRETT, ERASMUS_MUNDUS, USA_AGREEMENTS, KIC_INNOENERGY,
            BRAZIL_SANTANDER);

}
