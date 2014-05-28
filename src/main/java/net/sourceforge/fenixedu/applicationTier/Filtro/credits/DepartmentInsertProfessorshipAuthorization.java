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
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author jpvl
 */
public class DepartmentInsertProfessorshipAuthorization extends AbstractTeacherDepartmentAuthorization<String> {

    public static final DepartmentInsertProfessorshipAuthorization instance = new DepartmentInsertProfessorshipAuthorization();

    @Override
    protected String getTeacherId(String istId) {
        Teacher teacher = Teacher.readByIstId(istId);
        return (teacher == null) ? null : teacher.getExternalId();
    }

}