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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public class DegreeLog extends DegreeLog_Base {

    public enum DegreeLogTypes {

        CANDIDACIES, COORDINATION_TEAM, PROGRAM_TUTORED_PARTICIPATION, QUC_RESULTS, SCIENTIFIC_COMISSION;

        public String getQualifiedName() {
            return DegreeLogTypes.class.getSimpleName() + "." + name();
        }

        private static final Collection<DegreeLogTypes> typesAsList = Collections.unmodifiableList(Arrays.asList(values()));

        public static Collection<DegreeLogTypes> valuesAsList() {
            return typesAsList;
        }
    }

    public DegreeLog() {
        super();
    }

    public DegreeLog(Degree degree, ExecutionYear executionYear, String description) {
        super();
        if (getDegree() == null) {
            setDegree(degree);
        }
        if (getExecutionYear() == null) {
            setExecutionYear(executionYear);
        }
        setDescription(description);
    }

    protected static String generateLabelDescription(String bundle, String key, String... args) {
        return BundleUtil.getString(bundle, key, args);
    }

    public DegreeLogTypes getDegreeLogType() {
        return null;
    }

    @Override
    public void delete() {
        setExecutionYear(null);
        setDegree(null);
        super.delete();
    }

}
