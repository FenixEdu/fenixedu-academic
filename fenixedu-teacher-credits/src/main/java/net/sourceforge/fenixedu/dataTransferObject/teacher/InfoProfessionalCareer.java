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

import org.fenixedu.academic.dto.InfoTeacher;
import org.fenixedu.academic.domain.CareerType;
import org.fenixedu.academic.domain.teacher.ProfessionalCareer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoProfessionalCareer extends InfoCareer {
    private String entity;

    private String function;

    /**
     *  
     */
    public InfoProfessionalCareer() {
        setCareerType(CareerType.PROFESSIONAL);
    }

    /**
     * @return Returns the entity.
     */
    public String getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            The entity to set.
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     * @return Returns the function.
     */
    public String getFunction() {
        return function;
    }

    /**
     * @param function
     *            The function to set.
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.fenixedu.academic.dto.teacher.InfoCareer#copyFromDomain
     * (Dominio.teacher.Career)
     */
    public void copyFromDomain(ProfessionalCareer professionalCareer) {
        super.copyFromDomain(professionalCareer);
        if (professionalCareer != null) {
            setEntity(professionalCareer.getEntity());
            setFunction(professionalCareer.getFunction());
            setInfoTeacher(InfoTeacher.newInfoFromDomain(professionalCareer.getTeacher()));
        }
    }

    public static InfoProfessionalCareer newInfoFromDomain(ProfessionalCareer professionalCareer) {
        InfoProfessionalCareer infoProfessionalCareer = null;
        if (professionalCareer != null) {
            infoProfessionalCareer = new InfoProfessionalCareer();
            infoProfessionalCareer.copyFromDomain(professionalCareer);
        }

        return infoProfessionalCareer;
    }
}