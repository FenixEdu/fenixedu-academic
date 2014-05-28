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
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation -
 * Code and Comments
 */
package net.sourceforge.fenixedu.domain.gesdis;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class StudentCourseReport extends StudentCourseReport_Base {

    public StudentCourseReport() {
        super();
        setRootDomainObject(Bennu.getInstance());
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

    @Deprecated
    public boolean hasStrongPoints() {
        return getStrongPoints() != null;
    }

    @Deprecated
    public boolean hasLastModificationDateDateTime() {
        return getLastModificationDateDateTime() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasWeakPoints() {
        return getWeakPoints() != null;
    }

    @Deprecated
    public boolean hasStudentReport() {
        return getStudentReport() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

}
