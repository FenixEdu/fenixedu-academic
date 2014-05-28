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
 * Created on 9/Fev/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class InfoInquiryStatistics extends DataTranferObject implements ISiteComponent {
    private StudentTestQuestion infoStudentTestQuestion;

    private List optionStatistics;

    public InfoInquiryStatistics() {
    }

    public List getOptionStatistics() {
        return optionStatistics;
    }

    public StudentTestQuestion getInfoStudentTestQuestion() {
        return infoStudentTestQuestion;
    }

    public void setOptionStatistics(List list) {
        optionStatistics = list;
    }

    public void setInfoStudentTestQuestion(StudentTestQuestion question) {
        infoStudentTestQuestion = question;
    }

}