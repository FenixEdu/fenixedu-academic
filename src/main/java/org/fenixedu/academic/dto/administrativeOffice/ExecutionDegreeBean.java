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
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

public class ExecutionDegreeBean implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private Degree degree;

    private DegreeCurricularPlan degreeCurricularPlan;

    private ExecutionDegree executionDegree;

    private ExecutionYear executionYear;

    private CycleType cycleType;

    public ExecutionDegreeBean() {
        super();

        this.degree = null;
        this.degreeCurricularPlan = null;
        this.executionDegree = null;
        this.executionYear = null;
    }

    public ExecutionDegreeBean(Degree degree) {
        this();

        setDegree(degree);
    }

    public ExecutionDegreeBean(DegreeCurricularPlan degreeCurricularPlan) {
        this(degreeCurricularPlan == null ? null : degreeCurricularPlan.getDegree());

        setDegreeCurricularPlan(degreeCurricularPlan);
    }

    public ExecutionDegreeBean(ExecutionDegree executionDegree) {
        this(executionDegree == null ? null : executionDegree.getDegreeCurricularPlan());

        setExecutionDegree(executionDegree);
    }

    public Degree getDegree() {
        return this.degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return this.degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public ExecutionDegree getExecutionDegree() {
        return this.executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public CycleType getCycleType() {
        return cycleType;
    }

    public void setCycleType(CycleType cycleType) {
        this.cycleType = cycleType;
    }

}
