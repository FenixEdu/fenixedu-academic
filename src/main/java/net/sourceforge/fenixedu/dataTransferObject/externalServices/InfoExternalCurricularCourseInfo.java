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
 * Created on 4:02:48 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * 
 *         Created at 4:02:48 PM, Mar 11, 2005
 */
public class InfoExternalCurricularCourseInfo {

    private String name;
    private String code;
    private String ECTSCredits;
    private String credits;
    private String weigth;
    private String curricularYear;

    public String getCredits() {
        return this.credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getECTSCredits() {
        return this.ECTSCredits;
    }

    public void setECTSCredits(String credits) {
        ECTSCredits = credits;
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @param code
     *            The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    static public InfoExternalCurricularCourseInfo newFromDomain(CurricularCourse course) {
        Integer year = null;
        for (Object element2 : course.getDegreeModuleScopes()) {
            DegreeModuleScope element = (DegreeModuleScope) element2;
            int currentYear = element.getCurricularYear().intValue();
            if (year == null || currentYear < year.intValue()) {
                year = currentYear;
            }
        }

        InfoExternalCurricularCourseInfo info = new InfoExternalCurricularCourseInfo();

        if (year != null) {
            info.setCurricularYear(new Integer(year).toString());
        }
        info.setCode(course.getAcronym());
        info.setName(course.getName());
        if (course.getWeigth() != null) {
            info.setWeigth(course.getWeigth().toString());
        }
        if (course.getEctsCredits() != null) {
            info.setECTSCredits(course.getEctsCredits().toString());
        }
        if (course.getCredits() != null) {
            info.setCredits(course.getCredits().toString());
        }

        return info;
    }

    public String getWeigth() {
        return this.weigth;
    }

    public void setWeigth(String weigth) {
        this.weigth = weigth;
    }

    public String getCurricularYear() {
        return this.curricularYear;
    }

    public void setCurricularYear(String curricularYear) {
        this.curricularYear = curricularYear;
    }
}
