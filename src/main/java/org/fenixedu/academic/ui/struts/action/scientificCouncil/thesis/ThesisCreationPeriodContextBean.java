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
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class ThesisCreationPeriodContextBean implements Serializable {

    private ExecutionYear executionYear;

    private ExecutionDegree executionDegree;

    public ThesisCreationPeriodContextBean() {
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionYear(final ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public void setExecutionDegree(final ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

}
