/*
 * Created on 19/Set/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadShiftsByDistributedTestTest extends TestCaseReadServices
{

    /**
	 * @param testName
	 */
    public ReadShiftsByDistributedTestTest(String testName)
    {
        super(testName);

    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadShiftsByDistributedTest";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {
        Object[] args = { new Integer(26), new Integer(25)};
        return args;
    }

    protected int getNumberOfItemsToRetrieve()
    {
        return 1;
    }

    protected Object getObjectToCompare()
    {
        List shiftList = new ArrayList();
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            ITurno shift = (ITurno) sp.getITurnoPersistente().readByOID(Turno.class, new Integer(19));
            assertNotNull("shift null", shift);
            sp.confirmarTransaccao();

            InfoShift infoShift = (InfoShift) Cloner.get(shift);

            shiftList.add(infoShift);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("exception: ExcepcaoPersistencia ");
        }
        return shiftList;
    }

    protected boolean needsAuthorization()
    {
        return true;
    }
}
