/*
 * Created on 23/Set/2004
 *
 */
package ServidorAplicacao.strategy.tests;

import DataBeans.InfoStudentTestQuestion;
import ServidorAplicacao.strategy.tests.strategys.FENIX_LIDQuestionCorrectionStrategy;
import ServidorAplicacao.strategy.tests.strategys.FENIX_NUMQuestionCorrectionStrategy;
import ServidorAplicacao.strategy.tests.strategys.FENIX_STRQuestionCorrectionStrategy;
import ServidorAplicacao.strategy.tests.strategys.IMS_LIDQuestionCorrectionStrategy;
import ServidorAplicacao.strategy.tests.strategys.IMS_NUMQuestionCorrectionStrategy;
import ServidorAplicacao.strategy.tests.strategys.IMS_STRQuestionCorrectionStrategy;
import ServidorAplicacao.strategy.tests.strategys.IQuestionCorrectionStrategy;
import Util.tests.CorrectionFormula;
import Util.tests.QuestionType;

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
            InfoStudentTestQuestion infoStudentTestQuestion) {
        IQuestionCorrectionStrategy questionCorrectionStrategy = null;

        if (infoStudentTestQuestion.getCorrectionFormula().getFormula().equals(
                new Integer(CorrectionFormula.IMS))) {
            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID)
                questionCorrectionStrategy = new IMS_LIDQuestionCorrectionStrategy();
            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR)
                questionCorrectionStrategy = new IMS_STRQuestionCorrectionStrategy();
            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM)
                questionCorrectionStrategy = new IMS_NUMQuestionCorrectionStrategy();

        } else if (infoStudentTestQuestion.getCorrectionFormula().getFormula().equals(
                new Integer(CorrectionFormula.FENIX))) {
            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID)
                questionCorrectionStrategy = new FENIX_LIDQuestionCorrectionStrategy();
            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR)
                questionCorrectionStrategy = new FENIX_STRQuestionCorrectionStrategy();
            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM)
                questionCorrectionStrategy = new FENIX_NUMQuestionCorrectionStrategy();

        }
        return questionCorrectionStrategy;
    }
}