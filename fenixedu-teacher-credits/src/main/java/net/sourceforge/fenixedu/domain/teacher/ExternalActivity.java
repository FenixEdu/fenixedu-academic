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
/*
 * Created on 15/Nov/2003
 *
 */
package org.fenixedu.academic.domain.teacher;

import org.fenixedu.academic.dto.teacher.InfoExternalActivity;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ExternalActivity extends ExternalActivity_Base {

    public ExternalActivity() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ExternalActivity(Teacher teacher, InfoExternalActivity infoExternalActivity) {
        this();
        if (teacher == null) {
            throw new DomainException("The teacher should not be null!");
        }

        setTeacher(teacher);
        this.setActivity(infoExternalActivity.getActivity());
    }

    public void delete() {
        setTeacher(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public void edit(InfoExternalActivity infoExternalActivity) {

        this.setActivity(infoExternalActivity.getActivity());

    }

    @Deprecated
    public java.util.Date getLastModificationDate() {
        org.joda.time.DateTime dt = getLastModificationDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setLastModificationDate(java.util.Date date) {
        if (date == null) {
            setLastModificationDateDateTime(null);
        } else {
            setLastModificationDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

}
