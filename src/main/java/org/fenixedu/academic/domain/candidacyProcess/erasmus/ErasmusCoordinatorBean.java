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
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityCoordinator;

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
