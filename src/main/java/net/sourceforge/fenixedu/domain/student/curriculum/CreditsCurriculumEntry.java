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
package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Grade;

import org.joda.time.DateTime;

public abstract class CreditsCurriculumEntry extends CurriculumEntry {

    @Override
    public BigDecimal getWeigthForCurriculum() {
        return null;
    }

    @Override
    public BigDecimal getWeigthTimesGrade() {
        return null;
    }

    @Override
    public Grade getGrade() {
        return Grade.createEmptyGrade();
    }

    @Override
    public DateTime getCreationDateDateTime() {
        return null;
    }

}
