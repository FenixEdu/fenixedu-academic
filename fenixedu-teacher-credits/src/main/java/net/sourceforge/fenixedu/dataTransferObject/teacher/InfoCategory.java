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
 * Created on 7/Nov/2003
 * 
 */
package org.fenixedu.academic.dto.teacher;

import org.fenixedu.academic.dto.InfoObject;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoCategory extends InfoObject {
    private Boolean canBeExecutionCourseResponsible;

    private String code;

    private String longName;

    private String shortName;

    public InfoCategory() {
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoCategory) {
            resultado = getCode().equals(((InfoCategory) obj).getCode());
        }
        return resultado;
    }

    /**
     * @return Returns the canBeExecutionCourseResponsible.
     */
    public Boolean getCanBeExecutionCourseResponsible() {
        return this.canBeExecutionCourseResponsible;
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return Returns the longName.
     */
    public String getLongName() {
        return longName;
    }

    /**
     * @return Returns the shortName.
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param canBeExecutionCourseResponsible
     *            The canBeExecutionCourseResponsible to set.
     */
    public void setCanBeExecutionCourseResponsible(Boolean canBeExecutionCourseResponsible) {
        this.canBeExecutionCourseResponsible = canBeExecutionCourseResponsible;
    }

    /**
     * @param code
     *            The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @param longName
     *            The longName to set.
     */
    public void setLongName(String longName) {
        this.longName = longName;
    }

    /**
     * @param shortName
     *            The shortName to set.
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.fenixedu.academic.dto.InfoObject#copyFromDomain
     * (Dominio.DomainObject)
     */

    public void copyFromDomain(ProfessionalCategory category) {
        super.copyFromDomain(category);
        if (category != null) {
            setCanBeExecutionCourseResponsible(category.isTeacherProfessorCategory());
            setCode(category.getName().getContent());
            setLongName(category.getName().getContent());
            setShortName(category.getName().getContent());
        }
    }

    public static InfoCategory newInfoFromDomain(ProfessionalCategory category) {
        InfoCategory infoCategory = null;
        if (category != null) {
            infoCategory = new InfoCategory();
            infoCategory.copyFromDomain(category);
        }
        return infoCategory;
    }
}