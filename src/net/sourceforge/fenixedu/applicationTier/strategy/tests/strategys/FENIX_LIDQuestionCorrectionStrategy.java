/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestion;
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

    public InfoStudentTestQuestion getMark(InfoStudentTestQuestion infoStudentTestQuestion) {
        Integer fenixCorrectResponseIndex = getFenixCorrectResponseIndex(infoStudentTestQuestion
                .getQuestion().getResponseProcessingInstructions());
        Integer empty = new Integer(getEmptyOptionIndex(infoStudentTestQuestion.getQuestion()
                .getOptions()));
        if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID) {
            List correctResponseList = getFenixCorrectResponse4LID(infoStudentTestQuestion.getQuestion()
                    .getResponseProcessingInstructions(), fenixCorrectResponseIndex);
            if (infoStudentTestQuestion.getQuestion().getQuestionType().getCardinalityType().getType()
                    .intValue() == CardinalityType.MULTIPLE) {
                //2*(correctOptionsChosen+wrongOptionNotChosen)/allOptionNumber-1

                int emptyNumber = 0;
                int emptyIndex = getEmptyOptionIndex(infoStudentTestQuestion.getQuestion().getOptions());
                if (emptyIndex != -1)
                    emptyNumber = 1;
                int allOptionNumber = infoStudentTestQuestion.getQuestion().getOptionNumber().intValue()
                        - emptyNumber;
                int correctOptionNumber = getCorrectOptionsNumber(correctResponseList, emptyIndex);
                int wrongOptionsNumber = allOptionNumber - correctOptionNumber;
                int correctOptionsChosen = 0;
                int wrongOptionChosen = 0;
                Boolean[] isCorrect = new Boolean[((ResponseLID) infoStudentTestQuestion.getResponse())
                        .getResponse().length];
                for (int i = 0; i < ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse().length; i++) {
                    Iterator responseConditionIt = correctResponseList.iterator();
                    boolean correct = false;
                    boolean chooseEmpty = false;
                    while (responseConditionIt.hasNext()) {
                        if ((((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse()[i])
                                .equals(empty.toString()))
                            chooseEmpty = true;
                        if (((ResponseCondition) responseConditionIt.next()).isCorrectLID(new String(
                                ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse()[i]))) {
                            correct = true;
                            break;
                        }
                    }
                    isCorrect[i] = new Boolean(correct);

                    if (!chooseEmpty) {
                        if (correct)
                            correctOptionsChosen++;
                        else
                            wrongOptionChosen++;
                    }
                }
                int wrongOptionNotChosen = wrongOptionsNumber - wrongOptionChosen;

                if (allOptionNumber > 1) {
                    infoStudentTestQuestion.setTestQuestionMark(new Double(infoStudentTestQuestion
                            .getTestQuestionValue().doubleValue()
                            * (2 * (correctOptionsChosen + wrongOptionNotChosen)
                                    * (java.lang.Math.pow(allOptionNumber, -1)) - 1)));
                } else {
                    infoStudentTestQuestion.setTestQuestionMark(new Double(infoStudentTestQuestion
                            .getTestQuestionValue().doubleValue()
                            * correctOptionsChosen));
                }
                ResponseLID r = (ResponseLID) infoStudentTestQuestion.getResponse();
                r.setIsCorrect(isCorrect);
                infoStudentTestQuestion.setResponse(r);
                return infoStudentTestQuestion;

            } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getCardinalityType()
                    .getType().intValue() == CardinalityType.SINGLE) {
                //(1/num_op)-1
                if (isCorrectLID(correctResponseList, new String(((ResponseLID) infoStudentTestQuestion
                        .getResponse()).getResponse()[0]))) {

                    infoStudentTestQuestion.setTestQuestionMark(new Double(infoStudentTestQuestion
                            .getTestQuestionValue().doubleValue()));
                    ResponseLID r = (ResponseLID) infoStudentTestQuestion.getResponse();
                    r.setIsCorrect(new Boolean[] { new Boolean(true) });
                    infoStudentTestQuestion.setResponse(r);
                } else {
                    if (infoStudentTestQuestion.getQuestion().getOptionNumber().intValue() > 1) {
                        infoStudentTestQuestion.setTestQuestionMark(new Double(-(infoStudentTestQuestion
                                .getTestQuestionValue().intValue() * (java.lang.Math.pow(
                                infoStudentTestQuestion.getQuestion().getOptionNumber().intValue() - 1,
                                -1)))));
                    } else
                        infoStudentTestQuestion.setTestQuestionMark(new Double(0));
                    ResponseLID r = (ResponseLID) infoStudentTestQuestion.getResponse();
                    r.setIsCorrect(new Boolean[] { new Boolean(false) });
                    infoStudentTestQuestion.setResponse(r);
                }
                return infoStudentTestQuestion;
            }
        }
        infoStudentTestQuestion.setTestQuestionMark(new Double(0));
        return infoStudentTestQuestion;
    }

    private int getCorrectOptionsNumber(List correctResponseList, int emptyIndex) {
        int result = correctResponseList.size();
        for (int i = 0; i < correctResponseList.size(); i++) {
            ResponseCondition rc = (ResponseCondition) correctResponseList.get(i);
            if (rc.getResponse().equals(new Integer(emptyIndex).toString()))
                return result - 1;
        }
        return result;
    }

    public List getFenixCorrectResponse4LID(List responseProcessingList, Integer index) {

        List oneResponse = new ArrayList();
        ResponseProcessing responseProcessing = (ResponseProcessing) responseProcessingList.get(index
                .intValue());

        if (responseProcessing.isFenixCorrectResponse()) {
            oneResponse = new ArrayList();
            Iterator itResponseCondition = responseProcessing.getResponseConditions().iterator();
            while (itResponseCondition.hasNext()) {
                ResponseCondition rc = (ResponseCondition) itResponseCondition.next();

                if (rc != null && rc.getCondition().intValue() == ResponseCondition.VAREQUAL)
                    oneResponse.add(rc);

            }
        }

        return oneResponse;
    }

}