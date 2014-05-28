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
package net.sourceforge.fenixedu.domain;

public class ScientificCommissionLog extends ScientificCommissionLog_Base {

    public ScientificCommissionLog() {
        super();
    }

    public ScientificCommissionLog(Degree degree, ExecutionYear executionYear, String description) {
        super();
        if (getDegree() == null) {
            setDegree(degree);
        }
        if (getExecutionYear() == null) {
            setExecutionYear(executionYear);
        }
        setDescription(description);
    }

    public static ScientificCommissionLog createScientificCommissionLog(Degree degree, ExecutionYear executionYear,
            String description) {
        return new ScientificCommissionLog(degree, executionYear, description);
    }

    public static ScientificCommissionLog createLog(Degree degree, ExecutionYear executionYear, String bundle, String key,
            String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createScientificCommissionLog(degree, executionYear, label);
    }

    @Override
    public DegreeLogTypes getDegreeLogType() {
        return DegreeLogTypes.SCIENTIFIC_COMISSION;
    }

}
