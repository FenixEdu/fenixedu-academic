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
/*
 * Created on Nov 11, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum QualificationType {

    DEGREE(FormationType.PRE_BOLONHA_DEGREE_EQUILAVENT_FORMATION), DEGREE_FOREIGNER_WITH_EQUIVALENCE(null),
    DEGREE_FOREIGNER_WITHOUT_EQUIVALENCE(null), MASTER_DEGREE(FormationType.PRE_BOLONHA_DEGREE_EQUILAVENT_FORMATION),
    MASTER_DEGREE_WITH_RECOGNITION(null), MASTER_DEGREE_FOREIGNER_WITH_EQUIVALENCE(null),
    MASTER_DEGREE_FOREIGNER_WITHOUT_EQUIVALENCE(null), MASTER(null), DOCTORATE_DEGREE(
            FormationType.PRE_BOLONHA_DEGREE_EQUILAVENT_FORMATION), DOCTORATE_DEGREE_BOLOGNA(null),
    DOCTORATE_DEGREE_WITH_RECOGNITION(null), DOCTORATE_DEGREE_WITH_REGISTER(null), DOCTORATE_DEGREE_FOREIGNER_WITH_EQUIVALENCE(
            null), DOCTORATE_DEGREE_FOREIGNER_WITHOUT_EQUIVALENCE(null), BACHELOR_DEGREE(
            FormationType.PRE_BOLONHA_DEGREE_EQUILAVENT_FORMATION), BACHELOR_DEGREE_FOREIGNER_WITH_EQUIVALENCE(null),
    BACHELOR_DEGREE_FOREIGNER_WITHOUT_EQUIVALENCE(null),

    OTHER(null),

    FIRST_CLASS(null), THIRD_CLASS(null), FOUR_CLASS(null),

    SIXTH_YEAR_PREPARATORY_CYCLE(null), APCC(null), BASIC_LEVEL2(null),

    SECOND_YEAR_GERAL_COURSE_SCHOOL(null),

    FIFTH_YEAR(null), SEVEN_YEAR_INCOMPLETE(null), SEVEN_YEAR(null), EIGHTH_YEAR(null), NINETH_YEAR(null), TEN_YEAR(null),
    ELEVEN_YEAR(null), ELEVEN_YEAR_INCOMPLETE(null), TWELVE_YEAR(null),

    UNIVERSITY_FIRST_YEAR(null), UNIVERSITY_SECOND_YEAR(null), UNIVERSITY_THIRD_YEAR(null), UNIVERSITY_FOUR_YEAR(null),
    UNIVERSITY_FIVE_YEAR(null),

    PROPEDEUTICO(null),

    ACTION_FORMATION(null), AGGREGATION(null),

    ACCOUNTING_AND_ADMINISTRATION_COMPLEMENT_COURSE(null), COURSE_COMPLEMENT(null), COURSE_COMPLEMENT_SCHOOL(null),
    COURSE_COMPLEMENT_SCHOOL_INCOMPLETE(null), GERAL_COURSE(null), COMMERCE_AND_ADMINISTRATION_GERAL_COURSE(null),
    COMMERCE_GERAL_COURSE_INCOMPLETE(null), GERAL_COURSE_SCHOOL(null),

    SUPERIOR_COURSE_INCOMPLETE(null),

    NITH_YEAR_EQUIVALENT_FOR_PROFESSIONAL(null), ELEVEN_YEAR_EQUIVALENT_FOR_PUBLIC_FUNCTIONS(null), TWELVE_YEAR_EQUIVALENT(null),

    EQUIVALENT_FOR_PROFESSIONAL_AND_COMPLEMENTAR_COURSE(null),

    DEGREE_FREQUENCY(null), BACHELOR_FREQUENCY(null), INTEGRATED_MASTER_DEGREE_FREQUENCY(null), MASTER_DEGREE_FREQUENCY(null),
    FIRST_CYCLE_FREQUENCY(null),
    SECOND_CYCLE_FREQUENCY(null),

    PRIMARY_TEACHING(null),

    FIRST_CYCLE(FormationType.POST_BOLONHA_DEGREE_EQUIVALENT_FORMATION),
    SECOND_CYCLE(FormationType.POST_BOLONHA_DEGREE_EQUIVALENT_FORMATION),
    INTEGRATED_MASTER_DEGREE(FormationType.POST_BOLONHA_DEGREE_EQUIVALENT_FORMATION),
    DEA(FormationType.POST_BOLONHA_DEGREE_EQUIVALENT_FORMATION),
    // DEGREE(FormationType.PRE_BOLONHA_DEGREE_EQUILAVENT_FORMATION),
    // MASTER_DEGREE(FormationType.PRE_BOLONHA_DEGREE_EQUILAVENT_FORMATION),
    // DOCTORATE_DEGREE(FormationType.PRE_BOLONHA_DEGREE_EQUILAVENT_FORMATION),
    FREE_COURSE(FormationType.PRE_BOLONHA_DEGREE_EQUILAVENT_FORMATION),
    // BACHELOR_DEGREE(FormationType.PRE_BOLONHA_DEGREE_EQUILAVENT_FORMATION),
    BACHELOR_AND_DEGREE(FormationType.PRE_BOLONHA_DEGREE_EQUILAVENT_FORMATION), MBA(FormationType.NON_DEGREE_ADVANCED_FORMATION),
    DFA(FormationType.NON_DEGREE_ADVANCED_FORMATION), SUPERIOR_SPECIALIZATION(FormationType.NON_DEGREE_ADVANCED_FORMATION),
    RECYCLING(FormationType.PROFESSIONAL_FORMATION), SPECIALIZATION(FormationType.PROFESSIONAL_FORMATION), PEDAGOGIC_FORMATION(
            FormationType.PROFESSIONAL_FORMATION), SHORT_FORMATION(FormationType.PROFESSIONAL_FORMATION), CONTINUOUS_FORMATION(
            FormationType.PROFESSIONAL_FORMATION), QUALIFIED_FORMATION(FormationType.PROFESSIONAL_FORMATION),

    HIGH_SCHOOL(null), TECHNICAL_SPECIALIZATION_COURSE(null);

    private FormationType formationType;

    private QualificationType(FormationType formationType) {
        this.formationType = formationType;
    }

    public String getName() {
        return name();
    }

    public FormationType getFormationType() {
        return formationType;
    }

    public static List<QualificationType> getbyFormationType(FormationType formationType) {
        List<QualificationType> types = new ArrayList<QualificationType>();
        for (QualificationType type : QualificationType.values()) {
            if (type.getFormationType() != null && type.getFormationType().equals(formationType)) {
                types.add(type);
            }
        }

        return types;
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, locale, getQualifiedName());
    }

    public String getQualifiedName() {
        return QualificationType.class.getSimpleName() + "." + name();
    }

    public boolean isDegree() {
        return equals(DEGREE) || equals(DEGREE_FOREIGNER_WITH_EQUIVALENCE) || equals(DEGREE_FOREIGNER_WITHOUT_EQUIVALENCE);
    }

    public boolean isMaster() {
        return equals(MASTER) || equals(MASTER_DEGREE) || equals(MASTER_DEGREE_FOREIGNER_WITH_EQUIVALENCE)
                || equals(MASTER_DEGREE_FOREIGNER_WITHOUT_EQUIVALENCE) || equals(MASTER_DEGREE_WITH_RECOGNITION);
    }

    public boolean isDoctorate() {
        return equals(DOCTORATE_DEGREE) || equals(DOCTORATE_DEGREE_BOLOGNA)
                || equals(DOCTORATE_DEGREE_FOREIGNER_WITH_EQUIVALENCE) || equals(DOCTORATE_DEGREE_FOREIGNER_WITHOUT_EQUIVALENCE)
                || equals(DOCTORATE_DEGREE_WITH_RECOGNITION) || equals(DOCTORATE_DEGREE_WITH_REGISTER);
    }
}
