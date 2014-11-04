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
 * Created on 30/Jun/2004
 *
 */
package org.fenixedu.academic.dto.teacher;

import org.fenixedu.academic.dto.InfoTeacher;
import org.fenixedu.academic.domain.teacher.TeachingCareer;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoTeachingCareerWithInfoCategory extends InfoTeachingCareer {

    private MultiLanguageString categoryName;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.fenixedu.academic.dto.teacher.InfoTeachingCareer
     * #copyFromDomain(Dominio.teacher.TeachingCareer)
     */
    @Override
    public void copyFromDomain(TeachingCareer teachingCareer) {
        super.copyFromDomain(teachingCareer);
        if (teachingCareer != null) {
            setCategoryName(teachingCareer.getCategoryName());
            setInfoTeacher(InfoTeacher.newInfoFromDomain(teachingCareer.getTeacher()));
        }
    }

    public static InfoTeachingCareer newInfoFromDomain(TeachingCareer teachingCareer) {
        InfoTeachingCareerWithInfoCategory infoTeachingCareer = null;
        if (teachingCareer != null) {
            infoTeachingCareer = new InfoTeachingCareerWithInfoCategory();
            infoTeachingCareer.copyFromDomain(teachingCareer);
        }
        return infoTeachingCareer;
    }

    public MultiLanguageString getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(MultiLanguageString categoryName) {
        this.categoryName = categoryName;
    }
}