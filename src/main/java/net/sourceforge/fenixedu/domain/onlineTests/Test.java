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
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.util.tests.CorrectionFormula;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author Susana Fernandes
 */
public class Test extends Test_Base {

    public Test(String title, String information, TestScope testScope) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setTitle(title);
        setInformation(information);
        DateTime now = new DateTime();
        setCreationDateDateTime(now);
        setLastModifiedDateDateTime(now);
        setTestScope(testScope);

    }

    public void insertTestQuestion(final Question question, final Integer testQuestionOrder, final Double testQuestionValue,
            final CorrectionFormula formula) {
        organizeTestQuestionsOrder(testQuestionOrder);
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestion(question);
        testQuestion.setTestQuestionOrder(testQuestionOrder);
        testQuestion.setTestQuestionValue(testQuestionValue);
        testQuestion.setCorrectionFormula(formula);
        testQuestion.setTest(this);
        this.setLastModifiedDateDateTime(new DateTime());

    }

    private void organizeTestQuestionsOrder(Integer testQuestionToInsertOrder) {

        Collection<TestQuestion> testQuestions = getTestQuestions();
        for (final TestQuestion testQuestion : testQuestions) {
            Integer iterQuestionOrder = testQuestion.getTestQuestionOrder();
            if (testQuestionToInsertOrder.compareTo(iterQuestionOrder) <= 0) {
                testQuestion.setTestQuestionOrder(Integer.valueOf(iterQuestionOrder.intValue() + 1));
            }
        }
    }

    public void deleteTestQuestion(final TestQuestion testQuestionToDelete) {
        List<TestQuestion> aux = new ArrayList<TestQuestion>();

        Collection<TestQuestion> testQuestions = getTestQuestions();
        for (final TestQuestion testQuestion : testQuestions) {
            if (!testQuestionToDelete.equals(testQuestion)) {
                Integer iterQuestionOrder = testQuestion.getTestQuestionOrder();
                if (testQuestionToDelete.getTestQuestionOrder().compareTo(iterQuestionOrder) < 0) {
                    testQuestion.setTestQuestionOrder(Integer.valueOf(iterQuestionOrder.intValue() - 1));
                }
            } else {
                aux.add(testQuestion);
            }
        }

        for (final TestQuestion testQuestion : aux) {
            testQuestion.delete();
        }

        setLastModifiedDateDateTime(new DateTime());
    }

    public TestQuestion getTestQuestion(final Question question) {
        for (final TestQuestion testQuestion : getTestQuestions()) {
            if (testQuestion.getQuestion() == question) {
                return testQuestion;
            }
        }
        return null;
    }

    public void delete() {
        for (; !getTestQuestions().isEmpty(); getTestQuestions().iterator().next().delete()) {
            ;
        }
        setTestScope(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public Question findQuestionByOID(String questionId) {
        for (TestQuestion testQuestion : this.getTestQuestions()) {
            if (testQuestion.getQuestion().getExternalId().equals(questionId)) {
                return testQuestion.getQuestion();
            }
        }
        return null;
    }

    public String getLastModifiedDateFormatted() {
        DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm");
        return dateFormat.print(getLastModifiedDateDateTime());
    }

    public String getCreationDateFormatted() {
        DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm");
        return dateFormat.print(getCreationDateDateTime());
    }

    @Deprecated
    public java.util.Date getCreationDate() {
        org.joda.time.DateTime dt = getCreationDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setCreationDate(java.util.Date date) {
        if (date == null) {
            setCreationDateDateTime(null);
        } else {
            setCreationDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Date getLastModifiedDate() {
        org.joda.time.DateTime dt = getLastModifiedDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setLastModifiedDate(java.util.Date date) {
        if (date == null) {
            setLastModifiedDateDateTime(null);
        } else {
            setLastModifiedDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.TestQuestion> getTestQuestions() {
        return getTestQuestionsSet();
    }

    @Deprecated
    public boolean hasAnyTestQuestions() {
        return !getTestQuestionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasInformation() {
        return getInformation() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasTestScope() {
        return getTestScope() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDateDateTime() {
        return getLastModifiedDateDateTime() != null;
    }

    @Deprecated
    public boolean hasCreationDateDateTime() {
        return getCreationDateDateTime() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

}
