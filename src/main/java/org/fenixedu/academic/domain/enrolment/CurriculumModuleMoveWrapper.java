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
package org.fenixedu.academic.domain.enrolment;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;

public class CurriculumModuleMoveWrapper extends EnroledCurriculumModuleWrapper {

    private static final long serialVersionUID = 8766523234444669518L;
    private boolean collectRules;

    public CurriculumModuleMoveWrapper(final CurriculumModule curriculumModule, final ExecutionInterval executionInterval) {
        super(curriculumModule, executionInterval);
        checkParameters(curriculumModule, executionInterval);
        collectRules = curriculumModule.isRoot() ? true : !curriculumModule.isNoCourseGroupCurriculumGroup();
    }

    private void checkParameters(final CurriculumModule curriculumModule, final ExecutionInterval executionInterval) {
        if (curriculumModule == null) {
            throw new DomainException("error.CurriculumModuleMoveWrapper.invalid.curriculumModule");
        }
        if (executionInterval == null) {
            throw new DomainException("error.CurriculumModuleMoveWrapper.invalid.executionPeriod");
        }
    }

    @Override
    public boolean canCollectRules() {
        return collectRules;
    }

    static public CurriculumModuleMoveWrapper create(final CurriculumGroup parent, final ExecutionInterval executionInterval) {
        return new CurriculumModuleMoveWrapper(parent, executionInterval);
    }
}
