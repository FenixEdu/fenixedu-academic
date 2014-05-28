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

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCoordinatorBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

public class MobilityCoordinator extends MobilityCoordinator_Base {

    private MobilityCoordinator() {
        super();

        setRootDomainObject(Bennu.getInstance());
    }

    public MobilityCoordinator(final MobilityApplicationProcess applicationProcess, final ErasmusCoordinatorBean bean) {
        this(applicationProcess, bean.getTeacher(), bean.getDegree());
    }

    public MobilityCoordinator(final MobilityApplicationProcess applicationProcess, final Teacher teacher, final Degree degree) {
        this();

        check(applicationProcess, teacher, degree);

        this.setMobilityApplicationProcess(applicationProcess);
        this.setTeacher(teacher);
        this.setDegree(degree);
    }

    private void check(final MobilityApplicationProcess applicationProcess, final Teacher teacher, final Degree degree) {
        if (applicationProcess == null) {
            throw new DomainException("error.erasmus.coordinator.candidacyProcess.must.not.be.null");
        }

        if (teacher == null) {
            throw new DomainException("error.erasmus.coordinator.teacher.must.not.be.null");
        }

        if (degree == null) {
            throw new DomainException("error.erasmus.coordinator.degree.must.not.be.null");
        }

        if (applicationProcess.isTeacherErasmusCoordinatorForDegree(teacher, degree)) {
            throw new DomainException("error.erasmus.coordinator.teacher.is.assigned.for.process.and.degree");
        }
    }

    public void delete() {
        setDegree(null);
        setMobilityApplicationProcess(null);
        setTeacher(null);
        setRootDomainObject(null);

        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasMobilityApplicationProcess() {
        return getMobilityApplicationProcess() != null;
    }

}
