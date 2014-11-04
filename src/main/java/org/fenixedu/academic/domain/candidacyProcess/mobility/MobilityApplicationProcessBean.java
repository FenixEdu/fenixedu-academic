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
package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType;

public class MobilityApplicationProcessBean extends CandidacyProcessBean {

    private ErasmusApplyForSemesterType forSemester;

    public MobilityApplicationProcessBean(final ExecutionInterval executionInterval) {
        super(executionInterval);
    }

    public MobilityApplicationProcessBean(final CandidacyProcess process) {
        super(process);
    }

    public ErasmusApplyForSemesterType getForSemester() {
        return forSemester;
    }

    public void setForSemester(ErasmusApplyForSemesterType forSemester) {
        this.forSemester = forSemester;
    }

}
