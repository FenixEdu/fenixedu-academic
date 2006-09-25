/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests;

import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.FENIX_LIDQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.FENIX_NUMQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.FENIX_STRQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IMS_LIDQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IMS_NUMQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IMS_STRQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import net.sourceforge.fenixedu.util.tests.QuestionType;

/**
 * @author Susana Fernandes
 * 
 */
public class QuestionCorrectionStrategyFactory implements IQuestionCorrectionStrategyFactory {
    private static QuestionCorrectionStrategyFactory instance = null;

    private QuestionCorrectionStrategyFactory() {
    }

    public static synchronized QuestionCorrectionStrategyFactory getInstance() {
        if (instance == null) {
            instance = new QuestionCorrectionStrategyFactory();
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public IQuestionCorrectionStrategy getQuestionCorrectionStrategy(
            StudentTestQuestion studentTestQuestion) {
        IQuestionCorrectionStrategy questionCorrectionStrategy = null;
        if (studentTestQuestion.getCorrectionFormula().getFormula().equals(
                new Integer(CorrectionFormula.IMS))) {
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID)
                questionCorrectionStrategy = new IMS_LIDQuestionCorrectionStrategy();
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.STR)
                questionCorrectionStrategy = new IMS_STRQuestionCorrectionStrategy();
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.NUM)
                questionCorrectionStrategy = new IMS_NUMQuestionCorrectionStrategy();
        } else if (studentTestQuestion.getCorrectionFormula().getFormula().equals(
                new Integer(CorrectionFormula.FENIX))) {
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID)
                questionCorrectionStrategy = new FENIX_LIDQuestionCorrectionStrategy();
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.STR)
                questionCorrectionStrategy = new FENIX_STRQuestionCorrectionStrategy();
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.NUM)
                questionCorrectionStrategy = new FENIX_NUMQuestionCorrectionStrategy();
        }
        return questionCorrectionStrategy;
    }
}