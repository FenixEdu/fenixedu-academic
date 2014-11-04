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

public class CandidaciesLog extends CandidaciesLog_Base {

    public CandidaciesLog() {
        super();
    }

    public CandidaciesLog(Degree degree, ExecutionYear executionYear, String description) {
        super();
        if (getDegree() == null) {
            setDegree(degree);
        }
        if (getExecutionYear() == null) {
            setExecutionYear(executionYear);
        }
        setDescription(description);
    }

    public static CandidaciesLog createCandidaciesLog(Degree degree, ExecutionYear executionYear, String description) {
        return new CandidaciesLog(degree, executionYear, description);
    }

    public static CandidaciesLog createLog(Degree degree, ExecutionYear executionYear, String bundle, String key, String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createCandidaciesLog(degree, executionYear, label);
    }

    @Override
    public DegreeLogTypes getDegreeLogType() {
        return DegreeLogTypes.CANDIDACIES;
    }

}
