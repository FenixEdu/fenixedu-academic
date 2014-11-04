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
package org.fenixedu.academic.dto.alumni;

import java.io.Serializable;
import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accessControl.StudentsConcludedInExecutionYearGroup;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.Sender;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.NobodyGroup;

import pt.ist.fenixframework.Atomic;

public class AlumniMailSendToBean implements Serializable {

    private ExecutionYear endExecutionYear;
    private DegreeType degreeType;
    private List<Degree> degrees;

    public AlumniMailSendToBean(DegreeType degreeType, List<Degree> degrees, ExecutionYear endYear) {
        setDegreeType(degreeType);
        setDegrees(degrees);
        setEndExecutionYear(endYear);
    }

    public AlumniMailSendToBean() {
    }

    public ExecutionYear getEndExecutionYear() {
        return this.endExecutionYear;
    }

    public void setEndExecutionYear(ExecutionYear executionYear) {
        this.endExecutionYear = executionYear;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType arg) {
        this.degreeType = arg;
    }

    public List<Degree> getDegrees() {
        return this.degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }

    @Atomic
    public void createRecipientGroup(Sender sender) {
        Group group = NobodyGroup.get();
        for (Degree degree : getDegrees()) {
            group = group.or(StudentsConcludedInExecutionYearGroup.get(degree, getEndExecutionYear()));
        }
        sender.addRecipients(Recipient.newInstance(group));
    }
}
