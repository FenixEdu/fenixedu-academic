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
package org.fenixedu.academic.domain.candidacy;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.util.workflow.Operation;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class MDCandidacy extends MDCandidacy_Base {

    public MDCandidacy(Person person, ExecutionDegree executionDegree) {
//        super();
//        init(person, executionDegree);
        throw new RuntimeException("Student Candidacy subclasses not supported anymore!");
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString(Bundle.CANDIDATE, "label.mdCandidacy") + " - "
                + getExecutionDegree().getDegreeCurricularPlan().getName() + " - "
                + getExecutionDegree().getExecutionYear().getYear();
    }

    @Override
    public Set<Operation> getOperations(CandidacySituation candidacySituation) {
        return new HashSet<Operation>();
    }

    @Override
    protected void moveToNextState(CandidacyOperationType candidacyOperationType, Person person) {
    }

    @Override
    public String getDefaultState() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getStateMapping() {
        return null;
    }
}
