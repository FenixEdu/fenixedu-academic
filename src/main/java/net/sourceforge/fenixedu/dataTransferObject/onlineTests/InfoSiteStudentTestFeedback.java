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
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;

/**
 * @author Susana Fernandes
 */
public class InfoSiteStudentTestFeedback extends DataTranferObject implements ISiteComponent {

    private Integer responseNumber;

    private Integer notResponseNumber;

    private List<String> errors;

    private List studentTestQuestionList;

    private StudentTestLog studentTestLog;

    public InfoSiteStudentTestFeedback() {
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Integer getNotResponseNumber() {
        return notResponseNumber;
    }

    public void setNotResponseNumber(Integer notResponseNumber) {
        this.notResponseNumber = notResponseNumber;
    }

    public Integer getResponseNumber() {
        return responseNumber;
    }

    public void setResponseNumber(Integer responseNumber) {
        this.responseNumber = responseNumber;
    }

    public List getStudentTestQuestionList() {
        return studentTestQuestionList;
    }

    public void setStudentTestQuestionList(List studentTestQuestionList) {
        this.studentTestQuestionList = studentTestQuestionList;
    }

    public StudentTestLog getStudentTestLog() {
        return studentTestLog;
    }

    public void setStudentTestLog(StudentTestLog studentTestLog) {
        this.studentTestLog = studentTestLog;
    }
}