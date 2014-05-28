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
 * Created on Oct 24, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoSiteStudentsTestMarks extends DataTranferObject implements ISiteComponent {

    private List infoStudentTestQuestionList;

    private Double maximumMark;

    private InfoDistributedTest infoDistributedTest;

    private InfoExecutionCourse executionCourse;

    public InfoSiteStudentsTestMarks() {
    }

    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public InfoDistributedTest getInfoDistributedTest() {
        return infoDistributedTest;
    }

    public List getInfoStudentTestQuestionList() {
        return infoStudentTestQuestionList;
    }

    public Double getMaximumMark() {
        return maximumMark;
    }

    public void setMaximumMark(Double maximumMark) {
        this.maximumMark = maximumMark;
    }

    public void setExecutionCourse(InfoExecutionCourse course) {
        executionCourse = course;
    }

    public void setInfoDistributedTest(InfoDistributedTest test) {
        infoDistributedTest = test;
    }

    public void setInfoStudentTestQuestionList(List list) {
        infoStudentTestQuestionList = list;
    }

}