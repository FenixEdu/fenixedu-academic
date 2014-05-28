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
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.gesdis;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.gesdis.CourseHistoric;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoCourseHistoricWithInfoCurricularCourse extends InfoCourseHistoric {
    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain
     * (Dominio.DomainObject)
     */
    @Override
    public void copyFromDomain(CourseHistoric courseHistoric) {
        super.copyFromDomain(courseHistoric);
        if (courseHistoric != null) {
            setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(courseHistoric.getCurricularCourse()));
        }
    }

    public static InfoCourseHistoric newInfoFromDomain(CourseHistoric courseHistoric) {
        InfoCourseHistoricWithInfoCurricularCourse infoCourseHistoric = null;
        if (courseHistoric != null) {
            infoCourseHistoric = new InfoCourseHistoricWithInfoCurricularCourse();
            infoCourseHistoric.copyFromDomain(courseHistoric);
        }
        return infoCourseHistoric;
    }
}