/*
 * Created on 23/Set/2004
 *
 */
package ServidorAplicacao.strategy.tests;

import DataBeans.InfoStudentTestQuestion;
import ServidorAplicacao.strategy.tests.strategys.IQuestionCorrectionStrategy;

/**
 * @author Susana Fernandes
 *  
 */
public interface IQuestionCorrectionStrategyFactory {
    public abstract IQuestionCorrectionStrategy getQuestionCorrectionStrategy(
            InfoStudentTestQuestion infoStudentTestQuestion);
}