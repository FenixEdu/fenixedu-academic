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
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentNotUpdatedAlumniInfoForSpecificDaysGroup extends PersistentNotUpdatedAlumniInfoForSpecificDaysGroup_Base {
    protected PersistentNotUpdatedAlumniInfoForSpecificDaysGroup(Integer daysNotUpdated, Boolean checkJobNotUpdated,
            Boolean checkFormationNotUpdated, Boolean checkPersonalDataNotUpdated) {
        super();
        setDaysNotUpdated(daysNotUpdated);
        setCheckJobNotUpdated(checkJobNotUpdated);
        setCheckFormationNotUpdated(checkFormationNotUpdated);
        setCheckPersonalDataNotUpdated(checkPersonalDataNotUpdated);
    }

    @Override
    public Group toGroup() {
        return NotUpdatedAlumniInfoForSpecificDaysGroup.get(getDaysNotUpdated(), getCheckJobNotUpdated(),
                getCheckFormationNotUpdated(), getCheckPersonalDataNotUpdated());
    }

    public static PersistentNotUpdatedAlumniInfoForSpecificDaysGroup getInstance(final Integer daysNotUpdated,
            final Boolean checkJobNotUpdated, final Boolean checkFormationNotUpdated, final Boolean checkPersonalDataNotUpdated) {
        return singleton(
                () -> select(daysNotUpdated, checkJobNotUpdated, checkFormationNotUpdated, checkPersonalDataNotUpdated),
                () -> new PersistentNotUpdatedAlumniInfoForSpecificDaysGroup(daysNotUpdated, checkJobNotUpdated,
                        checkFormationNotUpdated, checkPersonalDataNotUpdated));
    }

    private static Optional<PersistentNotUpdatedAlumniInfoForSpecificDaysGroup> select(final Integer daysNotUpdated,
            final Boolean checkJobNotUpdated, final Boolean checkFormationNotUpdated, final Boolean checkPersonalDataNotUpdated) {
        return filter(PersistentNotUpdatedAlumniInfoForSpecificDaysGroup.class).filter(
                group -> group.getDaysNotUpdated() == daysNotUpdated && group.getCheckJobNotUpdated() == checkJobNotUpdated
                && group.getCheckFormationNotUpdated() == checkFormationNotUpdated
                && group.getCheckPersonalDataNotUpdated() == checkPersonalDataNotUpdated).findAny();
    }
}
