/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IQuestionCorrectionStrategy;

/**
 * @author Susana Fernandes
 *  
 */
public interface IQuestionCorrectionStrategyFactory {
    public abstract IQuestionCorrectionStrategy getQuestionCorrectionStrategy(
            InfoStudentTestQuestion infoStudentTestQuestion);
}