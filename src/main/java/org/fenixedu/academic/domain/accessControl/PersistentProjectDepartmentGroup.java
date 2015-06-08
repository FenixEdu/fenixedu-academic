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
package org.fenixedu.academic.domain.accessControl;

import java.util.Optional;

import org.fenixedu.academic.domain.Project;
import org.fenixedu.bennu.core.groups.Group;

@Deprecated
public class PersistentProjectDepartmentGroup extends PersistentProjectDepartmentGroup_Base {
    protected PersistentProjectDepartmentGroup(Project project) {
        super();
        setProject(project);
    }

    @Override
    public Group toGroup() {
        return ProjectDepartmentGroup.get(getProject());
    }

    @Override
    protected void gc() {
        setProject(null);
        super.gc();
    }

    public static PersistentProjectDepartmentGroup getInstance(final Project project) {
        return singleton(() -> Optional.ofNullable(project.getProjectDepartmentGroup()),
                () -> new PersistentProjectDepartmentGroup(project));
    }
}
