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
 * Created on Apr 19, 2006
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;

public enum MarkSheetType {

    NORMAL(EnrolmentEvaluationType.NORMAL),

    IMPROVEMENT(EnrolmentEvaluationType.IMPROVEMENT),

    SPECIAL_SEASON(EnrolmentEvaluationType.SPECIAL_SEASON),

    SPECIAL_AUTHORIZATION(EnrolmentEvaluationType.NORMAL);

    private EnrolmentEvaluationType enrolmentEvaluationType;

    private MarkSheetType(EnrolmentEvaluationType enrolmentEvaluationType) {
        this.enrolmentEvaluationType = enrolmentEvaluationType;
    }

    public String getName() {
        return name();
    }

    public EnrolmentEvaluationType getEnrolmentEvaluationType() {
        return this.enrolmentEvaluationType;
    }
}
