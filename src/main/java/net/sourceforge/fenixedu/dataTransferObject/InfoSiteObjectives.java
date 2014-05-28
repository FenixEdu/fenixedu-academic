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
 * Created on 5/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 * 
 * 
 */
public class InfoSiteObjectives extends DataTranferObject implements ISiteComponent {

    private List infoCurricularCourses;

    private List infoCurriculums;

    public int getSize() {
        return infoCurriculums.size();
    }

    /**
     * @return
     */
    public List getInfoCurricularCourses() {
        return infoCurricularCourses;
    }

    /**
     * @param infoCurricularCourses
     */
    public void setInfoCurricularCourses(List infoCurricularCourses) {
        this.infoCurricularCourses = infoCurricularCourses;
    }

    /**
     * @return
     */
    public List getInfoCurriculums() {
        return infoCurriculums;
    }

    /**
     * @param infoCurriculums
     */
    public void setInfoCurriculums(List infoCurriculums) {
        this.infoCurriculums = infoCurriculums;
    }
}