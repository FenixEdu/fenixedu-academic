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
/*
 * Created on 17/Ago/2004
 */
package org.fenixedu.academic.domain;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author joaosa & rmalo
 */

public class ExportGrouping extends ExportGrouping_Base {

    public ExportGrouping() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ExportGrouping(Grouping groupProperties, ExecutionCourse executionCourse) {
        this();
        super.setGrouping(groupProperties);
        super.setExecutionCourse(executionCourse);
    }

    public void delete() {
        setExecutionCourse(null);
        setGrouping(null);
        setReceiverPerson(null);
        setSenderExecutionCourse(null);
        setSenderPerson(null);
        setRootDomainObject(null);

        super.deleteDomainObject();
    }

}
