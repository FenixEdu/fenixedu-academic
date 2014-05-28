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
 * Created on 21/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOrientation;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class Orientation extends Orientation_Base {

    public Orientation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Orientation(Teacher teacher, InfoOrientation infoOrientation) {
        this();
        if (teacher == null) {
            throw new DomainException("The teacher should not be null!");
        }

        setTeacher(teacher);
        setBasicProperties(infoOrientation);
    }

    public void edit(InfoOrientation infoOrientation) {
        setBasicProperties(infoOrientation);
    }

    private void setBasicProperties(InfoOrientation infoOrientation) {
        this.setDescription(infoOrientation.getDescription());
        this.setNumberOfStudents(infoOrientation.getNumberOfStudents());
        this.setOrientationType(infoOrientation.getOrientationType());
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
    public boolean hasLastModificationDateDateTime() {
        return getLastModificationDateDateTime() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasOrientationType() {
        return getOrientationType() != null;
    }

    @Deprecated
    public boolean hasNumberOfStudents() {
        return getNumberOfStudents() != null;
    }

}
