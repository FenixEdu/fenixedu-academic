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
package org.fenixedu.academic.domain.teacher;

import org.fenixedu.academic.dto.teacher.InfoProfessionalCareer;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ProfessionalCareer extends ProfessionalCareer_Base {

    public ProfessionalCareer() {
        super();
    }

    public ProfessionalCareer(Person person, Integer beginYear, Integer endYear, String function, String entity) {
        super();
        if (person == null) {
            throw new DomainException("The teacher should not be null!");
        }
        if (beginYear == null) {
            throw new DomainException("error.professionalcareer.beginYearIsMandatory");
        }
        if (endYear != null && endYear < beginYear) {
            throw new DomainException("error.professionalcareer.endYearBeforeStart");
        }
        setPerson(person);
        setBeginYear(beginYear);
        setEndYear(endYear);
        setFunction(function);
        setEntity(entity);
    }

    public ProfessionalCareer(Teacher teacher, Integer beginYear, Integer endYear, String function, String entity) {
        super();
        if (teacher == null) {
            throw new DomainException("The teacher should not be null!");
        }
        if (beginYear == null) {
            throw new DomainException("error.professionalcareer.beginYearIsMandatory");
        }
        if (endYear != null && endYear < beginYear) {
            throw new DomainException("error.professionalcareer.endYearBeforeStart");
        }
        setTeacher(teacher);
        setBeginYear(beginYear);
        setEndYear(endYear);
        setFunction(function);
        setEntity(entity);
    }

    public ProfessionalCareer(Teacher teacher, InfoProfessionalCareer infoProfessionalCareer) {
        if (teacher == null) {
            throw new DomainException("The teacher should not be null!");
        }
        setTeacher(teacher);
        setBasicProperties(infoProfessionalCareer);
    }

    public void edit(InfoProfessionalCareer infoProfessionalCareer) {
        setBasicProperties(infoProfessionalCareer);
    }

    private void setBasicProperties(InfoProfessionalCareer infoProfessionalCareer) {
        this.setBeginYear(infoProfessionalCareer.getBeginYear());
        this.setEndYear(infoProfessionalCareer.getEndYear());
        this.setEntity(infoProfessionalCareer.getEntity());
        this.setFunction(infoProfessionalCareer.getFunction());

    }

}
