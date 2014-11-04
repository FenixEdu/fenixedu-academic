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
package net.sourceforge.fenixedu.dataTransferObject.alumni.formation;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.EducationArea;

public class AlumniEducationArea implements Serializable {

    public static Comparator<AlumniEducationArea> COMPARATOR_BY_CODE = new Comparator<AlumniEducationArea>() {
        @Override
        public int compare(AlumniEducationArea leftEducationArea, AlumniEducationArea rightEducationArea) {
            int comparationResult =
                    leftEducationArea.getEducationArea().getCode().compareTo(rightEducationArea.getEducationArea().getCode());
            return (comparationResult == 0) ? leftEducationArea.getEducationArea().getExternalId()
                    .compareTo(rightEducationArea.getEducationArea().getExternalId()) : comparationResult;
        }
    };

    private EducationArea educationArea;

    public AlumniEducationArea(EducationArea area) {
        educationArea = area;
    }

    public EducationArea getEducationArea() {
        return this.educationArea;
    }

    public void setEducationArea(EducationArea educationArea) {
        this.educationArea = educationArea;
    }

    // FIXME only used for presentation display purposes
    public String getCodeIndentationValue() {
        String indent = "";
        for (int i = 1; i < getEducationArea().getCode().length(); i++) {
            indent += "&nbsp;&nbsp;";
        }
        return indent;
    }

}
