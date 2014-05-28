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
package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentAction;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

abstract public class CurriculumLineLog extends CurriculumLineLog_Base {

    protected CurriculumLineLog() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setDateDateTime(new DateTime());
    }

    protected void init(final EnrolmentAction action, final Registration registration, final DegreeModule degreeModule,
            final ExecutionSemester executionSemester, final String who) {

        checkParameters(action, registration, degreeModule, executionSemester);

        setAction(action);
        setStudent(registration);
        setDegreeModule(degreeModule);
        setExecutionPeriod(executionSemester);
        setWho(who);
    }

    private void checkParameters(final EnrolmentAction action, final Registration registration, final DegreeModule degreeModule,
            final ExecutionSemester executionSemester) {
        String[] args = {};
        if (action == null) {
            throw new DomainException("error.log.EnrolmentLog.invalid.action", args);
        }
        String[] args1 = {};
        if (registration == null) {
            throw new DomainException("error.log.EnrolmentLog.invalid.registration", args1);
        }
        String[] args2 = {};
        if (degreeModule == null) {
            throw new DomainException("error.log.DismissalLog.invalid.degreeModule", args2);
        }
        String[] args3 = {};
        if (executionSemester == null) {
            throw new DomainException("error.log.EnrolmentLog.invalid.executionSemester", args3);
        }
    }

    public void delete() {
        disconnect();
        super.deleteDomainObject();
    }

    protected void disconnect() {
        setRootDomainObject(null);
        setStudent(null);
        setDegreeModule(null);
        setExecutionPeriod(null);
    }

    abstract public String getDescription();

    public boolean isFor(final ExecutionSemester executionSemester) {
        return getExecutionPeriod() == executionSemester;
    }

    @Deprecated
    public java.util.Date getDate() {
        org.joda.time.DateTime dt = getDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setDate(java.util.Date date) {
        if (date == null) {
            setDateDateTime(null);
        } else {
            setDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDateDateTime() {
        return getDateDateTime() != null;
    }

    @Deprecated
    public boolean hasWho() {
        return getWho() != null;
    }

    @Deprecated
    public boolean hasAction() {
        return getAction() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasDegreeModule() {
        return getDegreeModule() != null;
    }

}
