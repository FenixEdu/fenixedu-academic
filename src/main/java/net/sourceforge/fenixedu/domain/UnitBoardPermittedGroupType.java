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
 * Author : Goncalo Luiz
 * Creation Date: Jun 27, 2006,11:48:57 AM
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jun 27, 2006,11:48:57 AM
 * 
 */
public enum UnitBoardPermittedGroupType {
    UB_UNIT_PERSONS, UB_PUBLIC, UB_MANAGER, UB_DEGREE_COORDINATOR, UB_CURRENT_DEGREE_COORDINATOR, UB_TEACHER, UB_WEBSITE_MANAGER,
    UB_EMPLOYEE, UB_DEGREE_STUDENT, UB_MASTER_DEGREE_STUDENT, UB_EXECUTION_COURSE_RESPONSIBLE, UB_MASTER_DEGREE_COORDINATOR,
    @Deprecated
    UB_DEPARTMENT_EMPLOYEES, UB_UNITSITE_MANAGERS, UB_TEACHER_AND_WEBSITE_MANAGER;
}
