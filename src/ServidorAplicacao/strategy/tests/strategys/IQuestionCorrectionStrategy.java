/*
 * Created on 23/Set/2004
 *
 */
package ServidorAplicacao.strategy.tests.strategys;

import DataBeans.InfoStudentTestQuestion;

/**
 * @author Susana Fernandes
 *  
 */
public interface IQuestionCorrectionStrategy {

    public InfoStudentTestQuestion getMark(InfoStudentTestQuestion infoStudentTestQuestion);

    public String validResponse(InfoStudentTestQuestion infoStudentTestQuestion);
}