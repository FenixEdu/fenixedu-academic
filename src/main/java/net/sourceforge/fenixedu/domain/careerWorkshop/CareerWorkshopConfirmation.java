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
package net.sourceforge.fenixedu.domain.careerWorkshop;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CareerWorkshopConfirmation extends CareerWorkshopConfirmation_Base {

    public CareerWorkshopConfirmation(Student student, CareerWorkshopConfirmationEvent confirmationEvent,
            CareerWorkshopApplication application) {
        super();

        if (student == null) {
            throw new DomainException("error.careerWorkshop.creatingNewApplication: Student cannot be a null value.");
        }
        setStudent(student);

        if (confirmationEvent == null) {
            throw new DomainException("error.careerWorkshop.creatingNewApplication: ConfirmationEvent cannot be a null value.");
        }
        setCareerWorkshopConfirmationEvent(confirmationEvent);

        if (application == null) {
            throw new DomainException("error.careerWorkshop.creatingNewApplication: Application cannot be a null value.");
        }
        setCareerWorkshopApplication(application);
    }

    @Atomic
    public void delete() {
        setStudent(null);
        setCareerWorkshopApplication(null);
        setCareerWorkshopConfirmationEvent(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Atomic
    @Override
    public void setConfirmation(Boolean confirmation) {
        super.setConfirmation(confirmation);
    }

    @Atomic
    @Override
    public void setConfirmationCode(String confirmationCode) {
        super.setConfirmationCode(confirmationCode);
    }

    @Atomic
    public void sealConfirmation() {
        DateTime timestamp = new DateTime();
        setSealStamp(timestamp);
        getCareerWorkshopConfirmationEvent().setLastUpdate(timestamp);
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasConfirmation() {
        return getConfirmation() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCareerWorkshopConfirmationEvent() {
        return getCareerWorkshopConfirmationEvent() != null;
    }

    @Deprecated
    public boolean hasConfirmationCode() {
        return getConfirmationCode() != null;
    }

    @Deprecated
    public boolean hasSealStamp() {
        return getSealStamp() != null;
    }

    @Deprecated
    public boolean hasCareerWorkshopApplication() {
        return getCareerWorkshopApplication() != null;
    }

}
