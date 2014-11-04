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
package org.fenixedu.academic.domain.candidacyProcess.erasmus;

import java.io.Serializable;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityCoordinator;

public class ErasmusCoordinatorBean implements Serializable {
    private Teacher teacher;
    private Degree degree;

    private MobilityCoordinator erasmusCoordinator;

    private String teacherId;

    public ErasmusCoordinatorBean() {

    }

    public ErasmusCoordinatorBean(final Teacher teacher, final Degree degree) {
        this.teacher = teacher;
        this.degree = degree;
    }

    public ErasmusCoordinatorBean(final MobilityCoordinator coordinator) {
        this.erasmusCoordinator = coordinator;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public MobilityCoordinator getErasmusCoordinator() {
        return erasmusCoordinator;
    }

    public void setErasmusCoordinator(MobilityCoordinator erasmusCoordinator) {
        this.erasmusCoordinator = erasmusCoordinator;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}
