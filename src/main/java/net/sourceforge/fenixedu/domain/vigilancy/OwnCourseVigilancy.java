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
package net.sourceforge.fenixedu.domain.vigilancy;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;

import org.joda.time.DateTime;

public class OwnCourseVigilancy extends OwnCourseVigilancy_Base {

    public OwnCourseVigilancy() {
        super();
    }

    public OwnCourseVigilancy(WrittenEvaluation writtenEvaluation) {
        this();
        this.setWrittenEvaluation(writtenEvaluation);
        super.initStatus();
    }

    @Override
    public int getEstimatedPoints() {
        return getAssociatedVigilantGroup().getPointsForTeacher();
    }

    @Override
    public int getPoints() {

        DateTime currentDate = new DateTime();
        if (currentDate.isBefore(this.getBeginDate())) {
            return POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN;
        }

        if (isActive() && !isStatusUndefined()) {
            return isAttended() ? getAssociatedVigilantGroup().getPointsForTeacher() : (isDismissed() ? getAssociatedVigilantGroup()
                    .getPointsForDismissedTeacher() : (getWrittenEvaluation().getVigilantsReport() ? getAssociatedVigilantGroup()
                    .getPointsForMissingTeacher() : this.POINTS_WON_FOR_CONVOKE_YET_TO_HAPPEN));
        }
        return getAssociatedVigilantGroup().getPointsForDisconvoked();

    }

    public Boolean getConfirmed() {
        return Boolean.TRUE;
    }

    public boolean isConfirmed() {
        return getConfirmed();
    }
}
