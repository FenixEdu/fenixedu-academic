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
package org.fenixedu.academic.dto.onlineTests;

import java.util.List;

import org.fenixedu.academic.dto.DataTranferObject;
import org.fenixedu.academic.domain.onlineTests.DistributedTest;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoSiteStudentsTestMarksStatistics extends DataTranferObject {

    private List correctAnswersPercentage;

    private List partiallyCorrectAnswersPercentage;

    private List wrongAnswersPercentage;

    private List notAnsweredPercentage;

    private List answeredPercentage;

    private DistributedTest distributedTest;

    public InfoSiteStudentsTestMarksStatistics() {
    }

    public List getCorrectAnswersPercentage() {
        return correctAnswersPercentage;
    }

    public DistributedTest getDistributedTest() {
        return distributedTest;
    }

    public List getNotAnsweredPercentage() {
        return notAnsweredPercentage;
    }

    public List getWrongAnswersPercentage() {
        return wrongAnswersPercentage;
    }

    public void setCorrectAnswersPercentage(List list) {
        correctAnswersPercentage = list;
    }

    public void setDistributedTest(DistributedTest distributedTest) {
        this.distributedTest = distributedTest;
    }

    public void setNotAnsweredPercentage(List list) {
        notAnsweredPercentage = list;
    }

    public void setWrongAnswersPercentage(List list) {
        wrongAnswersPercentage = list;
    }

    public List getPartiallyCorrectAnswersPercentage() {
        return partiallyCorrectAnswersPercentage;
    }

    public void setPartiallyCorrectAnswersPercentage(List partiallyCorrectAnswersPercentage) {
        this.partiallyCorrectAnswersPercentage = partiallyCorrectAnswersPercentage;
    }

    public List getAnsweredPercentage() {
        return answeredPercentage;
    }

    public void setAnsweredPercentage(List answeredPercentage) {
        this.answeredPercentage = answeredPercentage;
    }
}