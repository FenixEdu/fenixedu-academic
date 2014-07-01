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
package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum PhdThesisFinalGrade implements IPresentableEnum {

    NOT_APPROVED(true, false),

    APPROVED(true, true),

    APPROVED_WITH_PLUS(true, true),

    APPROVED_WITH_PLUS_PLUS(true, true),

    PRE_BOLONHA_NOT_APPROVED(true, false),

    PRE_BOLONHA_APPROVED(false, true),

    PRE_BOLONHA_APPROVED_WITH_PLUS(false, true),

    PRE_BOLONHA_APPROVED_WITH_PLUS_PLUS(false, true);

    private boolean forBolonha;

    private boolean approved;

    private PhdThesisFinalGrade(boolean forBolonha, boolean approved) {
        this.forBolonha = forBolonha;
        this.approved = approved;
    }

    public String getName() {
        return name();
    }

    @Override
    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return BundleUtil.getString(Bundle.PHD, locale, getQualifiedName());
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

    public boolean isForBolonha() {
        return forBolonha;
    }

    public boolean isApproved() {
        return approved;
    }

    static public List<PhdThesisFinalGrade> getGradesForBolonha() {
        ArrayList<PhdThesisFinalGrade> values = new ArrayList<PhdThesisFinalGrade>();
        for (PhdThesisFinalGrade grade : PhdThesisFinalGrade.values()) {
            if (grade.isForBolonha()) {
                values.add(grade);
            }
        }

        return values;
    }

    static public List<PhdThesisFinalGrade> getGradesForPreBolonha() {
        ArrayList<PhdThesisFinalGrade> values = new ArrayList<PhdThesisFinalGrade>();
        for (PhdThesisFinalGrade grade : PhdThesisFinalGrade.values()) {
            if (!grade.isForBolonha()) {
                values.add(grade);
            }
        }

        return values;
    }

}
