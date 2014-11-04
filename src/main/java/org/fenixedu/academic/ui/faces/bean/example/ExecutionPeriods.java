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
package org.fenixedu.academic.ui.faces.bean.example;

import java.util.Collection;

import org.fenixedu.academic.service.services.commons.ReadExecutionPeriods;

public class ExecutionPeriods {

    public Collection getExecutionPeriods() {
        try {

            final Collection infoExecutionPeriods = ReadExecutionPeriods.run();
            return infoExecutionPeriods;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setExecutionPeriods(Collection executionPeriods) {
        throw new RuntimeException("Not implemented");
    }

}