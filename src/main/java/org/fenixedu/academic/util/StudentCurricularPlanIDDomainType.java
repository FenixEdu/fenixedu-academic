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
package org.fenixedu.academic.util;

import java.io.Serializable;

public class StudentCurricularPlanIDDomainType implements Serializable {
    public static final String ALL_TYPE = "-1";

    public static final String NEWEST_TYPE = "-2";

    public static final String ALL_STRING = "Todos os planos curriculares";

    public static final String NEWEST_STRING = "Plano curricular mais recente";

    public static final StudentCurricularPlanIDDomainType ALL = new StudentCurricularPlanIDDomainType(
            StudentCurricularPlanIDDomainType.ALL_TYPE);

    public static final StudentCurricularPlanIDDomainType NEWEST = new StudentCurricularPlanIDDomainType(
            StudentCurricularPlanIDDomainType.NEWEST_TYPE);

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String idType) {
        id = idType;
    }

    public StudentCurricularPlanIDDomainType(String idType) {
        super();
        setId(idType);
    }

    @Override
    public String toString() {
        return "" + getId();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StudentCurricularPlanIDDomainType) {
            StudentCurricularPlanIDDomainType sc = (StudentCurricularPlanIDDomainType) o;
            if (getId().equals(sc.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAll() {
        return (this.equals(StudentCurricularPlanIDDomainType.ALL));
    }

    public boolean isNewest() {
        return (this.equals(StudentCurricularPlanIDDomainType.NEWEST));
    }
}
