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
package net.sourceforge.fenixedu.domain.careerWorkshop;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum CareerWorkshopThemes {

    RESUME(),

    LABOUR_MARKET_STRATEGIES(),

    RECRUITMENT_TECHNIQUES(),

    CAREER_MANAGEMENT();

    private CareerWorkshopThemes() {
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

    public String getDescription() {
        return getDescription(I18N.getLocale());
    }

    public String getDescription(final Locale locale) {
        return BundleUtil.getString(Bundle.STUDENT, locale, getQualifiedName());
    }

    static public Map<Integer, CareerWorkshopThemes> getEmptyRankings() {
        Map<Integer, CareerWorkshopThemes> rankings = new HashMap<Integer, CareerWorkshopThemes>();
        for (int i = 0; i < getTotalOptions(); i++) {
            rankings.put(i, null);
        }
        return rankings;
    }

    static public int getTotalOptions() {
        return CareerWorkshopThemes.values().length;
    }

}
