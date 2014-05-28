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
package net.sourceforge.fenixedu.dataTransferObject.gesdis;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoCourseReport extends InfoObject {

    private String report;

    private Date lastModificationDate;

    private InfoExecutionCourse infoExecutionCourse;

    public InfoCourseReport() {
    }

    public String getReport() {
        return report;
    }

    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    /**
     * @return Returns the lastModificationDate.
     */
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    /**
     * @param lastModificationDate
     *            The lastModificationDate to set.
     */
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public void copyFromDomain(CourseReport courseReport) {
        super.copyFromDomain(courseReport);
        if (courseReport != null) {
            setLastModificationDate(courseReport.getLastModificationDate());
            setReport(courseReport.getReport());
            setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(courseReport.getExecutionCourse()));
        }
    }

    public static InfoCourseReport newInfoFromDomain(CourseReport courseReport) {
        InfoCourseReport infoCourseReport = null;
        if (courseReport != null) {
            infoCourseReport = new InfoCourseReport();
            infoCourseReport.copyFromDomain(courseReport);
        }
        return infoCourseReport;
    }
}