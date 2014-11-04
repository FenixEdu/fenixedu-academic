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
 * Created on 4/Ago/2003
 *  
 */
package org.fenixedu.academic.service.services.teacher.onlineTests;

import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.dto.onlineTests.InfoQuestion;
import org.fenixedu.academic.dto.onlineTests.InfoStudentTestQuestion;
import org.fenixedu.academic.domain.onlineTests.Question;
import org.fenixedu.academic.domain.onlineTests.Test;
import org.fenixedu.academic.domain.onlineTests.TestQuestion;
import org.fenixedu.academic.domain.onlineTests.utils.ParseSubQuestion;
import org.fenixedu.academic.util.tests.QuestionOption;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class ReadQuestionImage {

    @Atomic
    public static String run(String exerciseId, Integer imageId, Integer feedbackId, Integer itemIndex)
            throws FenixServiceException {
        Question question = FenixFramework.getDomainObject(exerciseId);
        if (question != null) {
            if (question.getSubQuestions() == null || question.getSubQuestions().size() == 0) {
                ParseSubQuestion parse = new ParseSubQuestion();
                try {
                    question = parse.parseSubQuestion(question);
                } catch (ParseQuestionException e) {
                    throw new FenixServiceException();
                }
            }
            if (question.getSubQuestions().size() < itemIndex) {
                return null;
            }
            return question.getSubQuestions().get(itemIndex).getImage(imageId, feedbackId);
        }
        return null;
    }

    @Atomic
    public static String run(String distributedTestId, String questionId, String optionShuffle, Integer imageId,
            Integer feedbackId) throws FenixServiceException {

        Question question = null;
        Test test = FenixFramework.getDomainObject(distributedTestId);
        for (TestQuestion testQuestion : test.getTestQuestionsSet()) {
            if (testQuestion.getQuestion().getExternalId().equals(questionId)) {
                question = testQuestion.getQuestion();
                break;
            }
        }
        if (question == null) {
            throw new FenixServiceException("Unexisting Question!!!!!");
        }

        InfoStudentTestQuestion infoStudentTestQuestion = new InfoStudentTestQuestion();
        try {
            ParseSubQuestion parse = new ParseSubQuestion();
            infoStudentTestQuestion.setQuestion(InfoQuestion.newInfoFromDomain(question));
            infoStudentTestQuestion.setOptionShuffle(optionShuffle);
            // infoStudentTestQuestion =
            // parse.parseStudentTestQuestion(infoStudentTestQuestion,
            // path.replace('\\', '/'));
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        Iterator questionit = infoStudentTestQuestion.getQuestion().getQuestion().iterator();
        int imgIndex = 0;
        while (questionit.hasNext()) {
            LabelValueBean lvb = (LabelValueBean) questionit.next();
            if (lvb.getLabel().startsWith("image/")) {
                imgIndex++;
                if (imgIndex == imageId.intValue()) {
                    return lvb.getValue();
                }
            }
        }
        Iterator optionit = infoStudentTestQuestion.getQuestion().getOptions().iterator();
        while (optionit.hasNext()) {
            List optionContent = ((QuestionOption) optionit.next()).getOptionContent();
            for (int i = 0; i < optionContent.size(); i++) {
                LabelValueBean lvb = (LabelValueBean) optionContent.get(i);
                if (lvb.getLabel().startsWith("image/")) {
                    imgIndex++;
                    if (imgIndex == imageId.intValue()) {
                        return lvb.getValue();
                    }
                }
            }
        }

        // if (feedbackId != null) {
        // Iterator feedbackit = ((ResponseProcessing)
        // infoStudentTestQuestion.getQuestion().getResponseProcessingInstructions
        // ().get(
        // new Integer(feedbackId).intValue())).getFeedback().iterator();
        //
        // while (feedbackit.hasNext()) {
        // LabelValueBean lvb = (LabelValueBean) feedbackit.next();
        // if (lvb.getLabel().startsWith("image/")) {
        // imgIndex++;
        // if (imgIndex == imageId.intValue())
        // return lvb.getValue();
        // }
        // }
        // }

        return null;
    }
}