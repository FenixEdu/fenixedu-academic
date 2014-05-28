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
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys;

import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.CardinalityType;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;

/**
 * @author Susana Fernandes
 * 
 */
public class IMS_LIDQuestionCorrectionStrategy extends QuestionCorrectionStrategy {

    @Override
    public StudentTestQuestion getMark(StudentTestQuestion studentTestQuestion) {
        if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID) {
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.MULTIPLE) {
                ResponseProcessing responseProcessing =
                        getLIDResponseProcessing(studentTestQuestion.getSubQuestionByItem().getResponseProcessingInstructions(),
                                ((ResponseLID) studentTestQuestion.getResponse()).getResponse());
                if (responseProcessing != null) {
                    return setStudentTestQuestionResponse(studentTestQuestion, responseProcessing);
                }
            } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
                ResponseProcessing responseProcessing =
                        getLIDResponseProcessing(studentTestQuestion.getSubQuestionByItem().getResponseProcessingInstructions(),
                                ((ResponseLID) studentTestQuestion.getResponse()).getResponse()[0]);
                if (responseProcessing != null) {
                    return setStudentTestQuestionResponse(studentTestQuestion, responseProcessing);
                }
            }
            ResponseProcessing responseProcessing =
                    getOtherResponseProcessing(studentTestQuestion.getSubQuestionByItem().getResponseProcessingInstructions());
            if (responseProcessing != null) {
                return setStudentTestQuestionResponse(studentTestQuestion, responseProcessing);
            }
        }
        studentTestQuestion.setTestQuestionMark(new Double(0));
        return studentTestQuestion;
    }

    private StudentTestQuestion setStudentTestQuestionResponse(StudentTestQuestion studentTestQuestion,
            ResponseProcessing responseProcessing) {
        studentTestQuestion.setTestQuestionMark(responseProcessing.getResponseValue());
        ResponseLID r = (ResponseLID) studentTestQuestion.getResponse();
        r.setResponseProcessingIndex(studentTestQuestion.getSubQuestionByItem().getResponseProcessingInstructions()
                .indexOf(responseProcessing));
        studentTestQuestion.setResponse(r);
        studentTestQuestion.getSubQuestionByItem().setNextItemId(responseProcessing.getNextItem());
        return studentTestQuestion;
    }
}