/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestion;

/**
 * @author Susana Fernandes
 *  
 */
public interface IQuestionCorrectionStrategy {

    public InfoStudentTestQuestion getMark(InfoStudentTestQuestion infoStudentTestQuestion);

    public String validResponse(InfoStudentTestQuestion infoStudentTestQuestion);
}