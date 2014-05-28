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
 * Created on 29/Jul/2003
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.Collection;
import java.util.Comparator;

import net.sourceforge.fenixedu.util.tests.CorrectionFormula;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Susana Fernandes
 */
public class TestQuestion extends TestQuestion_Base {
    public static final Comparator<TestQuestion> COMPARATOR_BY_TEST_QUESTION_ORDER = new BeanComparator("testQuestionOrder");

    public TestQuestion() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void editTestQuestion(Integer newTestQuestionOrder, final Double newTestQuestionValue,
            final CorrectionFormula newFormula) {
        organizeTestQuestionsOrder(newTestQuestionOrder, this.getTestQuestionOrder());
        this.setTestQuestionOrder(newTestQuestionOrder);
        this.setTestQuestionValue(newTestQuestionValue);
        this.setCorrectionFormula(newFormula);
    }

    private void organizeTestQuestionsOrder(Integer newOrder, Integer oldOrder) {
        Collection<TestQuestion> testQuestions = getTest().getTestQuestions();
        int diffOrder = newOrder.intValue() - oldOrder.intValue();
        if (diffOrder != 0) {
            if (diffOrder > 0) {
                for (TestQuestion testQuestion : testQuestions) {
                    if (testQuestion.getTestQuestionOrder().compareTo(newOrder) <= 0
                            && testQuestion.getTestQuestionOrder().compareTo(oldOrder) > 0) {
                        testQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder() - 1);
                    }
                }
            } else {
                for (TestQuestion testQuestion : testQuestions) {
                    if (testQuestion.getTestQuestionOrder().compareTo(newOrder) >= 0
                            && testQuestion.getTestQuestionOrder().compareTo(oldOrder) < 0) {
                        testQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder() + 1);
                    }
                }
            }
        }
    }

    public void delete() {
        setQuestion(null);
        setTest(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasTest() {
        return getTest() != null;
    }

    @Deprecated
    public boolean hasTestQuestionOrder() {
        return getTestQuestionOrder() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasQuestion() {
        return getQuestion() != null;
    }

    @Deprecated
    public boolean hasTestQuestionValue() {
        return getTestQuestionValue() != null;
    }

    @Deprecated
    public boolean hasCorrectionFormula() {
        return getCorrectionFormula() != null;
    }

}
