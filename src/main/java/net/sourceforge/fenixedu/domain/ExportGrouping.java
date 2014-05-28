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
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.domain;

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

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasSenderPerson() {
        return getSenderPerson() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSenderExecutionCourse() {
        return getSenderExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasProposalState() {
        return getProposalState() != null;
    }

    @Deprecated
    public boolean hasReceiverPerson() {
        return getReceiverPerson() != null;
    }

    @Deprecated
    public boolean hasGrouping() {
        return getGrouping() != null;
    }

}
