/*
 * Created on 1/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

/**
 * @author lmac1
 */

public class ReadAllTeachersTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadAllTeachersTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadAllTeachers";
    }

    protected int getNumberOfItemsToRetrieve() {
        return 7;
    }
}