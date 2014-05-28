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
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

public class QueueJobResultFile extends QueueJobResultFile_Base {

    protected QueueJobResultFile(QueueJobWithFile job, Person operator, String filename, byte[] content) {
        super();
        setJob(job);
        init(GeneratedDocumentType.QUEUE_JOB, operator, operator, filename, content);
    }

    @Override
    protected Group computePermittedGroup() {
        return RoleGroup.get(RoleType.MANAGER);
    }

    @Override
    public void delete() {
        setJob(null);
        super.delete();
    }

    @Atomic
    public static void store(QueueJobWithFile job, Person person, String filename, byte[] content) {
        new QueueJobResultFile(job, person, filename, content);
    }

    @Deprecated
    public boolean hasJob() {
        return getJob() != null;
    }

}
