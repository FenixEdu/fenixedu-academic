/*
 * Created on 26/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDistributedTest;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTestByTestIdTest extends TestCaseReadServices {

    public ReadDistributedTestByTestIdTest(String testName) {
        super(testName);

    }

    protected String getNameOfServiceToBeTested() {
        return "ReadDistributedTestByTestId";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(3) };
        return args;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 3;
    }

    protected Object getObjectToCompare() {
        List result = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IPersistentTest persistentTest = sp.getIPersistentTest();
            ITest test = new Test(new Integer(3));
            test = (ITest) persistentTest.readByOID(Test.class, new Integer(3));
            assertNotNull("test null", test);

            IPersistentDistributedTest persistentDistributedTest = sp.getIPersistentDistributedTest();
            IDistributedTest distributedTest23 = new DistributedTest(new Integer(23));
            distributedTest23 = (IDistributedTest) persistentDistributedTest.readByOID(
                    DistributedTest.class, new Integer(23));
            assertNotNull("distributedTest null", distributedTest23);

            IDistributedTest distributedTest24 = new DistributedTest(new Integer(24));
            distributedTest24 = (IDistributedTest) persistentDistributedTest.readByOID(
                    DistributedTest.class, new Integer(24));
            assertNotNull("distributedTest null", distributedTest24);

            IDistributedTest distributedTest25 = new DistributedTest(new Integer(25));
            distributedTest25 = (IDistributedTest) persistentDistributedTest.readByOID(
                    DistributedTest.class, new Integer(25));
            assertNotNull("distributedTest null", distributedTest25);
            sp.confirmarTransaccao();

            InfoDistributedTest infoDistributedTest23 = InfoDistributedTest
                    .newInfoFromDomain(distributedTest23);
            InfoDistributedTest infoDistributedTest24 = InfoDistributedTest
                    .newInfoFromDomain(distributedTest24);
            InfoDistributedTest infoDistributedTest25 = InfoDistributedTest
                    .newInfoFromDomain(distributedTest25);

            result.add(infoDistributedTest23);
            result.add(infoDistributedTest24);
            result.add(infoDistributedTest25);

        } catch (ExcepcaoPersistencia e) {
            fail("exception: ExcepcaoPersistencia ");
        }

        return result;
    }

    protected boolean needsAuthorization() {
        return true;
    }
}