/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.CardinalityType;
import net.sourceforge.fenixedu.util.tests.QuestionOption;
import net.sourceforge.fenixedu.util.tests.RenderChoise;
import net.sourceforge.fenixedu.util.tests.RenderFIB;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseCondition;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;

/**
 * @author Susana Fernandes
 * 
 */
public abstract class QuestionCorrectionStrategy implements IQuestionCorrectionStrategy {

    protected boolean isCorrectLID(List responseCondicions, String[] studentResponse) {
        for (int st = 0; st < studentResponse.length; st++) {
            boolean match = false;
            for (int rc = 0; rc < responseCondicions.size(); rc++) {
                ResponseCondition responseCondition = (ResponseCondition) responseCondicions.get(rc);
              //  if (responseCondition.getCondition().intValue() == ResponseCondition.VAREQUAL)
                    if (responseCondition.isCorrectLID(studentResponse[st])) {
                        match = true;
                    }
            }
            if (!match)
                return false;
        }
        return true;
    }

    protected ResponseProcessing getLIDResponseProcessing(
            List<ResponseProcessing> responseProcessingList, String[] studentResponse) {
        for (ResponseProcessing responseProcessing : responseProcessingList) {
            if (isCorrectLID(responseProcessing.getResponseConditions(), studentResponse)) {
                return responseProcessing;
            }
        }
        return null;
    }

    protected boolean isCorrectLID(List responseCondicions, String studentResponse) {
        boolean match = false;
        for (int i = 0; i < responseCondicions.size(); i++) {
            ResponseCondition responseCondition = (ResponseCondition) responseCondicions.get(i);
           // if (responseCondition.getCondition().intValue() == ResponseCondition.VAREQUAL) {
                if (responseCondition.isCorrectLID(studentResponse)) {
                    match = true;
                }
//                else
//                    return false;
//            }
        }
        if (!match)
            return false;
        return true;
    }

    protected ResponseProcessing getLIDResponseProcessing(
            List<ResponseProcessing> responseProcessingList, String studentResponse) {
        for (ResponseProcessing responseProcessing : responseProcessingList) {
            if (isCorrectLID(responseProcessing.getResponseConditions(), studentResponse)) {
                return responseProcessing;
            }
        }
        return null;
    }

    protected boolean isCorrectSTR(List responseCondicions, String studentResponse) {
        boolean match = false;
        for (int i = 0; i < responseCondicions.size(); i++) {
            ResponseCondition responseCondition = (ResponseCondition) responseCondicions.get(i);
            if (responseCondition.isCorrectSTR(studentResponse))
                match = true;
            else
                return false;
        }
        if (!match)
            return false;
        return true;
    }

    protected ResponseProcessing getSTRResponseProcessing(
            List<ResponseProcessing> responseProcessingList, String studentResponse) {
        for (ResponseProcessing responseProcessing : responseProcessingList) {
            if (isCorrectSTR(responseProcessing.getResponseConditions(), studentResponse)) {
                return responseProcessing;
            }
        }
        return null;
    }

    protected boolean isCorrectNUM(List responseCondicions, Double studentResponse) {
        boolean match = false;
        for (int i = 0; i < responseCondicions.size(); i++) {
            ResponseCondition responseCondition = (ResponseCondition) responseCondicions.get(i);
            if (responseCondition.isCorrectNUM(studentResponse))
                match = true;
            else
                return false;
        }
        if (!match)
            return false;
        return true;
    }

    protected ResponseProcessing getNUMResponseProcessing(
            List<ResponseProcessing> responseProcessingList, Double studentResponse) {
        for (ResponseProcessing responseProcessing : responseProcessingList) {
            if (isCorrectNUM(responseProcessing.getResponseConditions(), studentResponse)) {
                return responseProcessing;
            }
        }
        return null;
    }

    public String validResponse(StudentTestQuestion studentTestQuestion, Response newResponse) {

        if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getRender() instanceof RenderFIB) {
            RenderFIB renderFIB = (RenderFIB) studentTestQuestion.getSubQuestionByItem()
                    .getQuestionType().getRender();
            try {
                if (renderFIB.getFibtype().intValue() == RenderFIB.INTEGER_CODE)
                    new Integer(((ResponseNUM) newResponse).getResponse());
                else if (renderFIB.getFibtype().intValue() == RenderFIB.DECIMAL_CODE) {
                    new Double(((ResponseNUM) newResponse).getResponse());
                }
            } catch (NumberFormatException ex) {
                return new String("Pergunta " + studentTestQuestion.getTestQuestionOrder().toString()
                        + ": Formato de resposta inválido");
            }
        } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getRender() instanceof RenderChoise) {
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getCardinalityType()
                    .getType().equals(new Integer(CardinalityType.MULTIPLE))) {
                int emptyOptionIndex = getEmptyOptionIndex(studentTestQuestion.getSubQuestionByItem()
                        .getOptions());
                if (emptyOptionIndex != -1) {
                    if (responseIsEmptyAndOther(((ResponseLID) newResponse).getResponse(),
                            emptyOptionIndex))
                        return new String("Pergunta "
                                + studentTestQuestion.getTestQuestionOrder().toString()
                                + ": Não pode responder \"Nenhuma\" e uma opção em simultâneo");
                }

            }
        }
        return null;
    }

    protected int getEmptyOptionIndex(List optionList) {
        for (int i = 0; i < optionList.size(); i++) {
            QuestionOption questionOption = (QuestionOption) optionList.get(i);
            if (questionOption.getEmptyResponse())
                return i + 1;
        }
        return -1;
    }

    private boolean responseIsEmptyAndOther(String[] response, int emptyResponse) {
        int optionsNotEmpty = 0;
        int empty = 0;
        for (int i = 0; i < response.length; i++) {
            if (response[i].equals((new Integer(emptyResponse)).toString()))
                empty++;
            else
                optionsNotEmpty++;
        }

        if (empty != 0 && optionsNotEmpty != 0)
            return true;
        return false;
    }

    protected Integer getFenixCorrectResponseIndex(List responseProcessingList) {
        Iterator itResponseProcessing = responseProcessingList.iterator();

        for (int i = 0; itResponseProcessing.hasNext(); i++) {
            if (((ResponseProcessing) itResponseProcessing.next()).isFenixCorrectResponse()) {
                return new Integer(i);
            }
        }
        return null;
    }

}