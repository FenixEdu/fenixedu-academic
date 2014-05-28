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
        if (responseCondicions.isEmpty()) {
            return false;
        }
        for (int rc = 0; rc < responseCondicions.size(); rc++) {
            ResponseCondition responseCondition = (ResponseCondition) responseCondicions.get(rc);
            if (!isCorrectResponse(studentResponse, responseCondition)) {
                return false;
            }
        }
        return true;

    }

    private boolean isCorrectResponse(String[] studentResponse, ResponseCondition responseCondition) {
        if (studentResponse.length == 0) {
            return false;
        }
        if (responseCondition.getCondition().intValue() == ResponseCondition.VAREQUAL) {
            for (String element : studentResponse) {
                if (responseCondition.isCorrectLID(element)) {
                    return true;
                }
            }
        }
        if (responseCondition.getCondition().intValue() == ResponseCondition.NOTVAREQUAL) {
            for (int st = 0; st < studentResponse.length; st++) {
                if (!responseCondition.isCorrectLID(studentResponse[st])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    protected ResponseProcessing getLIDResponseProcessing(List<ResponseProcessing> responseProcessingList,
            String[] studentResponse) {
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
            if (responseCondition.isCorrectLID(studentResponse)) {
                match = true;
            }
        }
        if (!match) {
            return false;
        }
        return true;
    }

    protected ResponseProcessing getLIDResponseProcessing(List<ResponseProcessing> responseProcessingList, String studentResponse) {
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
            if (responseCondition.isCorrectSTR(studentResponse)) {
                match = true;
            } else {
                return false;
            }
        }
        if (!match) {
            return false;
        }
        return true;
    }

    protected ResponseProcessing getSTRResponseProcessing(List<ResponseProcessing> responseProcessingList, String studentResponse) {
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
            if (responseCondition.isCorrectNUM(studentResponse)) {
                match = true;
            } else {
                return false;
            }
        }
        if (!match) {
            return false;
        }
        return true;
    }

    protected ResponseProcessing getNUMResponseProcessing(List<ResponseProcessing> responseProcessingList, Double studentResponse) {
        for (ResponseProcessing responseProcessing : responseProcessingList) {
            if (isCorrectNUM(responseProcessing.getResponseConditions(), studentResponse)) {
                return responseProcessing;
            }
        }
        return null;
    }

    @Override
    public String validResponse(StudentTestQuestion studentTestQuestion, Response newResponse) {

        if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getRender() instanceof RenderFIB) {
            RenderFIB renderFIB = (RenderFIB) studentTestQuestion.getSubQuestionByItem().getQuestionType().getRender();
            try {
                if (renderFIB.getFibtype().intValue() == RenderFIB.INTEGER_CODE) {
                    new Integer(((ResponseNUM) newResponse).getResponse());
                } else if (renderFIB.getFibtype().intValue() == RenderFIB.DECIMAL_CODE) {
                    new Double(((ResponseNUM) newResponse).getResponse());
                }
            } catch (NumberFormatException ex) {
                return "Pergunta " + studentTestQuestion.getTestQuestionOrder().toString() + ": Formato de resposta inválido";
            }
        } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getRender() instanceof RenderChoise) {
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getCardinalityType().getType()
                    .equals(new Integer(CardinalityType.MULTIPLE))) {
                int emptyOptionIndex = getEmptyOptionIndex(studentTestQuestion.getSubQuestionByItem().getOptions());
                if (emptyOptionIndex != -1) {
                    if (responseIsEmptyAndOther(((ResponseLID) newResponse).getResponse(), emptyOptionIndex)) {
                        return "Pergunta " + studentTestQuestion.getTestQuestionOrder().toString()
                                + ": Não pode responder \"Nenhuma\" e uma opção em simultâneo";
                    }
                }

            }
        }
        return null;
    }

    protected int getEmptyOptionIndex(List optionList) {
        for (int i = 0; i < optionList.size(); i++) {
            QuestionOption questionOption = (QuestionOption) optionList.get(i);
            if (questionOption.getEmptyResponse()) {
                return i + 1;
            }
        }
        return -1;
    }

    private boolean responseIsEmptyAndOther(String[] response, int emptyResponse) {
        int optionsNotEmpty = 0;
        int empty = 0;
        for (String element : response) {
            if (element.equals((Integer.toString(emptyResponse)))) {
                empty++;
            } else {
                optionsNotEmpty++;
            }
        }

        if (empty != 0 && optionsNotEmpty != 0) {
            return true;
        }
        return false;
    }

    protected Integer getFenixCorrectResponseIndex(List responseProcessingList) {
        Iterator itResponseProcessing = responseProcessingList.iterator();

        for (int i = 0; itResponseProcessing.hasNext(); i++) {
            if (((ResponseProcessing) itResponseProcessing.next()).isFenixCorrectResponse()) {
                return Integer.valueOf(i);
            }
        }
        return null;
    }

    protected ResponseProcessing getOtherResponseProcessing(List<ResponseProcessing> responseProcessingList) {
        for (ResponseProcessing responseProcessing : responseProcessingList) {
            if (responseProcessing.getResponseConditions().isEmpty() && responseProcessing.isOtherResponseProcessing()) {
                return responseProcessing;
            }
        }
        return null;
    }

}