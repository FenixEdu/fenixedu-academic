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
 * Created on 20/Ago/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.Calendar;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;

/**
 * @author Susana Fernandes
 */
public class InfoDistributedTest extends InfoObject {

    private String title;

    private String testInformation;

    private Calendar beginDate;

    private Calendar endDate;

    private Calendar beginHour;

    private Calendar endHour;

    private TestType testType;

    private CorrectionAvailability correctionAvailability;

    private Boolean imsFeedback;

    private Integer numberOfQuestions;

    private InfoTestScope infoTestScope;

    public InfoDistributedTest() {
    }

    public String getTitle() {
        return title;
    }

    public String getTestInformation() {
        return testInformation;
    }

    public Calendar getBeginDate() {
        return beginDate;
    }

    public Calendar getBeginHour() {
        return beginHour;
    }

    public CorrectionAvailability getCorrectionAvailability() {
        return correctionAvailability;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public Calendar getEndHour() {
        return endHour;
    }

    public TestType getTestType() {
        return testType;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setTitle(String string) {
        title = string;
    }

    public void setTestInformation(String string) {
        testInformation = string;
    }

    public void setBeginDate(Calendar calendar) {
        beginDate = calendar;
    }

    public void setBeginHour(Calendar calendar) {
        beginHour = calendar;
    }

    public void setCorrectionAvailability(CorrectionAvailability availability) {
        correctionAvailability = availability;
    }

    public void setEndDate(Calendar calendar) {
        endDate = calendar;
    }

    public void setEndHour(Calendar calendar) {
        endHour = calendar;
    }

    public void setTestType(TestType type) {
        testType = type;
    }

    public void setNumberOfQuestions(Integer integer) {
        numberOfQuestions = integer;
    }

    public InfoTestScope getInfoTestScope() {
        return infoTestScope;
    }

    public void setInfoTestScope(InfoTestScope scope) {
        infoTestScope = scope;
    }

    public Boolean getImsFeedback() {
        return imsFeedback;
    }

    public void setImsFeedback(Boolean imsFeedback) {
        this.imsFeedback = imsFeedback;
    }

    public String getBeginDateTimeFormatted() {
        String result = new String();
        Calendar date = getBeginDate();
        result += date.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += date.get(Calendar.MONTH) + 1;
        result += "/";
        result += date.get(Calendar.YEAR);
        result += " ";
        date = getBeginHour();
        result += date.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (date.get(Calendar.MINUTE) < 10) {
            result += "0";
        }
        result += date.get(Calendar.MINUTE);
        return result;
    }

    public String getEndDateTimeFormatted() {
        String result = new String();
        Calendar date = getEndDate();
        result += date.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += date.get(Calendar.MONTH) + 1;
        result += "/";
        result += date.get(Calendar.YEAR);
        result += " ";
        date = getEndHour();
        result += date.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (date.get(Calendar.MINUTE) < 10) {
            result += "0";
        }
        result += date.get(Calendar.MINUTE);
        return result;
    }

    public String getBeginDayFormatted() {
        String result = new String();
        if (getBeginDate().get(Calendar.DAY_OF_MONTH) < 10) {
            result += "0";
        }
        return result.concat(Integer.toString(getBeginDate().get(Calendar.DAY_OF_MONTH)));

    }

    public String getBeginMonthFormatted() {
        String result = new String();
        if (getBeginDate().get(Calendar.MONTH) + 1 < 10) {
            result += "0";
        }
        return result.concat(Integer.toString(getBeginDate().get(Calendar.MONTH) + 1));
    }

    public String getBeginYearFormatted() {
        return new Integer(getBeginDate().get(Calendar.YEAR)).toString();
    }

    public String getBeginHourFormatted() {
        String result = new String();
        if (getBeginHour().get(Calendar.HOUR_OF_DAY) < 10) {
            result += "0";
        }
        return result.concat(Integer.toString(getBeginHour().get(Calendar.HOUR_OF_DAY)));
    }

    public String getBeginMinuteFormatted() {
        String result = new String();
        if (getBeginHour().get(Calendar.MINUTE) < 10) {
            result += "0";
        }
        return result.concat(Integer.toString(getBeginHour().get(Calendar.MINUTE)));
    }

    public String getEndDayFormatted() {
        String result = new String();
        if (getEndDate().get(Calendar.DAY_OF_MONTH) < 10) {
            result += "0";
        }
        return result.concat(Integer.toString(getEndDate().get(Calendar.DAY_OF_MONTH)));
    }

    public String getEndMonthFormatted() {
        String result = new String();
        if (getEndDate().get(Calendar.MONTH) + 1 < 10) {
            result += "0";
        }
        return result.concat(Integer.toString(getEndDate().get(Calendar.MONTH) + 1));
    }

    public String getEndYearFormatted() {
        return new Integer(getEndDate().get(Calendar.YEAR)).toString();
    }

    public String getEndHourFormatted() {
        String result = new String();
        if (getEndHour().get(Calendar.HOUR_OF_DAY) < 10) {
            result += "0";
        }
        return result.concat(Integer.toString(getEndHour().get(Calendar.HOUR_OF_DAY)));

    }

    public String getEndMinuteFormatted() {
        String result = new String();
        if (getEndHour().get(Calendar.MINUTE) < 10) {
            result += "0";
        }
        return result.concat(Integer.toString(getEndHour().get(Calendar.MINUTE)));
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoDistributedTest) {
            InfoDistributedTest infoDistributedTest = (InfoDistributedTest) obj;
            result =
                    (getExternalId().equals(infoDistributedTest.getExternalId()))
                            && (getTitle().equals(infoDistributedTest.getTitle()))
                            && (getBeginDate().equals(infoDistributedTest.getBeginDate()))
                            && (getBeginHour().equals(infoDistributedTest.getBeginHour()))
                            && (getEndDate().equals(infoDistributedTest.getEndDate()))
                            && (getEndHour().equals(infoDistributedTest.getEndHour()))
                            && (getTestType().equals(infoDistributedTest.getTestType()))
                            && (getCorrectionAvailability().equals(infoDistributedTest.getCorrectionAvailability()))
                            && (getImsFeedback().equals(infoDistributedTest.getImsFeedback()))
                            && (getNumberOfQuestions().equals(infoDistributedTest.getNumberOfQuestions()));
        }
        return result;
    }

    public void copyFromDomain(DistributedTest distributedTest) {
        super.copyFromDomain(distributedTest);
        if (distributedTest != null) {
            setTitle(distributedTest.getTitle());
            setTestInformation(distributedTest.getTestInformation());
            setBeginDate(distributedTest.getBeginDate());
            setBeginHour(distributedTest.getBeginHour());
            setEndDate(distributedTest.getEndDate());
            setEndHour(distributedTest.getEndHour());
            setTestType(distributedTest.getTestType());
            setCorrectionAvailability(distributedTest.getCorrectionAvailability());
            setImsFeedback(distributedTest.getImsFeedback());
            setNumberOfQuestions(distributedTest.getNumberOfQuestions());
        }
    }

    public static InfoDistributedTest newInfoFromDomain(DistributedTest distributedTest) {
        InfoDistributedTest infoDistributedTest = null;
        if (distributedTest != null) {
            infoDistributedTest = new InfoDistributedTest();
            infoDistributedTest.copyFromDomain(distributedTest);
        }
        return infoDistributedTest;
    }
}