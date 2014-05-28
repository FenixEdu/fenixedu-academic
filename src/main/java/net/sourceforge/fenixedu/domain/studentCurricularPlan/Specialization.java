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
 * Specialization.java
 * 
 * Created on 18 de Novembro de 2002, 17:32
 */

/**
 * 
 * Authors : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.domain.studentCurricularPlan;

public enum Specialization {

    STUDENT_CURRICULAR_PLAN_MASTER_DEGREE,

    STUDENT_CURRICULAR_PLAN_INTEGRATED_MASTER_DEGREE,

    STUDENT_CURRICULAR_PLAN_SPECIALIZATION;

    public String getName() {
        return name();
    }

}
