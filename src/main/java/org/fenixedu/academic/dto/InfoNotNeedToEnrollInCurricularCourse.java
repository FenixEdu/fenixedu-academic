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
/**
 * Jul 26, 2005
 */
/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InfoNotNeedToEnrollInCurricularCourse extends InfoObject {

    private InfoCurricularCourse infoCurricularCourse;

    private InfoStudentCurricularPlan studentCurricularPlan;

    public InfoNotNeedToEnrollInCurricularCourse() {
    }

    public InfoCurricularCourse getInfoCurricularCourse() {
        return infoCurricularCourse;
    }

    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
        this.infoCurricularCourse = infoCurricularCourse;
    }

    public InfoStudentCurricularPlan getStudentCurricularPlan() {
        return studentCurricularPlan;
    }

    public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public void copyFromDomain(NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse) {
        super.copyFromDomain(notNeedToEnrollInCurricularCourse);

        if (notNeedToEnrollInCurricularCourse != null) {
            setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(notNeedToEnrollInCurricularCourse
                    .getCurricularCourse()));
            setInfoStudentCurricularPlan(InfoStudentCurricularPlan.newInfoFromDomain(notNeedToEnrollInCurricularCourse
                    .getStudentCurricularPlan()));
        }
    }

    public static InfoNotNeedToEnrollInCurricularCourse newInfoFromDomain(
            NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse) {
        InfoNotNeedToEnrollInCurricularCourse infoNotNeedToEnrollInCurricularCourse = null;
        if (notNeedToEnrollInCurricularCourse != null) {
            infoNotNeedToEnrollInCurricularCourse = new InfoNotNeedToEnrollInCurricularCourse();
            infoNotNeedToEnrollInCurricularCourse.copyFromDomain(notNeedToEnrollInCurricularCourse);
        }
        return infoNotNeedToEnrollInCurricularCourse;
    }

}
