/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.util.Locale;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

@Deprecated(forRemoval = true)
public class EnrolmentInstructions extends EnrolmentInstructions_Base {

    /**
     * TODO: remove the field 'tempInstructions' and convert the type of the field
     * instructions from String to LocalizedString
     */

    public EnrolmentInstructions() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public EnrolmentInstructions(final ExecutionInterval executionInterval) {
        this();
        setExecutionSemester(executionInterval);
        setInstructions("");
        setTempInstructions(new LocalizedString());
    }

    @Atomic
    public static void createIfNecessary(final ExecutionInterval executionInterval) {
        if (executionInterval.getEnrolmentInstructions() == null) {
            new EnrolmentInstructions(executionInterval);
        }
    }

    @Override
    public LocalizedString getTempInstructions() {
        LocalizedString instruction = super.getTempInstructions();
        if (instruction != null) {
            return instruction;
        }
        return new LocalizedString(Locale.getDefault(), getInstructions());
    }

}
