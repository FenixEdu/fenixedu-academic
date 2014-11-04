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

import java.util.Date;

import org.fenixedu.academic.dto.InfoObject;
import org.fenixedu.academic.dto.InfoTeacher;
import org.fenixedu.academic.domain.CareerType;
import org.fenixedu.academic.domain.teacher.Career;
import org.fenixedu.academic.domain.teacher.ProfessionalCareer;
import org.fenixedu.academic.domain.teacher.TeachingCareer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public abstract class InfoCareer extends InfoObject {

    private Integer beginYear;
    private Integer endYear;
    private CareerType careerType;
    private InfoTeacher infoTeacher;
    private Date lastModificationDate;

    public InfoCareer() {
        super();
    }

    public Integer getBeginYear() {
        return beginYear;
    }

    public void setBeginYear(Integer beginYear) {
        this.beginYear = beginYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

    public CareerType getCareerType() {
        return careerType;
    }

    public void setCareerType(CareerType careerType) {
        this.careerType = careerType;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public void copyFromDomain(Career career) {
        super.copyFromDomain(career);
        if (career != null) {
            setBeginYear(career.getBeginYear());
            setEndYear(career.getEndYear());
            setLastModificationDate(career.getLastModificationDate());
        }
    }

    public static InfoCareer newInfoFromDomain(Career career) {
        InfoCareer infoCareer = null;
        if (career != null) {
            if (career instanceof TeachingCareer) {
                infoCareer = InfoTeachingCareerWithInfoCategory.newInfoFromDomain((TeachingCareer) career);
            } else if (career instanceof ProfessionalCareer) {
                infoCareer = InfoProfessionalCareer.newInfoFromDomain((ProfessionalCareer) career);
            }
        }
        return infoCareer;
    }
}