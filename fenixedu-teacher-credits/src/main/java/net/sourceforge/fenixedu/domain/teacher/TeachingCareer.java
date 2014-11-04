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

import org.fenixedu.academic.dto.teacher.InfoTeachingCareer;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class TeachingCareer extends TeachingCareer_Base {

    public TeachingCareer() {
        super();
    }

    public TeachingCareer(Teacher teacher, MultiLanguageString category, InfoTeachingCareer infoTeachingCareer) {
        if (teacher == null || category == null) {
            throw new DomainException("Neither teacher nor category should be null!");
        }

        setTeacher(teacher);
        setCategoryName(category);
        setBasicProperties(infoTeachingCareer);
    }

    @Override
    public void delete() {
        super.delete();
    }

    public void edit(InfoTeachingCareer infoTeachingCareer, MultiLanguageString category) {
        if (category == null) {
            throw new DomainException("The category should not be null!");
        }

        setBasicProperties(infoTeachingCareer);
        this.setCategoryName(category);
    }

    private void setBasicProperties(InfoTeachingCareer infoTeachingCareer) {
        this.setBeginYear(infoTeachingCareer.getBeginYear());
        this.setEndYear(infoTeachingCareer.getEndYear());
        this.setCourseOrPosition(infoTeachingCareer.getCourseOrPosition());

    }

    @Override
    public void setCourseOrPosition(String courseOrPosition) {
        if (courseOrPosition != null && courseOrPosition.length() > 100) {
            throw new DomainException("error.courseOrPosition.max.length.exceeded");
        }
        super.setCourseOrPosition(courseOrPosition);
    }

}
