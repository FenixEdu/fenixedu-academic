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
package org.fenixedu.academic.domain;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.teacher.TeacherService;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class NonRegularTeachingService extends NonRegularTeachingService_Base {

    public NonRegularTeachingService(Professorship professorship, Shift shift, Double percentage) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setPercentage(percentage);
        setProfessorship(professorship);
        setShift(shift);
    }

    @Atomic
    public static void createOrEdit(Professorship professorship, Shift shift, Double percentage) {

        if (percentage != null) {
            if (percentage > 100 || percentage < 0) {
                throw new DomainException("message.invalid.professorship.percentage");
            }
            Double availablePercentage = TeacherService.getAvailableShiftPercentage(shift, professorship);
            if (percentage > availablePercentage) {
                throw new DomainException("message.exceeded.professorship.percentage");
            }
        }

        for (NonRegularTeachingService nonRegularTeachingService : professorship.getNonRegularTeachingServicesSet()) {
            if (nonRegularTeachingService.getShift().equals(shift)) {
                if (percentage == null || percentage.equals(new Double(0.0))) {
                    nonRegularTeachingService.delete();
                } else {
                    nonRegularTeachingService.setPercentage(percentage);
                }
                return;
            }
        }
        if (percentage != null && !percentage.equals(new Double(0.0))) {
            new NonRegularTeachingService(professorship, shift, percentage);
        }
    }

    private void delete() {
        setProfessorship(null);
        setShift(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

}
