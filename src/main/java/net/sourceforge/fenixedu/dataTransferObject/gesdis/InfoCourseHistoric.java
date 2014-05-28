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
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.gesdis;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.gesdis.CourseHistoric;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoCourseHistoric extends InfoObject {

    private Integer enrolled;

    private Integer evaluated;

    private Integer approved;

    private String curricularYear;

    private Integer semester;

    private InfoCurricularCourse infoCurricularCourse;

    public InfoCourseHistoric() {
    }

    /**
     * @return Returns the approved.
     */
    public Integer getApproved() {
        return approved;
    }

    /**
     * @param approved
     *            The approved to set.
     */
    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    /**
     * @return Returns the curricularYear.
     */
    public String getCurricularYear() {
        return curricularYear;
    }

    /**
     * @param curricularYear
     *            The curricularYear to set.
     */
    public void setCurricularYear(String curricularYear) {
        this.curricularYear = curricularYear;
    }

    /**
     * @return Returns the enrolled.
     */
    public Integer getEnrolled() {
        return enrolled;
    }

    /**
     * @param enrolled
     *            The enrolled to set.
     */
    public void setEnrolled(Integer enrolled) {
        this.enrolled = enrolled;
    }

    /**
     * @return Returns the evaluated.
     */
    public Integer getEvaluated() {
        return evaluated;
    }

    /**
     * @param evaluated
     *            The evaluated to set.
     */
    public void setEvaluated(Integer evaluated) {
        this.evaluated = evaluated;
    }

    /**
     * @return Returns the infoCurricularCourse.
     */
    public InfoCurricularCourse getInfoCurricularCourse() {
        return infoCurricularCourse;
    }

    /**
     * @param infoCurricularCourse
     *            The infoCurricularCourse to set.
     */
    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
        this.infoCurricularCourse = infoCurricularCourse;
    }

    /**
     * @return Returns the semester.
     */
    public Integer getSemester() {
        return semester;
    }

    /**
     * @param semester
     *            The semester to set.
     */
    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain
     * (Dominio.DomainObject)
     */
    public void copyFromDomain(CourseHistoric courseHistoric) {
        super.copyFromDomain(courseHistoric);
        if (courseHistoric != null) {
            setApproved(courseHistoric.getApproved());
            setEnrolled(courseHistoric.getEnrolled());
            setEvaluated(courseHistoric.getEvaluated());
            setCurricularYear(courseHistoric.getCurricularYear());
            setSemester(courseHistoric.getSemester());
        }
    }

    public static InfoCourseHistoric newInfoFromDomain(CourseHistoric courseHistoric) {
        InfoCourseHistoric infoCourseHistoric = null;
        if (courseHistoric != null) {
            infoCourseHistoric = new InfoCourseHistoric();
            infoCourseHistoric.copyFromDomain(courseHistoric);
        }
        return infoCourseHistoric;
    }
}