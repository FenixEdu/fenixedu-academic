/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests;

import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;

/**
 * @author Susana Fernandes
 * 
 */
public interface IQuestionCorrectionStrategyFactory {
    public abstract IQuestionCorrectionStrategy getQuestionCorrectionStrategy(
            StudentTestQuestion studentTestQuestion);
}