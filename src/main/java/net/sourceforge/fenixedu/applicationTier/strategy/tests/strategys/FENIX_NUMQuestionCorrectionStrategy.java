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
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;

/**
 * @author Susana Fernandes
 * 
 */
public class FENIX_NUMQuestionCorrectionStrategy extends QuestionCorrectionStrategy {

    @Override
    public StudentTestQuestion getMark(StudentTestQuestion studentTestQuestion) {
        if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.NUM) {
            ResponseProcessing responseProcessing =
                    getNUMResponseProcessing(studentTestQuestion.getSubQuestionByItem().getResponseProcessingInstructions(),
                            new Double(((ResponseNUM) studentTestQuestion.getResponse()).getResponse()));
            if (responseProcessing != null) {
                if (responseProcessing.isFenixCorrectResponse()) {
                    studentTestQuestion.setTestQuestionMark(new Double(studentTestQuestion.getTestQuestionValue().doubleValue()));
                    ResponseNUM r = (ResponseNUM) studentTestQuestion.getResponse();
                    r.setIsCorrect(new Boolean(true));
                    studentTestQuestion.setResponse(r);
                    studentTestQuestion.getSubQuestionByItem().setNextItemId(responseProcessing.getNextItem());
                    return studentTestQuestion;
                }
                studentTestQuestion.getSubQuestionByItem().setNextItemId(responseProcessing.getNextItem());
            }
            ResponseNUM r = (ResponseNUM) studentTestQuestion.getResponse();
            r.setIsCorrect(new Boolean(false));
            studentTestQuestion.setResponse(r);
        }
        studentTestQuestion.setTestQuestionMark(new Double(0));
        return studentTestQuestion;
    }
}