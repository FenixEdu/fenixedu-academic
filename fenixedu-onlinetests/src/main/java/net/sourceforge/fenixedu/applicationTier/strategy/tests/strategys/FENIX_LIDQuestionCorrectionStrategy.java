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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.CardinalityType;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseCondition;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;

/**
 * @author Susana Fernandes
 * 
 */
public class FENIX_LIDQuestionCorrectionStrategy extends QuestionCorrectionStrategy {

    @Override
    public StudentTestQuestion getMark(StudentTestQuestion studentTestQuestion) {
        if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID) {
            Integer fenixCorrectResponseIndex =
                    getFenixCorrectResponseIndex(studentTestQuestion.getSubQuestionByItem().getResponseProcessingInstructions());
            if (fenixCorrectResponseIndex != null) {
                if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.MULTIPLE) {
                    // 2*(correctOptionsChosen+wrongOptionNotChosen)/allOptionNumber-
                    // 1
                    Integer empty = Integer.valueOf(getEmptyOptionIndex(studentTestQuestion.getSubQuestionByItem().getOptions()));
                    List<ResponseCondition> correctResponseList =
                            getFenixCorrectResponse4LID(studentTestQuestion.getSubQuestionByItem()
                                    .getResponseProcessingInstructions(), fenixCorrectResponseIndex);
                    int emptyNumber = 0;
                    int emptyIndex = getEmptyOptionIndex(studentTestQuestion.getSubQuestionByItem().getOptions());
                    if (emptyIndex != -1) {
                        emptyNumber = 1;
                    }
                    int allOptionNumber = studentTestQuestion.getSubQuestionByItem().getOptions().size() - emptyNumber;
                    int correctOptionNumber = getCorrectOptionsNumber(correctResponseList, emptyIndex);
                    int wrongOptionsNumber = allOptionNumber - correctOptionNumber;
                    int correctOptionsChosen = 0;
                    int wrongOptionChosen = 0;
                    Boolean[] isCorrect = new Boolean[((ResponseLID) studentTestQuestion.getResponse()).getResponse().length];
                    for (int i = 0; i < ((ResponseLID) studentTestQuestion.getResponse()).getResponse().length; i++) {
                        boolean correct = false;
                        boolean chooseEmpty = false;
                        for (ResponseCondition responseCondition : correctResponseList) {
                            if ((((ResponseLID) studentTestQuestion.getResponse()).getResponse()[i]).equals(empty.toString())) {
                                chooseEmpty = true;
                            }
                            if (responseCondition.isCorrectLID(new String(((ResponseLID) studentTestQuestion.getResponse())
                                    .getResponse()[i]))) {
                                correct = true;
                                break;
                            }
                        }
                        isCorrect[i] = new Boolean(correct);

                        if (!chooseEmpty) {
                            if (correct) {
                                correctOptionsChosen++;
                            } else {
                                wrongOptionChosen++;
                            }
                        }
                    }
                    int wrongOptionNotChosen = wrongOptionsNumber - wrongOptionChosen;

                    if (allOptionNumber > 1) {
                        studentTestQuestion.setTestQuestionMark(new Double(
                                studentTestQuestion.getTestQuestionValue().doubleValue()
                                        * (2 * (correctOptionsChosen + wrongOptionNotChosen)
                                                * (java.lang.Math.pow(allOptionNumber, -1)) - 1)));
                    } else {
                        studentTestQuestion.setTestQuestionMark(new Double(studentTestQuestion.getTestQuestionValue()
                                .doubleValue() * correctOptionsChosen));
                    }
                    ResponseLID r = (ResponseLID) studentTestQuestion.getResponse();
                    r.setIsCorrect(isCorrect);
                    studentTestQuestion.setResponse(r);

                    ResponseProcessing responseProcessing =
                            getLIDResponseProcessing(studentTestQuestion.getSubQuestionByItem()
                                    .getResponseProcessingInstructions(),
                                    ((ResponseLID) studentTestQuestion.getResponse()).getResponse());
                    if (responseProcessing != null) {
                        studentTestQuestion.getSubQuestionByItem().setNextItemId(responseProcessing.getNextItem());
                    }
                    return studentTestQuestion;
                } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
                    // (1/num_op)-1
                    ResponseProcessing responseProcessing =
                            getLIDResponseProcessing(studentTestQuestion.getSubQuestionByItem()
                                    .getResponseProcessingInstructions(),
                                    ((ResponseLID) studentTestQuestion.getResponse()).getResponse()[0]);
                    if (responseProcessing != null && responseProcessing.isFenixCorrectResponse()) {
                        studentTestQuestion.setTestQuestionMark(new Double(studentTestQuestion.getTestQuestionValue()
                                .doubleValue()));
                        ResponseLID r = (ResponseLID) studentTestQuestion.getResponse();
                        r.setIsCorrect(new Boolean[] { new Boolean(true) });
                        studentTestQuestion.setResponse(r);
                        studentTestQuestion.getSubQuestionByItem().setNextItemId(responseProcessing.getNextItem());
                        return studentTestQuestion;
                    } else if (studentTestQuestion.getSubQuestionByItem().getOptions().size() > 1) {
                        studentTestQuestion.setTestQuestionMark(new Double(-(studentTestQuestion.getTestQuestionValue()
                                .intValue() * (java.lang.Math.pow(
                                studentTestQuestion.getSubQuestionByItem().getOptions().size() - 1, -1)))));
                        ResponseLID r = (ResponseLID) studentTestQuestion.getResponse();
                        r.setIsCorrect(new Boolean[] { new Boolean(false) });
                        studentTestQuestion.setResponse(r);
                        if (responseProcessing != null) {
                            studentTestQuestion.getSubQuestionByItem().setNextItemId(responseProcessing.getNextItem());
                        }
                        return studentTestQuestion;
                    }
                }
            }
        }
        studentTestQuestion.setTestQuestionMark(new Double(0));
        return studentTestQuestion;
    }

    private int getCorrectOptionsNumber(List correctResponseList, int emptyIndex) {
        int result = correctResponseList.size();
        for (int i = 0; i < correctResponseList.size(); i++) {
            ResponseCondition rc = (ResponseCondition) correctResponseList.get(i);
            if (rc.getResponse().equals(Integer.toString(emptyIndex))) {
                return result - 1;
            }
        }
        return result;
    }

    public List<ResponseCondition> getFenixCorrectResponse4LID(List responseProcessingList, Integer index) {
        List<ResponseCondition> oneResponse = new ArrayList<ResponseCondition>();
        ResponseProcessing responseProcessing = (ResponseProcessing) responseProcessingList.get(index.intValue());
        if (responseProcessing.isFenixCorrectResponse()) {
            for (ResponseCondition rc : responseProcessing.getResponseConditions()) {
                if (rc != null && rc.getCondition().intValue() == ResponseCondition.VAREQUAL) {
                    oneResponse.add(rc);
                }
            }
        }
        return oneResponse;
    }

}