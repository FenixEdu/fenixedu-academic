/*
 * Created on 12/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servicos.TestCaseReadServices;

/**
 * @author Susana Fernandes
 */
public class ReadQuestionImageTest extends TestCaseReadServices {
    /**
     * @param testName
     */
    public ReadQuestionImageTest(String testName) {
        super(testName);

    }

    protected String getNameOfServiceToBeTested() {
        return "ReadQuestionImage";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(1), new Integer(1) };
        return args;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 0;
    }

    protected Object getObjectToCompare() {
        return "R0lGODlhIAAQAIAAAAAAAP///ywAAAAAIAAQAAACJYyPqcvtD6OctFoDMtY38fB1IHCE4mdeKCmWbMa2I5zK9o3nTgEAOw==";
    }

    protected boolean needsAuthorization() {
        return true;
    }
}