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
 * Created on 13/Nov/2003
 *
 */
package org.fenixedu.academic.dto.teacher;

import org.fenixedu.academic.domain.CareerType;
import org.fenixedu.academic.domain.teacher.TeachingCareer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoTeachingCareer extends InfoCareer {
    private InfoCategory infoCategory;

    private String courseOrPosition;

    /**
     *  
     */
    public InfoTeachingCareer() {
        setCareerType(CareerType.TEACHING);
    }

    /**
     * @return Returns the courseOrPosition.
     */
    public String getCourseOrPosition() {
        return courseOrPosition;
    }

    /**
     * @param courseOrPosition
     *            The courseOrPosition to set.
     */
    public void setCourseOrPosition(String courseOrPosition) {
        this.courseOrPosition = courseOrPosition;
    }

    /**
     * @return Returns the infoCategory.
     */
    public InfoCategory getInfoCategory() {
        return infoCategory;
    }

    /**
     * @param infoCategory
     *            The infoCategory to set.
     */
    public void setInfoCategory(InfoCategory infoCategory) {
        this.infoCategory = infoCategory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.fenixedu.academic.dto.teacher.InfoCareer#copyFromDomain
     * (Dominio.teacher.Career)
     */
    public void copyFromDomain(TeachingCareer teachingCareer) {
        super.copyFromDomain(teachingCareer);
        if (teachingCareer != null) {
            setCourseOrPosition(teachingCareer.getCourseOrPosition());
        }
    }

    public static InfoTeachingCareer newInfoFromDomain(TeachingCareer teachingCareer) {
        InfoTeachingCareer infoTeachingCareer = null;
        if (teachingCareer != null) {
            infoTeachingCareer = new InfoTeachingCareer();
            infoTeachingCareer.copyFromDomain(teachingCareer);
        }
        return infoTeachingCareer;
    }
}